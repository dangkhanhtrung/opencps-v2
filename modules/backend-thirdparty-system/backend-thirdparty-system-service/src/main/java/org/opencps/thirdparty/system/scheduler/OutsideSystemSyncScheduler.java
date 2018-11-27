package org.opencps.thirdparty.system.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.opencps.dossiermgt.service.DeliverableLocalService;
import org.opencps.dossiermgt.service.DossierActionLocalService;
import org.opencps.dossiermgt.service.DossierActionLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLocalService;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalService;
import org.opencps.dossiermgt.service.DossierRequestUDLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierSyncLocalService;
import org.opencps.dossiermgt.service.ProcessActionLocalServiceUtil;
import org.opencps.dossiermgt.service.ProcessOptionLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceConfigLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceInfoLocalServiceUtil;
import org.opencps.dossiermgt.service.ServiceProcessLocalServiceUtil;
import org.opencps.thirdparty.system.constants.SyncServerTerm;
import org.opencps.thirdparty.system.messagequeue.model.MessageQueueDetailModel;
import org.opencps.thirdparty.system.messagequeue.model.MessageQueueInputModel;
import org.opencps.thirdparty.system.model.ThirdPartyDossierSync;
import org.opencps.thirdparty.system.nsw.model.Body;
import org.opencps.thirdparty.system.nsw.model.Content;
import org.opencps.thirdparty.system.nsw.model.Envelope;
import org.opencps.thirdparty.system.nsw.model.From;
import org.opencps.thirdparty.system.nsw.model.Header;
import org.opencps.thirdparty.system.nsw.model.KetQuaXuLy;
import org.opencps.thirdparty.system.nsw.model.NSWRequest;
import org.opencps.thirdparty.system.nsw.model.PhanhoiYeucauSua;
import org.opencps.thirdparty.system.nsw.model.RequestPayload;
import org.opencps.thirdparty.system.nsw.model.Subject;
import org.opencps.thirdparty.system.nsw.model.To;
import org.opencps.thirdparty.system.rest.client.OpenCPSRestClient;
import org.opencps.thirdparty.system.rest.client.PrefsProperties;
import org.opencps.thirdparty.system.service.ThirdPartyDossierSyncLocalService;
import org.opencps.thirdparty.system.util.OutsideSystemConverter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseSchedulerEntryMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelper;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.scheduler.TriggerFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

@Component(immediate = true, service = OutsideSystemSyncScheduler.class)
public class OutsideSystemSyncScheduler extends BaseSchedulerEntryMessageListener {
	@Override
	protected void doReceive(Message message) throws Exception {
		_log.info("Starting sync with third party system is starting at  : "
				+ APIDateTimeUtils.convertDateToString(new Date()));

		List<ThirdPartyDossierSync> lstSyncs = _thirdPartyDossierSyncLocalService.findAll(QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		for (ThirdPartyDossierSync sync : lstSyncs) {
			try {
				doSync(sync);
			} catch(Exception e) {
				_log.error(e);
			}
		}

		_log.info("Sync with third party system finished at  : " + APIDateTimeUtils.convertDateToString(new Date()));
	}
	
	private void doSync(ThirdPartyDossierSync sync) throws PortalException {
		OpenCPSRestClient client = new OpenCPSRestClient(PrefsProperties.getJaxRsUrl());
		String jaxRsPublicUrl = PrefsPropsUtil.getString(SyncServerTerm.JAXRS_PUBLIC_URL);
		
		if (sync.getMethod() == 0) {
			Dossier dossier = _dossierLocalService.fetchDossier(sync.getDossierId());
			ThirdPartyDossierSync dossierSync = _thirdPartyDossierSyncLocalService
					.fetchThirdPartyDossierSync(sync.getDossierSyncId());
			
			long dossierActionId = dossierSync.getMethod() == 0 ? dossierSync.getClassPK() : 0;
			
			_log.info("===sync===" + sync.getDossierId() + "="+ dossierSync.getMethod() + "=" + dossierSync.getClassPK());

			NSWRequest nswRequest = new NSWRequest();
			RequestPayload requestPayload = new RequestPayload();

			nswRequest.setRequestPayload(requestPayload);

			nswRequest.setOfficeCode("BGTVT");
			Envelope envelope = new Envelope();
			Header header = new Header();
			envelope.setHeader(header);
			requestPayload.setEnvelope(envelope);
			From from = new From();
			from.setName("Bộ giao thông vận tải");
			from.setCountryCode("VN");
			from.setIdentity("BGTVT");
			from.setMinistryCode("BGTVT");
			from.setOrganizationCode("TCDBVN");
			if (!dossier.getGovAgencyCode().equals("TCDBVN")) {
				from.setUnitCode(dossier.getGovAgencyCode());					
			}
			else {
				from.setUnitCode(StringPool.BLANK);
			}
			header.setFrom(from);
			To to = new To();
			to.setName(dossier.getApplicantName());
			to.setIdentity("BGTVT");
			to.setMinistryCode("BTC");
			to.setOrganizationCode("BTC");
			to.setUnitCode("BTC");
			to.setCountryCode("VN");
			
			header.setTo(to);
			org.opencps.thirdparty.system.nsw.model.Reference reference = new org.opencps.thirdparty.system.nsw.model.Reference();

			reference.setVersion("1");
			reference.setMessageId(PortalUUIDUtil.generate());

			header.setReference(reference);

			Subject subject = new Subject();
			subject.setDocumentType(dossier.getServiceCode());

			header.setSubject(subject);
			subject.setReference(dossier.getReferenceUid());
			subject.setPreReference(dossier.getReferenceUid());
			Calendar cal = Calendar.getInstance();

			if (dossier.getReceiveDate() != null) {
				cal.setTime(dossier.getReceiveDate());
				subject.setDocumentYear(cal.get(Calendar.YEAR));
			} else {
				cal.setTime(dossier.getCreateDate());
				subject.setDocumentYear(cal.get(Calendar.YEAR));
			}
			Body body = new Body();
			envelope.setBody(body);
			Content content = new Content();
			body.setContent(content);

			DossierAction dossierAction = _dossierActionLocalService.fetchDossierAction(dossierActionId);
			
			org.opencps.thirdparty.system.nsw.vt.model.NSWRequest nswRequest2 = new org.opencps.thirdparty.system.nsw.vt.model.NSWRequest();
			org.opencps.thirdparty.system.nsw.vt.model.RequestPayload requestPayload2 = new org.opencps.thirdparty.system.nsw.vt.model.RequestPayload();

			nswRequest2.setRequestPayload(requestPayload2);

			nswRequest2.setOfficeCode("BGTVT");
			org.opencps.thirdparty.system.nsw.vt.model.Envelope envelope2 = new org.opencps.thirdparty.system.nsw.vt.model.Envelope();
			org.opencps.thirdparty.system.nsw.vt.model.Header header2 = new org.opencps.thirdparty.system.nsw.vt.model.Header();
			envelope2.setHeader(header2);
			requestPayload2.setEnvelope(envelope2);
			org.opencps.thirdparty.system.nsw.vt.model.From from2 = new org.opencps.thirdparty.system.nsw.vt.model.From();
			from2.setName("Bộ giao thông vận tải");
			from2.setCountryCode("VN");
			from2.setIdentity("BGTVT");
			from2.setMinistryCode("BGTVT");
			from2.setOrganizationCode("TCDBVN");
			if (!dossier.getGovAgencyCode().equals("TCDBVN")) {
				from2.setUnitCode(dossier.getGovAgencyCode());					
			}
			else {
				from2.setUnitCode(StringPool.BLANK);
			}
			header2.setFrom(from2);
			org.opencps.thirdparty.system.nsw.vt.model.To to2 = new org.opencps.thirdparty.system.nsw.vt.model.To();
			to2.setName(dossier.getApplicantName());
			to2.setIdentity("BGTVT");
			to2.setMinistryCode("BTC");
			to2.setOrganizationCode("BTC");
			to2.setUnitCode("BTC");
			to2.setCountryCode("VN");
			
			header2.setTo(to2);
			org.opencps.thirdparty.system.nsw.vt.model.Reference reference2 = new org.opencps.thirdparty.system.nsw.vt.model.Reference();

			reference2.setVersion("1");
			reference2.setMessageId(PortalUUIDUtil.generate());

			header2.setReference(reference2);

			org.opencps.thirdparty.system.nsw.vt.model.Subject subject2 = new org.opencps.thirdparty.system.nsw.vt.model.Subject();
			subject.setDocumentType(dossier.getServiceCode());

			header2.setSubject(subject2);
			subject2.setReference(dossier.getReferenceUid());
			subject2.setPreReference(dossier.getReferenceUid());

			if (dossier.getReceiveDate() != null) {
				cal.setTime(dossier.getReceiveDate());
				subject2.setDocumentYear(cal.get(Calendar.YEAR));
			} else {
				cal.setTime(dossier.getCreateDate());
				subject2.setDocumentYear(cal.get(Calendar.YEAR));
			}
			org.opencps.thirdparty.system.nsw.vt.model.Body body2 = new org.opencps.thirdparty.system.nsw.vt.model.Body();
			envelope2.setBody(body2);
			org.opencps.thirdparty.system.nsw.vt.model.Content content2 = new org.opencps.thirdparty.system.nsw.vt.model.Content();
			body2.setContent(content2);
			
			if (dossierAction != null) {
				if (dossierAction.getSyncActionCode().equals("1105")) {
					nswRequest.setDocumentType(dossier.getServiceCode());

					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("11");
					subject.setFunction("05");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					KetQuaXuLy ketqua = new KetQuaXuLy();
					content.setKetQuaXuLy(ketqua);
					
					String[] nhomGiaHan = new String[]{"BGTVT0600018","BGTVT0600035","BGTVT0600057","BGTVT0600065","BGTVT0600047"};
					if(!ArrayUtil.contains(nhomGiaHan, dossier.getServiceCode())) {
						ketqua.setSoGp(StringPool.BLANK);
					}
					
					if (dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600018")
							|| dossier.getServiceCode().equals("BGTVT0600035")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							|| dossier.getServiceCode().equals("BGTVT0600065")
							|| dossier.getServiceCode().equals("BGTVT0600047")
							) {
						ketqua.setSoTn(dossier.getDossierNo());
					}
					
					ketqua.setNoiDung(dossierAction.getActionNote());
					ketqua.setDonViXuLy(dossier.getGovAgencyName());
					ketqua.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
					ketqua.setLinkCongvan(StringPool.BLANK);
					
//					String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);
					body.setPersonSignature("");
					envelope.setSystemSignature("");
					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);
					
					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("11");
					model.setFunction("05");
					model.setReference(dossier.getReferenceUid());
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));
					model.setRetryCount(0);
					model.setDirection(2);

					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);

						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1106")) {
					nswRequest.setDocumentType(dossier.getServiceCode());

					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("11");
					subject.setFunction("06");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					KetQuaXuLy ketqua = new KetQuaXuLy();
					content.setKetQuaXuLy(ketqua);

					String[] nhomGiaHan = new String[]{"BGTVT0600018","BGTVT0600035","BGTVT0600057","BGTVT0600065","BGTVT0600047"};
					if(!ArrayUtil.contains(nhomGiaHan, dossier.getServiceCode())) {
						ketqua.setSoGp(StringPool.BLANK);
					}
					
					if (dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600018")
							|| dossier.getServiceCode().equals("BGTVT0600035")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							|| dossier.getServiceCode().equals("BGTVT0600065")
							|| dossier.getServiceCode().equals("BGTVT0600047")
							) {
						ketqua.setSoTn(dossier.getDossierNo());
					}						
					ketqua.setNoiDung(dossierAction.getActionNote());
					if (Validator.isNull(ketqua.getNoiDung())) {
						ketqua.setNoiDung("Hồ sơ bị thiếu cần bổ sung!!");
					}
					ketqua.setDonViXuLy(dossier.getGovAgencyName());
					ketqua.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
					ketqua.setLinkCongvan(StringPool.BLANK);
					
					body.setPersonSignature("");
					envelope.setSystemSignature("");

					List<DossierFile> dossierFileList = DossierFileLocalServiceUtil
							.getAllDossierFile(dossier.getDossierId());

					String templateNo = StringPool.BLANK;

					ServiceInfo serviceInfo = ServiceInfoLocalServiceUtil.getByCode(dossier.getGroupId(),
							dossier.getServiceCode());
					ServiceConfig serviceConfig = ServiceConfigLocalServiceUtil.getBySICodeAndGAC(
							dossier.getGroupId(), serviceInfo.getServiceCode(), dossier.getGovAgencyCode());

					ProcessOption processOption = ProcessOptionLocalServiceUtil.getByDTPLNoAndServiceCF(
							dossier.getGroupId(), dossier.getDossierTemplateNo(),
							serviceConfig.getServiceConfigId());

					ServiceProcess serviceProcess = ServiceProcessLocalServiceUtil
							.fetchServiceProcess(processOption.getServiceProcessId());
					ProcessAction processAction = ProcessActionLocalServiceUtil
							.fetchBySPID_AC(serviceProcess.getServiceProcessId(), dossierAction.getActionCode());

					for (DossierFile dossierFile : dossierFileList) {
						templateNo = dossierFile.getFileTemplateNo();

						String returnDossierFiles = processAction.getReturnDossierFiles();
						String[] returnDossierFilesArr = StringUtil.split(returnDossierFiles);
						for (String returnDossierFile : returnDossierFilesArr) {
							if (templateNo.equals(returnDossierFile)) {
								String linkCongVan = jaxRsPublicUrl + "dossiers/" + dossier.getDossierId()
										+ "/files/" + dossierFile.getReferenceUid();
								if (dossier.getPassword() != null) {
									linkCongVan += "/public/" + dossier.getPassword();
								}
								ketqua.setLinkCongvan(linkCongVan);
							}
						}
					}

					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);
					
					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("11");
					model.setFunction("06");
					model.setReference(dossier.getReferenceUid());
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));
					model.setRetryCount(0);
					model.setDirection(2);

					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1114")) {
					nswRequest.setDocumentType(dossier.getServiceCode());

					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("11");
					subject.setFunction("14");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					KetQuaXuLy ketqua = new KetQuaXuLy();
					content.setKetQuaXuLy(ketqua);

					/*String[] nhomGiaHan = new String[]{"BGTVT0600018","BGTVT0600035","BGTVT0600057","BGTVT0600065","BGTVT0600047"};
					if(!ArrayUtil.contains(nhomGiaHan, dossier.getServiceCode())) {
						ketqua.setSoGp(StringPool.BLANK);
					}*/
					
					if (dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")
							|| dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")
							|| dossier.getServiceCode().equals("BGTVT0600056")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							) {
						ketqua.setSoTn(dossier.getDossierNo());
					}
					if (dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")
							|| dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")) {
						ketqua.setSoGp(StringPool.BLANK);
					}
					
					ketqua.setNoiDung(dossierAction.getActionNote());
					ketqua.setDonViXuLy(dossier.getGovAgencyName());
					ketqua.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
					ketqua.setLinkCongvan(StringPool.BLANK);
					
//					String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);

					body.setPersonSignature("");
					envelope.setSystemSignature("");

					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);
					
					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("11");
					model.setFunction("14");
					model.setReference(dossier.getReferenceUid());
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
					model.setRetryCount(0);
					model.setDirection(2);
										
					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1117")) {
					nswRequest.setDocumentType(dossier.getServiceCode());

					List<DossierFile> dossierFileList = DossierFileLocalServiceUtil
							.getAllDossierFile(dossier.getDossierId());


					for (DossierFile dossierFile : dossierFileList) {
						subject.setDocumentType(dossier.getServiceCode());
						subject.setType("11");
						subject.setFunction("17");
						subject.setReference(dossier.getReferenceUid());
						subject.setPreReference(dossier.getReferenceUid());
						subject.setSendDate(
								APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

						KetQuaXuLy ketqua = new KetQuaXuLy();
						content.setKetQuaXuLy(ketqua);
						String templateNo = StringPool.BLANK;
						String partNo = StringPool.BLANK;

						templateNo = dossierFile.getFileTemplateNo();
						partNo = dossierFile.getDossierPartNo();

//						DossierPart part = DossierPartLocalServiceUtil.getByFileTemplateNo(dossierFile.getGroupId(), templateNo);
						
						String[] nhomGiaHan = new String[]{"BGTVT0600018","BGTVT0600035","BGTVT0600057","BGTVT0600065","BGTVT0600047"};
						if(!ArrayUtil.contains(nhomGiaHan, dossier.getServiceCode())) {
							if (dossierFile.getDossierPartType() == 2) {
								ketqua.setSoGp(dossierFile.getDeliverableCode());
							}
							if (Validator.isNull(ketqua.getSoGp())) {
								ketqua.setSoGp("11");
							}
						}
						
						if (dossier.getServiceCode().equals("BGTVT0600042")
								|| dossier.getServiceCode().equals("BGTVT0600043")
								|| dossier.getServiceCode().equals("BGTVT0600018")
								|| dossier.getServiceCode().equals("BGTVT0600035")
								|| dossier.getServiceCode().equals("BGTVT0600057")
								|| dossier.getServiceCode().equals("BGTVT0600065")
								|| dossier.getServiceCode().equals("BGTVT0600047")) {
							ketqua.setSoTn(dossier.getDossierNo());
						}
						
						ketqua.setNoiDung(dossierAction.getActionNote());
						ketqua.setDonViXuLy(dossier.getGovAgencyName());
						ketqua.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
						ketqua.setLinkCongvan(StringPool.BLANK);
						
						body.setPersonSignature("");
						envelope.setSystemSignature("");

//						String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);
						String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

						MessageQueueInputModel model = new MessageQueueInputModel();
						model.setRawMessage(rawMessage);
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
						model.setType("11");
						model.setFunction("17");
						model.setReference(dossier.getReferenceUid());
						cal.setTime(dossier.getCreateDate());
						model.setDocumentYear(cal.get(Calendar.YEAR));

						if (dossier.getReceiveDate() != null) {
							cal.setTime(dossier.getReceiveDate());
							subject.setDocumentYear(cal.get(Calendar.YEAR));
						} else {
							cal.setTime(dossier.getCreateDate());
							subject.setDocumentYear(cal.get(Calendar.YEAR));
						}
						model.setPreReference(dossier.getReferenceUid());
						model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
						model.setRetryCount(0);
						model.setDirection(2);

						client.postMessageQueue(model);
					}
					
					long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

					DossierAction foundAction = DossierActionLocalServiceUtil
							.fetchDossierAction(clientDossierActionId);
					if (foundAction != null) {
						DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
					}

					_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

					_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					
				} else if (dossierAction.getSyncActionCode().equals("1409")) {
					nswRequest.setDocumentType(dossier.getServiceCode());
					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("14");
					subject.setFunction("09");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					PhanhoiYeucauSua phanhoi = new PhanhoiYeucauSua();
					phanhoi.setNoiDung(dossierAction.getActionNote());
					phanhoi.setDonViXuLy(dossier.getGovAgencyName());
					phanhoi.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));

					if (dossier.getServiceCode().equals("BGTVT0600005")
							|| dossier.getServiceCode().equals("BGTVT0600006")
							|| dossier.getServiceCode().equals("BGTVT0600007")
							|| dossier.getServiceCode().equals("BGTVT0600008")
							|| dossier.getServiceCode().equals("BGTVT0600009")
							|| dossier.getServiceCode().equals("BGTVT0600010")
							|| dossier.getServiceCode().equals("BGTVT0600011")
							|| dossier.getServiceCode().equals("BGTVT0600012")
							|| dossier.getServiceCode().equals("BGTVT0600023")
							|| dossier.getServiceCode().equals("BGTVT0600024")
							|| dossier.getServiceCode().equals("BGTVT0600025")
							|| dossier.getServiceCode().equals("BGTVT0600026")
							|| dossier.getServiceCode().equals("BGTVT0600027")
							|| dossier.getServiceCode().equals("BGTVT0600028")
							|| dossier.getServiceCode().equals("BGTVT0600029")
							|| dossier.getServiceCode().equals("BGTVT0600036")
							|| dossier.getServiceCode().equals("BGTVT0600037")
							|| dossier.getServiceCode().equals("BGTVT0600038")
							|| dossier.getServiceCode().equals("BGTVT0600039")
							|| dossier.getServiceCode().equals("BGTVT0600040")
							|| dossier.getServiceCode().equals("BGTVT0600041")
							|| dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600044")
							|| dossier.getServiceCode().equals("BGTVT0600045")
							|| dossier.getServiceCode().equals("BGTVT0600046")
							|| dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")
							|| dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")
							|| dossier.getServiceCode().equals("BGTVT0600056")
							|| dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")
							|| dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")
							|| dossier.getServiceCode().equals("BGTVT0600063")
							|| dossier.getServiceCode().equals("BGTVT0600064")) {
						
						List<DossierRequestUD> lstRequests = DossierRequestUDLocalServiceUtil.getDossierRequestByRT(
								dossier.getCompanyId(),
								dossier.getGroupId(),
								dossier.getDossierId(), 
								"correcting");
						
						if (lstRequests.size() > 0) {
							DossierRequestUD dossierRequest = lstRequests.get(0);
							phanhoi.setSoGp(dossierRequest.getComment());
						}
					}
		
					content.setPhanhoiYeucauSua(phanhoi);
					
					body.setPersonSignature("");
					envelope.setSystemSignature("");

//					String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);
					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("14");
					model.setFunction("09");
					model.setReference(dossier.getReferenceUid());
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));	
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
					model.setRetryCount(0);
					model.setDirection(2);

					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1410")) {
					nswRequest.setDocumentType(dossier.getServiceCode());

					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("14");
					subject.setFunction("10");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					List<DossierRequestUD> lstRequests = DossierRequestUDLocalServiceUtil.getDossierRequestByRT(
							dossier.getCompanyId(),
							dossier.getGroupId(),
							dossier.getDossierId(), 
							"correcting");
					
					PhanhoiYeucauSua phanhoi = new PhanhoiYeucauSua();					
					phanhoi.setNoiDung(dossierAction.getActionNote());
					phanhoi.setDonViXuLy(dossier.getGovAgencyName());
					phanhoi.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
					_log.info("1410: " + dossier.getServiceCode() + "," + lstRequests.size());
					
					if (dossier.getServiceCode().equals("BGTVT0600005")
							|| dossier.getServiceCode().equals("BGTVT0600006")
							|| dossier.getServiceCode().equals("BGTVT0600007")
							|| dossier.getServiceCode().equals("BGTVT0600008")
							|| dossier.getServiceCode().equals("BGTVT0600009")
							|| dossier.getServiceCode().equals("BGTVT0600010")
							|| dossier.getServiceCode().equals("BGTVT0600011")
							|| dossier.getServiceCode().equals("BGTVT0600012")
							|| dossier.getServiceCode().equals("BGTVT0600023")
							|| dossier.getServiceCode().equals("BGTVT0600024")
							|| dossier.getServiceCode().equals("BGTVT0600025")
							|| dossier.getServiceCode().equals("BGTVT0600026")
							|| dossier.getServiceCode().equals("BGTVT0600027")
							|| dossier.getServiceCode().equals("BGTVT0600028")
							|| dossier.getServiceCode().equals("BGTVT0600029")
							|| dossier.getServiceCode().equals("BGTVT0600036")
							|| dossier.getServiceCode().equals("BGTVT0600037")
							|| dossier.getServiceCode().equals("BGTVT0600038")
							|| dossier.getServiceCode().equals("BGTVT0600039")
							|| dossier.getServiceCode().equals("BGTVT0600040")
							|| dossier.getServiceCode().equals("BGTVT0600041")
							|| dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600044")
							|| dossier.getServiceCode().equals("BGTVT0600045")
							|| dossier.getServiceCode().equals("BGTVT0600046")
							|| dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")
							|| dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")
							|| dossier.getServiceCode().equals("BGTVT0600056")
							|| dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")
							|| dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")
							|| dossier.getServiceCode().equals("BGTVT0600063")
							|| dossier.getServiceCode().equals("BGTVT0600064")) {
						if (lstRequests.size() > 0) {
							DossierRequestUD dossierRequest = lstRequests.get(0);
							_log.info("1410 request: " + dossierRequest.getComment());
							phanhoi.setSoGp(dossierRequest.getComment());
						}
					}
					content.setPhanhoiYeucauSua(phanhoi);
					
					body.setPersonSignature("");
					envelope.setSystemSignature("");

//					String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);
					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);

					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("14");
					model.setFunction("10");
					model.setReference(dossier.getReferenceUid());
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
					model.setRetryCount(0);
					model.setDirection(2);
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));

					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1815")) {
					nswRequest.setDocumentType(dossier.getServiceCode());
					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("18");
					subject.setFunction("15");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					body.setPersonSignature("");
					envelope.setSystemSignature("");
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					
					nswRequest2.setDocumentType(dossier.getServiceCode());
					subject2.setDocumentType(dossier.getServiceCode());
					subject2.setType("18");
					subject2.setFunction("15");
					subject2.setReference(dossier.getReferenceUid());
					subject2.setPreReference(dossier.getReferenceUid());
					subject2.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					body2.setPersonSignature("");
					envelope2.setSystemSignature("");
					
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					
					if (dossier.getServiceCode().equals("BGTVT0600001")
							|| dossier.getServiceCode().equals("BGTVT0600002")
							|| dossier.getServiceCode().equals("BGTVT0600003")
							|| dossier.getServiceCode().equals("BGTVT0600004")
							|| dossier.getServiceCode().equals("BGTVT0600019")
							|| dossier.getServiceCode().equals("BGTVT0600020")
							|| dossier.getServiceCode().equals("BGTVT0600021")
							|| dossier.getServiceCode().equals("BGTVT0600022")) {

						MessageQueueInputModel model = BGTVT0600001.convertResult(dossier, dossierSync, envelope, "18", "15");
						
						MessageQueueDetailModel result = client.postMessageQueue(model);
						
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
					}
					else if (dossier.getServiceCode().equals("BGTVT0600005")
							|| dossier.getServiceCode().equals("BGTVT0600006")
							|| dossier.getServiceCode().equals("BGTVT0600007")
							|| dossier.getServiceCode().equals("BGTVT0600008")
							|| dossier.getServiceCode().equals("BGTVT0600009")
							|| dossier.getServiceCode().equals("BGTVT0600010")
							|| dossier.getServiceCode().equals("BGTVT0600011")
							|| dossier.getServiceCode().equals("BGTVT0600012")
							|| dossier.getServiceCode().equals("BGTVT0600023")
							|| dossier.getServiceCode().equals("BGTVT0600024")
							|| dossier.getServiceCode().equals("BGTVT0600025")
							|| dossier.getServiceCode().equals("BGTVT0600026")
							|| dossier.getServiceCode().equals("BGTVT0600027")
							|| dossier.getServiceCode().equals("BGTVT0600028")
							|| dossier.getServiceCode().equals("BGTVT0600029")
							) {
						List<MessageQueueInputModel> lstResults = BGTVT0600005.convertResult(dossier, dossierSync, envelope, "18", "15");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());

					}
					else if (dossier.getServiceCode().equals("BGTVT0600013")
							|| dossier.getServiceCode().equals("BGTVT0600014")
							|| dossier.getServiceCode().equals("BGTVT0600015")
							|| dossier.getServiceCode().equals("BGTVT0600030")
							|| dossier.getServiceCode().equals("BGTVT0600031")
							|| dossier.getServiceCode().equals("BGTVT0600032")) {
						MessageQueueInputModel model = BGTVT0600013.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600016")
							|| dossier.getServiceCode().equals("BGTVT0600033")) {
						MessageQueueInputModel model = BGTVT0600016.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600017")
							|| dossier.getServiceCode().equals("BGTVT0600034")) {
						MessageQueueInputModel model = BGTVT0600017.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600036")
							|| dossier.getServiceCode().equals("BGTVT0600037")
							|| dossier.getServiceCode().equals("BGTVT0600038")
							|| dossier.getServiceCode().equals("BGTVT0600039")
							|| dossier.getServiceCode().equals("BGTVT0600040")
							|| dossier.getServiceCode().equals("BGTVT0600041")
							|| dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600036.convertResult(dossier, dossierSync, envelope, "18", "15");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
					else if (dossier.getServiceCode().equals("BGTVT0600044")
							|| dossier.getServiceCode().equals("BGTVT0600045")
							|| dossier.getServiceCode().equals("BGTVT0600046")) {
						MessageQueueInputModel model = BGTVT0600044.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")) {
						
						List<MessageQueueInputModel> lstResults = BGTVT0600048.convertResult(dossier, dossierSync, envelope, "18", "15");
						_log.info("SIZE OF VEHICLE: " + lstResults.size());
						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}	
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}	
						
						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
					else if (dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600052.convertResult(dossier, dossierSync, envelope, "18", "15");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());

					}
					else if (dossier.getServiceCode().equals("BGTVT0600056")) {
						MessageQueueInputModel model = BGTVT0600056.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")) {
						MessageQueueInputModel model = BGTVT0600058.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")
							|| dossier.getServiceCode().equals("BGTVT0600063")
							|| dossier.getServiceCode().equals("BGTVT0600064")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600061.convertResult(dossier, dossierSync, envelope, "18", "15");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());

					}
					else if (dossier.getServiceCode().equals("BGTVT0600018")
							|| dossier.getServiceCode().equals("BGTVT0600035")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							|| dossier.getServiceCode().equals("BGTVT0600065")
							) {
						MessageQueueInputModel model = BGTVT0600018.convertResult(dossier, dossierSync, envelope, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600047")
							) {
						MessageQueueInputModel model = BGTVT0600047.convertResult(dossier, dossierSync, envelope2, "18", "15");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}						
				} else if (dossierAction.getSyncActionCode().equals("1613")) {
					nswRequest.setDocumentType(dossier.getServiceCode());
					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("16");
					subject.setFunction("13");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					body.setPersonSignature("");
					envelope.setSystemSignature("");
					
					KetQuaXuLy ketqua = new KetQuaXuLy();
					content.setKetQuaXuLy(ketqua);
					if (dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")
							|| dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")
							|| dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")
							|| dossier.getServiceCode().equals("BGTVT0600056")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							|| dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")
							|| dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")) {
						ketqua.setSoTn(dossier.getDossierNo());
					}
					ketqua.setNoiDung(dossierAction.getActionNote());
					ketqua.setDonViXuLy(dossier.getGovAgencyName());
					ketqua.setNgayXuLy(DateTimeUtils.convertDateToString(new Date(), DateTimeUtils._NSW_DATE_TIME_FORMAT));
					ketqua.setLinkCongvan(StringPool.BLANK);
					
//					String rawMessage = OutsideSystemConverter.convertToNSWXML(nswRequest);

					String rawMessage = OutsideSystemConverter.convertToNSWXML(envelope);
					MessageQueueInputModel model = new MessageQueueInputModel();
					model.setRawMessage(rawMessage);
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
					model.setType("16");
					model.setFunction("13");
					model.setReference(dossier.getReferenceUid());
					if (dossier.getReceiveDate() != null) {
						cal.setTime(dossier.getReceiveDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					} else {
						cal.setTime(dossier.getCreateDate());
						subject.setDocumentYear(cal.get(Calendar.YEAR));
					}
					model.setPreReference(dossier.getReferenceUid());
					model.setSendDate(APIDateTimeUtils.convertDateToString(new Date()));
					model.setRetryCount(0);
					model.setDirection(2);
					cal.setTime(dossier.getCreateDate());
					model.setDocumentYear(cal.get(Calendar.YEAR));

					MessageQueueDetailModel result = client.postMessageQueue(model);
					if (result != null) {
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK() : sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
				} else if (dossierAction.getSyncActionCode().equals("1816")) {
					nswRequest.setDocumentType(dossier.getServiceCode());
					subject.setDocumentType(dossier.getServiceCode());
					subject.setType("18");
					subject.setFunction("16");
					subject.setReference(dossier.getReferenceUid());
					subject.setPreReference(dossier.getReferenceUid());
					subject.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					body.setPersonSignature("");
					envelope.setSystemSignature("");
					
					nswRequest2.setDocumentType(dossier.getServiceCode());
					subject2.setDocumentType(dossier.getServiceCode());
					subject2.setType("18");
					subject2.setFunction("16");
					subject2.setReference(dossier.getReferenceUid());
					subject2.setPreReference(dossier.getReferenceUid());
					subject2.setSendDate(
							APIDateTimeUtils.convertDateToString(new Date(), APIDateTimeUtils._NSW_PATTERN));

					body2.setPersonSignature("");
					envelope2.setSystemSignature("");
					
					if (dossier.getServiceCode().equals("BGTVT0600001")
							|| dossier.getServiceCode().equals("BGTVT0600002")
							|| dossier.getServiceCode().equals("BGTVT0600003")
							|| dossier.getServiceCode().equals("BGTVT0600004") 
							|| dossier.getServiceCode().equals("BGTVT0600019")
							|| dossier.getServiceCode().equals("BGTVT0600020")
							|| dossier.getServiceCode().equals("BGTVT0600021")
							|| dossier.getServiceCode().equals("BGTVT0600022")) {

							MessageQueueInputModel model = BGTVT0600001.convertResult(dossier, dossierSync, envelope, "18", "16");

							MessageQueueDetailModel result = client.postMessageQueue(model);
							if (result != null) {
								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
										: sync.getMethod());

								DossierAction foundAction = DossierActionLocalServiceUtil
										.fetchDossierAction(clientDossierActionId);
								if (foundAction != null) {
									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
								}

								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
							}
					}
					else if (dossier.getServiceCode().equals("BGTVT0600005")
							|| dossier.getServiceCode().equals("BGTVT0600006")
							|| dossier.getServiceCode().equals("BGTVT0600007")
							|| dossier.getServiceCode().equals("BGTVT0600008")
							|| dossier.getServiceCode().equals("BGTVT0600009")
							|| dossier.getServiceCode().equals("BGTVT0600010")
							|| dossier.getServiceCode().equals("BGTVT0600011")
							|| dossier.getServiceCode().equals("BGTVT0600012")
							|| dossier.getServiceCode().equals("BGTVT0600023")
							|| dossier.getServiceCode().equals("BGTVT0600024")
							|| dossier.getServiceCode().equals("BGTVT0600025")
							|| dossier.getServiceCode().equals("BGTVT0600026")
							|| dossier.getServiceCode().equals("BGTVT0600027")
							|| dossier.getServiceCode().equals("BGTVT0600028")
							|| dossier.getServiceCode().equals("BGTVT0600029")
							) {
						List<MessageQueueInputModel> lstResults = BGTVT0600005.convertResult(dossier, dossierSync, envelope, "18", "16");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
					else if (dossier.getServiceCode().equals("BGTVT0600013")
							|| dossier.getServiceCode().equals("BGTVT0600014")
							|| dossier.getServiceCode().equals("BGTVT0600015")
							|| dossier.getServiceCode().equals("BGTVT0600030")
							|| dossier.getServiceCode().equals("BGTVT0600031")
							|| dossier.getServiceCode().equals("BGTVT0600032")) {
						MessageQueueInputModel model = BGTVT0600013.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600016")
							|| dossier.getServiceCode().equals("BGTVT0600033")) {
						MessageQueueInputModel model = BGTVT0600016.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600017")
							|| dossier.getServiceCode().equals("BGTVT0600034")) {
						MessageQueueInputModel model = BGTVT0600017.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600036")
							|| dossier.getServiceCode().equals("BGTVT0600037")
							|| dossier.getServiceCode().equals("BGTVT0600038")
							|| dossier.getServiceCode().equals("BGTVT0600039")
							|| dossier.getServiceCode().equals("BGTVT0600040")
							|| dossier.getServiceCode().equals("BGTVT0600041")
							|| dossier.getServiceCode().equals("BGTVT0600042")
							|| dossier.getServiceCode().equals("BGTVT0600043")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600036.convertResult(dossier, dossierSync, envelope, "18", "16");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}
					else if (dossier.getServiceCode().equals("BGTVT0600044")
							|| dossier.getServiceCode().equals("BGTVT0600045")
							|| dossier.getServiceCode().equals("BGTVT0600046")) {
						MessageQueueInputModel model = BGTVT0600044.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}	
					else if (dossier.getServiceCode().equals("BGTVT0600048")
							|| dossier.getServiceCode().equals("BGTVT0600049")
							|| dossier.getServiceCode().equals("BGTVT0600050")
							|| dossier.getServiceCode().equals("BGTVT0600051")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600048.convertResult(dossier, dossierSync, envelope, "18", "16");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());

					}	
					else if (dossier.getServiceCode().equals("BGTVT0600052")
							|| dossier.getServiceCode().equals("BGTVT0600053")
							|| dossier.getServiceCode().equals("BGTVT0600054")
							|| dossier.getServiceCode().equals("BGTVT0600055")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600052.convertResult(dossier, dossierSync, envelope, "18", "16");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());

					}	
					else if (dossier.getServiceCode().equals("BGTVT0600056")) {
						MessageQueueInputModel model = BGTVT0600056.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}	
					else if (dossier.getServiceCode().equals("BGTVT0600058")
							|| dossier.getServiceCode().equals("BGTVT0600059")
							|| dossier.getServiceCode().equals("BGTVT0600060")) {
						MessageQueueInputModel model = BGTVT0600058.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}	
					else if (dossier.getServiceCode().equals("BGTVT0600061")
							|| dossier.getServiceCode().equals("BGTVT0600062")
							|| dossier.getServiceCode().equals("BGTVT0600063")
							|| dossier.getServiceCode().equals("BGTVT0600064")) {
						List<MessageQueueInputModel> lstResults = BGTVT0600061.convertResult(dossier, dossierSync, envelope, "18", "16");

						for (MessageQueueInputModel model : lstResults) {
							MessageQueueDetailModel result = client.postMessageQueue(model);
//							if (result != null) {
//								long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
//										: sync.getMethod());
//
//								DossierAction foundAction = DossierActionLocalServiceUtil
//										.fetchDossierAction(clientDossierActionId);
//								if (foundAction != null) {
//									DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
//								}
//
//								_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());
//
//								_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
//							}
						}
						long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
								: sync.getMethod());

						DossierAction foundAction = DossierActionLocalServiceUtil
								.fetchDossierAction(clientDossierActionId);
						if (foundAction != null) {
							DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
						}

						_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

						_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
					}	
					else if (dossier.getServiceCode().equals("BGTVT0600018")
							|| dossier.getServiceCode().equals("BGTVT0600035")
							|| dossier.getServiceCode().equals("BGTVT0600057")
							|| dossier.getServiceCode().equals("BGTVT0600065")
							) {
						MessageQueueInputModel model = BGTVT0600018.convertResult(dossier, dossierSync, envelope, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}
					else if (dossier.getServiceCode().equals("BGTVT0600047")
							
							) {
						MessageQueueInputModel model = BGTVT0600047.convertResult(dossier, dossierSync, envelope2, "18", "16");

						MessageQueueDetailModel result = client.postMessageQueue(model);
						if (result != null) {
							long clientDossierActionId = (sync.getMethod() == 0 ? sync.getClassPK()
									: sync.getMethod());

							DossierAction foundAction = DossierActionLocalServiceUtil
									.fetchDossierAction(clientDossierActionId);
							if (foundAction != null) {
								DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
							}

							_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

							_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
						}
						
					}						
				} else {
					long clientDossierActionId = (dossierSync.getMethod() == 0 ? dossierSync.getClassPK()
							: dossierSync.getMethod());

					DossierAction foundAction = DossierActionLocalServiceUtil
							.fetchDossierAction(clientDossierActionId);
					if (foundAction != null) {
						DossierActionLocalServiceUtil.updatePending(clientDossierActionId, false);
					}

					_dossierSyncLocalService.deleteDossierSync(sync.getBaseDossierSyncId());

					_thirdPartyDossierSyncLocalService.deleteThirdPartyDossierSync(sync.getDossierSyncId());
				}
			}
		}
	}
	
	@Activate
	@Modified
	protected void activate() {
		schedulerEntryImpl.setTrigger(TriggerFactoryUtil.createTrigger(getEventListenerClass(), getEventListenerClass(),
				15, TimeUnit.SECOND));
		_schedulerEngineHelper.register(this, schedulerEntryImpl, DestinationNames.SCHEDULER_DISPATCH);
	}

	@Deactivate
	protected void deactivate() {
		_schedulerEngineHelper.unregister(this);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Reference(unbind = "-")
	protected void setSchedulerEngineHelper(SchedulerEngineHelper schedulerEngineHelper) {

		_schedulerEngineHelper = schedulerEngineHelper;
	}

	@Reference(unbind = "-")
	protected void setTriggerFactory(TriggerFactory triggerFactory) {
	}

	private SchedulerEngineHelper _schedulerEngineHelper;

	@Reference
	private DossierLocalService _dossierLocalService;

	@Reference
	private ThirdPartyDossierSyncLocalService _thirdPartyDossierSyncLocalService;

	@Reference
	private DossierSyncLocalService _dossierSyncLocalService;

	@Reference
	private DossierFileLocalService _dossierFileLocalService;

	@Reference
	private DeliverableLocalService _deliverableLocalService;

	@Reference
	private DossierActionLocalService _dossierActionLocalService;

	private Log _log = LogFactoryUtil.getLog(OutsideSystemSyncScheduler.class);
}
