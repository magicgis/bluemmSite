package cn.com.bluemoon.jeesite.modules.pay.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 资源配置管理
 * @author hunaihong
 *
 */
public class HttpProperties {
	
	private static Map<String,String> paramsMap = new HashMap<String,String>();
	
	static {
		Properties prop = PropUtil.getUrlProperties("property/url.properties");
		// 返回Properties中包含的key-value的Set视图
		Set<Entry<Object, Object>> set = prop.entrySet();
		// 返回在此Set中的元素上进行迭代的迭代器
		Iterator<Map.Entry<Object, Object>> it = set.iterator();
		String key = null, value = null;
		// 循环取出key-value
		while (it.hasNext()) {
			Entry<Object, Object> entry = it.next();
			key = String.valueOf(entry.getKey());
			value = String.valueOf(entry.getValue());
			key = key == null ? key : key.trim().toUpperCase();
			value = value == null ? value : value.trim();
			// 将key-value放入map中
			paramsMap.put(key, value);
		}
	}
	
	public static String getVal(String key) {
		key = key == null ? "" : key.trim().toUpperCase();
		return paramsMap.get(key);
	}


	private static String backPayUrl = null;//"http://localhost:8080/moonMall-gateway";
	private static String moonMallPayUrl = null;//"http://localhost:8080/moonMall-pay";

	/**
	 * "http://tmallapi.bluemoon.com.cn/moonMall-gateway";
	 * @return
	 */
	public static String getBackPayUrl() {
		if(null==backPayUrl){
			backPayUrl = getVal("backPayUrl");
		}  
		return backPayUrl;
	}
	
	/**
	 * "http://tmallapi.bluemoon.com.cn/moonMall-pay";
	 * @return
	 */
	public static String getMoonMallPayUrl() {
		if(null==moonMallPayUrl){
			moonMallPayUrl = getVal("moonMallPayUrl");
		}  
		return moonMallPayUrl;
	}
}
