package org.opencps.inland.rest.application;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Iterator;
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
import org.opencps.dossiermgt.model.Deliverable;
import org.opencps.dossiermgt.model.Dossier;
import org.opencps.dossiermgt.model.DossierFile;
import org.opencps.dossiermgt.service.DeliverableLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierFileLocalServiceUtil;
import org.opencps.dossiermgt.service.DossierLocalServiceUtil;
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

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
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
			@Context ServiceContext serviceContext, 
			@QueryParam("serviceCode") String serviceCode,
			@QueryParam("dossierPartNo") String dossierPartNo,
			@QueryParam("fileTemplateNo") String fileTemplateNo,
			@QueryParam("templateNo") String templateNo,
			@QueryParam("licenceType") String licenceType,
			@QueryParam("dossierFileId") String dossierFileId,
			@QueryParam("licenceNo") String licenceNo,
			@QueryParam("validUntil") String validUntil,
			@QueryParam("validFrom") String validFrom,
			@QueryParam("signDate") String signDate,
			@QueryParam("issuedDate") String issuedDate) {
		
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
			
			if(GetterUtil.getLong(dossierFileId) > 0) {
				DossierFile dossierFile = DossierFileLocalServiceUtil.fetchDossierFile(GetterUtil.getLong(dossierFileId));
				if(dossierFile != null) {
					
					String formData = dossierFile.getFormData();
					
					if(Validator.isNotNull(licenceNo) 
							|| Validator.isNotNull(validFrom)
							|| Validator.isNotNull(validUntil)
							|| Validator.isNotNull(signDate)) {
						
						JSONObject temp = JSONFactoryUtil.createJSONObject(dossierFile.getFormData());
						
						if(Validator.isNotNull(licenceNo) && temp.has("LicenceNo")) {
							temp.put("LicenceNo", licenceNo);
						}
						
						if(Validator.isNotNull(validFrom) && temp.has("ValidFrom")) {
							temp.put("ValidFrom", validFrom);
						}
						
						if(Validator.isNotNull(validUntil) && temp.has("ValidUntil")) {
							temp.put("ValidUntil", validUntil);
						}
						
						if(Validator.isNotNull(signDate) && temp.has("SignDate")) {
							temp.put("SignDate", signDate);
						}
						
						if(Validator.isNotNull(issuedDate) && temp.has("IssuedDate")) {
							temp.put("IssuedDate", issuedDate);
						}
						
						
						formData = temp.toString();
					}
					
					result.setFormData(formData);
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
	
	@GET
	@Path("/{dossierId}/prints")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML })
	public Response getPrintHtmlByDossier(@Context HttpServletRequest request, @Context HttpHeaders header,
			@Context Company company, @Context Locale locale, @Context User user,
			@Context ServiceContext serviceContext, 
			@PathParam("dossierId") String dossierId,
			@QueryParam("deliverables") String deliverables,
			@QueryParam("printType") String printType,
			@QueryParam("ph") String ph
			) {
		
		BackendAuth auth = new BackendAuthImpl();

		try {
			if (!auth.isAuth(serviceContext)) {
				throw new UnauthenticationException();
			}
			Dossier dossier = DossierLocalServiceUtil.fetchDossier(GetterUtil.getLong(dossierId));
			String html = "<html><body>";
			
			String[] deliverableArr = StringUtil.split(deliverables);
			for (String deliverableCode : deliverableArr) {
				Deliverable deliverable = DeliverableLocalServiceUtil.getByCode(deliverableCode);
				DossierFile dossierFile = DossierFileLocalServiceUtil.getByDeliverableCode(deliverableCode);
				
				if (deliverable != null) {
					JSONObject formDataObj = JSONFactoryUtil.createJSONObject(deliverable.getFormData());
					
					if(formDataObj != null) {
						String licenceType = StringPool.BLANK;
						if (formDataObj.has("LicenceType")) {
							licenceType = formDataObj.getString("LicenceType");
						}
						else {
							
						}
						InlandPrintTemplate printTemplate = null;
						try {
							if (printType == null || printType.equals("DELIVERABLE")) {
								if (Validator.isNull(licenceType)) {
									printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN(user.getUserId(), dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), dossierFile.getFileTemplateNo());					
								}
								else {
									printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(user.getUserId(), dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), dossierFile.getFileTemplateNo(), licenceType);					
								}
							}
							else {
								printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(user.getUserId(), dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), ph, licenceType);													
							}
						}
						catch (NoSuchInlandPrintTemplateException e) {
							
						}
						
						if(printTemplate == null) {
							try {
								if (printType == null || printType.equals("DELIVERABLE")) {
									if (Validator.isNull(licenceType)) {
										printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN(0, dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), dossierFile.getFileTemplateNo());					
									}
									else {
										printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(0, dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), dossierFile.getFileTemplateNo(), licenceType);					
									}
								}
								else {
									printTemplate = InlandPrintTemplateLocalServiceUtil.findBySC_TN_PN_FTN_LT(0, dossier.getServiceCode(), dossier.getDossierTemplateNo(), dossierFile.getDossierPartNo(), ph, licenceType);													
								}
							}
							catch (NoSuchInlandPrintTemplateException e) {
								
							}
						}
						
						if (printTemplate != null) {
							String styleCss = _buildStylePrintHTML(printTemplate.getOriginalDocumentURL());
							html += "<div style='" + styleCss + "'>";
							
							String formTemplate = printTemplate.getFormTemplate();
							_log.info("==getPrintHtmlByDossier==formTemplate==="+ printTemplate);
							JSONObject formTemplateObj = JSONFactoryUtil.createJSONObject(formTemplate);
							Iterator<String> keys = formTemplateObj.keys();
							while (keys.hasNext()) {
								String key = keys.next();
								String cssDiv = StringPool.BLANK;
								
								if (formDataObj.has("css")) {
									cssDiv = formDataObj.getString("css");
									cssDiv += ";";
								}
								
								if (formTemplateObj.getJSONObject(key).has("css")) {
									cssDiv += formTemplateObj.getJSONObject(key).getString("css");
									cssDiv += ";";
								}
								
								if(cssDiv.indexOf("font-size") < 0) {
									cssDiv += "font-size:14px;";
								}
								
								cssDiv += "position:absolute;";
								
								if (key.indexOf("array_") != -1) {
									String[] newKeyArr = key.split("_");
									int key1 = GetterUtil.getInteger(newKeyArr[1]);
									String newKey = newKeyArr[2];
									if (formDataObj.has(newKey)) {
										String[] itemDataArr = formDataObj.getString(newKey).split("/");
										if (itemDataArr.length > key1) {
											html += "<div class=\"" + key + "\" style=\"" + cssDiv + "left:";
											html += formTemplateObj.getJSONObject(key).getString("offsetX");
											html += "px;top:" + formTemplateObj.getJSONObject(key).getString("offsetY") + "px\">";
											html += itemDataArr[key1];
											html += "</div>";
										}
									}
								} else if (key.indexOf("_") != -1) {
									String[] newKeyArr = key.split("_");
									String newKey = newKeyArr[1];
									if (formDataObj.has(newKey)) {
										html += "<div class=\"" + key + "\" style=\"" + cssDiv + "left:";
										html += formTemplateObj.getJSONObject(key).getString("offsetX");
										html += "px;top:" + formTemplateObj.getJSONObject(key).getString("offsetY") + "px\">";
										html += formDataObj.getString(newKey);
										html += "</div>";
									}
								} else {
									if (key.toLowerCase().indexOf("text") > -1 && formTemplateObj.getJSONObject(key).has("value")) {
										html += "<div class=\"" + key + "\" style=\"" + cssDiv + "left:";
										html += formTemplateObj.getJSONObject(key).getString("offsetX");
										html += "px;top:" + formTemplateObj.getJSONObject(key).getString("offsetY") + "px\">";
										html += formTemplateObj.getJSONObject(key).getString("value");
										html += "</div>";
									} else if (formDataObj.has(key)) {
										html += "<div class=\"" + key + "\" style=\"" + cssDiv + "left:";
										html += formTemplateObj.getJSONObject(key).getString("offsetX");
										html += "px;top:" + formTemplateObj.getJSONObject(key).getString("offsetY") + "px\">";
										html += formDataObj.getString(key);
										html += "</div>";
									} else {
										_log.info("===getPrintHtmlByDossier===key===" + key);
									}
								}
							}
							
							html += "</div>";
						} else {
							_log.info("printTemplate is null");
						}
					} else {
						_log.info("formData is null");
					}
				}
			}
			html += "</body></html>";
			
			return Response.status(200).entity(html).build();
		} catch (Exception e) {
			_log.error(e);
			ErrorMsg error = new ErrorMsg();

			error.setMessage("Content not found!");
			error.setCode(404);
			error.setDescription(e.getMessage());

			return Response.status(404).entity(error).build();
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
	
	private String _buildStylePrintHTML(String originalDocumentURL) {
		
		String style = "";
		
		try {
			JSONArray jsonArray = JSONFactoryUtil.createJSONArray(originalDocumentURL);
			
			JSONObject json = jsonArray.getJSONObject(0);
			
			int height = 0;
			int width = 0;
			
			if(json.has("width") && json.has("height")) {
				width = GetterUtil.getInteger(json.get("width"));
				height = GetterUtil.getInteger(json.get("height"));
			}
				
			style = "width: " + width +"px; height: " + height +"px;position: relative;";
		} catch(Exception e) {
			_log.error(e);
		}
		
		return style;
	}
	
}
