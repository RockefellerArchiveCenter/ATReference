//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.21 at 08:16:30 PM EST 
//


package org.rac.structure.patronImportSchema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for patronPublicationsSubjects complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patronPublicationsSubjects">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subjectTerm" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subjectTermType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="subjectSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "patronPublicationsSubjects", propOrder = {
    "subjectTerm",
    "subjectTermType",
    "subjectSource"
})
public class PatronPublicationsSubjects {

    @XmlElement(required = true)
    protected String subjectTerm;
    @XmlElement(required = true)
    protected String subjectTermType;
    protected String subjectSource;

    /**
     * Gets the value of the subjectTerm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectTerm() {
        return subjectTerm;
    }

    /**
     * Sets the value of the subjectTerm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectTerm(String value) {
        this.subjectTerm = value;
    }

    /**
     * Gets the value of the subjectTermType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectTermType() {
        return subjectTermType;
    }

    /**
     * Sets the value of the subjectTermType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectTermType(String value) {
        this.subjectTermType = value;
    }

    /**
     * Gets the value of the subjectSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectSource() {
        return subjectSource;
    }

    /**
     * Sets the value of the subjectSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectSource(String value) {
        this.subjectSource = value;
    }

}
