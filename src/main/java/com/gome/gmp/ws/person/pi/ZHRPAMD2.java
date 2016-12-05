
package com.gome.gmp.ws.person.pi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * ZHR_PA_MD complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ZHR_PA_MD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MANDT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PERNR" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NACHN" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VORNA" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NACH2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="GBDAT" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ENDDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="DAT01" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="DAT02" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="DAT03" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ICNUM" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MPHON" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TELE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="EMAIL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGEH" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORG_BEGDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ORGEH_L1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX_L1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGEH_L2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX_L2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGEH_L3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX_L3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGEH_L4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX_L4" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGEH_L5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ORGTX_L5" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="KOSTL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="KOSTX" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PERSG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZYGZJ" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZYG_BEGDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ZYGZJG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="STELL" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="8"/>
 *               &lt;pattern value="\d+"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="STLTX" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ST_BEGDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="PERSK" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PE_BEGDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ABKRS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZBANCHE" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZPQGS" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZYDJ" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ZYD_BEGDA" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *         &lt;element name="ZAD_ACCOUNT" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UPD_FLAG" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AEDTM" type="{urn:sap-com:document:sap:rfc:functions}date" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZHR_PA_MD", propOrder = { "mandt", "pernr", "nachn", "vorna", "nach2", "gbdat", "endda", "dat01",
		"dat02", "dat03", "icnum", "mphon", "tele", "email", "orgeh", "orgtx", "orgbegda", "orgehl1", "orgtxl1",
		"orgehl2", "orgtxl2", "orgehl3", "orgtxl3", "orgehl4", "orgtxl4", "orgehl5", "orgtxl5", "kostl", "kostx",
		"persg", "zygzj", "zygbegda", "zygzjg", "stell", "stltx", "stbegda", "persk", "pebegda", "abkrs", "zbanche",
		"zpqgs", "zydj", "zydbegda", "zadaccount", "updflag", "aedtm" })
public class ZHRPAMD2 {

	@XmlElement(name = "MANDT")
	protected String mandt;
	@XmlElement(name = "PERNR")
	protected String pernr;
	@XmlElement(name = "NACHN")
	protected String nachn;
	@XmlElement(name = "VORNA")
	protected String vorna;
	@XmlElement(name = "NACH2")
	protected String nach2;
	@XmlElement(name = "GBDAT")
	protected String gbdat;
	@XmlElement(name = "ENDDA")
	protected String endda;
	@XmlElement(name = "DAT01")
	protected String dat01;
	@XmlElement(name = "DAT02")
	protected String dat02;
	@XmlElement(name = "DAT03")
	protected String dat03;
	@XmlElement(name = "ICNUM")
	protected String icnum;
	@XmlElement(name = "MPHON")
	protected String mphon;
	@XmlElement(name = "TELE")
	protected String tele;
	@XmlElement(name = "EMAIL")
	protected String email;
	@XmlElement(name = "ORGEH")
	protected String orgeh;
	@XmlElement(name = "ORGTX")
	protected String orgtx;
	@XmlElement(name = "ORG_BEGDA")
	protected String orgbegda;
	@XmlElement(name = "ORGEH_L1")
	protected String orgehl1;
	@XmlElement(name = "ORGTX_L1")
	protected String orgtxl1;
	@XmlElement(name = "ORGEH_L2")
	protected String orgehl2;
	@XmlElement(name = "ORGTX_L2")
	protected String orgtxl2;
	@XmlElement(name = "ORGEH_L3")
	protected String orgehl3;
	@XmlElement(name = "ORGTX_L3")
	protected String orgtxl3;
	@XmlElement(name = "ORGEH_L4")
	protected String orgehl4;
	@XmlElement(name = "ORGTX_L4")
	protected String orgtxl4;
	@XmlElement(name = "ORGEH_L5")
	protected String orgehl5;
	@XmlElement(name = "ORGTX_L5")
	protected String orgtxl5;
	@XmlElement(name = "KOSTL")
	protected String kostl;
	@XmlElement(name = "KOSTX")
	protected String kostx;
	@XmlElement(name = "PERSG")
	protected String persg;
	@XmlElement(name = "ZYGZJ")
	protected String zygzj;
	@XmlElement(name = "ZYG_BEGDA")
	protected String zygbegda;
	@XmlElement(name = "ZYGZJG")
	protected String zygzjg;
	@XmlElement(name = "STELL")
	protected String stell;
	@XmlElement(name = "STLTX")
	protected String stltx;
	@XmlElement(name = "ST_BEGDA")
	protected String stbegda;
	@XmlElement(name = "PERSK")
	protected String persk;
	@XmlElement(name = "PE_BEGDA")
	protected String pebegda;
	@XmlElement(name = "ABKRS")
	protected String abkrs;
	@XmlElement(name = "ZBANCHE")
	protected String zbanche;
	@XmlElement(name = "ZPQGS")
	protected String zpqgs;
	@XmlElement(name = "ZYDJ")
	protected String zydj;
	@XmlElement(name = "ZYD_BEGDA")
	protected String zydbegda;
	@XmlElement(name = "ZAD_ACCOUNT")
	protected String zadaccount;
	@XmlElement(name = "UPD_FLAG")
	protected String updflag;
	@XmlElement(name = "AEDTM")
	protected String aedtm;

	/**
	 * 获取mandt属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMANDT() {
		return mandt;
	}

	/**
	 * 设置mandt属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMANDT(String value) {
		this.mandt = value;
	}

	/**
	 * 获取pernr属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPERNR() {
		return pernr;
	}

	/**
	 * 设置pernr属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPERNR(String value) {
		this.pernr = value;
	}

	/**
	 * 获取nachn属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNACHN() {
		return nachn;
	}

	/**
	 * 设置nachn属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNACHN(String value) {
		this.nachn = value;
	}

	/**
	 * 获取vorna属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVORNA() {
		return vorna;
	}

	/**
	 * 设置vorna属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVORNA(String value) {
		this.vorna = value;
	}

	/**
	 * 获取nach2属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNACH2() {
		return nach2;
	}

	/**
	 * 设置nach2属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNACH2(String value) {
		this.nach2 = value;
	}

	/**
	 * 获取gbdat属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGBDAT() {
		return gbdat;
	}

	/**
	 * 设置gbdat属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGBDAT(String value) {
		this.gbdat = value;
	}

	/**
	 * 获取endda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getENDDA() {
		return endda;
	}

	/**
	 * 设置endda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setENDDA(String value) {
		this.endda = value;
	}

	/**
	 * 获取dat01属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDAT01() {
		return dat01;
	}

	/**
	 * 设置dat01属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDAT01(String value) {
		this.dat01 = value;
	}

	/**
	 * 获取dat02属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDAT02() {
		return dat02;
	}

	/**
	 * 设置dat02属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDAT02(String value) {
		this.dat02 = value;
	}

	/**
	 * 获取dat03属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDAT03() {
		return dat03;
	}

	/**
	 * 设置dat03属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDAT03(String value) {
		this.dat03 = value;
	}

	/**
	 * 获取icnum属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getICNUM() {
		return icnum;
	}

	/**
	 * 设置icnum属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setICNUM(String value) {
		this.icnum = value;
	}

	/**
	 * 获取mphon属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMPHON() {
		return mphon;
	}

	/**
	 * 设置mphon属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMPHON(String value) {
		this.mphon = value;
	}

	/**
	 * 获取tele属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTELE() {
		return tele;
	}

	/**
	 * 设置tele属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTELE(String value) {
		this.tele = value;
	}

	/**
	 * 获取email属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEMAIL() {
		return email;
	}

	/**
	 * 设置email属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEMAIL(String value) {
		this.email = value;
	}

	/**
	 * 获取orgeh属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEH() {
		return orgeh;
	}

	/**
	 * 设置orgeh属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEH(String value) {
		this.orgeh = value;
	}

	/**
	 * 获取orgtx属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTX() {
		return orgtx;
	}

	/**
	 * 设置orgtx属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTX(String value) {
		this.orgtx = value;
	}

	/**
	 * 获取orgbegda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGBEGDA() {
		return orgbegda;
	}

	/**
	 * 设置orgbegda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGBEGDA(String value) {
		this.orgbegda = value;
	}

	/**
	 * 获取orgehl1属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEHL1() {
		return orgehl1;
	}

	/**
	 * 设置orgehl1属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEHL1(String value) {
		this.orgehl1 = value;
	}

	/**
	 * 获取orgtxl1属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTXL1() {
		return orgtxl1;
	}

	/**
	 * 设置orgtxl1属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTXL1(String value) {
		this.orgtxl1 = value;
	}

	/**
	 * 获取orgehl2属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEHL2() {
		return orgehl2;
	}

	/**
	 * 设置orgehl2属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEHL2(String value) {
		this.orgehl2 = value;
	}

	/**
	 * 获取orgtxl2属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTXL2() {
		return orgtxl2;
	}

	/**
	 * 设置orgtxl2属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTXL2(String value) {
		this.orgtxl2 = value;
	}

	/**
	 * 获取orgehl3属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEHL3() {
		return orgehl3;
	}

	/**
	 * 设置orgehl3属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEHL3(String value) {
		this.orgehl3 = value;
	}

	/**
	 * 获取orgtxl3属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTXL3() {
		return orgtxl3;
	}

	/**
	 * 设置orgtxl3属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTXL3(String value) {
		this.orgtxl3 = value;
	}

	/**
	 * 获取orgehl4属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEHL4() {
		return orgehl4;
	}

	/**
	 * 设置orgehl4属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEHL4(String value) {
		this.orgehl4 = value;
	}

	/**
	 * 获取orgtxl4属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTXL4() {
		return orgtxl4;
	}

	/**
	 * 设置orgtxl4属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTXL4(String value) {
		this.orgtxl4 = value;
	}

	/**
	 * 获取orgehl5属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGEHL5() {
		return orgehl5;
	}

	/**
	 * 设置orgehl5属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGEHL5(String value) {
		this.orgehl5 = value;
	}

	/**
	 * 获取orgtxl5属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getORGTXL5() {
		return orgtxl5;
	}

	/**
	 * 设置orgtxl5属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setORGTXL5(String value) {
		this.orgtxl5 = value;
	}

	/**
	 * 获取kostl属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getKOSTL() {
		return kostl;
	}

	/**
	 * 设置kostl属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setKOSTL(String value) {
		this.kostl = value;
	}

	/**
	 * 获取kostx属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getKOSTX() {
		return kostx;
	}

	/**
	 * 设置kostx属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setKOSTX(String value) {
		this.kostx = value;
	}

	/**
	 * 获取persg属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPERSG() {
		return persg;
	}

	/**
	 * 设置persg属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPERSG(String value) {
		this.persg = value;
	}

	/**
	 * 获取zygzj属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZYGZJ() {
		return zygzj;
	}

	/**
	 * 设置zygzj属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZYGZJ(String value) {
		this.zygzj = value;
	}

	/**
	 * 获取zygbegda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZYGBEGDA() {
		return zygbegda;
	}

	/**
	 * 设置zygbegda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZYGBEGDA(String value) {
		this.zygbegda = value;
	}

	/**
	 * 获取zygzjg属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZYGZJG() {
		return zygzjg;
	}

	/**
	 * 设置zygzjg属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZYGZJG(String value) {
		this.zygzjg = value;
	}

	/**
	 * 获取stell属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSTELL() {
		return stell;
	}

	/**
	 * 设置stell属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSTELL(String value) {
		this.stell = value;
	}

	/**
	 * 获取stltx属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSTLTX() {
		return stltx;
	}

	/**
	 * 设置stltx属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSTLTX(String value) {
		this.stltx = value;
	}

	/**
	 * 获取stbegda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSTBEGDA() {
		return stbegda;
	}

	/**
	 * 设置stbegda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSTBEGDA(String value) {
		this.stbegda = value;
	}

	/**
	 * 获取persk属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPERSK() {
		return persk;
	}

	/**
	 * 设置persk属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPERSK(String value) {
		this.persk = value;
	}

	/**
	 * 获取pebegda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPEBEGDA() {
		return pebegda;
	}

	/**
	 * 设置pebegda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPEBEGDA(String value) {
		this.pebegda = value;
	}

	/**
	 * 获取abkrs属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getABKRS() {
		return abkrs;
	}

	/**
	 * 设置abkrs属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setABKRS(String value) {
		this.abkrs = value;
	}

	/**
	 * 获取zbanche属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZBANCHE() {
		return zbanche;
	}

	/**
	 * 设置zbanche属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZBANCHE(String value) {
		this.zbanche = value;
	}

	/**
	 * 获取zpqgs属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZPQGS() {
		return zpqgs;
	}

	/**
	 * 设置zpqgs属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZPQGS(String value) {
		this.zpqgs = value;
	}

	/**
	 * 获取zydj属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZYDJ() {
		return zydj;
	}

	/**
	 * 设置zydj属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZYDJ(String value) {
		this.zydj = value;
	}

	/**
	 * 获取zydbegda属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZYDBEGDA() {
		return zydbegda;
	}

	/**
	 * 设置zydbegda属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZYDBEGDA(String value) {
		this.zydbegda = value;
	}

	/**
	 * 获取zadaccount属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getZADACCOUNT() {
		return zadaccount;
	}

	/**
	 * 设置zadaccount属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setZADACCOUNT(String value) {
		this.zadaccount = value;
	}

	/**
	 * 获取updflag属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUPDFLAG() {
		return updflag;
	}

	/**
	 * 设置updflag属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUPDFLAG(String value) {
		this.updflag = value;
	}

	/**
	 * 获取aedtm属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAEDTM() {
		return aedtm;
	}

	/**
	 * 设置aedtm属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAEDTM(String value) {
		this.aedtm = value;
	}

}
