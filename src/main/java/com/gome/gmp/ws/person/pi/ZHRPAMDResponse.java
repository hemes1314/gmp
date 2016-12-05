
package com.gome.gmp.ws.person.pi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * anonymous complex type的 Java 类。
 * 
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="JYID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="T_OUT">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZHR_PA_MD" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZHR_PA_MD.Response")
public class ZHRPAMDResponse {

	@XmlElement(name = "JYID", required = true)
	protected String jyid;
	@XmlElement(name = "T_OUT", required = true)
	protected ZHRPAMDResponse.TOUT tout;

	/**
	 * 获取jyid属性的值。
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getJYID() {
		return jyid;
	}

	/**
	 * 设置jyid属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setJYID(String value) {
		this.jyid = value;
	}

	/**
	 * 获取tout属性的值。
	 * 
	 * @return possible object is {@link ZHRPAMDResponse.TOUT }
	 * 
	 */
	public ZHRPAMDResponse.TOUT getTOUT() {
		return tout;
	}

	/**
	 * 设置tout属性的值。
	 * 
	 * @param value
	 *            allowed object is {@link ZHRPAMDResponse.TOUT }
	 * 
	 */
	public void setTOUT(ZHRPAMDResponse.TOUT value) {
		this.tout = value;
	}

	/**
	 * <p>
	 * anonymous complex type的 Java 类。
	 * 
	 * <p>
	 * 以下模式片段指定包含在此类中的预期内容。
	 * 
	 * <pre>
	 * &lt;complexType>
	 *   &lt;complexContent>
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	 *       &lt;sequence>
	 *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZHR_PA_MD" maxOccurs="unbounded" minOccurs="0"/>
	 *       &lt;/sequence>
	 *     &lt;/restriction>
	 *   &lt;/complexContent>
	 * &lt;/complexType>
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "item" })
	public static class TOUT {

		protected List<ZHRPAMD2> item;

		/**
		 * Gets the value of the item property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the item property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getItem().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link ZHRPAMD2 }
		 * 
		 * 
		 */
		public List<ZHRPAMD2> getItem() {
			if (item == null) {
				item = new ArrayList<ZHRPAMD2>();
			}
			return this.item;
		}

	}

}
