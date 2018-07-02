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

package org.opencps.dossiermgt.service.impl;

import java.util.Date;
import java.util.List;

import org.opencps.dossiermgt.model.DossierSync;
import org.opencps.dossiermgt.service.base.DossierSyncLocalServiceBaseImpl;

/**
 * The implementation of the dossier sync local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.opencps.dossiermgt.service.DossierSyncLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author huymq
 * @see DossierSyncLocalServiceBaseImpl
 * @see org.opencps.dossiermgt.service.DossierSyncLocalServiceUtil
 */
public class DossierSyncLocalServiceImpl extends DossierSyncLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link org.opencps.dossiermgt.service.DossierSyncLocalServiceUtil} to access the dossier sync local service.
	 */

	public List<DossierSync> getDossierSyncList(String actionCode, int syncType, Integer start, Integer limit) {
		return dossierSyncFinder.findDossierSyncByActionOrTop(actionCode, syncType, start, limit);
	}

	public long countDossierSyncList(String actionCode, int syncType) {
		return dossierSyncFinder.countDossierSyncByActionOrTop(actionCode, syncType);
	}

	public List<DossierSync> getDossierSyncByIdList(Long dossierId, Integer model, int actionCodeNo, Integer start, Integer limit) {
		return dossierSyncFinder.findDossierSyncByIdList(dossierId, model, actionCodeNo, start, limit);
	}

	public long countDossierSyncByIdList(Long dossierId, Integer model, int actionCodeNo) {
		return dossierSyncFinder.countDossierSyncByIdList(dossierId, model, actionCodeNo);
	}

	public DossierSync updateDossierSync(long groupId, long userId, long dossierId, String dossierReferenceUid, String syncRefUid,
			long dossierActionId, String actionCode, String actionName, String  actionUser, String  actionNote,
			int syncType, String  payload, String serverNo, int state) {

		DossierSync dossierSync = null;

//		dossierSync = dossierSyncPersistence.fetchByF_D_M_CPK_FR(dossierId, method, classPK, fileReferenceUid);
//
//		if (Validator.isNull(dossierSync)) {
			long dossierSyncId = counterLocalService.increment(DossierSync.class.getName());

			Date now = new Date();

			dossierSync = dossierSyncPersistence.create(dossierSyncId);

			dossierSync.setCreateDate(now);
			dossierSync.setModifiedDate(now);
			dossierSync.setGroupId(groupId);
			dossierSync.setUserId(userId);

			dossierSync.setDossierId(dossierId);
			dossierSync.setDossierRefUid(dossierReferenceUid);
			dossierSync.setSyncRefUid(syncRefUid);
			dossierSync.setDossierActionId(dossierActionId);
			dossierSync.setActionCode(actionCode);
			dossierSync.setActionName(actionName);
			dossierSync.setActionUser(actionUser);
			dossierSync.setActionNote(actionNote);
			dossierSync.setSyncType(syncType);
			dossierSync.setPayload(payload);
			dossierSync.setServerNo(serverNo);
			dossierSync.setState(state);

			dossierSyncPersistence.update(dossierSync);

//		}

		return dossierSync;
	}
}