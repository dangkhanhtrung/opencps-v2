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
import org.opencps.thirdparty.system.nsw.model.CLVNonCommercialCrossBorderTransportPermit;
import org.opencps.thirdparty.system.nsw.model.Envelope;
import org.opencps.thirdparty.system.nsw.model.IssuingAuthority;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600052 {
	private static String DUMMY_DATA = "1";

	public static MessageQueueInputModel convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync,
			Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		CLVNonCommercialCrossBorderTransportPermit clvNonCommercialCrossBorderTransportPermit = new CLVNonCommercialCrossBorderTransportPermit();
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

		List<DossierFile> dossierFileList = DossierFileLocalServiceUtil.getAllDossierFile(dossier.getDossierId());

		String templateNo = StringPool.BLANK;
		String partNo = StringPool.BLANK;
		int partType = 2;
		boolean eSign = true;
		String deliverableCode = StringPool.BLANK;

		ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getByCode(dossier.getGroupId(), dossier.getServiceCode());
		ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.getBySICodeAndGAC(dossier.getGroupId(),
				serviceInfo.getServiceCode(), dossier.getGovAgencyCode());

		ProcessOption processOption = ProcessOptionLocalServiceUtil.getByDTPLNoAndServiceCF(dossier.getGroupId(),
				dossier.getDossierTemplateNo(), serviceConfig.getServiceConfigId());

		ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
				.fetchServiceProcess(processOption.getServiceProcessId());
		DossierAction dossierAction = DossierActionLocalServiceUtil.fetchDossierAction(dossierActionId);

		ProcessAction processAction = ProcessActionLocalServiceUtil.fetchBySPID_AC(serviceProcess.getServiceProcessId(),
				dossierAction.getActionCode());

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

					String linkCongVan = jaxRsPublicUrl + "dossiers/" + dossier.getDossierId() + "/files/"
							+ dossierFile.getReferenceUid();
					if (dossier.getPassword() != null) {
						linkCongVan += "/public/" + dossier.getPassword();
					}
					attachedFile.setFileURL(linkCongVan);
					JSONObject formDataObj = JSONFactoryUtil.createJSONObject(dossierFile.getFormData());
					
					if (formDataObj.has("RegistrationNumber")) {
						clvNonCommercialCrossBorderTransportPermit.setRegistrationNumber(formDataObj.getString("RegistrationNumber"));
					}
					if (formDataObj.has("ManufacturedYear")) {
						String manufacturedYearStr = formDataObj.getString("ManufacturedYear");
						Date manufacturedYear = DateTimeUtils.convertStringToDate(manufacturedYearStr);

						clvNonCommercialCrossBorderTransportPermit.setManufacturedYear(
								DateTimeUtils.convertDateToString(manufacturedYear, DateTimeUtils._NSW_DATE_TIME_FORMAT));

					} else {
						Date now = new Date();
						clvNonCommercialCrossBorderTransportPermit.setManufacturedYear(
								DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					}
					
					if (formDataObj.has("TrademarkCode")) {
						clvNonCommercialCrossBorderTransportPermit.setTrademarkCode(formDataObj.getString("TrademarkCode"));
					}
					if (formDataObj.has("TrademarkName")) {
						clvNonCommercialCrossBorderTransportPermit.setTrademarkName(formDataObj.getString("TrademarkName"));
					}
					if (formDataObj.has("Model")) {
						clvNonCommercialCrossBorderTransportPermit.setModel(formDataObj.getString("Model"));
					}
					
					if (formDataObj.has("Truck")) {
						clvNonCommercialCrossBorderTransportPermit.getVehicleType().setTruck(formDataObj.getString("Truck"));
					}
					if (formDataObj.has("Bus")) {
						clvNonCommercialCrossBorderTransportPermit.getVehicleType().setBus(formDataObj.getString("Bus"));
					}
					if (formDataObj.has("Others")) {
						clvNonCommercialCrossBorderTransportPermit.getVehicleType().setOthers(formDataObj.getString("Others"));
					}
					
					if (formDataObj.has("VehicleColor")) {
						clvNonCommercialCrossBorderTransportPermit.setVehicleColor(formDataObj.getString("VehicleColor"));
					}
					if (formDataObj.has("EngineNumber")) {
						clvNonCommercialCrossBorderTransportPermit.setEngineNumber(formDataObj.getString("EngineNumber"));
					}
					if (formDataObj.has("ChassisNumber")) {
						clvNonCommercialCrossBorderTransportPermit.setChassisNumber(formDataObj.getString("ChassisNumber"));
					}
					if (formDataObj.has("NameOfCompany")) {
						clvNonCommercialCrossBorderTransportPermit.setNameOfCompany(formDataObj.getString("NameOfCompany"));
					}
					if (formDataObj.has("Address")) {
						clvNonCommercialCrossBorderTransportPermit.setAddress(formDataObj.getString("Address"));
					}
					if (formDataObj.has("Tel")) {
						clvNonCommercialCrossBorderTransportPermit.setTel(formDataObj.getString("Tel"));
					}
					if (formDataObj.has("Fax")) {
						clvNonCommercialCrossBorderTransportPermit.setFax(formDataObj.getString("Fax"));
					}
					if (formDataObj.has("VehicleOwner")) {
						clvNonCommercialCrossBorderTransportPermit.setVehicleOwner(formDataObj.getString("VehicleOwner"));
					}
					if (formDataObj.has("VehicleOwnerAddress")) {
						clvNonCommercialCrossBorderTransportPermit.setVehicleOwnerAddress(formDataObj.getString("VehicleOwnerAddress"));
					}
					if (formDataObj.has("VehicleOwnerTel")) {
						clvNonCommercialCrossBorderTransportPermit.setVehicleOwnerTel(formDataObj.getString("VehicleOwnerTel"));
					}
					if (formDataObj.has("VehicleOwnerFax")) {
						clvNonCommercialCrossBorderTransportPermit.setVehicleOwnerFax(formDataObj.getString("VehicleOwnerFax"));
					}
					if (formDataObj.has("LicenceNo")) {
						clvNonCommercialCrossBorderTransportPermit.setLicenceNo(formDataObj.getString("LicenceNo"));
					}
					if (formDataObj.has("ExpiredDate")) {
						String expiredDaterStr = formDataObj.getString("ExpiredDate");
						Date expiredDate = DateTimeUtils.convertStringToDate(expiredDaterStr);

						clvNonCommercialCrossBorderTransportPermit.setExpiredDate(
								DateTimeUtils.convertDateToString(expiredDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));

					} else {
						Date now = new Date();
						clvNonCommercialCrossBorderTransportPermit.setExpiredDate(
								DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					}
					if (formDataObj.has("ExpImpGateCode")) {
						clvNonCommercialCrossBorderTransportPermit.setExpImpGateCode(formDataObj.getString("ExpImpGateCode"));
					}
					if (formDataObj.has("ExpImpGate")) {
						clvNonCommercialCrossBorderTransportPermit.setExpImpGate(formDataObj.getString("ExpImpGate"));
					}
					if (formDataObj.has("TravelingArea")) {
						clvNonCommercialCrossBorderTransportPermit.setTravelingArea(formDataObj.getString("TravelingArea"));
					}
					if (formDataObj.has("Routes")) {
						clvNonCommercialCrossBorderTransportPermit.setRoutes(formDataObj.getString("Routes"));
					}
					if (formDataObj.has("GoodsPassengerTransport")) {
						clvNonCommercialCrossBorderTransportPermit.setGoodsPassengerTransport(formDataObj.getString("GoodsPassengerTransport"));
					}
					if (formDataObj.has("OtherRemarks")) {
						clvNonCommercialCrossBorderTransportPermit.setOtherRemarks(formDataObj.getString("OtherRemarks"));
					}
					
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
					
					clvNonCommercialCrossBorderTransportPermit.setIssuingAuthority(issuingAuthority);
					
				}
			}

		}
		clvNonCommercialCrossBorderTransportPermit.getAttachedFile().addAll(lstFiles);
		envelope.getBody().getContent().setCLVNonCommercialCrossBorderTransportPermit(clvNonCommercialCrossBorderTransportPermit);
		
		String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

		model.setRawMessage(rawMessage);

		return model;
	}
}
