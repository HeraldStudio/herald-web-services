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
// Generated on: 2013.05.15 at 02:17:05 ���� CST 
//


package cn.edu.seu.herald.ws.api.library;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.edu.seu.herald.ws.cn.edu.seu.herald.ws.api.library package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.edu.seu.herald.ws.cn.edu.seu.herald.ws.api.library
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BooklistLinkType }
     *
     */
    public BooklistLinkType createBooklistLinkType() {
        return new BooklistLinkType();
    }

    /**
     * Create an instance of {@link Booklist }
     *
     */
    public Booklist createBooklist() {
        return new Booklist();
    }

    /**
     * Create an instance of {@link CopiesType }
     *
     */
    public CopiesType createCopiesType() {
        return new CopiesType();
    }

    /**
     * Create an instance of {@link User }
     *
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link ReservationType }
     *
     */
    public ReservationType createReservationType() {
        return new ReservationType();
    }

    /**
     * Create an instance of {@link RenewalType }
     *
     */
    public RenewalType createRenewalType() {
        return new RenewalType();
    }

    /**
     * Create an instance of {@link Book }
     *
     */
    public Book createBook() {
        return new Book();
    }

    /**
     * Create an instance of {@link CopyType }
     *
     */
    public CopyType createCopyType() {
        return new CopyType();
    }

}
