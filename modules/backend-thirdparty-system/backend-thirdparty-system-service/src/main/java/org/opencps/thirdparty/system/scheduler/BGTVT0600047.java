package org.opencps.thirdparty.system.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.opencps.auth.utils.APIDateTimeUtils;
import org.opencps.datamgt.utils.DateTimeUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierAction;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.ProcessAction;
import org.opencps.dossiermgt.model.ProcessOption;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.model.ServiceInfo;
import org.opencps.dossiermgt.model.ServiceProcess;
import org.opencps.dossiermgt.service.DossierActionLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.ProcessActionLocalServiceUtil;
import org.opencps.dossiermgt.service.ProcessOptionLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.thirdparty.system.constants.SyncServerTerm;
import org.opencps.thirdparty.system.messagequeue.model.MessageQueueInputModel;
import org.opencps.thirdparty.system.model.ThirdPartyDossierSync;
import org.opencps.thirdparty.system.nsw.vt.model.AttachedFile;
import org.opencps.thirdparty.system.nsw.vt.model.Envelope;
import org.opencps.thirdparty.system.nsw.vt.model.IssuingAuthority;
import org.opencps.thirdparty.system.nsw.vt.model.ResultOfExtending;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600047 {
	public static MessageQueueInputModel convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync, Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		ResultOfExtending resultOfExtending = new ResultOfExtending();
		AttachedFile attachedFile = new AttachedFile();
		List<AttachedFile> lstFiles = new ArrayList<>();

		MessageQueueInputModel model = new MessageQueueInputModel();
		model.setContent("");
		model.setSender("BGTVT");
		model.setReceiver("NSW");
		model.setPersonSignature("");
		model.setSystemSignature("");
		model.setStatus(0);
		model.setMessageId(PortalUUIDUtil.generate());
		model.setFromName("BGTVT");
		model.setFromCountryCode("VN");
		model.setFromMinistryCode("BGTVT");
		model.setFromOrganizationCode("TCDBVN");
		model.setFromUnitCode("");
		model.setFromIdentity("");
		model.setToName("NSW");
		model.setToCountryCode("VN");
		model.setToIdentity("");
		model.setToMinistryCode("NSW");
		model.setToOrganizationCode("NSW");
		model.setToUnitCode("");
		model.setDocumentType(dossier.getServiceCode());
		model.setType(type);
		model.setFunction(function);
		model.setReference(dossier.getReferenceUid());
		model.setPreReference(dossier.getReferenceUid());
		model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
		model.setRetryCount(0);
		model.setDirection(2);
		Calendar cal = Calendar.getInstance();
		
		if (dossier.getReceiveDate() != null) {
			cal.setTime(dossier.getReceiveDate());
		} else {
			cal.setTime(dossier.getCreateDate());
		}
		model.setDocumentYear(cal.get(Calendar.YEAR));

		List<DossierFile> dossierFileList = DossierFileLocalServiceUtil
				.getAllDossierFile(dossier.getDossierId());

		String templateNo = StringPool.BLANK;
		String partNo = StringPool.BLANK;
		int partType = 2;
		boolean eSign = true;
		String deliverableCode = StringPool.BLANK;

		ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getByCode(dossier.getGroupId(),
				dossier.getServiceCode());
		ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.getBySICodeAndGAC(
				dossier.getGroupId(), serviceInfo.getServiceCode(), dossier.getGovAgencyCode());

		ProcessOption processOption = ProcessOptionLocalServiceUtil.getByDTPLNoAndServiceCF(
				dossier.getGroupId(), dossier.getDossierTemplateNo(),
				serviceConfig.getServiceConfigId());

		ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
				.fetchServiceProcess(processOption.getServiceProcessId());
		DossierAction dossierAction = DossierActionLocalServiceUtil.fetchDossierAction(dossierActionId);

		ProcessAction processAction = ProcessActionLocalServiceUtil.fetchBySPID_AC(
				serviceProcess.getServiceProcessId(), dossierAction.getActionCode());

		for (DossierFile dossierFile : dossierFileList) {
			partNo = dossierFile.getDossierPartNo();
			templateNo = dossierFile.getFileTemplateNo();

			String returnDossierFiles = processAction.getReturnDossierFiles();
			String[] returnDossierFilesArr = StringUtil.split(returnDossierFiles);
			for (String returnDossierFile : returnDossierFilesArr) {
				if (templateNo.equals(returnDossierFile)) {
					attachedFile = new AttachedFile();
					attachedFile.setAttachedNote("");
					attachedFile.setAttachedTypeCode(templateNo);
					attachedFile.setAttachedTypeName(partNo);
					attachedFile.setFullFileName(dossierFile.getDisplayName());

					lstFiles.add(attachedFile);

					String linkCongVan = jaxRsPublicUrl + "dossiers/" + dossier.getDossierId()
							+ "/files/" + dossierFile.getReferenceUid();
					if (dossier.getPassword() != null) {
						linkCongVan += "/public/" + dossier.getPassword();
					}
					attachedFile.setFileURL(linkCongVan);
					JSONObject formDataObj = JSONFactoryUtil
							.createJSONObject(dossierFile.getFormData());
					if (formDataObj.has("NameOfCompany")) {
						resultOfExtending.setNameOfCompany(formDataObj.getString("NameOfCompany"));						
					}
					if (formDataObj.has("Address")) {
						resultOfExtending.setAddress(formDataObj.getString("Address"));						
					}
					if (formDataObj.has("Tel")) {
						resultOfExtending.setTel(formDataObj.getString("Tel"));						
					}
					if (formDataObj.has("Fax")) {
						resultOfExtending.setFax(formDataObj.getString("Fax"));						
					}
					if (formDataObj.has("Email")) {
						resultOfExtending.setEmail(formDataObj.getString("Email"));						
					}
					if (formDataObj.has("BizCertificationNo")) {
						resultOfExtending.setBizCertificationNo(formDataObj.getString("BizCertificationNo"));						
					}
					
					if (formDataObj.has("BizCertificationDate")) {
						String expiredDateStr = formDataObj.getString("BizCertificationDate");
						Date expiredDate = DateTimeUtils.convertStringToDate(expiredDateStr);
						
						resultOfExtending.setBizCertificationDate(DateTimeUtils.convertDateToString(expiredDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						resultOfExtending.setBizCertificationDate(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					
					if (formDataObj.has("BizCertificationExpiredDate")) {
						String expiredDateStr = formDataObj.getString("BizCertificationExpiredDate");
						Date expiredDate = DateTimeUtils.convertStringToDate(expiredDateStr);
						
						resultOfExtending.setBizCertificationExpiredDate(DateTimeUtils.convertDateToString(expiredDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						resultOfExtending.setBizCertificationExpiredDate(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					
					if (formDataObj.has("BizCertificationIssuedBy")) {
						resultOfExtending.setBizCertificationIssuedBy(formDataObj.getString("BizCertificationIssuedBy"));						
					}
					
					if (formDataObj.has("Extend")) {
						resultOfExtending.setExtend(formDataObj.getString("Extend"));						
					}
					if (formDataObj.has("ExtendFrom")) {
						String expiredDateStr = formDataObj.getString("ExtendFrom");
						Date expiredDate = DateTimeUtils.convertStringToDate(expiredDateStr);
						
						resultOfExtending.setExtendFrom(DateTimeUtils.convertDateToString(expiredDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						resultOfExtending.setExtendFrom(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					if (formDataObj.has("ExtendUntil")) {
						String expiredDateStr = formDataObj.getString("ExtendUntil");
						Date expiredDate = DateTimeUtils.convertStringToDate(expiredDateStr);
						
						resultOfExtending.setExtendUntil(DateTimeUtils.convertDateToString(expiredDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						resultOfExtending.setExtendUntil(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}					
					
					if (formDataObj.has("Remarks")) {
						resultOfExtending.setRemarks(formDataObj.getString("Remarks"));						
					}
					IssuingAuthority issuingAuthority = new IssuingAuthority();
					Date now = new Date();
					
					if (formDataObj.has("SignName")) {
						issuingAuthority.setSignName(formDataObj.getString("SignName"));						
					}
					else {
						issuingAuthority.setSignName("");						
					}
					if (formDataObj.has("SignDate")) {
						String signDateStr = formDataObj.getString("SignDate");
						Date signDate = DateTimeUtils.convertStringToDate(signDateStr);
						
						issuingAuthority.setSignDate(DateTimeUtils.convertDateToString(signDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						issuingAuthority.setSignDate(DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					if (formDataObj.has("SignPlace")) {
						issuingAuthority.setSignPlace(formDataObj.getString("SignPlace"));
					}
					else {
						issuingAuthority.setSignPlace("Hà Nội");
					}
					if (formDataObj.has("SignTitle")) {
						issuingAuthority.setSignTitle(formDataObj.getString("SignTitle"));						
					}
					else {
						issuingAuthority.setSignTitle("Cục trưởng");												
					}
					
					if (formDataObj.has("LicenceType")) {
						resultOfExtending.setLicenceType(formDataObj.getString("LicenceType"));						
					}
					
					resultOfExtending.setIssuingAuthority(issuingAuthority);
				}
			}
		}

		resultOfExtending.getAttachedFile().addAll(lstFiles);
		envelope.getBody().getContent().setResultOfExtending(resultOfExtending);

		String rawMessage = OutsideSystemConverter.convertToNSWXMLVT(envelope);

		model.setRawMessage(rawMessage);
		
		return model;
	}
}
