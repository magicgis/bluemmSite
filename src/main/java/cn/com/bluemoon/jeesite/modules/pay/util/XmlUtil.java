package cn.com.bluemoon.jeesite.modules.pay.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XmlUtil {

	/**
	 * 序列化一个对象到XML(重写XStream，加上<![CDATA[????]]>)
	 * 
	 * @param obj
	 *            实体对象
	 * @return
	 */
	public static String serializeSingleObject(Object obj) {
		// 将根目录进行重命名为xml
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * 序列化一个对象到XML(原生xml格式，未进行XStream重写)
	 * 
	 * @param obj
	 *            实体对象
	 * @return
	 */
	public static String ObjectToXml(Object obj) {
		// XStream xstream = new XStream(new DomDriver());
		XStream xstream = new XStream(new DomDriver("UTF-8",
				new XmlFriendlyReplacer("_-", "_")));
		// 将根目录进行重命名为xml
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj);
	}

	/**
	 * 
	 * @param map
	 */
	public static String simpleMapToXMLMessage(Map map) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set set = map.keySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = (String) map.get(key);
			if (null == value) {
				value = "";
			}
			sb.append("<" + key + ">");
			sb.append(value);
			sb.append("</" + key + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 将xml转换成javabean
	 * 
	 * @param xml
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T XmlToObject(String xml, Class<T> clazz) {
		Object obj = null;
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("xml", clazz);
		obj = xstream.fromXML(xml);
		return (T) obj;
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver(
			new XmlFriendlyReplacer("_-", "_")) {

		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	// public static void main(String[] args) {
	// WeixinUserList weixinUserList = new WeixinUserList();
	// weixinUserList.setTotal(1);
	// weixinUserList.setCount(567);
	// weixinUserList.setNextOpenId("890");
	// List list = new ArrayList();
	// list.add("123");
	// list.add("456");
	// weixinUserList.setOpenIdList(list);
	//
	// String xml = XmlUtil.serializeSingleObject(weixinUserList);
	// System.out.println(xml);
	//
	// String xm = XmlUtil.ObjectToXml(weixinUserList);
	// System.out.println(xm);
	//
	// weixinUserList = (WeixinUserList) XmlUtil.XmlToObject(xml,
	// WeixinUserList.class);
	// System.out.println("xml转换成java:" + weixinUserList.getNextOpenId());
	// }
}
