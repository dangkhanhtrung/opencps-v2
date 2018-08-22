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
import org.opencps.thirdparty.system.nsw.model.AttachedFile;
import org.opencps.thirdparty.system.nsw.model.Envelope;
import org.opencps.thirdparty.system.nsw.model.IssuingAuthority;
import org.opencps.thirdparty.system.nsw.model.Transporter;
import org.opencps.thirdparty.system.nsw.model.VLInterRoadTransportLicence;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600001 {
	private static Log _log = LogFactoryUtil.getLog(BGTVT0600001.class);
	
	public static MessageQueueInputModel convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync, Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		VLInterRoadTransportLicence vlInterRoadTransportLicense = new VLInterRoadTransportLicence();
		vlInterRoadTransportLicense.setLicenceNo("");
		vlInterRoadTransportLicense.setFirstRegistrationDate("");
		vlInterRoadTransportLicense.setValidUntil("");
		vlInterRoadTransportLicense.setTransportOperation("");
		Transporter transporter = new Transporter();
		vlInterRoadTransportLicense.setTransporter(transporter);
		transporter.setNameOfCompany("");
		transporter.setAddress("");
		transporter.setTel("");
		transporter.setFax("");
		transporter.setEmail("");
		transporter.setWebsite("");
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
		Calendar cal = Calendar.getInstance();
		
		if (dossier.getReceiveDate() != null) {
			cal.setTime(dossier.getReceiveDate());
		} else {
			cal.setTime(dossier.getCreateDate());
		}
		model.setDocumentYear(cal.get(Calendar.YEAR));
		
		model.setReference(dossier.getReferenceUid());
		model.setPreReference(dossier.getReferenceUid());
		model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
		model.setRetryCount(1);
		model.setDirection(2);

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

//					if (dossierFile.getFileEntryId() > 0) {
//						FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(dossierFile.getFileEntryId());
//
//						File file = DLFileEntryLocalServiceUtil.getFile(fileEntry.getFileEntryId(), fileEntry.getVersion(),
//								true);
//						
//				        String encodedBase64 = null;
//				        try {
//				            FileInputStream fileInputStreamReader = new FileInputStream(file);
//				            byte[] bytes = new byte[(int)file.length()];
//				            fileInputStreamReader.read(bytes);
//				            encodedBase64 = new String(Base64.getEncoder().encodeToString(bytes));
//				            
//				            fileInputStreamReader.close();
//				        } catch (FileNotFoundException e) {
//				            e.printStackTrace();
//				        } catch (IOException e) {
//				            e.printStackTrace();
//				        }						
//				        
//					}
					
					JSONObject formDataObj = JSONFactoryUtil
							.createJSONObject(dossierFile.getFormData());
					vlInterRoadTransportLicense.setLicenceNo(dossierFile.getDeliverableCode());
					
					if (formDataObj.has("FirstRegistrationDate")) {
						String firstRegistrationDateStr = formDataObj.getString("FirstRegistrationDate");
						Date firstRegistrationDate = DateTimeUtils.convertStringToDate(firstRegistrationDateStr);
						
						vlInterRoadTransportLicense.setFirstRegistrationDate(DateTimeUtils.convertDateToString(firstRegistrationDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
						
					}
					if (formDataObj.has("ValidUntil")) {
						String validUntilStr = formDataObj.getString("ValidUntil");
						Date validUntilDate = DateTimeUtils.convertStringToDate(validUntilStr);
						
						vlInterRoadTransportLicense.setValidUntil(DateTimeUtils.convertDateToString(validUntilDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					else {
						vlInterRoadTransportLicense.setValidUntil(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
					}
					
					transporter.setNameOfCompany(formDataObj.getString("NameOfCompany"));
					transporter.setAddress(formDataObj.getString("Address"));
					transporter.setTel(formDataObj.getString("Tel"));
					transporter.setFax(formDataObj.getString("Fax"));
					transporter.setEmail(formDataObj.getString("Email"));
					transporter.setWebsite(formDataObj.getString("Website"));
					StringBuilder transportOperation = new StringBuilder();
					if (formDataObj.has("IsFixedRoadTransport")) {
						if ("1".equals(formDataObj.getString("IsFixedRoadTransport"))) {
							if (transportOperation.toString().isEmpty()) {
								transportOperation.append("Vận tải hành khách bằng ô tô theo tuyến cố định");
							}
							else {
								transportOperation.append(",Vận tải hành khách bằng ô tô theo tuyến cố định");
							}
						}
					}
					if (formDataObj.has("IsContractRoadTransport")) {
						if ("1".equals(formDataObj.getString("IsContractRoadTransport"))) {
							if (transportOperation.toString().isEmpty()) {
								transportOperation.append("Vận tải hành khách theo hợp đồng");
							}
							else {
								transportOperation.append(",Vận tải hành khách theo hợp đồng");
							}
						}
					}
					if (formDataObj.has("IsPassengerTransport")) {
						if ("1".equals(formDataObj.getString("IsPassengerTransport"))) {
							if (transportOperation.toString().isEmpty()) {
								transportOperation.append("Vận tải khách du lịch bằng xe ô tô");
							}
							else {
								transportOperation.append(",Vận tải khách du lịch bằng xe ô tô");
							}
						}
					}
					if (formDataObj.has("IsCargoTransport")) {
						if ("1".equals(formDataObj.getString("IsCargoTransport"))) {
							if (transportOperation.toString().isEmpty()) {
								transportOperation.append("Vận tải hàng hóa bằng xe ô tô");
							}
							else {
								transportOperation.append(",Vận tải hàng hóa bằng xe ô tô");
							}
						}
					}
					if (formDataObj.has("IsPassengerTaxi")) {
						if ("1".equals(formDataObj.getString("IsPassengerTaxi"))) {
							if (transportOperation.toString().isEmpty()) {
								transportOperation.append("Vận tải hành khách bằng xe taxi");
							}
							else {
								transportOperation.append(",Vận tải hành khách bằng xe taxi");
							}
						}
					}
					vlInterRoadTransportLicense.setTransportOperation(transportOperation.toString());
					
					IssuingAuthority issuingAuthority = new IssuingAuthority();
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
						issuingAuthority.setSignDate(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));						
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
					
					vlInterRoadTransportLicense.setIssuingAuthority(issuingAuthority);
					
				}
			}
		}

		vlInterRoadTransportLicense.getAttachedFile().addAll(lstFiles);
		envelope.getBody().getContent().setVLInterRoadTransportLicence(vlInterRoadTransportLicense);

		String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

		model.setRawMessage(rawMessage);
		
		return model;
	}
}
