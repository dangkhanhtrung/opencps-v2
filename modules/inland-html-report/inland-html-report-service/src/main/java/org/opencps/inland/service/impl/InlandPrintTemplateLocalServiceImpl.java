/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.opencps.inland.service.impl;

import aQute.bnd.annotation.ProviderType;

import java.util.Date;

import org.opencps.inland.exception.NoSuchInlandPrintTemplateException;
import org.opencps.inland.model.InlandPrintTemplate;
import org.opencps.inland.service.base.InlandPrintTemplateLocalServiceBaseImpl;

/**
 * The implementation of the inland print template local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.inland.service.InlandPrintTemplateLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see InlandPrintTemplateLocalServiceBaseImpl
 * @see org.opencps.inland.service.InlandPrintTemplateLocalServiceUtil
 */
@ProviderType
public class InlandPrintTemplateLocalServiceImpl
	extends InlandPrintTemplateLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link org.opencps.inland.service.InlandPrintTemplateLocalServiceUtil} to access the inland print template local service.
	 */
	
	public InlandPrintTemplate addInlandPrintTemplate(
			long groupId, 
			long userId, 
			long createUserId,
			long employeeId,
			String serviceCode,
			String dossierPartNo,
			String fileTemplateNo,
			String templateNo,
			String formTemplate,
			String defaultCss,
			String originalDocumentURL,
			String licenceType
			) {
		long printTemplateId = counterLocalService.increment(InlandPrintTemplate.class.getName());
		Date now = new Date();
		InlandPrintTemplate inlandPrintTemplate = inlandPrintTemplatePersistence.create(printTemplateId);
		inlandPrintTemplate.setCreateDate(now);
		inlandPrintTemplate.setModifiedDate(now);
		
		inlandPrintTemplate.setGroupId(groupId);
		inlandPrintTemplate.setUserId(userId);
		inlandPrintTemplate.setCreateUserId(createUserId);
		inlandPrintTemplate.setServiceCode(serviceCode);
		inlandPrintTemplate.setTemplateNo(templateNo);
		inlandPrintTemplate.setDossierPartNo(dossierPartNo);
		inlandPrintTemplate.setFileTemplateNo(fileTemplateNo);
		inlandPrintTemplate.setFormTemplate(formTemplate);
		inlandPrintTemplate.setDefaultCss(defaultCss);
		inlandPrintTemplate.setOriginalDocumentURL(originalDocumentURL);
		inlandPrintTemplate.setLicenceType(licenceType);
		
		return inlandPrintTemplatePersistence.update(inlandPrintTemplate);
	}
	
	public InlandPrintTemplate updateInlandPrintTemplate(
			long groupId, 
			long userId, 
			long printTemplateId,
			long createUserId,
			long employeeId,
			String serviceCode,
			String dossierPartNo,
			String fileTemplateNo,
			String templateNo,
			String formTemplate,
			String defaultCss,
			String originalDocumentURL,
			String licenceType
			) throws NoSuchInlandPrintTemplateException {
		Date now = new Date();
		InlandPrintTemplate inlandPrintTemplate = inlandPrintTemplatePersistence.findByPrimaryKey(printTemplateId);
		
		inlandPrintTemplate.setModifiedDate(now);
		
		inlandPrintTemplate.setGroupId(groupId);
		inlandPrintTemplate.setUserId(userId);
		inlandPrintTemplate.setCreateUserId(createUserId);
		inlandPrintTemplate.setServiceCode(serviceCode);
		inlandPrintTemplate.setTemplateNo(templateNo);
		inlandPrintTemplate.setDossierPartNo(dossierPartNo);
		inlandPrintTemplate.setFileTemplateNo(fileTemplateNo);
		inlandPrintTemplate.setFormTemplate(formTemplate);
		inlandPrintTemplate.setDefaultCss(defaultCss);
		inlandPrintTemplate.setOriginalDocumentURL(originalDocumentURL);
		inlandPrintTemplate.setLicenceType(licenceType);
		
		return inlandPrintTemplatePersistence.update(inlandPrintTemplate);
	}	
	
	public InlandPrintTemplate deleteInlandPrintTemplate(long printTemplateId) throws NoSuchInlandPrintTemplateException {
		return inlandPrintTemplatePersistence.remove(printTemplateId);
	}
	
	public InlandPrintTemplate findBySC_TN_PN_FTN(long createUserId, String serviceCode, String templateNo, String dossierPartNo, String fileTemplateNo) throws NoSuchInlandPrintTemplateException {
		return inlandPrintTemplatePersistence.findBySC_TN_PN_FTN(createUserId, serviceCode, templateNo, dossierPartNo, fileTemplateNo);
	}

	public InlandPrintTemplate findBySC_TN_PN_FTN_LT(long createUserId, String serviceCode, String templateNo, String dossierPartNo, String fileTemplateNo, String licenceType) throws NoSuchInlandPrintTemplateException {
		return inlandPrintTemplatePersistence.findBySC_TN_PN_FTN_LT(createUserId, serviceCode, templateNo, dossierPartNo, fileTemplateNo, licenceType);
	}
}