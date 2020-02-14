//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.02.14 at 10:07:53 AM ICT 
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
 *         &lt;element name="ProcessStep" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="stepCode">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="stepName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="sequenceNo">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="groupName">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="dossierStatus">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="dossierSubStatus">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="durationCount">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="instructionNote">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="briefNote">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="roleAsStep">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;maxLength value="255"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="checkInput" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="roles">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="StepRole" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="roleCode">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;maxLength value="255"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="roleName">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;maxLength value="255"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                       &lt;element name="moderator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                       &lt;element name="condition">
 *                                         &lt;simpleType>
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             &lt;maxLength value="255"/>
 *                                           &lt;/restriction>
 *                                         &lt;/simpleType>
 *                                       &lt;/element>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
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
    "processStep"
})
@XmlRootElement(name = "steps")
public class Steps {

    @XmlElement(name = "ProcessStep", required = true)
    protected List<Steps.ProcessStep> processStep;

    /**
     * Gets the value of the processStep property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processStep property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessStep().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Steps.ProcessStep }
     * 
     * 
     */
    public List<Steps.ProcessStep> getProcessStep() {
        if (processStep == null) {
            processStep = new ArrayList<Steps.ProcessStep>();
        }
        return this.processStep;
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
     *         &lt;element name="stepCode">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="stepName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="sequenceNo">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="groupName">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="dossierStatus">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="dossierSubStatus">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="durationCount">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="instructionNote">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="briefNote">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="roleAsStep">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;maxLength value="255"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="checkInput" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="roles">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="StepRole" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="roleCode">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="255"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="roleName">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="255"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                             &lt;element name="moderator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *                             &lt;element name="condition">
     *                               &lt;simpleType>
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   &lt;maxLength value="255"/>
     *                                 &lt;/restriction>
     *                               &lt;/simpleType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
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
        "stepCode",
        "stepName",
        "sequenceNo",
        "groupName",
        "dossierStatus",
        "dossierSubStatus",
        "durationCount",
        "instructionNote",
        "briefNote",
        "roleAsStep",
        "checkInput",
        "roles"
    })
    public static class ProcessStep {

        @XmlElement(required = true)
        protected String stepCode;
        @XmlElement(required = true)
        protected String stepName;
        @XmlElement(required = true)
        protected String sequenceNo;
        @XmlElement(required = true)
        protected String groupName;
        @XmlElement(required = true)
        protected String dossierStatus;
        @XmlElement(required = true)
        protected String dossierSubStatus;
        @XmlElement(required = true)
        protected String durationCount;
        @XmlElement(required = true)
        protected String instructionNote;
        @XmlElement(required = true)
        protected String briefNote;
        @XmlElement(required = true)
        protected String roleAsStep;
        protected int checkInput;
        @XmlElement(required = true)
        protected Steps.ProcessStep.Roles roles;

        /**
         * Gets the value of the stepCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStepCode() {
            return stepCode;
        }

        /**
         * Sets the value of the stepCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStepCode(String value) {
            this.stepCode = value;
        }

        /**
         * Gets the value of the stepName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStepName() {
            return stepName;
        }

        /**
         * Sets the value of the stepName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStepName(String value) {
            this.stepName = value;
        }

        /**
         * Gets the value of the sequenceNo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSequenceNo() {
            return sequenceNo;
        }

        /**
         * Sets the value of the sequenceNo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSequenceNo(String value) {
            this.sequenceNo = value;
        }

        /**
         * Gets the value of the groupName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroupName() {
            return groupName;
        }

        /**
         * Sets the value of the groupName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroupName(String value) {
            this.groupName = value;
        }

        /**
         * Gets the value of the dossierStatus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDossierStatus() {
            return dossierStatus;
        }

        /**
         * Sets the value of the dossierStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDossierStatus(String value) {
            this.dossierStatus = value;
        }

        /**
         * Gets the value of the dossierSubStatus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDossierSubStatus() {
            return dossierSubStatus;
        }

        /**
         * Sets the value of the dossierSubStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDossierSubStatus(String value) {
            this.dossierSubStatus = value;
        }

        /**
         * Gets the value of the durationCount property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDurationCount() {
            return durationCount;
        }

        /**
         * Sets the value of the durationCount property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDurationCount(String value) {
            this.durationCount = value;
        }

        /**
         * Gets the value of the instructionNote property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getInstructionNote() {
            return instructionNote;
        }

        /**
         * Sets the value of the instructionNote property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setInstructionNote(String value) {
            this.instructionNote = value;
        }

        /**
         * Gets the value of the briefNote property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBriefNote() {
            return briefNote;
        }

        /**
         * Sets the value of the briefNote property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBriefNote(String value) {
            this.briefNote = value;
        }

        /**
         * Gets the value of the roleAsStep property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRoleAsStep() {
            return roleAsStep;
        }

        /**
         * Sets the value of the roleAsStep property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRoleAsStep(String value) {
            this.roleAsStep = value;
        }

        /**
         * Gets the value of the checkInput property.
         * 
         */
        public int getCheckInput() {
            return checkInput;
        }

        /**
         * Sets the value of the checkInput property.
         * 
         */
        public void setCheckInput(int value) {
            this.checkInput = value;
        }

        /**
         * Gets the value of the roles property.
         * 
         * @return
         *     possible object is
         *     {@link Steps.ProcessStep.Roles }
         *     
         */
        public Steps.ProcessStep.Roles getRoles() {
            return roles;
        }

        /**
         * Sets the value of the roles property.
         * 
         * @param value
         *     allowed object is
         *     {@link Steps.ProcessStep.Roles }
         *     
         */
        public void setRoles(Steps.ProcessStep.Roles value) {
            this.roles = value;
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
         *         &lt;element name="StepRole" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="roleCode">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="255"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="roleName">
         *                     &lt;simpleType>
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         &lt;maxLength value="255"/>
         *                       &lt;/restriction>
         *                     &lt;/simpleType>
         *                   &lt;/element>
         *                   &lt;element name="moderator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
         *                   &lt;element name="condition">
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
            "stepRole"
        })
        public static class Roles {

            @XmlElement(name = "StepRole", required = true)
            protected List<Steps.ProcessStep.Roles.StepRole> stepRole;

            /**
             * Gets the value of the stepRole property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the stepRole property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getStepRole().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Steps.ProcessStep.Roles.StepRole }
             * 
             * 
             */
            public List<Steps.ProcessStep.Roles.StepRole> getStepRole() {
                if (stepRole == null) {
                    stepRole = new ArrayList<Steps.ProcessStep.Roles.StepRole>();
                }
                return this.stepRole;
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
             *         &lt;element name="roleCode">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;maxLength value="255"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="roleName">
             *           &lt;simpleType>
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               &lt;maxLength value="255"/>
             *             &lt;/restriction>
             *           &lt;/simpleType>
             *         &lt;/element>
             *         &lt;element name="moderator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
             *         &lt;element name="condition">
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
                "roleCode",
                "roleName",
                "moderator",
                "condition"
            })
            public static class StepRole {

                @XmlElement(required = true)
                protected String roleCode;
                @XmlElement(required = true)
                protected String roleName;
                protected boolean moderator;
                @XmlElement(required = true)
                protected String condition;

                /**
                 * Gets the value of the roleCode property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getRoleCode() {
                    return roleCode;
                }

                /**
                 * Sets the value of the roleCode property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setRoleCode(String value) {
                    this.roleCode = value;
                }

                /**
                 * Gets the value of the roleName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getRoleName() {
                    return roleName;
                }

                /**
                 * Sets the value of the roleName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setRoleName(String value) {
                    this.roleName = value;
                }

                /**
                 * Gets the value of the moderator property.
                 * 
                 */
                public boolean isModerator() {
                    return moderator;
                }

                /**
                 * Sets the value of the moderator property.
                 * 
                 */
                public void setModerator(boolean value) {
                    this.moderator = value;
                }

                /**
                 * Gets the value of the condition property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCondition() {
                    return condition;
                }

                /**
                 * Sets the value of the condition property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCondition(String value) {
                    this.condition = value;
                }

            }

        }

    }

}
