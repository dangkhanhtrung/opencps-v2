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
import org.opencps.thirdparty.system.nsw.model.ApprovalOfVTFixRoadTransport;
import org.opencps.thirdparty.system.nsw.model.ApprovalOfVTFixRoadTransport.VehicleList;
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
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

public class BGTVT0600044 {
	private static String DUMMY_DATA = "1";

	public static MessageQueueInputModel convertResult(Dossier dossier, ThirdPartyDossierSync dossierSync,
			Envelope envelope, String type, String function) throws PortalException {
		long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);

		ApprovalOfVTFixRoadTransport approvalOfVTFixRoadTransport = new ApprovalOfVTFixRoadTransport();
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
					approvalOfVTFixRoadTransport.setOfficialDispatchNo(formDataObj.getString("OfficialDispatchNo"));

					if (formDataObj.has("IssuingDispatchNo")) {
						approvalOfVTFixRoadTransport.setIssuingDispatchNo(formDataObj.getString("IssuingDispatchNo"));
					} else {
						approvalOfVTFixRoadTransport.setIssuingDispatchNo(StringPool.BLANK);
					}
					if (formDataObj.has("OfficialDispatchNo")) {
						approvalOfVTFixRoadTransport.setOfficialDispatchNo(formDataObj.getString("OfficialDispatchNo"));
					} else {
						approvalOfVTFixRoadTransport.setOfficialDispatchNo(StringPool.BLANK);
					}
					if (formDataObj.has("OfficialDispatchDate")) {
						String officialDispatchDateStr = formDataObj.getString("OfficialDispatchDate");
						Date officialDispatchDate = DateTimeUtils.convertStringToDate(officialDispatchDateStr);

						approvalOfVTFixRoadTransport.setOfficialDispatchDate(DateTimeUtils
								.convertDateToString(officialDispatchDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					} else {
						approvalOfVTFixRoadTransport.setOfficialDispatchDate(StringPool.BLANK);
					}
					if (formDataObj.has("NameOfCompany")) {
						approvalOfVTFixRoadTransport.setNameOfCompany(formDataObj.getString("NameOfCompany"));
					} else {
						approvalOfVTFixRoadTransport.setNameOfCompany(StringPool.BLANK);
					}
					if (formDataObj.has("DepartureFrom")) {
						approvalOfVTFixRoadTransport.setDepartureFrom(formDataObj.getString("DepartureFrom"));
					} else {
						approvalOfVTFixRoadTransport.setDepartureFrom(StringPool.BLANK);
					}
					if (formDataObj.has("DepartureTo")) {
						approvalOfVTFixRoadTransport.setDepartureTo(formDataObj.getString("DepartureTo"));
					} else {
						approvalOfVTFixRoadTransport.setDepartureTo(StringPool.BLANK);
					}
					if (formDataObj.has("FromParkingLot")) {
						approvalOfVTFixRoadTransport.setFromParkingLot(formDataObj.getString("FromParkingLot"));
					} else {
						approvalOfVTFixRoadTransport.setFromParkingLot(StringPool.BLANK);
					}
					if (formDataObj.has("FromProvinceCode")) {
						approvalOfVTFixRoadTransport.setFromProvinceCode(formDataObj.getString("FromProvinceCode"));
					} else {
						approvalOfVTFixRoadTransport.setFromProvinceCode(DUMMY_DATA);
					}
					if (formDataObj.has("FromProvinceName")) {
						approvalOfVTFixRoadTransport.setFromProvinceName(formDataObj.getString("FromProvinceName"));
					} else {
						approvalOfVTFixRoadTransport.setFromProvinceName(DUMMY_DATA);
					}
					if (formDataObj.has("ToParkingLot")) {
						approvalOfVTFixRoadTransport.setToParkingLot(formDataObj.getString("ToParkingLot"));
					} else {
						approvalOfVTFixRoadTransport.setToParkingLot(DUMMY_DATA);
					}
					if (formDataObj.has("ToProvinceCode")) {
						approvalOfVTFixRoadTransport.setToProvinceCode(formDataObj.getString("ToProvinceCode"));
					} else {
						approvalOfVTFixRoadTransport.setToProvinceCode(DUMMY_DATA);
					}
					if (formDataObj.has("ToProvinceName")) {
						approvalOfVTFixRoadTransport.setToProvinceName(formDataObj.getString("ToProvinceName"));
					} else {
						approvalOfVTFixRoadTransport.setToProvinceName(DUMMY_DATA);
					}
					if (formDataObj.has("ItemNo")) {
						approvalOfVTFixRoadTransport.setItemNo(formDataObj.getString("ItemNo"));
					} else {
						approvalOfVTFixRoadTransport.setItemNo(StringPool.BLANK);
					}
					if (formDataObj.has("CircularNo")) {
						approvalOfVTFixRoadTransport.setCircularNo(formDataObj.getString("CircularNo"));
					} else {
						approvalOfVTFixRoadTransport.setCircularNo(StringPool.BLANK);
					}
					if (formDataObj.has("CircularDate")) {
						String circularDateStr = formDataObj.getString("CircularDate");
						Date circularDate = DateTimeUtils.convertStringToDate(circularDateStr);

						approvalOfVTFixRoadTransport.setCircularDate(
								DateTimeUtils.convertDateToString(circularDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));

					} else {
						Date now = new Date();
						approvalOfVTFixRoadTransport.setCircularDate(
								DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					}
					if (formDataObj.has("NumberOfVehicle")) {
						approvalOfVTFixRoadTransport.setNumberOfVehicle(formDataObj.getString("NumberOfVehicle"));
					} else {
						approvalOfVTFixRoadTransport.setNumberOfVehicle(StringPool.BLANK);
					}
					if (formDataObj.has("ToVNAuthority")) {
						approvalOfVTFixRoadTransport.setToVNAuthority(formDataObj.getString("ToVNAuthority"));
					} else {
						approvalOfVTFixRoadTransport.setToVNAuthority(StringPool.BLANK);
					}
					if (formDataObj.has("ToTQAuthority")) {
						approvalOfVTFixRoadTransport.setToTQAuthority(formDataObj.getString("ToTQAuthority"));
					} else {
						approvalOfVTFixRoadTransport.setToTQAuthority(StringPool.BLANK);
					}
					if (formDataObj.has("AtParkingLot")) {
						approvalOfVTFixRoadTransport.setAtParkingLot(formDataObj.getString("AtParkingLot"));
					} else {
						approvalOfVTFixRoadTransport.setAtParkingLot(DUMMY_DATA);
					}
					if (formDataObj.has("AtProvinceName")) {
						approvalOfVTFixRoadTransport.setAtProvinceName(formDataObj.getString("AtProvinceName"));
					} else {
						approvalOfVTFixRoadTransport.setAtProvinceName(DUMMY_DATA);
					}

					IssuingAuthority issuingAuthority = new IssuingAuthority();
					Date now = new Date();

					if (formDataObj.has("SignName")) {
						issuingAuthority.setSignName(formDataObj.getString("SignName"));
					} else {
						issuingAuthority.setSignName("Nguyễn Tô An");
					}
					if (formDataObj.has("SignDate")) {
						String signDateStr = formDataObj.getString("SignDate");
						Date signDate = DateTimeUtils.convertStringToDate(signDateStr);

						issuingAuthority.setSignDate(
								DateTimeUtils.convertDateToString(signDate, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					} else {
						issuingAuthority.setSignDate(
								DateTimeUtils.convertDateToString(now, DateTimeUtils._NSW_DATE_TIME_FORMAT));
					}
					if (formDataObj.has("SignPlace")) {
						issuingAuthority.setSignPlace(formDataObj.getString("SignPlace"));
					} else {
						issuingAuthority.setSignPlace("Hà Nội");
					}
					if (formDataObj.has("SignTitle")) {
						issuingAuthority.setSignTitle(formDataObj.getString("SignTitle"));
					} else {
						issuingAuthority.setSignTitle("Cục trưởng");
					}

					approvalOfVTFixRoadTransport.setIssuingAuthority(issuingAuthority);

					if (formDataObj.has("VehicleList")) {
						List<VehicleList> lstVehicleList = new ArrayList<>();
						JSONArray vehicleArr = JSONFactoryUtil.createJSONArray(formDataObj.getString("VehicleList"));
						for (int i = 0; i < vehicleArr.length(); i++) {
							JSONObject vehicleObj = vehicleArr.getJSONObject(i);
							VehicleList vl = new VehicleList();
							if (vehicleObj.has("SequenceNo")) {
								vl.setSequenceNo(vehicleObj.getString("SequenceNo"));
							} else {
								vl.setSequenceNo(DUMMY_DATA);
							}
							if (vehicleObj.has("RegistrationNumber")) {
								vl.setRegistrationNumber(vehicleObj.getString("RegistrationNumber"));
							} else {
								vl.setRegistrationNumber(DUMMY_DATA);
							}
							if (vehicleObj.has("RegistrationDriver")) {
								vl.setRegistrationDriver(vehicleObj.getString("RegistrationDriver"));
							} else {
								vl.setRegistrationDriver(DUMMY_DATA);
							}
							if (vehicleObj.has("VehicleModelCode")) {
								vl.setVehicleModelCode(vehicleObj.getString("VehicleModelCode"));
							} else {
								vl.setVehicleModelCode(DUMMY_DATA);
							}
							if (vehicleObj.has("VehicleModelName")) {
								vl.setVehicleModelName(vehicleObj.getString("VehicleModelName"));
							} else {
								vl.setVehicleModelName(DUMMY_DATA);
							}
							if (vehicleObj.has("Capacity")) {
								vl.setCapacity(vehicleObj.getString("Capacity"));
							} else {
								vl.setCapacity(DUMMY_DATA);
							}
							if (vehicleObj.has("ManufacturedYear")) {
								vl.setManufacturedYear(vehicleObj.getString("ManufacturedYear"));
							} else {
								vl.setManufacturedYear(DUMMY_DATA);
							}
							if (vehicleObj.has("VehicleExpImpGateCode")) {
								vl.setVehicleExpImpGateCode(vehicleObj.getString("VehicleExpImpGateCode"));
							} else {
								vl.setVehicleExpImpGateCode(DUMMY_DATA);
							}
							if (vehicleObj.has("VehicleExpImpGate")) {
								vl.setVehicleExpImpGate(vehicleObj.getString("VehicleExpImpGate"));
							} else {
								vl.setVehicleExpImpGate(DUMMY_DATA);
							}
							if (dossier.getServiceCode().equals("BGTVT0600013")) {
								vl.setOperationType("1");
							} else if (dossier.getServiceCode().equals("BGTVT0600014")) {
								vl.setOperationType("2");
							} else if (dossier.getServiceCode().equals("BGTVT0600015")) {
								vl.setOperationType("3");
							}
							if (dossier.getServiceCode().equals("BGTVT0600030")) {
								vl.setOperationType("1");
							} else if (dossier.getServiceCode().equals("BGTVT0600031")) {
								vl.setOperationType("2");
							} else if (dossier.getServiceCode().equals("BGTVT0600032")) {
								vl.setOperationType("3");
							}

							lstVehicleList.add(vl);
						}

						approvalOfVTFixRoadTransport.getVehicleList().addAll(lstVehicleList);
					}
				}
			}

		}
		approvalOfVTFixRoadTransport.getAttachedFile().addAll(lstFiles);
		envelope.getBody().getContent().setApprovalOfVTFixRoadTransport(approvalOfVTFixRoadTransport);

		String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

		model.setRawMessage(rawMessage);

		return model;
	}
}
