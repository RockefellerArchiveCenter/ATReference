//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.04.11 at 01:13:59 PM EDT 
//


package org.archiviststoolkit.structure.EAD;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for daogrp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="daogrp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="daodesc" type="{urn:isbn:1-931666-22-9}daodesc" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="daoloc" type="{urn:isbn:1-931666-22-9}daoloc"/>
 *           &lt;group ref="{urn:isbn:1-931666-22-9}extended.els"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:isbn:1-931666-22-9}a.common"/>
 *       &lt;attGroup ref="{urn:isbn:1-931666-22-9}extended.atts"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "daogrp", namespace = "urn:isbn:1-931666-22-9", propOrder = {
    "daodesc",
    "daolocOrResourceOrArc"
})
public class Daogrp {

    @XmlElement(namespace = "urn:isbn:1-931666-22-9")
    protected Daodesc daodesc;
    @XmlElements({
        @XmlElement(name = "ptrloc", namespace = "urn:isbn:1-931666-22-9", type = Ptrloc.class),
        @XmlElement(name = "extrefloc", namespace = "urn:isbn:1-931666-22-9", type = Extrefloc.class),
        @XmlElement(name = "refloc", namespace = "urn:isbn:1-931666-22-9", type = Refloc.class),
        @XmlElement(name = "daoloc", namespace = "urn:isbn:1-931666-22-9", type = Daoloc.class),
        @XmlElement(name = "arc", namespace = "urn:isbn:1-931666-22-9", type = Arc.class),
        @XmlElement(name = "extptrloc", namespace = "urn:isbn:1-931666-22-9", type = Extptrloc.class),
        @XmlElement(name = "resource", namespace = "urn:isbn:1-931666-22-9", type = Resource.class)
    })
    protected List<Object> daolocOrResourceOrArc;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String altrender;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String audience;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String role;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anySimpleType")
    protected String title;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String type;

    /**
     * Gets the value of the daodesc property.
     * 
     * @return
     *     possible object is
     *     {@link Daodesc }
     *     
     */
    public Daodesc getDaodesc() {
        return daodesc;
    }

    /**
     * Sets the value of the daodesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Daodesc }
     *     
     */
    public void setDaodesc(Daodesc value) {
        this.daodesc = value;
    }

    /**
     * Gets the value of the daolocOrResourceOrArc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the daolocOrResourceOrArc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDaolocOrResourceOrArc().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ptrloc }
     * {@link Extrefloc }
     * {@link Refloc }
     * {@link Daoloc }
     * {@link Arc }
     * {@link Extptrloc }
     * {@link Resource }
     * 
     * 
     */
    public List<Object> getDaolocOrResourceOrArc() {
        if (daolocOrResourceOrArc == null) {
            daolocOrResourceOrArc = new ArrayList<Object>();
        }
        return this.daolocOrResourceOrArc;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the altrender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltrender() {
        return altrender;
    }

    /**
     * Sets the value of the altrender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltrender(String value) {
        this.altrender = value;
    }

    /**
     * Gets the value of the audience property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAudience() {
        return audience;
    }

    /**
     * Sets the value of the audience property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAudience(String value) {
        this.audience = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "extended";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
