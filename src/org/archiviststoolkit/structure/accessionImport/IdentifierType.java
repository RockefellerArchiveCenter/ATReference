//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.27 at 12:28:55 PM EDT 
//


package org.archiviststoolkit.structure.accessionImport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for identifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="identifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="composite" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;sequence>
 *           &lt;element name="part1">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="10"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="part2" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="10"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="part3" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="10"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="part4" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="10"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identifierType", propOrder = {
    "composite",
    "part1",
    "part2",
    "part3",
    "part4"
})
public class IdentifierType {

    protected String composite;
    protected String part1;
    protected String part2;
    protected String part3;
    protected String part4;

    /**
     * Gets the value of the composite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComposite() {
        return composite;
    }

    /**
     * Sets the value of the composite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComposite(String value) {
        this.composite = value;
    }

    /**
     * Gets the value of the part1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart1() {
        return part1;
    }

    /**
     * Sets the value of the part1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart1(String value) {
        this.part1 = value;
    }

    /**
     * Gets the value of the part2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart2() {
        return part2;
    }

    /**
     * Sets the value of the part2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart2(String value) {
        this.part2 = value;
    }

    /**
     * Gets the value of the part3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart3() {
        return part3;
    }

    /**
     * Sets the value of the part3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart3(String value) {
        this.part3 = value;
    }

    /**
     * Gets the value of the part4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPart4() {
        return part4;
    }

    /**
     * Sets the value of the part4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPart4(String value) {
        this.part4 = value;
    }

}
