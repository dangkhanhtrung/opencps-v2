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

package org.opencps.dossiermgt.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.service.ServiceContext;

import org.opencps.dossiermgt.service.persistence.DossierUserPK;

import java.io.Serializable;

/**
 * The base model interface for the DossierUser service. Represents a row in the &quot;opencps_dossieruser&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link org.opencps.dossiermgt.model.impl.DossierUserModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link org.opencps.dossiermgt.model.impl.DossierUserImpl}.
 * </p>
 *
 * @author huymq
 * @see DossierUser
 * @see org.opencps.dossiermgt.model.impl.DossierUserImpl
 * @see org.opencps.dossiermgt.model.impl.DossierUserModelImpl
 * @generated
 */
@ProviderType
public interface DossierUserModel extends BaseModel<DossierUser> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a dossier user model instance should use the {@link DossierUser} interface instead.
	 */

	/**
	 * Returns the primary key of this dossier user.
	 *
	 * @return the primary key of this dossier user
	 */
	public DossierUserPK getPrimaryKey();

	/**
	 * Sets the primary key of this dossier user.
	 *
	 * @param primaryKey the primary key of this dossier user
	 */
	public void setPrimaryKey(DossierUserPK primaryKey);

	/**
	 * Returns the uuid of this dossier user.
	 *
	 * @return the uuid of this dossier user
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this dossier user.
	 *
	 * @param uuid the uuid of this dossier user
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the dossier ID of this dossier user.
	 *
	 * @return the dossier ID of this dossier user
	 */
	public long getDossierId();

	/**
	 * Sets the dossier ID of this dossier user.
	 *
	 * @param dossierId the dossier ID of this dossier user
	 */
	public void setDossierId(long dossierId);

	/**
	 * Returns the user ID of this dossier user.
	 *
	 * @return the user ID of this dossier user
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this dossier user.
	 *
	 * @param userId the user ID of this dossier user
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this dossier user.
	 *
	 * @return the user uuid of this dossier user
	 */
	public String getUserUuid();

	/**
	 * Sets the user uuid of this dossier user.
	 *
	 * @param userUuid the user uuid of this dossier user
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the moderator of this dossier user.
	 *
	 * @return the moderator of this dossier user
	 */
	public int getModerator();

	/**
	 * Sets the moderator of this dossier user.
	 *
	 * @param moderator the moderator of this dossier user
	 */
	public void setModerator(int moderator);

	/**
	 * Returns the visited of this dossier user.
	 *
	 * @return the visited of this dossier user
	 */
	public boolean getVisited();

	/**
	 * Returns <code>true</code> if this dossier user is visited.
	 *
	 * @return <code>true</code> if this dossier user is visited; <code>false</code> otherwise
	 */
	public boolean isVisited();

	/**
	 * Sets whether this dossier user is visited.
	 *
	 * @param visited the visited of this dossier user
	 */
	public void setVisited(boolean visited);

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(DossierUser dossierUser);

	@Override
	public int hashCode();

	@Override
	public CacheModel<DossierUser> toCacheModel();

	@Override
	public DossierUser toEscapedModel();

	@Override
	public DossierUser toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}