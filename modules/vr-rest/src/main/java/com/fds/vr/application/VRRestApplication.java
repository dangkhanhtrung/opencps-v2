package com.fds.vr.application;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.opencps.datamgt.model.DictItem;
import org.opencps.datamgt.service.DictItemLocalServiceUtil;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.thirdparty.system.model.ILDataItem;
import org.opencps.thirdparty.system.model.ILDoanhNghiep;
import org.opencps.thirdparty.system.model.ILHopDongThue;
import org.opencps.thirdparty.system.model.ILPhuHieuBienHieu;
import org.opencps.thirdparty.system.model.ILPhuongTien;
import org.opencps.thirdparty.system.model.ILTuyen;
import org.opencps.thirdparty.system.model.ILTuyenKhaiThacXe;
import org.opencps.thirdparty.system.model.ILTuyenKhaiThach;
import org.opencps.thirdparty.system.model.ILViPham;
import org.opencps.thirdparty.system.model.ViewGiayPhepVanTai;
import org.opencps.thirdparty.system.model.ViewPhuongTien;
import org.opencps.thirdparty.system.service.ILDataItemLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILDoanhNghiepLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILHopDongThueLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILPhuHieuBienHieuLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILPhuongTienLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILTuyenKhaiThacXeLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILTuyenKhaiThachLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILTuyenLocalServiceUtil;
import org.opencps.thirdparty.system.service.ILViPhamLocalServiceUtil;
import org.opencps.thirdparty.system.service.ViewGiayPhepVanTaiLocalServiceUtil;
import org.opencps.thirdparty.system.service.ViewPhuongTienLocalServiceUtil;
import org.osgi.service.component.annotations.Component;

import com.backend.migrate.vr.model.PhuongTien;
import com.backend.migrate.vr.service.PhuongTienLocalServiceUtil;
import com.fds.vr.business.action.ILCertificateActions;
import com.fds.vr.business.action.VRActions;
import com.fds.vr.business.action.impl.DictItemUtil;
import com.fds.vr.business.action.impl.ILCertificateActionsImpl;
import com.fds.vr.business.action.impl.VRActionsImpl;
import com.fds.vr.business.action.util.ConvertFormatDate;
import com.fds.vr.business.model.ILCertificate;
import com.fds.vr.business.model.ILVehicle;
import com.fds.vr.business.model.ILVehicleCustomsBorderGuard;
import com.fds.vr.business.model.VRReport;
import com.fds.vr.business.service.ILCertificateLocalServiceUtil;
import com.fds.vr.business.service.ILVehicleCustomsBorderGuardLocalServiceUtil;
import com.fds.vr.business.service.ILVehicleLocalServiceUtil;
import com.fds.vr.business.service.VRReportLocalServiceUtil;
import com.fds.vr.ilcertificate.model.ILCBGuardModel;
import com.fds.vr.ilcertificate.model.ILCBGuardResultModel;
import com.fds.vr.ilcertificate.model.ILCertificateModel;
import com.fds.vr.ilcertificate.model.ILCertificateResultModel;
import com.fds.vr.ilcertificate.model.ILCertificateSearchModel;
import com.fds.vr.ilcertificate.model.ILVehicleModel;
import com.fds.vr.util.ILCertificateUtils;
import com.liferay.counter.kernel.model.Counter;
import com.liferay.counter.kernel.service.CounterLocalServiceUtil;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author admin
 */
@ApplicationPath("/vr-app")
@Component(immediate = true, service = Application.class)
@XmlSeeAlso({ ILCertificateResultModel.class, ILCBGuardResultModel.class, ILCBGuardModel.class,
		ILCertificateModel.class, ILCertificateSearchModel.class })
public class VRRestApplication extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	public static final String PRE_FIX_CERT = "DKLR_CERT@";
	public static final String PRE_FIX_CERT_CURR = "DKLR_CERT_CURR@";
	public static final String PRE_FIX_CERT_ELM = "DKLR_CERT_ELM@";

	@GET
	@Path("/doanhnghiep")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findDoanhNghiep(@Context HttpHeaders header, @QueryParam("keyword") String keyword,
			@QueryParam("start") Integer start, @QueryParam("end") Integer end) {
		JSONObject obj = JSONFactoryUtil.createJSONObject();
		
		List<ILDoanhNghiep> lstDns = ILDoanhNghiepLocalServiceUtil.findByKeyword(keyword, start, end);
		obj.put("total", ILDoanhNghiepLocalServiceUtil.countByKeyword(keyword));

		if (lstDns.size() > 0) {
			if (start == null) {
				start = QueryUtil.ALL_POS;
				end = QueryUtil.ALL_POS;
			}

			obj.put("data", JSONFactoryUtil.looseSerialize(lstDns));
		}
		return Response.status(200).entity(obj.toJSONString()).build();
	}

	@GET
	@Path("/doanhnghiep/{doanhnghiepid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findDoanhNghiepDetail(@Context HttpHeaders header, @PathParam("doanhnghiepid") long doanhnghiepid,
			@QueryParam("start") Integer start, @QueryParam("end") Integer end) {

		JSONObject error = JSONFactoryUtil.createJSONObject();

		ILDoanhNghiep doanhnghiep = ILDoanhNghiepLocalServiceUtil.fetchILDoanhNghiep(doanhnghiepid);

		if (Validator.isNotNull(doanhnghiep)) {
			return Response.status(200).entity(JSONFactoryUtil.looseSerialize(doanhnghiep)).build();
		} else {
			error.put("code", "404");
			error.put("message", "DOANH_NHIEP_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/doanhnghiep/{doanhnghiepid}/phuongtien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienByDoanhNghiep(@Context HttpHeaders header,
			@PathParam("doanhnghiepid") long doanhnghiepid, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILDoanhNghiep doanhnghiep = ILDoanhNghiepLocalServiceUtil.fetchILDoanhNghiep(doanhnghiepid);

		if (Validator.isNotNull(doanhnghiep)) {

			if (start == null) {
				start = 0;
				end = 9;
			}

			long count = ILPhuongTienLocalServiceUtil.countByDoanhNghiep((int) doanhnghiep.getId());

			List<ILPhuongTien> ilPhuongTiens = ILPhuongTienLocalServiceUtil.getByDoanhNghiep((int) doanhnghiep.getId(),
					start, end);

			result.put("total", count);
			result.put("data", JSONFactoryUtil.looseSerialize(ilPhuongTiens));

			return Response.status(200).entity(result.toJSONString()).build();
		} else {
			error.put("code", "404");
			error.put("message", "DOANH_NHIEP_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/doanhnghiep/{doanhnghiepid}/giayphepkinhdoanhvantai")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGiayPhepVanTaiByDoanhNghiep(@Context HttpHeaders header,
			@PathParam("doanhnghiepid") long doanhnghiepid, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILDoanhNghiep doanhnghiep = ILDoanhNghiepLocalServiceUtil.fetchILDoanhNghiep(doanhnghiepid);

		if (Validator.isNotNull(doanhnghiep)) {
			/*
			long count = ILGiayPhepVanTaiLocalServiceUtil.countByDoanhNghiep(doanhnghiep.getId());

			List<ILGiayPhepVanTai> giayphepvantais = ILGiayPhepVanTaiLocalServiceUtil
					.getByDoanhNghiep(doanhnghiep.getId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
			 */
			
			List<ViewGiayPhepVanTai> giaypheps = ViewGiayPhepVanTaiLocalServiceUtil.searchGiayPhepVanTai(doanhnghiepid);
			
			long count = giaypheps.size();
			
			result.put("total", count);
			result.put("data", JSONFactoryUtil.looseSerialize(giaypheps));

			return Response.status(200).entity(result.toJSONString()).build();
		} else {
			error.put("code", "404");
			error.put("message", "DOANH_NHIEP_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/doanhnghiep/{doanhnghiepid}/tuyenkhaithac")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findTuyenKhaiThacByDoanhNghiep(@Context HttpHeaders header,
			@PathParam("doanhnghiepid") long doanhnghiepid, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILDoanhNghiep doanhnghiep = ILDoanhNghiepLocalServiceUtil.fetchILDoanhNghiep(doanhnghiepid);

		if (Validator.isNotNull(doanhnghiep)) {

			long count = ILTuyenKhaiThachLocalServiceUtil.count(doanhnghiep.getId());

			List<ILTuyenKhaiThach> ilTuyenKhaiThacs = ILTuyenKhaiThachLocalServiceUtil
					.getByDoanhNghiep(doanhnghiep.getId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			if (ilTuyenKhaiThacs.size() == 0) {
				error.put("code", "404");
				error.put("message", "TUYEN_KHAI_THAC_NOT_FOUND");

				return Response.status(404).entity(error.toJSONString()).build();
			}

			JSONArray data = JSONFactoryUtil.createJSONArray();

			for (ILTuyenKhaiThach ilTuyenKhaiThac : ilTuyenKhaiThacs) {

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				ILTuyen tuyen = ILTuyenLocalServiceUtil.fetchILTuyen(ilTuyenKhaiThac.getTuyen_id());

				if (Validator.isNotNull(tuyen)) {
					jsonObject.put("matuyen", tuyen.getMatuyen());
					jsonObject.put("tentuyen", tuyen.getTentuyen());

					ILDataItem noidi = ILDataItemLocalServiceUtil
							.fetchILDataItem(Long.parseLong(tuyen.getDvhc_noidi_id()));
					ILDataItem noiden = ILDataItemLocalServiceUtil
							.fetchILDataItem(Long.parseLong(tuyen.getDvhc_noiden_id()));

					jsonObject.put("noidi", noidi.getTen());
					jsonObject.put("noiden", noiden.getTen());
					jsonObject.put("culy", tuyen.getCulytuyen());
					jsonObject.put("trangthai", tuyen.getTrangthai());
					jsonObject.put("chapthuankhaithac", tuyen.getSocv_chapthuan());

				}

				data.put(jsonObject);

			}

			result.put("total", count);
			result.put("data", data);

			return Response.status(200).entity(result.toJSONString()).build();
		} else {
			error.put("code", "404");
			error.put("message", "DOANH_NHIEP_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/phuongtien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTien(@Context HttpHeaders header,@QueryParam("keyword") String keyword, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end) {

		JSONObject result = JSONFactoryUtil.createJSONObject();

		if (start == null || end == null) {
			start = 0;
			end = 9;
		}

		JSONArray data = JSONFactoryUtil.createJSONArray();
		
		long count = ViewPhuongTienLocalServiceUtil.countPhuongTien(keyword);
		
		List<ViewPhuongTien> phuongTiens = ViewPhuongTienLocalServiceUtil.searchPhuongTien(keyword, start, end);


		for (ViewPhuongTien ilPhuongTien : phuongTiens) {

			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

			jsonObject.put("id", ilPhuongTien.getId());

			jsonObject.put("bienkiemsoat", ilPhuongTien.getBienkiemsoat());


			jsonObject.put("tendangky", ilPhuongTien.getTennguoisohuu());
			jsonObject.put("sokhung", ilPhuongTien.getSokhung());
			jsonObject.put("somay", ilPhuongTien.getSomay());
			
			if (Validator.isNotNull(ilPhuongTien.getGhichu()) && ilPhuongTien.getGhichu().contains("XXX")) {
				
				String [] elmData = StringUtil.split(ilPhuongTien.getGhichu(), StringPool.AT);
				
				if (elmData.length == 3) {
					jsonObject.put("nhanhieu", elmData[1]);
					jsonObject.put("loaiphuongtien", elmData[2]);
				}
				
			} else {

				ILDataItem nhanhieu = ILDataItemLocalServiceUtil.fetchILDataItem(ilPhuongTien.getTenhieuxe_id());
				if (Validator.isNotNull(nhanhieu)) {
					jsonObject.put("nhanhieu", nhanhieu.getTen());
				}
				ILDataItem loaiphuongtien = ILDataItemLocalServiceUtil
						.fetchILDataItem(ilPhuongTien.getLoaihinhvantai_id());

				if (Validator.isNotNull(loaiphuongtien)) {
					jsonObject.put("loaiphuongtien", loaiphuongtien.getTen());
				}
			}

			JSONArray xuanhapcanh = JSONFactoryUtil.createJSONArray();

			List<ILVehicleCustomsBorderGuard> borderGuards = ILVehicleCustomsBorderGuardLocalServiceUtil
					.getBorderGuardList(ilPhuongTien.getBienkiemsoat());


			for (ILVehicleCustomsBorderGuard borderGuard : borderGuards) {
				JSONObject jsonObjectBorderGuard = JSONFactoryUtil.createJSONObject();

				jsonObjectBorderGuard.put("thoidiemxuatcan", formatDate(borderGuard.getBorderGuardsArrivalDate()));
				jsonObjectBorderGuard.put("thoidiemnhapcanh", formatDate(borderGuard.getBorderGuardsDepartureDate()));

				xuanhapcanh.put(jsonObjectBorderGuard);
			}

			jsonObject.put("xuanhapcanh", xuanhapcanh);

			data.put(jsonObject);

		}

		result.put("total", count);
		result.put("data", data);

		return Response.status(200).entity(result.toJSONString()).build();

	}
	


	@GET
	@Path("/phuongtien/{biensoxe}/refer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findChungChi(@Context HttpHeaders header,
			@PathParam("biensoxe") String biensoxe, @QueryParam("start") Integer start,
			@QueryParam("end") Integer end) {

		JSONObject result = JSONFactoryUtil.createJSONObject();
		JSONObject error = JSONFactoryUtil.createJSONObject();
		
		try {
			ILCertificate certificate = ILCertificateLocalServiceUtil.searchByRegNumbber(biensoxe);
			
			if (Validator.isNotNull(certificate)) {
				
				result.put("TrademarkCode", certificate.getTrademarkCode());
				result.put("PermitedFrom", certificate.getValidFrom());
				result.put("Capacity", certificate.getCapacity());
				result.put("ProposedStops", certificate.getStops());
				result.put("RegistrationNumber", certificate.getRegistrationNumber());
				result.put("PermitedTo", certificate.getValidUntil());
				result.put("TransportType", certificate.getVehicleType());
				result.put("RouteCode", certificate.getRouteCode());
				
				/*TrademarkCode
				PermitedFrom
				Capacity
				ProposedStops
				RegistrationNumber
				PermitedTo
				TransportType
				RouteCode
				bienSoXe
				
				mucDich
				soKhung
				soMay
				mauSon
				nhanHieu
				trongTai
				loaiHangHoa
				soHanhKhach
				diemDungDo
				diemDungTrenDuong
				hinhThuc
				hanhTrinh
				ngayCap
				ngayHetHan
				tinhDi
				tinhDen
				benDi
				benDen
				cuaKhau
				*/
				
				return Response.status(200).entity(JSONFactoryUtil.looseSerialize(certificate)).build();

			} else {
				error.put("code", "404");
				error.put("message", "GIAY_PHEP_NOT_FOUND");
				return Response.status(404).entity(error.toJSONString()).build();
			}
			
			

		} catch (Exception e) {
			error.put("code", "500");
			error.put("message", "Internal Error");
			return Response.status(404).entity(error.toJSONString()).build();
		}
	}

	
	@POST
	@Path("/phuongtien/{sogiayphep}/update")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCertificate(@Context HttpHeaders header,
			@PathParam("sogiayphep") String sogiayphep, String body) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		
		try {
			
			ILCertificate ilCertificate = ILCertificateLocalServiceUtil.fetchByLicenceNo(sogiayphep);
			
			if (Validator.isNotNull(ilCertificate)) {
				
				long id = CounterLocalServiceUtil.increment(ILCertificate.class.getName());
				
				ilCertificate.setId(id);
				
				_log.info("****body****" + body);
				
				if (Validator.isNotNull(body)) {
					
					JSONObject jsonBody = JSONFactoryUtil.createJSONObject(body);
					
					//TODO Update key here
					
					String licenceNo = jsonBody.getString("licenceNo");
					
					String strValidUntil = jsonBody.getString("validUntil");
					
					String strValidFrom = jsonBody.getString("validFrom");
					
					_log.info("licenceNo" + licenceNo);
					_log.info("strValidUtil" + strValidUntil);
					_log.info("strValidFrom" + strValidFrom);
					
					ilCertificate.setLicenceNo(licenceNo);

					SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
					
					if (Validator.isNotNull(strValidUntil)) {
						
						try {
							Date validUntil = dateFormat.parse(strValidUntil);
							Date validFrom = dateFormat.parse(strValidFrom);
							
							ilCertificate.setValidUntil(validUntil);
							ilCertificate.setValidFrom(validFrom);
							ilCertificate.setSignDate(new Date());
							ilCertificate.setIssuedDate(new Date());
							
						} catch (Exception e) {
							_log.info("__DateFormatFail****");
						}
					}
				}
				
				ILCertificate updateCertificate = ILCertificateLocalServiceUtil.addILCertificate(ilCertificate);
				
				return Response.status(200).entity(JSONFactoryUtil.looseSerialize(updateCertificate)).build();
				
			} else {
				error.put("code", "404");
				error.put("message", "GIAY_PHEP_NOT_FOUND");
				return Response.status(404).entity(error.toJSONString()).build();
			}

		} catch (Exception e) {
			error.put("code", "500");
			error.put("message", "Internal Error");
			return Response.status(404).entity(error.toJSONString()).build();
		}
	}

	@GET
	@Path("/phuongtien/{phuongtienid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienDetail(@Context HttpHeaders header, @PathParam("phuongtienid") String phuongtienid) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		
		List<ViewPhuongTien> phuongTiens = ViewPhuongTienLocalServiceUtil.searchPhuongTien(phuongtienid, 0, 1);
		
		if (phuongTiens.size() != 0) {
			ViewPhuongTien ilPhuongTien = phuongTiens.get(0);
			
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject();
			
			jsonObject.put("id", ilPhuongTien.getId());

			jsonObject.put("bienkiemsoat", ilPhuongTien.getBienkiemsoat());

			ILDataItem nhanhieu = ILDataItemLocalServiceUtil.fetchILDataItem(ilPhuongTien.getTenhieuxe_id());

			if (Validator.isNotNull(nhanhieu)) {
				jsonObject.put("nhanhieu", nhanhieu.getTen());
			}
			jsonObject.put("tendangky", ilPhuongTien.getTennguoisohuu());
			jsonObject.put("sokhung", ilPhuongTien.getSokhung());
			jsonObject.put("somay", ilPhuongTien.getSomay());

			ILDataItem loaiphuongtien = ILDataItemLocalServiceUtil.fetchILDataItem(ilPhuongTien.getLoaihinhvantai_id());

			if (Validator.isNotNull(loaiphuongtien)) {
				jsonObject.put("loaiphuongtien", loaiphuongtien.getTen());
			}

			JSONArray xuanhapcanh = JSONFactoryUtil.createJSONArray();

			List<ILVehicleCustomsBorderGuard> borderGuards = ILVehicleCustomsBorderGuardLocalServiceUtil
					.getBorderGuardList(ilPhuongTien.getBienkiemsoat());

			// add sample
			JSONObject sample = JSONFactoryUtil.createJSONObject();

			sample.put("thoidiemxuatcan", new Date());
			sample.put("thoidiemnhapcanh", new Date());

			xuanhapcanh.put(sample);

			for (ILVehicleCustomsBorderGuard borderGuard : borderGuards) {
				JSONObject jsonObjectBorderGuard = JSONFactoryUtil.createJSONObject();

				jsonObjectBorderGuard.put("thoidiemxuatcan", borderGuard.getBorderGuardsArrivalDate());
				jsonObjectBorderGuard.put("thoidiemnhapcanh", borderGuard.getBorderGuardsDepartureDate());

				xuanhapcanh.put(jsonObjectBorderGuard);
			}

			jsonObject.put("xuanhapcanh", xuanhapcanh);

			return Response.status(200).entity(jsonObject.toJSONString()).build();
			
		} else {
			error.put("code", "404");
			error.put("message", "PHUONG_TIEN_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/phuongtien/{phuongtienid}/tuyenkhaithac")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienTuyenKhaiThac(@Context HttpHeaders header,
			@PathParam("phuongtienid") long phuongtienid) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILPhuongTien ilPhuongTien = ILPhuongTienLocalServiceUtil.fetchILPhuongTien(phuongtienid);

		if (Validator.isNotNull(ilPhuongTien)) {

			List<ILTuyenKhaiThacXe> ilTuyenKhaiThacXes = ILTuyenKhaiThacXeLocalServiceUtil
					.getByPhuongTienId(phuongtienid);

			if (ilTuyenKhaiThacXes.size() == 0) {
				error.put("code", "404");
				error.put("message", "TUYEN_KHAI_THAC_NOT_FOUND");

				return Response.status(404).entity(error.toJSONString()).build();
			}

			JSONArray data = JSONFactoryUtil.createJSONArray();

			result.put("total", ilTuyenKhaiThacXes.size());

			for (ILTuyenKhaiThacXe ilTuyenKhaiThac : ilTuyenKhaiThacXes) {

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				ILTuyen tuyen = ILTuyenLocalServiceUtil.fetchILTuyen(ilTuyenKhaiThac.getTuyen_id());

				if (Validator.isNotNull(tuyen)) {
					jsonObject.put("matuyen", tuyen.getMatuyen());
					jsonObject.put("tentuyen", tuyen.getTentuyen());

					ILDataItem noidi = ILDataItemLocalServiceUtil
							.fetchILDataItem(Long.parseLong(tuyen.getDvhc_noidi_id()));
					ILDataItem noiden = ILDataItemLocalServiceUtil
							.fetchILDataItem(Long.parseLong(tuyen.getDvhc_noiden_id()));

					jsonObject.put("noidi", noidi.getTen());
					jsonObject.put("noiden", noiden.getTen());
					jsonObject.put("culy", tuyen.getCulytuyen());
					jsonObject.put("trangthai", tuyen.getTrangthai());
					jsonObject.put("chapthuankhaithac", tuyen.getSocv_chapthuan());

				}

				data.put(jsonObject);

			}

			result.put("data", data);

			return Response.status(200).entity(result.toJSONString()).build();

		} else {
			error.put("code", "404");
			error.put("message", "PHUONG_TIEN_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}
	}
	
	

	@GET
	@Path("/phuongtien/{biensoxe}/giayphepphuongtien")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienGiayPhep(@Context HttpHeaders header,
			@PathParam("biensoxe") String biensoxe) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();
		
		List<ILCertificate> lsCertificates = ILCertificateLocalServiceUtil.searchListByRegistionNumber(biensoxe);
		
		long count = lsCertificates.size();

		//ILPhuongTien ilPhuongTien = ILPhuongTienLocalServiceUtil.fetchILPhuongTien(phuongtienid);
		
		if (count != 0) {
			result.put("total", count);
			result.put("data", JSONFactoryUtil.looseSerialize(lsCertificates));
		} else{
			error.put("code", "404");
			error.put("message", "GIAY_PHEP_NOT_FOUND");
			return Response.status(404).entity(error.toJSONString()).build();
		}

			return Response.status(200).entity(result.toJSONString()).build();
	}
	
	@GET
	@Path("/giayphepvantaiquocte")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGiayPhepVanTaiQuocTe(@Context HttpHeaders header, @QueryParam("keyword") String keyword,
			@QueryParam("start") Integer start, @QueryParam("end") Integer end) {
		JSONObject obj = JSONFactoryUtil.createJSONObject();

		JSONArray data = JSONFactoryUtil.createJSONArray();

		long count = ILCertificateLocalServiceUtil.searchGiayPhep(keyword, QueryUtil.ALL_POS, QueryUtil.ALL_POS).size();

		obj.put("total", count);

		if (count > 0) {
			if (start == null) {
				start = 0;
				end = 9;
			}

			List<ILCertificate> certificates = ILCertificateLocalServiceUtil.searchGiayPhep(keyword, start, end);

			for (ILCertificate certificate : certificates) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("sogiayphep", certificate.getLicenceNo());
				jsonObject.put("noicap", certificate.getGovAgencyName());
				jsonObject.put("ngaycap", formatDate(certificate.getValidFrom()));
				jsonObject.put("ngayhethan", formatDate(certificate.getValidFrom()));
				jsonObject.put("trangthai", "");
				jsonObject.put("dnvantai", certificate.getApplicantName());
				jsonObject.put("loaihinhkinhdoanh", certificate.getTransportOperation());
				jsonObject.put("download", getFileURL(certificate.getDossierFileId()));

				data.put(jsonObject);
			}

			obj.put("data", data);
		}
		return Response.status(200).entity(obj.toJSONString()).build();
	}

	@GET
	@Path("/giaypheplienvan")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findGiayPhepLienVan(@Context HttpHeaders header, @QueryParam("keyword") String keyword,
			@QueryParam("start") Integer start, @QueryParam("end") Integer end) {
		JSONObject obj = JSONFactoryUtil.createJSONObject();

		JSONArray data = JSONFactoryUtil.createJSONArray();

		long count = ILCertificateLocalServiceUtil.searchLienVan(keyword, QueryUtil.ALL_POS, QueryUtil.ALL_POS).size();

		obj.put("total", count);

		if (count > 0) {
			if (start == null) {
				start = 0;
				end = 9;
			}

			List<ILCertificate> certificates = ILCertificateLocalServiceUtil.searchLienVan(keyword, start, end);

			for (ILCertificate certificate : certificates) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("sogiaypheplienvan", certificate.getLicenceNo());
				jsonObject.put("noicap", certificate.getGovAgencyName());
				jsonObject.put("ngaycap", formatDate(certificate.getValidFrom()));
				jsonObject.put("ngayhethan", formatDate(certificate.getValidUntil()));
				jsonObject.put("trangthai", "");
				jsonObject.put("donvikhaithac", certificate.getApplicantName());
				jsonObject.put("bienkiemsoat", certificate.getRegistrationNumber());
				jsonObject.put("loaihinhkinhdoanh", certificate.getTransportOperation());
				jsonObject.put("loaiphuongtien", certificate.getVehicleType());
				jsonObject.put("download", getFileURL(certificate.getDossierFileId()));

				data.put(jsonObject);
			}

			obj.put("data", data);
		}
		return Response.status(200).entity(obj.toJSONString()).build();
	}

	private String getFileURL(long dossierFileId) {

		String fileURL = "";

		try {
			Company company = CompanyLocalServiceUtil.getCompanyByMx(PropsUtil.get(PropsKeys.COMPANY_DEFAULT_WEB_ID));

			String portalURL = PortalUtil.getPortalURL(company.getVirtualHostname(),
					PortalUtil.getPortalServerPort(false), false);

			DossierFile dossierFile = DossierFileLocalServiceUtil.getDossierFile(dossierFileId);

			DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(dossierFile.getFileEntryId());

			fileURL = portalURL + "/c/document_library/get_file?uuid=" + dlFileEntry.getUuid() + "&groupId="
					+ dlFileEntry.getGroupId();

		} catch (PortalException e) {

			//_log.error(e);
		}

		return fileURL;
	}

	private String formatDate(Date date) {
		String stringDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

		if (Validator.isNotNull(date)) {
			stringDate = sdf.format(date);
		}

		return stringDate;

	}

	private static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss";

	@GET
	@Path("/phuongtien/{phuongtienid}/thongtinvipham")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienThongTinViPham(@Context HttpHeaders header,
			@PathParam("phuongtienid") long phuongtienid) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILPhuongTien ilPhuongTien = ILPhuongTienLocalServiceUtil.fetchILPhuongTien(phuongtienid);

		if (Validator.isNotNull(ilPhuongTien)) {

			List<ILViPham> ilViPhams = ILViPhamLocalServiceUtil.getByPhuongTien(phuongtienid);

			if (ilViPhams.size() == 0) {
				error.put("code", "404");
				error.put("message", "VI_PHAM_NOT_FOUND");

				return Response.status(404).entity(error.toJSONString()).build();
			}

			JSONArray data = JSONFactoryUtil.createJSONArray();

			result.put("total", ilViPhams.size());

			for (ILViPham ilVipham : ilViPhams) {

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				try {
					jsonObject.put("phuongtien_id", ilVipham.getPhuongtien_id());
					jsonObject.put("ngayvipham", ilVipham.getNgayvipham());
					jsonObject.put("bienkiemsoat", ilPhuongTien.getBienkiemsoat());

					ILDataItem loaivipham = ILDataItemLocalServiceUtil.fetchILDataItem(ilVipham.getLoaivipham_id());

					jsonObject.put("loaivipham", loaivipham.getTen());
					jsonObject.put("mota", ilVipham.getMota());
					jsonObject.put("urlbienban", ilVipham.getUrl_bienban());
				} catch (Exception e) {
					_log.error(e);
				}

				data.put(jsonObject);

			}

			result.put("data", data);

			return Response.status(200).entity(result.toJSONString()).build();

		} else {
			error.put("code", "404");
			error.put("message", "PHUONG_TIEN_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/phuongtien/{phuongtienid}/hopdong")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienHopDongThue(@Context HttpHeaders header,
			@PathParam("phuongtienid") long phuongtienid) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILPhuongTien ilPhuongTien = ILPhuongTienLocalServiceUtil.fetchILPhuongTien(phuongtienid);

		if (Validator.isNotNull(ilPhuongTien)) {

			List<ILHopDongThue> ilHopDongThues = ILHopDongThueLocalServiceUtil.getByPhuongTien(phuongtienid);

			if (ilHopDongThues.size() == 0) {
				error.put("code", "404");
				error.put("message", "HOP_DONG_NOT_FOUND");

				return Response.status(404).entity(error.toJSONString()).build();
			}

			JSONArray data = JSONFactoryUtil.createJSONArray();

			result.put("total", ilHopDongThues.size());

			for (ILHopDongThue ilHopDongThue : ilHopDongThues) {

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("bienkiemsoat", ilPhuongTien.getBienkiemsoat());
				jsonObject.put("tendoanhnghiepchothue", ilHopDongThue.getTendoituongchothue());

				ILDataItem loaihinhchothue = ILDataItemLocalServiceUtil
						.fetchILDataItem(ilHopDongThue.getLoaihinhthue_id());

				if (Validator.isNotNull(loaihinhchothue)) {
					jsonObject.put("loaihinhchothue", loaihinhchothue.getTen());

				}

				jsonObject.put("ngayky", ilHopDongThue.getNgayky());
				jsonObject.put("thoihanthue", ilHopDongThue.getThoihan());
				jsonObject.put("hopdongcuoi", ilHopDongThue.getLa_hopdongcuoi());

				// TODO xem lai phan nay
				jsonObject.put("hopdong", ilHopDongThue.getGhichu());

				data.put(jsonObject);

			}

			result.put("data", data);

			return Response.status(200).entity(result.toJSONString()).build();

		} else {
			error.put("code", "404");
			error.put("message", "PHUONG_TIEN_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@GET
	@Path("/phuongtien/{phuongtienid}/phuhieu")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPhuongTienPhuHieu(@Context HttpHeaders header, @PathParam("phuongtienid") long phuongtienid) {

		JSONObject error = JSONFactoryUtil.createJSONObject();
		JSONObject result = JSONFactoryUtil.createJSONObject();

		ILPhuongTien ilPhuongTien = ILPhuongTienLocalServiceUtil.fetchILPhuongTien(phuongtienid);

		if (Validator.isNotNull(ilPhuongTien)) {

			List<ILPhuHieuBienHieu> ilPhuHieuBienHieus = ILPhuHieuBienHieuLocalServiceUtil
					.getByPhuongTien(phuongtienid);

			// List<ILHopDongThue> ilHopDongThues =
			// ILHopDongThueLocalServiceUtil.getByPhuongTien(phuongtienid);

			if (ilPhuHieuBienHieus.size() == 0) {
				error.put("code", "404");
				error.put("message", "PHU_HIEU_NOT_FOUND");

				return Response.status(404).entity(error.toJSONString()).build();
			}

			JSONArray data = JSONFactoryUtil.createJSONArray();

			result.put("total", ilPhuHieuBienHieus.size());

			for (ILPhuHieuBienHieu ilPhuHieuBienHieu : ilPhuHieuBienHieus) {

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("sophuhieu", ilPhuHieuBienHieu.getSophuhieu());
				jsonObject.put("ngaycap", ilPhuHieuBienHieu.getNgaycap());

				ILDataItem loaiphuhieu = ILDataItemLocalServiceUtil.fetchILDataItem(ilPhuHieuBienHieu.getLoaihinh_id());

				if (Validator.isNotNull(loaiphuhieu)) {
					jsonObject.put("loaiphuhieu", loaiphuhieu.getTen());

				}

				jsonObject.put("ngayhethan", ilPhuHieuBienHieu.getNgayhethan());
				jsonObject.put("thuhoiphuhieu", ilPhuHieuBienHieu.getNgaythuhoi());
				jsonObject.put("thongtinphuhieu", ilPhuHieuBienHieu.getLydo_thuhoi());

				data.put(jsonObject);

			}

			result.put("data", data);

			return Response.status(200).entity(result.toJSONString()).build();

		} else {
			error.put("code", "404");
			error.put("message", "PHUONG_TIEN_NOT_FOUND");

			return Response.status(404).entity(error.toJSONString()).build();
		}

	}

	@POST
	@Path("/validvehiclelicence")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validVehicleLicence(@Context HttpHeaders header, @FormParam("licenceNo") String licenceNo,
			@FormParam("licenceDate") String licenceDate) {
		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		JSONObject obj = JSONFactoryUtil.createJSONObject();
		try {
			ILCertificate ilCertificate = ILCertificateLocalServiceUtil.fetchByLicenceNo(licenceNo);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date licenceDateTemp = null;
			try {
				licenceDateTemp = sdf.parse(licenceDate);
			} catch (Exception e) {
				return null;
			}

			if (licenceDateTemp != null) {
				if (ilCertificate != null) {
					if (ilCertificate.getValidFrom() != null
							&& ilCertificate.getValidFrom().compareTo(licenceDateTemp) == 0) {
						Date now = new Date();
						if (ilCertificate.getValidUntil() != null
								&& ilCertificate.getValidUntil().getTime() < now.getTime()) {
							obj.put("valid", true);
							obj.put("message", StringPool.BLANK);
						} else {
							obj.put("valid", false);
							obj.put("message", "Giấy phép đã hết hạn");
						}
					} else {
						obj.put("valid", false);
						obj.put("message", "Ngày cấp phép không đúng");
					}
				} else {
					obj.put("valid", false);
					obj.put("message", "Số giấy phép không đúng");
				}
			} else {
				obj.put("valid", false);
				obj.put("message", "Ngày cấp giấy phép không đúng");
			}
		} catch (PortalException e) {
			obj.put("valid", false);
			obj.put("message", StringPool.BLANK);
		}
		return Response.status(200).entity(obj.toJSONString()).build();
	}

	@GET
	@Path("/vehicle/{registrationNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkVehicle(@Context HttpHeaders header,
			@PathParam("registrationNumber") String registrationNumber) {
		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		JSONObject obj = JSONFactoryUtil.createJSONObject();
		PhuongTien phuongTien = PhuongTienLocalServiceUtil.findByBKS(registrationNumber);
		ILVehicle vehicle = ILVehicleLocalServiceUtil.getByRegistrationNumber(registrationNumber);
		
		List<ILViPham> viphams = ILViPhamLocalServiceUtil.getByPhuongTien(phuongTien != null ? phuongTien.getId() : (vehicle != null ? vehicle.getId() : 0));

		if (vehicle != null || phuongTien != null) {
			if (viphams.size() > 0) {
				obj.put("vehicleStatus", 2);
			} else {
				obj.put("vehicleStatus", 3);
			}
		} else {
			obj.put("vehicleStatus", 1);
		}
		return Response.status(200).entity(obj.toJSONString()).build();
	}

	@GET
	@Path("/techspecs/vehicleclass/{vehicleClass}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTechSpecByVehicleClass(@Context HttpHeaders header,
			@PathParam("vehicleClass") String vehicleClass) {
		VRActions actions = new VRActionsImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		String module = header.getHeaderString("module");
		long dossierId = GetterUtil.getLong(header.getHeaderString("dossierId"));
		long dossierFileId = GetterUtil.getLong(header.getHeaderString("dossierFileId"));

		try {
			JSONObject object = actions.getTechSpecByVehicleClass(groupId, module, dossierId, dossierFileId,
					vehicleClass);

			return Response.status(object.getInt("status")).entity(object.getString("content")).build();

		} catch (Exception e) {

			return Response.status(500).entity("error").build();
		}

	}

	@GET
	@Path("/techspecs/{vehicleClass}/vehicleclassexts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTechSpecByVehicleClassExt(@Context HttpHeaders header,
			@PathParam("vehicleClass") String vehicleClass) {
		VRActions actions = new VRActionsImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		String module = header.getHeaderString("module");
		long dossierId = GetterUtil.getLong(header.getHeaderString("dossierId"));
		long dossierFileId = GetterUtil.getLong(header.getHeaderString("dossierFileId"));
		String fileTemplateNo = GetterUtil.getString(header.getHeaderString("fileTemplateNo"));

		try {
			JSONObject object = actions.getTechSpecByVehicleClassExt(groupId, module, dossierId, dossierFileId,
					fileTemplateNo, vehicleClass);

			return Response.status(object.getInt("status")).entity(object.getString("content")).build();

		} catch (Exception e) {

			return Response.status(500).entity("error").build();
		}

	}

	@GET
	@Path("/techspecs/{vehicleclass:.*}/{vehicletype: .*}/vehicleclassexts")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTechSpecByVehicleType(@Context HttpHeaders header,
			@PathParam("vehicleclass") String vehicleClass, @PathParam("vehicletype") String vehicleType) {
		VRActions actions = new VRActionsImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		String module = header.getHeaderString("module");
		long dossierId = GetterUtil.getLong(header.getHeaderString("dossierId"));
		long dossierFileId = GetterUtil.getLong(header.getHeaderString("dossierFileId"));
		String fileTemplateNo = GetterUtil.getString(header.getHeaderString("fileTemplateNo"));

		try {
			JSONObject object = actions.getTechSpecByVehicleType(groupId, module, dossierId, dossierFileId,
					fileTemplateNo, vehicleClass, vehicleType);

			return Response.status(object.getInt("status")).entity(object.getString("content")).build();

		} catch (Exception e) {

			return Response.status(500).entity("error").build();
		}

	}

	@GET
	@Path("/techspecslimit/vehicleclass/{vehicleclass: .*}/vehicletype/{vehicletype: .*}/formulatype/{formulatype: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTecSpecs(@Context HttpHeaders header, @PathParam("vehicleclass") String vehicleClass,
			@PathParam("vehicletype") String vehicleType, @PathParam("formulatype") long fomulaType) {
		VRActions actions = new VRActionsImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		String module = header.getHeaderString("module");
		long dossierId = GetterUtil.getLong(header.getHeaderString("dossierId"));
		long dossierFileId = GetterUtil.getLong(header.getHeaderString("dossierFileId"));
		try {
			JSONObject object = actions.getTechSpecLimit(groupId, module, dossierId, dossierFileId, vehicleClass,
					vehicleType, fomulaType);

			return Response.status(object.getInt("status")).entity(object.getString("content")).build();

		} catch (Exception e) {

			return Response.status(500).entity("error").build();
		}

	}

	@GET
	@Path("/techspecs/{vehicleclass:.*}/{vehicletype: .*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTechSpecByVehicleClass(@Context HttpHeaders header,
			@PathParam("vehicleclass") String vehicleClass, @PathParam("vehicletype") String vehicleType) {
		VRActions actions = new VRActionsImpl();

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		String module = header.getHeaderString("module");
		long dossierId = GetterUtil.getLong(header.getHeaderString("dossierId"));
		long dossierFileId = GetterUtil.getLong(header.getHeaderString("dossierFileId"));
		try {
			JSONObject object = actions.getTechSpecByVehicleClassType(groupId, module, dossierId, dossierFileId,
					vehicleClass, vehicleType);

			return Response.status(object.getInt("status")).entity(object.getString("content")).build();

		} catch (Exception e) {

			return Response.status(500).entity("error").build();
		}

	}

	@GET
	@Path("/json/{code: .*}/items")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getForJasper(@Context HttpHeaders header, @PathParam("code") String code) {

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		JSONObject resp = JSONFactoryUtil.createJSONObject();

		DictItemUtil dictUtil = new DictItemUtil();

		long dictCollectionId = dictUtil.getCollectionId(code, groupId);

		try {

			List<DictItem> items = DictItemLocalServiceUtil.findByF_dictCollectionId(dictCollectionId);

			JSONArray arrReturn = JSONFactoryUtil.createJSONArray();

			for (DictItem di : items) {
				JSONObject ob = JSONFactoryUtil.createJSONObject();

				ob.put(di.getItemCode(), di.getItemName());

				arrReturn.put(ob);
			}

			resp.put("Items", arrReturn);

			return Response.status(200).entity(resp.toString()).build();

		} catch (Exception e) {
			return Response.status(404).entity(resp.toString()).build();

		}

	}

	@GET
	@Path("/collections/{code: .*}/{type: .*}/items")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDictItems(@Context HttpHeaders header, @PathParam("code") String code,
			@PathParam("type") String type) {

		long groupId = GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		JSONObject resp = JSONFactoryUtil.createJSONObject();

		DictItemUtil dictUtil = new DictItemUtil();

		long dictCollectionId = dictUtil.getCollectionId(code, groupId);

		try {
			List<DictItem> items = DictItemLocalServiceUtil.findByF_dictCollectionId(dictCollectionId);

			JSONArray itemsJson = dictUtil.getDictItem(items, type);

			resp.put("Items", itemsJson);
			resp.put("Total", itemsJson.length());

			return Response.status(200).entity(resp.toString()).build();

		} catch (Exception e) {
			return Response.status(404).entity(resp.toString()).build();
		}

	}

	@POST
	@Path("/report/{code: .*}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response getReport(@Context HttpHeaders header, @PathParam("code") String code,
			@FormParam("year") String year, @FormParam("fromDate") String fromDate, @FormParam("toDate") String toDate,
			@FormParam("organization") String organization, @FormParam("location") String location,
			@FormParam("reportDate") String reportDate, @FormParam("userCreate") String userCreate,
			@FormParam("userReview") String userReview, @FormParam("note") String note) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		try {
			VRReport report = VRReportLocalServiceUtil.getReport(code);

			String formReport = report.getFormReport();

			String formData = ""; // TODO: build formData for report

			Message message = new Message();

			message.put("formReport", formReport);

			message.put("formData", formData);

			message.setResponseId(String.valueOf(report.getPrimaryKeyObj()));
			message.setResponseDestinationName("jasper/engine/preview/callback");

			String previewResponse = (String) MessageBusUtil.sendSynchronousMessage("jasper/engine/preview/destination",
					message, 10000);

			File file = new File(previewResponse);

			ResponseBuilder responseBuilder = Response.ok((Object) file);

			responseBuilder.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
			responseBuilder.header("Content-Type", "application/pdf");

			return responseBuilder.build();
		} catch (Exception e) {
			return Response.status(500).entity("error").build();
		}
	}

	@GET
	@Path("/certnumbers")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertNumbers(@Context HttpHeaders header) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		try {
			List<Counter> counters = CounterLocalServiceUtil.getCounters(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

			JSONObject jsObj = JSONFactoryUtil.createJSONObject();

			JSONArray jsArr = JSONFactoryUtil.createJSONArray();

			for (Counter cnt : counters) {
				JSONObject elm = JSONFactoryUtil.createJSONObject();

				if (cnt.getName().contains(PRE_FIX_CERT)) {

					String[] splitPattern = StringUtil.split(cnt.getName(), StringPool.AT);

					elm.put("certId", cnt.getName());
					elm.put("pattern", splitPattern[1]);
					elm.put("year", splitPattern[2]);
					elm.put("initNumber", cnt.getCurrentId());

					jsArr.put(elm);
				}
			}

			jsObj.put("total", jsArr.length());

			jsObj.put("data", jsArr);

			return Response.status(200).entity(jsObj.toString()).build();
		} catch (Exception e) {
			return Response.status(500).entity("error").build();
		}
	}

	@GET
	@Path("/certnumbers/{certid}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDetailCertNumbers(@Context HttpHeaders header, @PathParam("certid") String certid) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		try {
			Counter counter = CounterLocalServiceUtil.getCounter(certid);
			String[] splitPattern = StringUtil.split(counter.getName(), StringPool.AT);

			JSONObject elm = JSONFactoryUtil.createJSONObject();

			elm.put("certId", counter.getName());
			elm.put("pattern", splitPattern[1]);
			elm.put("year", splitPattern[2]);
			elm.put("initNumber", counter.getCurrentId());

			return Response.status(200).entity(elm.toString()).build();

		} catch (Exception e) {
			return Response.status(500).entity("error").build();
		}
	}

	@POST
	@Path("/certnumbers")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addCertNumbers(@Context HttpHeaders header, @FormParam("pattern") String pattern,
			@FormParam("year") int year, @FormParam("initNumber") int initNumber) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		// CounterLocalServiceUtil.increment();

		JSONObject jsObj = JSONFactoryUtil.createJSONObject();

		try {

			String certId = PRE_FIX_CERT + pattern + StringPool.AT + year;

			Counter counterInit = CounterLocalServiceUtil.createCounter(certId);
			counterInit.setCurrentId(initNumber);

			CounterLocalServiceUtil.updateCounter(counterInit);

			jsObj.put("status", "done");

			return Response.status(200).entity(jsObj.toString()).build();
		} catch (Exception e) {
			jsObj.put("status", "error");

			return Response.status(500).entity(jsObj.toString()).build();
		}
	}

	@PUT
	@Path("/certnumbers/{certid}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response updateSertNumbers(@Context HttpHeaders header, @PathParam("certid") long certid,
			@FormParam("pattern") String pattern, @FormParam("year") int year,
			@FormParam("initNumber") int initNumber) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));
		JSONObject jsObj = JSONFactoryUtil.createJSONObject();

		try {
			String certId = PRE_FIX_CERT + pattern + StringPool.AT + year;

			Counter counter = CounterLocalServiceUtil.getCounter(certId);

			counter.setCurrentId(initNumber);

			CounterLocalServiceUtil.updateCounter(counter);

			jsObj.put("status", "done");
			return Response.status(200).entity(jsObj.toString()).build();
		} catch (Exception e) {
			jsObj.put("status", "error");

			return Response.status(500).entity(jsObj.toString()).build();
		}
	}

	@GET
	@Path("/certnumbers/{pattern}/dossier/{dossierid}/generate")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
	public Response generatorCertNumbers(@Context HttpHeaders header, @PathParam("pattern") String pattern,
			@PathParam("dossierid") long dossierid) {

		// long groupId =
		// GetterUtil.getLong(header.getHeaderString(Field.GROUP_ID));

		String certNumber;

		try {

			long _counterNumber = 0;

			Calendar cal = Calendar.getInstance();

			cal.setTime(new Date());

			// int curYear = cal.get(Calendar.YEAR);

			DateFormat df = new SimpleDateFormat("yyyy");
			DateFormat sdf = new SimpleDateFormat("yy");

			String curYear = df.format(cal.getTime());
			String shortCurYear = sdf.format(cal.getTime());

			String certConfigId = PRE_FIX_CERT + pattern + StringPool.AT + curYear;

			_log.info("___certConfigId" + certConfigId);

			String certConfigCurrId = PRE_FIX_CERT_CURR + pattern + StringPool.AT + curYear;

			_log.info("___certConfigCurrId" + certConfigCurrId);

			Counter counterConfig = CounterLocalServiceUtil.fetchCounter(certConfigId);

			String elmCertId = PRE_FIX_CERT_ELM + pattern + StringPool.AT + curYear + StringPool.AT + dossierid;

			// Counter counter = CounterLocalServiceUtil.fetchCounter(certId);

			if (Validator.isNotNull(counterConfig)) {
				// create counter config

				Counter currCounter = CounterLocalServiceUtil.fetchCounter(certConfigCurrId);

				if (Validator.isNull(currCounter)) {
					_log.info("COUTER_CURR_CONFIG_IS_NULL");

					currCounter = CounterLocalServiceUtil.createCounter(certConfigCurrId);

					currCounter.setCurrentId(counterConfig.getCurrentId());

					_counterNumber = counterConfig.getCurrentId();

					CounterLocalServiceUtil.updateCounter(currCounter);

					// Create elmCounter
					Counter elmCounter = CounterLocalServiceUtil.createCounter(elmCertId);

					elmCounter.setCurrentId(_counterNumber);

					CounterLocalServiceUtil.updateCounter(elmCounter);

				} else {
					_log.info("COUTER_CURR_CONFIG_IS_NOT_NULL");

					// check counter for element
					Counter elmCounter = CounterLocalServiceUtil.fetchCounter(elmCertId);

					if (Validator.isNotNull(elmCounter)) {
						_log.info("ELM_COUTER_CONFIG_IS_NOT_NULL");

						_counterNumber = elmCounter.getCurrentId();
					} else {
						// create elm Counter
						_log.info("ELM_COUTER_CONFIG_IS_NULL");
						elmCounter = CounterLocalServiceUtil.createCounter(elmCertId);

						// increment CurrentCounter

						currCounter.setCurrentId(currCounter.getCurrentId() + 1);
						CounterLocalServiceUtil.updateCounter(currCounter);

						_counterNumber = currCounter.getCurrentId();

						elmCounter.setCurrentId(_counterNumber);

						CounterLocalServiceUtil.updateCounter(elmCounter);
					}

				}

				certNumber = String.format("%05d", _counterNumber) + StringPool.FORWARD_SLASH + pattern
						+ StringPool.FORWARD_SLASH + shortCurYear;

			} else {
				throw new Exception("Don't have counter config");
			}

			return Response.status(200).entity(certNumber).build();

		} catch (Exception e) {

			return Response.status(500).entity(e.getMessage()).build();
		}
	}

	/** Process search result - START */
	// Search document acceptable
	@GET
	@Path("/certDoc")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDocAccept(@Context HttpHeaders header, @QueryParam("keywords") String keywords,
			@QueryParam("serviceCode") String serviceCode, @QueryParam("govAgencyCode") String govAgencyCode,
			@QueryParam("routeCode") String routeCode, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate, @QueryParam("start") int start, @QueryParam("limit") int limit) {

		ILCertificateActions actions = new ILCertificateActionsImpl();

		try {
			// Process key search
			String keySearch = StringPool.BLANK;
			String serviceSearch = StringPool.BLANK;
			String govCodeSearch = StringPool.BLANK;
			String routeCodeSearch = StringPool.BLANK;
			String fromDateSearch = StringPool.BLANK;
			String toDateSearch = StringPool.BLANK;
			if (Validator.isNotNull(keywords)) {
				keywords = keywords.trim();
				keySearch = keywords.toLowerCase() + "%";
			}
			if (Validator.isNotNull(serviceCode)) {
				if (serviceCode.contains(StringPool.COMMA)) {
					String[] serviceSplit = StringUtil.split(serviceCode, StringPool.COMMA);
					if (serviceSplit != null && serviceSplit.length > 0) {
						StringBuilder sb = new StringBuilder();
						int lenghtService = serviceSplit.length;
						for (int i = 0; i < lenghtService; i++) {
							if (i == 0) {
								sb.append(StringPool.APOSTROPHE);
								sb.append(serviceSplit[i]);
								sb.append(StringPool.APOSTROPHE);
							} else {
								sb.append(StringPool.COMMA);
								sb.append(StringPool.APOSTROPHE);
								sb.append(serviceSplit[i]);
								sb.append(StringPool.APOSTROPHE);
							}
						}
						serviceSearch = sb.toString();
						if (Validator.isNotNull(serviceSearch)) {
							serviceSearch = serviceSearch.toLowerCase();
						}
					}
				} else {
					serviceSearch = StringPool.APOSTROPHE + serviceCode.toLowerCase() + StringPool.APOSTROPHE;
				}
			}
			_log.info("serviceSearch: " + serviceSearch);
			if (Validator.isNotNull(govAgencyCode)) {
				govCodeSearch = govAgencyCode.toLowerCase();
			}
			if (Validator.isNotNull(routeCode)) {
				routeCodeSearch = routeCode.toLowerCase();
			}
			if (Validator.isNotNull(fromDate)) {
				fromDate = ConvertFormatDate.formatDateSQL(fromDate);
			}
			if (Validator.isNotNull(toDate)) {
				toDateSearch = ConvertFormatDate.formatDateSQL(toDate);
			}

			JSONObject jsonData = actions.getDocAcceptList(keySearch, serviceSearch, govCodeSearch, routeCodeSearch,
					fromDateSearch, toDateSearch, start, limit);

			ILCertificateResultModel result = new ILCertificateResultModel();
			// List<ILCertificateModel> certModelList =
			// ILCertificateUtils.mappingToResultCertificate(certList);
			if (jsonData != null && jsonData.getInt("total") > 0) {
				result.setTotal(jsonData.getInt("total"));
				result.getData().addAll(
						ILCertificateUtils.mappingToResultCertificate((List<ILCertificate>) jsonData.get("data")));
			} else {
				result.setTotal(0);
			}

			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			_log.error(e);
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	/** Process search result - END */

	// Get detail certificate info
	@GET
	@Path("/certDoc/{id}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDetailCert(@Context HttpHeaders header, @PathParam("id") String id) {

		ILCertificateActions actions = new ILCertificateActionsImpl();
		long certificateId = Long.valueOf(id);

		try {
			// Process key search

			ILCertificate certInfo = actions.getDetailCert(certificateId);

			ILCertificateModel result = ILCertificateUtils.mappingToDetailCertificate(certInfo);

			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			_log.error(e);
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	@GET
	@Path("/certDoc/borderGuard/{registrationNumber}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getBorderGuardList(@Context HttpHeaders header,
			@PathParam("registrationNumber") String registrationNumber, @QueryParam("start") int start,
			@QueryParam("end") int end) {

		ILCertificateActions actions = new ILCertificateActionsImpl();

		try {
			// Process key search
			List<ILVehicleCustomsBorderGuard> cbGuardList = actions.getBorderGuardList(registrationNumber, start, end);
			ILCBGuardResultModel result = new ILCBGuardResultModel();

			result.setTotal(actions.countBorderGuardList(registrationNumber));

			if (cbGuardList != null && cbGuardList.size() > 0) {
				result.setRegistrationNumber(registrationNumber);
				result.getData().addAll(ILCertificateUtils.mappingToResultCBGuard(cbGuardList));
			} else {
				result.setRegistrationNumber(registrationNumber);
			}
			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			_log.error(e);
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	@POST
	@Path("/certDoc/borderGuard/{registrationNumber}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addCustomBorderGuard(@Context HttpHeaders header,
			@PathParam("registrationNumber") String registrationNumber,
			@FormParam("expImpGateType") String expImpGateType, @FormParam("expImpGate") String expImpGate,
			@FormParam("expImpGateCode") String expImpGateCode, @FormParam("driverName") String driverName,
			@FormParam("driverLicenceNo") String driverLicenceNo,
			@FormParam("registrationDate") String registrationDate) {

		ILCertificateActions actions = new ILCertificateActionsImpl();

		try {
			Date regDate = null;
			_log.info("registrationDate:" + registrationDate);
			if (Validator.isNotNull(registrationDate)) {
				regDate = ConvertFormatDate.parseStringToDate(registrationDate, ConvertFormatDate._NORMAL_DATE);
				_log.info("regDate:" + regDate);
			}
			ILVehicleCustomsBorderGuard cbGuardInput = actions.addCustomBorderGuard(registrationNumber, expImpGateType,
					expImpGateCode, expImpGate, driverName, driverLicenceNo, regDate);

			ILCBGuardModel result = ILCertificateUtils.mappingToDetailCBGuard(cbGuardInput);

			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			_log.error(e);
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	@GET
	@Path("/certDoc/applicant")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCertByApplicant(@Context HttpHeaders header, @QueryParam("applicantIdNo") String applicantIdNo,
			@QueryParam("registrationNumber") String registrationNumber,
			@QueryParam("serviceCode") String serviceCode) {

		ILCertificateActions actions = new ILCertificateActionsImpl();

		try {
			// Process key search
			String applicant = StringPool.BLANK;
			String regSearch = StringPool.BLANK;
			String serviceSearch = StringPool.BLANK;

			if (Validator.isNotNull(applicantIdNo)) {
				applicant = applicantIdNo.toLowerCase();
			}
			_log.info("applicantIdNo: " + applicantIdNo);
			_log.info("applicant: " + applicant);
			if (Validator.isNotNull(serviceCode)) {
				serviceSearch = serviceCode.toLowerCase();
			}
			_log.info("serviceCode: " + serviceCode);
			_log.info("serviceSearch: " + serviceSearch);
			if (Validator.isNotNull(registrationNumber)) {
				regSearch = registrationNumber.toLowerCase();
				_log.info("registrationNumber: " + registrationNumber);
				_log.info("regSearch: " + regSearch);
				ILCertificate certInfo = actions.getCertByValidFrom(serviceSearch, applicantIdNo, registrationNumber);
				ILCertificateModel result = null;
				if (certInfo != null) {
					result = ILCertificateUtils.mappingToDetailCertificate(certInfo);
				}

				return Response.status(200).entity(result).build();
			} else {
				JSONObject jsonData = actions.getCertByApplicant(serviceSearch, applicant);
				ILCertificateResultModel result = new ILCertificateResultModel();
				// List<ILCertificateModel> certModelList =
				// ILCertificateUtils.mappingToResultCertificate(certList);
				if (jsonData != null && jsonData.getInt("total") > 0) {
					result.setTotal(jsonData.getInt("total"));
					result.getData().addAll(
							ILCertificateUtils.mappingToResultCertificate((List<ILCertificate>) jsonData.get("data")));
				} else {
					result.setTotal(0);
				}
				return Response.status(200).entity(result).build();
			}

		} catch (Exception e) {
			_log.error(e);
			return Response.status(500).entity(e.getMessage()).build();

		}
	}

	@GET
	@Path("/ilvehicle/{registrationNumber}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getVehicleByRegistrationNumber(@Context HttpHeaders header,
			@PathParam("registrationNumber") String registrationNumber) {
		ILVehicleModel result = new ILVehicleModel();

		com.fds.vr.business.model.ILVehicle ilVehicle = ILVehicleLocalServiceUtil
				.getByRegistrationNumber(registrationNumber);
		if (ilVehicle != null) {
			result.setCapacity(ilVehicle.getCapacity());
			result.setVehicleModelCode(ilVehicle.getVehicleModelCode());
			result.setVehicleModelName(ilVehicle.getVehicleModelName());
			result.setRegistrationDriver(ilVehicle.getRegistrationDriver());
			result.setRegistrationNumber(ilVehicle.getRegistrationNumber());
			result.setTrademarkCode(ilVehicle.getTrademarkCode());
			result.setTrademarkName(ilVehicle.getTrademarkName());
			result.setChassisNumber(ilVehicle.getChassisNumber());
			result.setEngineNumber(ilVehicle.getEngineNumber());
			result.setVehicleColor(ilVehicle.getVehicleColor());
			result.setManufacturedYear(ilVehicle.getManufacturedYear());
		}

		return Response.status(200).entity(result).build();
	}

	@GET
	@Produces("text/plain")
	public String working() {
		return "It works!";
	}

	Log _log = LogFactoryUtil.getLog(VRRestApplication.class);

}