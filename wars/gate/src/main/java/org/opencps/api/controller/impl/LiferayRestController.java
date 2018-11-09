package org.opencps.api.controller.impl;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.session.AuthenticatedSessionManagerUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.opencps.api.configuration.WebKeys;
import org.opencps.api.model.FileTemplateMiniItem;
import org.opencps.datamgt.model.DictCollection;
import org.opencps.datamgt.model.FileAttach;
import org.opencps.datamgt.service.DictCollectionLocalServiceUtil;
import org.opencps.datamgt.service.FileAttachLocalServiceUtil;
import org.opencps.dossiermgt.model.ServiceFileTemplate;
import org.opencps.dossiermgt.service.ServiceFileTemplateLocalServiceUtil;
import org.opencps.dossiermgt.service.persistence.ServiceFileTemplatePK;
import org.opencps.socket.whiteboard.BundleLoader;
import org.opencps.usermgt.model.Employee;
import org.opencps.usermgt.model.EmployeeJobPos;
import org.opencps.usermgt.model.JobPos;
import org.opencps.usermgt.service.EmployeeLocalServiceUtil;
import org.opencps.usermgt.service.JobPosLocalServiceUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import backend.utils.FileUploadUtils;

/**
 * Rest Controller
 *
 * @author binhth
 */
@RestController
public class LiferayRestController {

	@RequestMapping(value = "/user/{id}/deactive", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void deactiveAccount(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") long id,
			@RequestBody String body) {

		try {

			JSONObject bodyData = JSONFactoryUtil.createJSONObject(body);

			User user = UserLocalServiceUtil.getUser(id);

			boolean locked = bodyData.getBoolean("locked");

			if (locked) {
				user.setStatus(WorkflowConstants.STATUS_INACTIVE);
			} else {
				user.setStatus(WorkflowConstants.STATUS_APPROVED);
			}

			UserLocalServiceUtil.updateUser(user);

			Indexer<User> indexer = IndexerRegistryUtil.nullSafeGetIndexer(User.class);

			indexer.reindex(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/user/{id}/changepass", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void changePassWordUser(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") long id, @RequestBody String body) {

		try {

			JSONObject bodyData = JSONFactoryUtil.createJSONObject(body);

			String password = bodyData.getString("password");

			User user = UserLocalServiceUtil.updatePassword(id, password, password, Boolean.FALSE);

			Indexer<User> indexer = IndexerRegistryUtil.nullSafeGetIndexer(User.class);

			indexer.reindex(user);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getUserId(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") long id) {

		JSONObject result = JSONFactoryUtil.createJSONObject();

		result.put("email", StringPool.BLANK);
		result.put("screenName", StringPool.BLANK);
		result.put("deactiveAccountFlag", 0);

		try {

			User user = UserLocalServiceUtil.fetchUser(id);

			result.put("email", user.getEmailAddress());
			result.put("screenName", user.getScreenName());
			result.put("deactiveAccountFlag", user.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result.toJSONString();
	}

	@RequestMapping(value = "/users/login", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getUserLoginInfo(HttpServletRequest request, HttpServletResponse response) {

		JSONArray dataUser = JSONFactoryUtil.createJSONArray();

		JSONObject result = JSONFactoryUtil.createJSONObject();

		result.put("email", StringPool.BLANK);
		result.put("role", StringPool.BLANK);
		result.put("deactiveAccountFlag", 0);

		try {

			long userId = 0;
			if (Validator.isNotNull(request.getAttribute(WebKeys.USER_ID))) {
				userId = Long.valueOf(request.getAttribute(WebKeys.USER_ID).toString());

				User user = UserLocalServiceUtil.fetchUser(userId);

				List<Role> roles = user.getRoles();

				String roleName = StringPool.BLANK;

				for (Role role : roles) {

					if (role.getName().equals("Administrator")) {
						roleName = "Administrator";
						break;
					}

					if (role.getName().equals("Administrator_data")) {
						roleName = "Administrator_data";
						break;
					}

				}

				result.put("email", user.getEmailAddress());
				result.put("role", roleName);
				result.put("deactiveAccountFlag", user.getStatus());

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		dataUser.put(result);

		return dataUser.toJSONString();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public String doLogin(HttpServletRequest request, HttpServletResponse response) {

		try {

			Enumeration<String> headerNames = request.getHeaderNames();

			String strBasic = StringPool.BLANK;

			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					String key = (String) headerNames.nextElement();
					String value = request.getHeader(key);
					if (key.trim().equalsIgnoreCase(WebKeys.AUTHORIZATION)) {
						strBasic = value;
						break;
					}
				}
			}

			// Get encoded user and password, comes after "BASIC "
			String userpassEncoded = strBasic.substring(6);
			String decodetoken = new String(Base64.decode(userpassEncoded), StringPool.UTF8);

			String account[] = decodetoken.split(":");

			String email = account[0];
			String password = account[1];

			long userId = AuthenticatedSessionManagerUtil.getAuthenticatedUserId(request, email, password,
					CompanyConstants.AUTH_TYPE_EA);

			if (userId > 0 && userId != 20103) {

				AuthenticatedSessionManagerUtil.login(request, response, email, password, true,
						CompanyConstants.AUTH_TYPE_EA);

				Employee employee = EmployeeLocalServiceUtil.fetchByFB_MUID(userId);

				User user = UserLocalServiceUtil.fetchUser(userId);

				if (Validator.isNotNull(employee)) {

					if (user != null && user.getStatus() == WorkflowConstants.STATUS_PENDING) {
						return "pending";
					} else {
						return "/c";
					}
				} else {
					if (user != null && user.getStatus() == WorkflowConstants.STATUS_PENDING) {
						return "pending";
					} else {
						return "ok";
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@RequestMapping(value = "/users/avatar/{className}/{pk}", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getAttachment(HttpServletRequest request,
			@PathVariable("className") String className, @PathVariable("pk") String pk) {
		
		String result = StringPool.BLANK;
		
		long groupId = 0;
		
		if (Validator.isNotNull(request.getHeader("groupId"))) {
			groupId = Long.valueOf(request.getHeader("groupId"));
		}
		
		List<FileAttach> fileAttachs = FileAttachLocalServiceUtil.findByF_className_classPK(groupId, className, pk);

		if (Validator.isNotNull(fileAttachs) && fileAttachs.size() > 0) {
			
			FileAttach fileAttach = fileAttachs.get(0);
			
			try {

				DLFileEntry file = DLFileEntryLocalServiceUtil.getFileEntry(fileAttach.getFileEntryId());

				result = "/documents/" + file.getGroupId() + StringPool.FORWARD_SLASH + file.getFolderId() + StringPool.FORWARD_SLASH
			            + file.getTitle() + StringPool.FORWARD_SLASH + file.getUuid();
				
			} catch (PortalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	@RequestMapping(value = "/users/upload/{code}/{className}/{pk}", method = RequestMethod.POST)
	public void uploadAttachment(MultipartHttpServletRequest request, @PathVariable("code") String code,
			@PathVariable("className") String className, @PathVariable("pk") String pk) {

		CommonsMultipartFile multipartFile = null;

		Iterator<String> iterator = request.getFileNames();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			// create multipartFile array if you upload multiple files
			multipartFile = (CommonsMultipartFile) request.getFile(key);
		}

		long userId = 0;
		if (Validator.isNotNull(request.getAttribute(WebKeys.USER_ID))) {
			userId = Long.valueOf(request.getAttribute(WebKeys.USER_ID).toString());
		}
		long groupId = 0;
		if (Validator.isNotNull(request.getHeader("groupId"))) {
			groupId = Long.valueOf(request.getHeader("groupId"));
		}
		long companyId = CompanyThreadLocal.getCompanyId();
		String desc = "FileAttach file upload";
		String destination = "FileAttach/";

		ServiceContext serviceContext = new ServiceContext();
		serviceContext.setUserId(userId);
		serviceContext.setCompanyId(companyId);
		serviceContext.setScopeGroupId(groupId);

		try {

			FileEntry fileEntry = FileUploadUtils.uploadFile(userId, companyId, groupId, multipartFile.getInputStream(),
					UUID.randomUUID() + "_" + multipartFile.getOriginalFilename(),
					multipartFile.getOriginalFilename()
							.substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1),
					multipartFile.getSize(), destination, desc, serviceContext);

			if (code.equals("opencps_adminconfig")) {

				ServiceFileTemplateLocalServiceUtil.addServiceFileTemplate(Long.valueOf(pk),
						fileEntry.getFileEntryId() + StringPool.BLANK, multipartFile.getOriginalFilename(),
						fileEntry.getFileEntryId(), serviceContext);

			} else {

				User user = UserLocalServiceUtil.fetchUser(userId);
				
				FileAttach fileAttach = FileAttachLocalServiceUtil.addFileAttach(userId, groupId, className, pk, user.getFullName(), user.getEmailAddress(),
						fileEntry.getFileEntryId(), StringPool.BLANK, StringPool.BLANK, 0, fileEntry.getFileName(), serviceContext);

				if (code.equals("opencps_employee")) {
					Employee employee = EmployeeLocalServiceUtil.fetchEmployee(Long.valueOf(pk));
					employee.setPhotoFileEntryId(fileAttach.getFileEntryId());
					EmployeeLocalServiceUtil.updateEmployee(employee);
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/filetemplate/{pk}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getServiceFileTemplate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("pk") long pk) {

		JSONObject result = JSONFactoryUtil.createJSONObject();

		JSONArray resultArray = JSONFactoryUtil.createJSONArray();

		List<ServiceFileTemplate> serviceFileTemplates = ServiceFileTemplateLocalServiceUtil.getByServiceInfoId(pk);

		result.put("total", serviceFileTemplates.size());

		JSONObject object = null;
		for (ServiceFileTemplate serviceFileTemplate : serviceFileTemplates) {

			try {

				object = JSONFactoryUtil.createJSONObject(JSONFactoryUtil.looseSerialize(serviceFileTemplate));

				long fileEntryId = serviceFileTemplate.getFileEntryId();

				FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);

				object.put("extension", fileEntry.getExtension());
				object.put("size", fileEntry.getSize());

				resultArray.put(object);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		result.put("data", resultArray);

		return result.toJSONString();

	}

	@RequestMapping(value = "/fileattach/{className}/{pk}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getAttachFileData(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("className") String className, @PathVariable("pk") String pk) {

		JSONObject result = JSONFactoryUtil.createJSONObject();

		JSONArray resultArray = JSONFactoryUtil.createJSONArray();

		long groupId = 0;

		if (Validator.isNotNull(request.getHeader("groupId"))) {
			groupId = Long.valueOf(request.getHeader("groupId"));
		}

		List<FileAttach> fileAttachs = FileAttachLocalServiceUtil.findByF_className_classPK(groupId, className, pk);

		result.put("total", fileAttachs.size());

		JSONObject object = null;
		for (FileAttach ett : fileAttachs) {

			try {
				
				String newName = ett.getFileName();

				if (newName.indexOf("_") > 0) {
					
					ett.setFileName(newName.substring(newName.indexOf("_") + 1));
					
				}
				
				object = JSONFactoryUtil.createJSONObject(JSONFactoryUtil.looseSerialize(ett));

				long fileEntryId = ett.getFileEntryId();

				FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);

				object.put("extension", fileEntry.getExtension());
				object.put("size", fileEntry.getSize());

				resultArray.put(object);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		result.put("data", resultArray);

		return result.toJSONString();

	}

	@RequestMapping(value = "/filetemplate/{serviceInfoId}/{fileTemplateNo}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeServiceFileTemplate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceInfoId") long serviceInfoId, @PathVariable("fileTemplateNo") String fileTemplateNo) {

		try {
			ServiceFileTemplate serviceFileTemplate = ServiceFileTemplateLocalServiceUtil
					.fetchByF_serviceInfoId_fileTemplateNo(serviceInfoId, fileTemplateNo);

			long fileEntryId = serviceFileTemplate.getFileEntryId();

			ServiceFileTemplateLocalServiceUtil.deleteServiceFileTemplate(serviceFileTemplate);
			DLAppLocalServiceUtil.deleteFileEntry(fileEntryId);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/filetemplate/{serviceInfoId}/{fileTemplateNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody byte[] downloadServiceFileTemplate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceInfoId") long serviceInfoId, @PathVariable("fileTemplateNo") String fileTemplateNo) {

		ServiceFileTemplate serviceFileTemplate = ServiceFileTemplateLocalServiceUtil
				.fetchByF_serviceInfoId_fileTemplateNo(serviceInfoId, fileTemplateNo);

		if (Validator.isNotNull(serviceFileTemplate)) {
			try {

				FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(serviceFileTemplate.getFileEntryId());

				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + serviceFileTemplate.getTemplateName() + fileEntry.getExtension());

				InputStream inputStream = fileEntry.getContentStream();

				return IOUtils.toByteArray(inputStream);

			} catch (Exception exception) {
				System.out.println(exception);
			}
		}
		return null;
	}

	@RequestMapping(value = "/filetemplate/{serviceInfoId}/{fileTemplateNo}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void upDateServiceFileTemplate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("serviceInfoId") long serviceInfoId, @PathVariable("fileTemplateNo") String fileTemplateNo,
			@RequestBody FileTemplateMiniItem fileTemplateMiniItem) {

		ServiceFileTemplate serviceFileTemplate = ServiceFileTemplateLocalServiceUtil
				.fetchByF_serviceInfoId_fileTemplateNo(serviceInfoId, fileTemplateNo);

		ServiceFileTemplatePK serviceFileTemplatePK = new ServiceFileTemplatePK(serviceInfoId, fileTemplateNo);

		ServiceFileTemplate serviceFileTemplateNew;
		try {
			serviceFileTemplateNew = ServiceFileTemplateLocalServiceUtil.getServiceFileTemplate(serviceFileTemplatePK);

			ServiceFileTemplateLocalServiceUtil.deleteServiceFileTemplate(serviceFileTemplate);

			if (Validator.isNotNull(serviceFileTemplateNew)) {

				if (Validator.isNotNull(fileTemplateMiniItem.getFileTemplateNo())) {
					serviceFileTemplateNew.setFileTemplateNo(fileTemplateMiniItem.getFileTemplateNo());
				}
				if (Validator.isNotNull(fileTemplateMiniItem.getTemplateName())) {
					serviceFileTemplateNew.setTemplateName(fileTemplateMiniItem.getTemplateName());
				}

				ServiceFileTemplateLocalServiceUtil.updateServiceFileTemplate(serviceFileTemplateNew);
			}
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/upload/", method = RequestMethod.POST)
	public String uploadFile(MultipartHttpServletRequest request) {
		CommonsMultipartFile multipartFile = null; // multipart file class depends on which class you use assuming you
													// are using
													// org.springframework.web.multipart.commons.CommonsMultipartFile

		Iterator<String> iterator = request.getFileNames();

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			// create multipartFile array if you upload multiple files
			multipartFile = (CommonsMultipartFile) request.getFile(key);
		}

		try {
			System.out.println("LiferayRestController.uploadFile()" + multipartFile.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "sdfds";
	}

	@RequestMapping(value = "/jexcel/{bundleName}/{modelName}/{serviceName}/{idCol}/{textCol}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ResponseStatus(HttpStatus.OK)
	public String getJExcelAutoComplate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("bundleName") String bundleName, @PathVariable("modelName") String modelName,
			@PathVariable("serviceName") String serviceName, @PathVariable("idCol") String idCol,
			@PathVariable("textCol") String textCol) {

		JSONArray result = JSONFactoryUtil.createJSONArray();

		try {

			BundleLoader bundleLoader = new BundleLoader(bundleName);

			Class<?> model = bundleLoader.getClassLoader().loadClass(modelName);

			Method method = bundleLoader.getClassLoader().loadClass(serviceName).getMethod("dynamicQuery");

			DynamicQuery dynamicQuery = (DynamicQuery) method.invoke(model);

			ProjectionList projectionList = ProjectionFactoryUtil.projectionList();

			projectionList.add(ProjectionFactoryUtil.property(idCol));
			projectionList.add(ProjectionFactoryUtil.property(textCol));

			dynamicQuery.setProjection(projectionList);

			Disjunction disjunction = RestrictionsFactoryUtil.disjunction();
			disjunction.add(RestrictionsFactoryUtil.eq("groupId", 0l));
			if (Validator.isNotNull(request.getHeader("groupId"))) {
				disjunction.add(RestrictionsFactoryUtil.eq("groupId", Long.valueOf(request.getHeader("groupId"))));
			}
			dynamicQuery.add(disjunction);

			if (Validator.isNotNull(request.getParameter("pk")) && Validator.isNotNull(request.getParameter("col"))) {
				dynamicQuery.add(PropertyFactoryUtil.forName(request.getParameter("col"))
						.eq(Validator.isNumber(request.getParameter("pk")) ? Long.valueOf(request.getParameter("pk"))
								: request.getParameter("pk")));
			}
			if (Validator.isNotNull(request.getParameter("collectionCode")) && Validator.isNotNull(request.getParameter("column")) && Validator.isNotNull(request.getParameter("type"))) {
				
				if (request.getParameter("type").equals("int")) {
					DictCollection dictCollection = DictCollectionLocalServiceUtil.fetchByF_dictCollectionCode(request.getParameter("collectionCode"), Long.valueOf(request.getHeader("groupId")));
					
					if (Validator.isNotNull(dictCollection)) {
						dynamicQuery.add(PropertyFactoryUtil.forName(request.getParameter("column"))
								.eq(dictCollection.getDictCollectionId()));
					}
				} else {
					dynamicQuery.add(PropertyFactoryUtil.forName(request.getParameter("column"))
							.eq(request.getParameter("collectionCode")));
				}
			}

			method = bundleLoader.getClassLoader().loadClass(serviceName).getMethod("dynamicQuery", DynamicQuery.class,
					int.class, int.class);

			List<Object[]> list = (List<Object[]>) method.invoke(model, dynamicQuery, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS);

			JSONObject object = null;
			for (Object[] objects : list) {

				object = JSONFactoryUtil.createJSONObject();

				object.put("id", objects[0]);

				if (modelName.equals(EmployeeJobPos.class.getName())) {

					long jobPostId = (long) objects[1];

					JobPos jobPos = JobPosLocalServiceUtil.fetchJobPos(jobPostId);

					String name = Validator.isNotNull(jobPos) ? jobPos.getTitle() : StringPool.BLANK;

					object.put("name", name);

				} else {
					object.put("name", objects[1]);
				}

				result.put(object);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toJSONString();

	}
}
