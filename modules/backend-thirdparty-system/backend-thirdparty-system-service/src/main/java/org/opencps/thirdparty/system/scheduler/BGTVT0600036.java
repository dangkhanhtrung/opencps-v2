package org.opencps.thirdparty.system.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.opencps.auth.utils.APIDateTimeUtils;
import org.opencps.datamgt.utils.DateTimeUtils;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierAction;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.model.DossierRequestUD;
import org.opencps.dossiermgt.model.ProcessAction;
import org.opencps.dossiermgt.model.ProcessOption;
import org.opencps.dossiermgt.model.ServiceConfig;
import org.opencps.dossiermgt.model.ServiceInfo;
import org.opencps.dossiermgt.model.ServiceProcess;
import org.opencps.dossiermgt.service.DossierActionLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierRequestUDLocalServiceUtil;
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
import org.opencps.thirdparty.system.nsw.model.VTOfficialTransportPermit;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600036 {
	private static String DUMMY_DATA = "1";
	
	public static List<MessageQueueInputModel> convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync, Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		List<MessageQueueInputModel> lstResults = new ArrayList<>();
		
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

		List<DossierRequestUD> lstRequests = DossierRequestUDLocalServiceUtil.getDossierRequestByRT(
				dossier.getCompanyId(),
				dossier.getGroupId(),
				dossier.getDossierId(), 
				"correcting");
		List<String> lstLicenceNos = new ArrayList<>();
		
		if (!lstRequests.isEmpty()) {
			DossierRequestUD dossierRequest = lstRequests.get(0);
			String[] temps = StringUtil.split(dossierRequest.getComment());
			if (temps.length > 0) {
				lstLicenceNos.addAll(Arrays.asList(temps));
			}
		}
		
		for (DossierFile dossierFile : dossierFileList) {
			templateNo = dossierFile.getFileTemplateNo();
			partNo = dossierFile.getDossierPartNo();

			String returnDossierFiles = processAction.getReturnDossierFiles();
			String[] returnDossierFilesArr = StringUtil.split(returnDossierFiles);
			for (String returnDossierFile : returnDossierFilesArr) {
				if (templateNo.equals(returnDossierFile)) {
					VTOfficialTransportPermit vtOfficialTransportPermit = new VTOfficialTransportPermit();

					AttachedFile attachedFile = new AttachedFile();
					List<AttachedFile> lstFiles = new ArrayList<>();

					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setContent("");
					model.setSender("BGTVT");
					model.setReceiver("NSW");
					model.setPersonSignature("");
					model.setSystemSignature("");
					model.setStatus(0);
//					model.setMessageId(PortalUUIDUtil.generate());
					model.setMessageId(UUID.randomUUID().toString());
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
					
					vtOfficialTransportPermit.setNameOfCompany(formDataObj.getString("NameOfCompany"));
					vtOfficialTransportPermit.setAddress(formDataObj.getString("Address"));
	
					if (formDataObj.has("Tel")) {
						vtOfficialTransportPermit.setTel(formDataObj.getString("Tel"));						
					}
					else {
						vtOfficialTransportPermit.setTel(DUMMY_DATA);												
					}

					if (formDataObj.has("ContactName")) {
						vtOfficialTransportPermit.setContactName(formDataObj.getString("ContactName"));						
					}
					else {
						vtOfficialTransportPermit.setContactName(formDataObj.getString("NameOfCompany"));												
					}
					
					if (formDataObj.has("RegistrationNumber")) {
						vtOfficialTransportPermit.setRegistrationNumber(formDataObj.getString("RegistrationNumber"));						
					}
					else {
						vtOfficialTransportPermit.setRegistrationNumber(StringPool.BLANK);												
					}

					if (formDataObj.has("Passenger")) {
						vtOfficialTransportPermit.setPassenger(formDataObj.getString("Passenger"));						
					}
					else {
						vtOfficialTransportPermit.setPassenger(StringPool.BLANK);												
					}

					if (formDataObj.has("Capacity")) {
						vtOfficialTransportPermit.setCapacity(formDataObj.getString("Capacity"));						
					}
					else {
						vtOfficialTransportPermit.setCapacity(StringPool.BLANK);												
					}

					if (formDataObj.has("TrademarkCode")) {
						vtOfficialTransportPermit.setTrademarkCode(formDataObj.getString("TrademarkCode"));						
					}
					else {
						vtOfficialTransportPermit.setTrademarkCode(StringPool.BLANK);												
					}

					if (formDataObj.has("TrademarkName")) {
						vtOfficialTransportPermit.setTrademarkName(formDataObj.getString("TrademarkName"));						
					}
					else {
						vtOfficialTransportPermit.setTrademarkName(StringPool.BLANK);												
					}

					if (formDataObj.has("VehicleColor")) {
						vtOfficialTransportPermit.setVehicleColor(formDataObj.getString("VehicleColor"));						
					}
					else {
						vtOfficialTransportPermit.setVehicleColor(StringPool.BLANK);												
					}

					if (formDataObj.has("EngineNumber")) {
						vtOfficialTransportPermit.setEngineNumber(formDataObj.getString("EngineNumber"));						
					}
					else {
						vtOfficialTransportPermit.setEngineNumber(StringPool.BLANK);												
					}

					if (formDataObj.has("ChassisNumber")) {
						vtOfficialTransportPermit.setChassisNumber(formDataObj.getString("ChassisNumber"));						
					}
					else {
						vtOfficialTransportPermit.setChassisNumber(StringPool.BLANK);												
					}

					if (formDataObj.has("Purpose")) {
						vtOfficialTransportPermit.setPurpose(formDataObj.getString("Purpose"));						
					}
					else {
						vtOfficialTransportPermit.setPurpose(StringPool.BLANK);												
					}

					if (formDataObj.has("NameOfCompany")) {
						vtOfficialTransportPermit.setNameOfCompany(formDataObj.getString("NameOfCompany"));						
					}
					else {
						vtOfficialTransportPermit.setNameOfCompany(StringPool.BLANK);												
					}
					if (formDataObj.has("DepartureFrom")) {
						vtOfficialTransportPermit.setDepartureFrom(formDataObj.getString("DepartureFrom"));						
					}
					else {
						vtOfficialTransportPermit.setDepartureFrom(StringPool.BLANK);												
					}
					if (formDataObj.has("DepartureTo")) {
						vtOfficialTransportPermit.setDepartureTo(formDataObj.getString("DepartureTo"));						
					}
					else {
						vtOfficialTransportPermit.setDepartureTo(StringPool.BLANK);												
					}
					if (formDataObj.has("FromParkingLot")) {
						vtOfficialTransportPermit.setFromParkingLot(formDataObj.getString("FromParkingLot"));						
					}
					else {
						vtOfficialTransportPermit.setFromParkingLot(StringPool.BLANK);												
					}
					if (formDataObj.has("FromProvinceCode")) {
						vtOfficialTransportPermit.setFromProvinceCode(formDataObj.getString("FromProvinceCode"));						
					}
					else {
						vtOfficialTransportPermit.setFromProvinceCode(DUMMY_DATA);												
					}
					if (formDataObj.has("FromProvinceName")) {
						vtOfficialTransportPermit.setFromProvinceName(formDataObj.getString("FromProvinceName"));						
					}
					else {
						vtOfficialTransportPermit.setFromProvinceName(DUMMY_DATA);												
					}
					if (formDataObj.has("ToParkingLot")) {
						vtOfficialTransportPermit.setToParkingLot(formDataObj.getString("ToParkingLot"));						
					}
					else {
						vtOfficialTransportPermit.setToParkingLot(DUMMY_DATA);												
					}
					if (formDataObj.has("ToProvinceCode")) {
						vtOfficialTransportPermit.setToProvinceCode(formDataObj.getString("ToProvinceCode"));						
					}
					else {
						vtOfficialTransportPermit.setToProvinceCode(DUMMY_DATA);												
					}
					if (formDataObj.has("ToProvinceName")) {
						vtOfficialTransportPermit.setToProvinceName(formDataObj.getString("ToProvinceName"));						
					}
					else {
						vtOfficialTransportPermit.setToProvinceName(DUMMY_DATA);												
					}
					if (formDataObj.has("Routes")) {
						vtOfficialTransportPermit.setRoutes(formDataObj.getString("Routes"));						
					}
					else {
						vtOfficialTransportPermit.setRoutes(StringPool.BLANK);												
					}
					if (!dossier.getServiceCode().equals("BGTVT0600041")) {
						if (formDataObj.has("GoodsType")) {
							vtOfficialTransportPermit.setGoodsType(formDataObj.getString("GoodsType"));						
						}
						else {
							vtOfficialTransportPermit.setGoodsType(StringPool.BLANK);												
						}						
					}
					if (formDataObj.has("Stops")) {
						vtOfficialTransportPermit.setStops(formDataObj.getString("Stops"));						
					}
					else {
						vtOfficialTransportPermit.setStops(DUMMY_DATA);												
					}
					if (formDataObj.has("LicenceType")) {
						vtOfficialTransportPermit.setLicenceType(formDataObj.getString("LicenceType"));						
					}
					else {
						vtOfficialTransportPermit.setLicenceType(DUMMY_DATA);												
					}
					if (formDataObj.has("LicenceNo")) {
						vtOfficialTransportPermit.setLicenceNo(formDataObj.getString("LicenceNo"));						
					}
					else {
						vtOfficialTransportPermit.setLicenceNo(DUMMY_DATA);												
					}
					if (formDataObj.has("ValidFrom")) {
						String validFromStr = formDataObj.getString("ValidFrom");
						Date validFromDate = DateTimeUtils.convertStringToDate(validFromStr);

						if (Validator.isNotNull(validFromDate)) {
							vtOfficialTransportPermit.setValidFrom(
									DateTimeUtils.convertDateToString(validFromDate, DateTimeUtils._NSW_DATE_TIME_FORMAT)
									);						
							
						}
						else {
							vtOfficialTransportPermit.setValidFrom(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));																			
						}
					}
					else {
						Date now = new Date();
						vtOfficialTransportPermit.setValidFrom(DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));												
					}

					if (formDataObj.has("ValidUntil")) {
						String validToStr = formDataObj.getString("ValidUntil");
						Date validToDate = DateTimeUtils.convertStringToDate(validToStr);

						vtOfficialTransportPermit.setValidUntil(
								DateTimeUtils.convertDateToString(validToDate, DateTimeUtils._NSW_DATE_TIME_FORMAT)
								);						

					}
					else {
						Date now = new Date();
						vtOfficialTransportPermit.setValidUntil(DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));												
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
					
					vtOfficialTransportPermit.setIssuingAuthority(issuingAuthority);										
					vtOfficialTransportPermit.getAttachedFile().addAll(lstFiles);
					envelope.getBody().getContent().setVTOfficialTransportPermit(vtOfficialTransportPermit);

					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

					model.setRawMessage(rawMessage);
					lstResults.add(model);						
					
//					if (lstLicenceNos.isEmpty()) {
//						lstResults.add(model);						
//					}
//					else {
//						if (formDataObj.has("LicenceNo") && Validator.isNotNull(formDataObj.getString("LicenceNo"))) {
//							if (lstLicenceNos.contains(formDataObj.getString("LicenceNo"))) {
//								lstResults.add(model);
//							}
//						}
//					}
				}
			}
		}

		return lstResults;
	}
}
