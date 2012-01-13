//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.12 at 10:00:45 AM EST 
//


package org.rac.structure.patronImportSchema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for patronFunding complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patronFunding">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="topic" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="awardDetails" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fundingType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fundingDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "patronFunding", propOrder = {
    "topic",
    "awardDetails",
    "fundingType",
    "fundingDate"
})
public class PatronFunding {

    protected String topic;
    protected String awardDetails;
    @XmlElement(required = true)
    protected String fundingType;
    @XmlElement(required = true)
    protected String fundingDate;

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Gets the value of the awardDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAwardDetails() {
        return awardDetails;
    }

    /**
     * Sets the value of the awardDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAwardDetails(String value) {
        this.awardDetails = value;
    }

    /**
     * Gets the value of the fundingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingType() {
        return fundingType;
    }

    /**
     * Sets the value of the fundingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingType(String value) {
        this.fundingType = value;
    }

    /**
     * Gets the value of the fundingDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundingDate() {
        return fundingDate;
    }

    /**
     * Sets the value of the fundingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundingDate(String value) {
        this.fundingDate = value;
    }

}
