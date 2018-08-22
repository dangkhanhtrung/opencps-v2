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
import org.opencps.thirdparty.system.nsw.model.ApprovalOfCLVFixRoadTransport;
import org.opencps.thirdparty.system.nsw.model.AttachedFile;
import org.opencps.thirdparty.system.nsw.model.Envelope;
import org.opencps.thirdparty.system.nsw.model.IssuingAuthority;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600056 {
	private static String DUMMY_DATA = "1";
	
	public static MessageQueueInputModel convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync, Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		ApprovalOfCLVFixRoadTransport approvalOfCLVFixRoadTransport = new ApprovalOfCLVFixRoadTransport();
		AttachedFile attachedFile = new AttachedFile();
		List<AttachedFile> lstFiles = new ArrayList<>();

		MessageQueueInputModel model = new MessageQueueInputModel();
		model.setContent("");
		model.setSender("BGTVT");
		model.setReceiver("NSW");
		model.setPersonSignature("");
		model.setSystemSignature("");
		model.setStatus(1);
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
		model.setRetryCount(1);
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
			templateNo = dossierFile.getFileTemplateNo();
			partNo = dossierFile.getDossierPartNo();

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
					if (formDataObj.has("IssuingDispatchNo")) {
						approvalOfCLVFixRoadTransport.setIssuingDispatchNo(formDataObj.getString("IssuingDispatchNo"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setIssuingDispatchNo(StringPool.BLANK);												
					}
					if (formDataObj.has("OfficialDispatchNo")) {
						approvalOfCLVFixRoadTransport.setOfficialDispatchNo(formDataObj.getString("OfficialDispatchNo"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setOfficialDispatchNo(StringPool.BLANK);												
					}
					if (formDataObj.has("OfficialDispatchDate")) {
						String officialDispatchDateStr = formDataObj.getString("OfficialDispatchDate");
						Date officialDispatchDate = DateTimeUtils.convertStringToDate(officialDispatchDateStr);

						approvalOfCLVFixRoadTransport.setOfficialDispatchDate(
								DateTimeUtils.convertDateToString(officialDispatchDate, DateTimeUtils._NSW_DATE_TIME_FORMAT)
								);						
					}
					else {
						approvalOfCLVFixRoadTransport.setOfficialDispatchDate(StringPool.BLANK);												
					}
					if (formDataObj.has("NameOfCompany")) {
						approvalOfCLVFixRoadTransport.setNameOfCompany(formDataObj.getString("NameOfCompany"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setNameOfCompany(StringPool.BLANK);												
					}
					if (formDataObj.has("DepartureFrom")) {
						approvalOfCLVFixRoadTransport.setDepartureFrom(formDataObj.getString("DepartureFrom"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setDepartureFrom(StringPool.BLANK);												
					}
					if (formDataObj.has("DepartureTo")) {
						approvalOfCLVFixRoadTransport.setDepartureTo(formDataObj.getString("DepartureTo"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setDepartureTo(StringPool.BLANK);												
					}
					if (formDataObj.has("FromParkingLot")) {
						approvalOfCLVFixRoadTransport.setFromParkingLot(formDataObj.getString("FromParkingLot"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setFromParkingLot(StringPool.BLANK);												
					}
					if (formDataObj.has("FromProvinceCode")) {
						approvalOfCLVFixRoadTransport.setFromProvinceCode(formDataObj.getString("FromProvinceCode"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setFromProvinceCode(DUMMY_DATA);												
					}
					if (formDataObj.has("FromProvinceName")) {
						approvalOfCLVFixRoadTransport.setFromProvinceName(formDataObj.getString("FromProvinceName"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setFromProvinceName(DUMMY_DATA);												
					}
					if (formDataObj.has("ToParkingLot")) {
						approvalOfCLVFixRoadTransport.setToParkingLot(formDataObj.getString("ToParkingLot"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToParkingLot(DUMMY_DATA);												
					}
					if (formDataObj.has("ToProvinceCode")) {
						approvalOfCLVFixRoadTransport.setToProvinceCode(formDataObj.getString("ToProvinceCode"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToProvinceCode(DUMMY_DATA);												
					}
					if (formDataObj.has("ToProvinceName")) {
						approvalOfCLVFixRoadTransport.setToProvinceName(formDataObj.getString("ToProvinceName"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToProvinceName(DUMMY_DATA);												
					}
					if (formDataObj.has("FromBorderGate")) {
						approvalOfCLVFixRoadTransport.setFromBorderGate(formDataObj.getString("FromBorderGate"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setFromBorderGate(DUMMY_DATA);												
					}
					if (formDataObj.has("ToBorderGate")) {
						approvalOfCLVFixRoadTransport.setToBorderGate(formDataObj.getString("ToBorderGate"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToBorderGate(DUMMY_DATA);												
					}
					if (formDataObj.has("NumberOfVehicle")) {
						approvalOfCLVFixRoadTransport.setNumberOfVehicle(formDataObj.getString("NumberOfVehicle"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setNumberOfVehicle(DUMMY_DATA);												
					}
					if (formDataObj.has("CircularNo")) {
						approvalOfCLVFixRoadTransport.setCircularNo(formDataObj.getString("CircularNo"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setCircularNo(StringPool.BLANK);												
					}
					if (formDataObj.has("CircularDate")) {
						String circularDateStr = formDataObj.getString("CircularDate");
						Date circularDate = DateTimeUtils.convertStringToDate(circularDateStr);

						approvalOfCLVFixRoadTransport.setCircularDate(
								DateTimeUtils.convertDateToString(circularDate, DateTimeUtils._NSW_DATE_TIME_FORMAT)
								);						
					}
					else {
						Date now = new Date();
						approvalOfCLVFixRoadTransport.setCircularDate(DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));												
					}
					if (formDataObj.has("ToVNAuthority")) {
						approvalOfCLVFixRoadTransport.setToVNAuthority(formDataObj.getString("ToVNAuthority"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToVNAuthority(StringPool.BLANK);												
					}
					if (formDataObj.has("ToLAOAuthority")) {
						approvalOfCLVFixRoadTransport.setToLAOAuthority(formDataObj.getString("ToLAOAuthority"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setToLAOAuthority(StringPool.BLANK);												
					}
					if (formDataObj.has("AtParkingLot")) {
						approvalOfCLVFixRoadTransport.setAtParkingLot(formDataObj.getString("AtParkingLot"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setAtParkingLot(DUMMY_DATA);												
					}
					if (formDataObj.has("AtProvinceName")) {
						approvalOfCLVFixRoadTransport.setAtProvinceName(formDataObj.getString("AtProvinceName"));						
					}
					else {
						approvalOfCLVFixRoadTransport.setAtProvinceName(DUMMY_DATA);												
					}
					
					IssuingAuthority issuingAuthority = new IssuingAuthority();
					Date now = new Date();
					
					if (formDataObj.has("SignName")) {
						issuingAuthority.setSignName(formDataObj.getString("SignName"));						
					}
					else {
						issuingAuthority.setSignName("Nguyễn Tô An");						
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
					
					approvalOfCLVFixRoadTransport.setIssuingAuthority(issuingAuthority);					
				}
			}
		}

		approvalOfCLVFixRoadTransport.getAttachedFile().addAll(lstFiles);
		envelope.getBody().getContent().setApprovalOfCLVFixRoadTransport(approvalOfCLVFixRoadTransport);

		String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

		model.setRawMessage(rawMessage);
		
		return model;
	}
}
