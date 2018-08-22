package org.opencps.inland.rest.application;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
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

import org.opencps.auth.api.BackendAuth;
import org.opencps.auth.api.BackendAuthImpl;
import org.opencps.auth.api.exception.UnauthenticationException;
import org.opencps.auth.api.exception.UnauthorizationException;
import org.opencps.inland.api.inlandprinttemplate.model.ErrorMsg;
import org.opencps.inland.api.inlandprinttemplate.model.InlandPrintTemplateInputModel;
import org.opencps.inland.api.inlandprinttemplate.model.InlandPrintTemplateModel;
import org.opencps.inland.context.provider.CompanyContextProvider;
import org.opencps.inland.context.provider.LocaleContextProvider;
import org.opencps.inland.context.provider.ServiceContextProvider;
import org.opencps.inland.context.provider.UserContextProvider;
import org.opencps.inland.exception.NoSuchInlandPrintTemplateException;
import org.opencps.inland.model.InlandPrintTemplate;
import org.opencps.inland.service.InlandPrintTemplateLocalServiceUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

@ApplicationPath("/v2/inland")
@Component(immediate = true, service = Application.class)
public class BackendInlandApiRestApplication extends Application {
	private static final Log _log = LogFactoryUtil.getLog(BackendInlandApiRestApplication.class.getName());

	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(this);
		
		singletons.add(_serviceContextProvider);
		singletons.add(_companyContextProvider);
		singletons.add(_localeContextProvider);
		singletons.add(_userContextProvider);
		
		return singletons;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getInlandPrintTemplateDetail(@Context HttpServletRequest request, @Context HttpHeaders header,
			@Context Company company, @Context Locale locale, @Context User user,
			@Context ServiceContext serviceContext, @QueryParam("serviceCode") String serviceCode,
			@QueryParam("dossierPartNo") String dossierPartNo,
			@QueryParam("fileTemplateNo") String fileTemplateNo,
			@QueryParam("templateNo") String templateNo,
			@QueryParam("licenceType") String licenceType) {
		
		BackendAuth auth = new BackendAuthImpl();

		try {
			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}
			
			_log.info("Get print template: " + user.getUserId() + "," + serviceCode + "," + templateNo + "," + dossierPartNo + "," + fileTemplateNo);
			
			InlandPrintTemplate printTemplate = null;
			try {
				if (Validator.isNull(licenceType)) {
					printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN(user.getUserId(), serviceCode, templateNo, dossierPartNo, fileTemplateNo);					
				}
				else {
					printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(user.getUserId(), serviceCode, templateNo, dossierPartNo, fileTemplateNo, licenceType);					
				}
			}
			catch (NoSuchInlandPrintTemplateException e) {
				
			}
			InlandPrintTemplateModel result = new InlandPrintTemplateModel();
			if (printTemplate != null) {
				result.setPrintTemplateId(printTemplate.getPrintTemplateId());
				result.setServiceCode(printTemplate.getServiceCode());
				result.setCreateDate("");
				result.setCreateUserId(printTemplate.getCreateUserId());
				result.setDossierPartNo(dossierPartNo);
				result.setFileTemplateNo(fileTemplateNo);
				result.setTemplateNo(templateNo);
				result.setFormTemplate(printTemplate.getFormTemplate());
				result.setDefaultCss(printTemplate.getDefaultCss());
				result.setOriginalDocumentURL(printTemplate.getOriginalDocumentURL());
				result.setLicenceType(printTemplate.getLicenceType());
			}
			else {
				try {
					if (Validator.isNull(licenceType)) {
						printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN(0, serviceCode, templateNo, dossierPartNo, fileTemplateNo);					
				
					}
					else {
						printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(0, serviceCode, templateNo, dossierPartNo, fileTemplateNo, licenceType);											
					}
				}
				catch (NoSuchInlandPrintTemplateException e) {
					
				}
				if (printTemplate != null) {
					result.setPrintTemplateId(printTemplate.getPrintTemplateId());
					result.setServiceCode(printTemplate.getServiceCode());
					result.setCreateDate("");
					result.setCreateUserId(printTemplate.getCreateUserId());
					result.setDossierPartNo(dossierPartNo);
					result.setFileTemplateNo(fileTemplateNo);
					result.setTemplateNo(templateNo);
					result.setFormTemplate(printTemplate.getFormTemplate());
					result.setDefaultCss(printTemplate.getDefaultCss());
					result.setOriginalDocumentURL(printTemplate.getOriginalDocumentURL());
					result.setLicenceType(printTemplate.getLicenceType());
				}				
			}
			
			return Response.status(200).entity(result).build();
		} catch (Exception e) {
			ErrorMsg error = new ErrorMsg();

			error.setMessage("Content not found!");
			error.setCode(404);
			error.setDescription(e.getMessage());

			return Response.status(404).entity(error).build();
		}
		
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response addInlandPrintTemplate(@Context HttpServletRequest request, @Context HttpHeaders header,
			@Context Company company, @Context Locale locale, @Context User user,
			@Context ServiceContext serviceContext, @BeanParam InlandPrintTemplateInputModel input) {
		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		BackendAuth auth = new BackendAuthImpl();

		try {
			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}
			InlandPrintTemplate inlandPrintTemplate = InlandPrintTemplateLocalServiceUtil.addInlandPrintTemplate(groupId, user.getUserId(), user.getUserId(), 0, input.getServiceCode(), input.getDossierPartNo(), input.getFileTemplateNo(), input.getTemplateNo(), input.getFormTemplate(), input.getDefaultCss(), input.getOriginalDocumentURL(), input.getLicenceType());
			

			InlandPrintTemplateModel result = new InlandPrintTemplateModel();

			if (inlandPrintTemplate != null) {
				result.setPrintTemplateId(inlandPrintTemplate.getPrintTemplateId());
				result.setFormTemplate(inlandPrintTemplate.getFormTemplate());
				result.setDefaultCss(inlandPrintTemplate.getDefaultCss());
				result.setOriginalDocumentURL(inlandPrintTemplate.getOriginalDocumentURL());
				result.setDossierPartNo(inlandPrintTemplate.getDossierPartNo());
				result.setEmployeeId(inlandPrintTemplate.getEmployeeId());
				result.setFileTemplateNo(inlandPrintTemplate.getFileTemplateNo());
				result.setServiceCode(inlandPrintTemplate.getServiceCode());
				result.setTemplateNo(inlandPrintTemplate.getTemplateNo());
				result.setLicenceType(inlandPrintTemplate.getLicenceType());
				result.setCreateUserId(user.getUserId());
			}
			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}
		
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateInlandPrintTemplate(@Context HttpServletRequest request, @Context HttpHeaders header,
			@Context Company company, @Context Locale locale, @Context User user,
			@Context ServiceContext serviceContext, @PathParam("id") long id, @BeanParam InlandPrintTemplateInputModel input) {
		long groupId = GetterUtil.getLong(header.getHeaderString("groupId"));

		BackendAuth auth = new BackendAuthImpl();

		try {
			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}

			InlandPrintTemplate inlandPrintTemplate = InlandPrintTemplateLocalServiceUtil.updateInlandPrintTemplate(
					groupId, 
					user.getUserId(), 
					input.getPrintTemplateId(), 
					user.getUserId(), 
					0L, 
					input.getServiceCode(), 
					input.getDossierPartNo(), 
					input.getFileTemplateNo(), 
					input.getTemplateNo(), 
					input.getFormTemplate(), 
					input.getDefaultCss(),
					input.getOriginalDocumentURL(),
					input.getLicenceType());
			
			InlandPrintTemplateModel result = new InlandPrintTemplateModel();

			if (inlandPrintTemplate != null) {
				result.setPrintTemplateId(inlandPrintTemplate.getPrintTemplateId());
				result.setFormTemplate(inlandPrintTemplate.getFormTemplate());
				result.setDefaultCss(inlandPrintTemplate.getDefaultCss());
				result.setOriginalDocumentURL(inlandPrintTemplate.getOriginalDocumentURL());
				result.setDossierPartNo(inlandPrintTemplate.getDossierPartNo());
				result.setEmployeeId(inlandPrintTemplate.getEmployeeId());
				result.setFileTemplateNo(inlandPrintTemplate.getFileTemplateNo());
				result.setServiceCode(inlandPrintTemplate.getServiceCode());
				result.setTemplateNo(inlandPrintTemplate.getTemplateNo());
				result.setLicenceType(inlandPrintTemplate.getLicenceType());
			}
			return Response.status(200).entity(result).build();

		} catch (Exception e) {
			ErrorMsg error = new ErrorMsg();

			if (e instanceof UnauthenticationException) {
				error.setMessage("Non-Authoritative Information.");
				error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
				error.setDescription("Non-Authoritative Information.");

				return Response.status(HttpURLConnection.HTTP_NOT_AUTHORITATIVE).entity(error).build();
			} else {
				if (e instanceof UnauthorizationException) {
					error.setMessage("Unauthorized.");
					error.setCode(HttpURLConnection.HTTP_NOT_AUTHORITATIVE);
					error.setDescription("Unauthorized.");

					return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build();

				} else {

					error.setMessage("Internal Server Error");
					error.setCode(HttpURLConnection.HTTP_FORBIDDEN);
					error.setDescription(e.getMessage());

					return Response.status(HttpURLConnection.HTTP_INTERNAL_ERROR).entity(error).build();

				}
			}
		}
		
	}
	
	@Reference
	private CompanyContextProvider _companyContextProvider;

	@Reference
	private LocaleContextProvider _localeContextProvider;

	@Reference
	private UserContextProvider _userContextProvider;

	@Reference
	private ServiceContextProvider _serviceContextProvider;
	
}
