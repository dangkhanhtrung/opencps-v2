//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.07 at 09:50:54 AM ICT 
//


package org.opencps.api.workingunit.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.liferay.portal.kernel.util.StringPool;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="enName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="govAgencyCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parentWorkingUnitId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="sibling" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="faxNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="website" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "enName",
    "govAgencyCode",
    "parentWorkingUnitId",
    "sibling",
    "address",
    "telNo",
    "faxNo",
    "email",
    "website",
    "ceremonyDate"
})
@XmlRootElement(name = "WorkingUnitInputModel")
public class WorkingUnitInputModel {

	@DefaultValue(StringPool.BLANK) @FormParam(value = "name")
    protected String name;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "enName")
    protected String enName;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "govAgencyCode")
    protected String govAgencyCode;
	@DefaultValue("0") @FormParam(value = "parentWorkingUnitId")
    protected long parentWorkingUnitId;
	@DefaultValue("0") @FormParam(value = "sibling")
    protected int sibling;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "address")
    protected String address;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "telNo")
    protected String telNo;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "faxNo")
    protected String faxNo;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "email")
    protected String email;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "website")
    protected String website;
	@DefaultValue(StringPool.BLANK) @FormParam(value = "ceremonyDate")
	protected String ceremonyDate;
    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the enName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnName() {
        return enName;
    }

    /**
     * Sets the value of the enName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnName(String value) {
        this.enName = value;
    }

    /**
     * Gets the value of the govAgencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGovAgencyCode() {
        return govAgencyCode;
    }

    /**
     * Sets the value of the govAgencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGovAgencyCode(String value) {
        this.govAgencyCode = value;
    }

    /**
     * Gets the value of the parentWorkingUnitId property.
     * 
     */
    public long getParentWorkingUnitId() {
        return parentWorkingUnitId;
    }

    /**
     * Sets the value of the parentWorkingUnitId property.
     * 
     */
    public void setParentWorkingUnitId(long value) {
        this.parentWorkingUnitId = value;
    }

    /**
     * Gets the value of the sibling property.
     * 
     */
    public int getSibling() {
        return sibling;
    }

    /**
     * Sets the value of the sibling property.
     * 
     */
    public void setSibling(int value) {
        this.sibling = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the telNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * Sets the value of the telNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelNo(String value) {
        this.telNo = value;
    }

    /**
     * Gets the value of the faxNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNo() {
        return faxNo;
    }

    /**
     * Sets the value of the faxNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNo(String value) {
        this.faxNo = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the website property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the value of the website property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsite(String value) {
        this.website = value;
    }

	public String getCeremonyDate() {
		return ceremonyDate;
	}

	public void setCeremonyDate(String ceremonyDate) {
		this.ceremonyDate = ceremonyDate;
	}

}
