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

package org.opencps.sms.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedAuditedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the SMSLookUpQueue service. Represents a row in the &quot;opencps_smsqueue&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link org.opencps.sms.model.impl.SMSLookUpQueueModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link org.opencps.sms.model.impl.SMSLookUpQueueImpl}.
 * </p>
 *
 * @author khoa
 * @see SMSLookUpQueue
 * @see org.opencps.sms.model.impl.SMSLookUpQueueImpl
 * @see org.opencps.sms.model.impl.SMSLookUpQueueModelImpl
 * @generated
 */
@ProviderType
public interface SMSLookUpQueueModel extends BaseModel<SMSLookUpQueue>,
	GroupedModel, ShardedModel, StagedAuditedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a sms look up queue model instance should use the {@link SMSLookUpQueue} interface instead.
	 */

	/**
	 * Returns the primary key of this sms look up queue.
	 *
	 * @return the primary key of this sms look up queue
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this sms look up queue.
	 *
	 * @param primaryKey the primary key of this sms look up queue
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this sms look up queue.
	 *
	 * @return the uuid of this sms look up queue
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this sms look up queue.
	 *
	 * @param uuid the uuid of this sms look up queue
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the queue ID of this sms look up queue.
	 *
	 * @return the queue ID of this sms look up queue
	 */
	public long getQueueId();

	/**
	 * Sets the queue ID of this sms look up queue.
	 *
	 * @param queueId the queue ID of this sms look up queue
	 */
	public void setQueueId(long queueId);

	/**
	 * Returns the group ID of this sms look up queue.
	 *
	 * @return the group ID of this sms look up queue
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this sms look up queue.
	 *
	 * @param groupId the group ID of this sms look up queue
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this sms look up queue.
	 *
	 * @return the company ID of this sms look up queue
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this sms look up queue.
	 *
	 * @param companyId the company ID of this sms look up queue
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this sms look up queue.
	 *
	 * @return the user ID of this sms look up queue
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this sms look up queue.
	 *
	 * @param userId the user ID of this sms look up queue
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this sms look up queue.
	 *
	 * @return the user uuid of this sms look up queue
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this sms look up queue.
	 *
	 * @param userUuid the user uuid of this sms look up queue
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the create date of this sms look up queue.
	 *
	 * @return the create date of this sms look up queue
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this sms look up queue.
	 *
	 * @param createDate the create date of this sms look up queue
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this sms look up queue.
	 *
	 * @return the modified date of this sms look up queue
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this sms look up queue.
	 *
	 * @param modifiedDate the modified date of this sms look up queue
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the moid of this sms look up queue.
	 *
	 * @return the moid of this sms look up queue
	 */
	@AutoEscape
	public String getMoid();

	/**
	 * Sets the moid of this sms look up queue.
	 *
	 * @param moid the moid of this sms look up queue
	 */
	public void setMoid(String moid);

	/**
	 * Returns the src of this sms look up queue.
	 *
	 * @return the src of this sms look up queue
	 */
	@AutoEscape
	public String getSrc();

	/**
	 * Sets the src of this sms look up queue.
	 *
	 * @param src the src of this sms look up queue
	 */
	public void setSrc(String src);

	/**
	 * Returns the dest of this sms look up queue.
	 *
	 * @return the dest of this sms look up queue
	 */
	@AutoEscape
	public String getDest();

	/**
	 * Sets the dest of this sms look up queue.
	 *
	 * @param dest the dest of this sms look up queue
	 */
	public void setDest(String dest);

	/**
	 * Returns the moseq of this sms look up queue.
	 *
	 * @return the moseq of this sms look up queue
	 */
	@AutoEscape
	public String getMoseq();

	/**
	 * Sets the moseq of this sms look up queue.
	 *
	 * @param moseq the moseq of this sms look up queue
	 */
	public void setMoseq(String moseq);

	/**
	 * Returns the cmdcode of this sms look up queue.
	 *
	 * @return the cmdcode of this sms look up queue
	 */
	@AutoEscape
	public String getCmdcode();

	/**
	 * Sets the cmdcode of this sms look up queue.
	 *
	 * @param cmdcode the cmdcode of this sms look up queue
	 */
	public void setCmdcode(String cmdcode);

	/**
	 * Returns the msgbody of this sms look up queue.
	 *
	 * @return the msgbody of this sms look up queue
	 */
	@AutoEscape
	public String getMsgbody();

	/**
	 * Sets the msgbody of this sms look up queue.
	 *
	 * @param msgbody the msgbody of this sms look up queue
	 */
	public void setMsgbody(String msgbody);

	/**
	 * Returns the password of this sms look up queue.
	 *
	 * @return the password of this sms look up queue
	 */
	@AutoEscape
	public String getPassword();

	/**
	 * Sets the password of this sms look up queue.
	 *
	 * @param password the password of this sms look up queue
	 */
	public void setPassword(String password);

	/**
	 * Returns the status of this sms look up queue.
	 *
	 * @return the status of this sms look up queue
	 */
	public int getStatus();

	/**
	 * Sets the status of this sms look up queue.
	 *
	 * @param status the status of this sms look up queue
	 */
	public void setStatus(int status);

	/**
	 * Returns the received date of this sms look up queue.
	 *
	 * @return the received date of this sms look up queue
	 */
	public Date getReceivedDate();

	/**
	 * Sets the received date of this sms look up queue.
	 *
	 * @param receivedDate the received date of this sms look up queue
	 */
	public void setReceivedDate(Date receivedDate);

	/**
	 * Returns the user name of this sms look up queue.
	 *
	 * @return the user name of this sms look up queue
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this sms look up queue.
	 *
	 * @param userName the user name of this sms look up queue
	 */
	@Override
	public void setUserName(String userName);

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
	public int compareTo(SMSLookUpQueue smsLookUpQueue);

	@Override
	public int hashCode();

	@Override
	public CacheModel<SMSLookUpQueue> toCacheModel();

	@Override
	public SMSLookUpQueue toEscapedModel();

	@Override
	public SMSLookUpQueue toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();
}