package cn.com.bluemoon.jeesite.modules.pay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 根据url取配置文件的对象
 * 
 */
public class PropUtil {
	public static Properties getUrlProperties(String url) {
		Properties p = new Properties();
		try {
			InputStream inputStream = PropUtil.class.getClassLoader()
					.getResourceAsStream(url);
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return p;
	}
}
