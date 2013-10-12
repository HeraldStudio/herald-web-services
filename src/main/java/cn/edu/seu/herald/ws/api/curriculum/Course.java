/*
 * The MIT License
 *
 * Copyright 2013 Herald Studio, Southeast University.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.24 at 05:40:35 ���� CST 
//


package cn.edu.seu.herald.ws.api.curriculum;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;


/**
 * <p>Java class for course complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="course">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://herald.seu.edu.cn/ws/curriculum}name"/>
 *         &lt;element name="lecturer" type="{http://herald.seu.edu.cn/ws/curriculum}lecturer"/>
 *         &lt;element name="credit" type="{http://herald.seu.edu.cn/ws/curriculum}credit"/>
 *         &lt;element name="week" type="{http://herald.seu.edu.cn/ws/curriculum}period"/>
 *         &lt;element name="students" type="{http://herald.seu.edu.cn/ws/curriculum}studentList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="href" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "course", propOrder = {
    "name",
    "lecturer",
    "credit",
    "week",
    "students"
})
@XmlRootElement(name = "course")
public class Course {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String lecturer;
    @XmlElement(required = true)
    protected BigDecimal credit;
    @XmlElement(required = true)
    protected Period week;
    protected StudentList students;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlTransient
    private int id;

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
     * Gets the value of the lecturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     * Sets the value of the lecturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLecturer(String value) {
        this.lecturer = value;
    }

    /**
     * Gets the value of the credit property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal }
     *
     */
    public BigDecimal getCredit() {
        return credit;
    }

    /**
     * Sets the value of the credit property.
     *
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal }
     *
     */
    public void setCredit(BigDecimal value) {
        this.credit = value;
    }

    /**
     * Gets the value of the week property.
     *
     * @return
     *     possible object is
     *     {@link Period }
     *
     */
    public Period getWeek() {
        return week;
    }

    /**
     * Sets the value of the week property.
     *
     * @param value
     *     allowed object is
     *     {@link Period }
     *
     */
    public void setWeek(Period value) {
        this.week = value;
    }

    /**
     * Gets the value of the students property.
     *
     * @return
     *     possible object is
     *     {@link StudentList }
     *
     */
    public StudentList getStudents() {
        return students;
    }

    /**
     * Sets the value of the students property.
     *
     * @param value
     *     allowed object is
     *     {@link StudentList }
     *     
     */
    public void setStudents(StudentList value) {
        this.students = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
