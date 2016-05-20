package cn.com.bluemoon.jeesite.common.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;


public class PropUtil {
	
	public static Properties getQiniuProperties() {

		Properties p = new Properties();

		try {
			InputStream inputStream = PropUtil.class.getClassLoader().getResourceAsStream(
					"property/qiniu.properties");

			p.load(inputStream);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return p;
	}
	
	public static Properties getFastDFSProperties() {

		Properties p = new Properties();
		InputStream inputStream =null;
		try {
			inputStream = PropUtil.class.getClassLoader().getResourceAsStream(
					"property/fdfs_client.properties");

			p.load(inputStream);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return p;
	}
	
	
	public static Properties getZookeeperProperties() {

		Properties p = new Properties();
		InputStream inputStream =null;
		try {
			inputStream = PropUtil.class.getClassLoader().getResourceAsStream(
					"property/zookeeper.properties");

			p.load(inputStream);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return p;
	}
	
	
	/**
	 * 根据url取配置文件的对象
	 * 
	 */
//	public static Properties getUrlProperties(String url) {
//
//		Properties p = new Properties();
//
//		try {
//			InputStream inputStream = PropUtil.class.getClassLoader().getResourceAsStream(
//					url);
//
//			p.load(inputStream);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		return p;
//	}
	public static Properties getUrlProperties(String url) {
		Properties p = new Properties();
		try {
			InputStreamReader inputStream =new InputStreamReader(PropUtil.class.getClassLoader().getResourceAsStream(url), "UTF-8");
			p.load(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return p;
	}
	
	/**
	 * 写入配置文件
	 * @param filePath
	 * @param name
	 * @param value
	 */
	public static void writeProperty(String filePath, String name, String value) {
		try {
			Properties prop = new Properties();
			//赋值
			prop.setProperty(name, value);
			//写入
			OutputStream fos = new FileOutputStream(PropUtil.class.getResource(filePath).getPath());
			prop.store(fos, "The properties file");
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			System.out.println("----获取文件路径失败----");
			e.printStackTrace();
		}
	}

	/**
	 * 读取配置文件
	 * @param filePath
	 * @param name
	 * @return
	 */
	public static String readProperty(String filePath, String name) {
		String value = "";
		try {
			//读取
			InputStream inputStream = PropUtil.class.getClassLoader().getResourceAsStream(filePath);
			Properties pro = new Properties();
			pro.load(inputStream);
			//赋值
			value = pro.getProperty(name);
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			System.out.println("----获取文件路径失败----");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("----加载文件流失败----");
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 读取配置中所有key值
	 * @param filePath
	 * @return
	 */
	public static Set<String> getKeySet(String filePath){
		Set<String> keys = new HashSet<String>();
		try {
			//读取
			InputStream inputStream = PropUtil.class.getClassLoader().getResourceAsStream(filePath);
			Properties pro = new Properties();
			pro.load(inputStream);
			//赋值
			keys = pro.stringPropertyNames();
			inputStream.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			System.out.println("----获取文件路径失败----");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("----加载文件流失败----");
			e.printStackTrace();
		}
		return keys;
	}

}
