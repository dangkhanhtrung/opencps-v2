//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.08.02 at 09:56:50 AM ICT 
//


package org.opencps.api.v21.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="ActionConfig" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="actionCode">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="actionName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="extraForm" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="formConfig">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="1000"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="sampleData">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="insideProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="userNote" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;element name="syncType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;element name="eventType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;element name="infoType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                   &lt;element name="rollbackable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="notificationType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="documentType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="mappingAction">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "actionConfig"
})
@XmlRootElement(name = "ActionConfigList")
public class ActionConfigList {

    @XmlElement(name = "ActionConfig", required = true)
    protected List<ActionConfigList.ActionConfig> actionConfig;

    /**
     * Gets the value of the actionConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActionConfigList.ActionConfig }
     * 
     * 
     */
    public List<ActionConfigList.ActionConfig> getActionConfig() {
        if (actionConfig == null) {
            actionConfig = new ArrayList<ActionConfigList.ActionConfig>();
        }
        return this.actionConfig;
    }


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
     *         &lt;element name="actionCode">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="actionName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="extraForm" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="formConfig">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="1000"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="sampleData">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="insideProcess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="userNote" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="syncType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="eventType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="infoType" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
     *         &lt;element name="rollbackable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="notificationType">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="documentType">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="mappingAction">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
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
        "actionCode",
        "actionName",
        "extraForm",
        "formConfig",
        "sampleData",
        "insideProcess",
        "userNote",
        "syncType",
        "eventType",
        "infoType",
        "rollbackable",
        "notificationType",
        "documentType",
        "mappingAction"
    })
    public static class ActionConfig {

        @XmlElement(required = true)
        protected String actionCode;
        @XmlElement(required = true)
        protected String actionName;
        protected Boolean extraForm;
        @XmlElement(required = true)
        protected String formConfig;
        @XmlElement(required = true)
        protected String sampleData;
        protected Boolean insideProcess;
        protected Integer userNote;
        protected Integer syncType;
        protected Integer eventType;
        protected Integer infoType;
        protected Boolean rollbackable;
        @XmlElement(required = true)
        protected String notificationType;
        @XmlElement(required = true)
        protected String documentType;
        @XmlElement(required = true)
        protected String mappingAction;

        /**
         * Gets the value of the actionCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionCode() {
            return actionCode;
        }

        /**
         * Sets the value of the actionCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionCode(String value) {
            this.actionCode = value;
        }

        /**
         * Gets the value of the actionName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionName() {
            return actionName;
        }

        /**
         * Sets the value of the actionName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionName(String value) {
            this.actionName = value;
        }

        /**
         * Gets the value of the extraForm property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isExtraForm() {
            return extraForm;
        }

        /**
         * Sets the value of the extraForm property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setExtraForm(Boolean value) {
            this.extraForm = value;
        }

        /**
         * Gets the value of the formConfig property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFormConfig() {
            return formConfig;
        }

        /**
         * Sets the value of the formConfig property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFormConfig(String value) {
            this.formConfig = value;
        }

        /**
         * Gets the value of the sampleData property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSampleData() {
            return sampleData;
        }

        /**
         * Sets the value of the sampleData property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSampleData(String value) {
            this.sampleData = value;
        }

        /**
         * Gets the value of the insideProcess property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isInsideProcess() {
            return insideProcess;
        }

        /**
         * Sets the value of the insideProcess property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setInsideProcess(Boolean value) {
            this.insideProcess = value;
        }

        /**
         * Gets the value of the userNote property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getUserNote() {
            return userNote;
        }

        /**
         * Sets the value of the userNote property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setUserNote(Integer value) {
            this.userNote = value;
        }

        /**
         * Gets the value of the syncType property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getSyncType() {
            return syncType;
        }

        /**
         * Sets the value of the syncType property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setSyncType(Integer value) {
            this.syncType = value;
        }

        /**
         * Gets the value of the eventType property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getEventType() {
            return eventType;
        }

        /**
         * Sets the value of the eventType property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setEventType(Integer value) {
            this.eventType = value;
        }

        /**
         * Gets the value of the infoType property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getInfoType() {
            return infoType;
        }

        /**
         * Sets the value of the infoType property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setInfoType(Integer value) {
            this.infoType = value;
        }

        /**
         * Gets the value of the rollbackable property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isRollbackable() {
            return rollbackable;
        }

        /**
         * Sets the value of the rollbackable property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setRollbackable(Boolean value) {
            this.rollbackable = value;
        }

        /**
         * Gets the value of the notificationType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNotificationType() {
            return notificationType;
        }

        /**
         * Sets the value of the notificationType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNotificationType(String value) {
            this.notificationType = value;
        }

        /**
         * Gets the value of the documentType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDocumentType() {
            return documentType;
        }

        /**
         * Sets the value of the documentType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDocumentType(String value) {
            this.documentType = value;
        }

        /**
         * Gets the value of the mappingAction property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMappingAction() {
            return mappingAction;
        }

        /**
         * Sets the value of the mappingAction property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMappingAction(String value) {
            this.mappingAction = value;
        }

    }

}
