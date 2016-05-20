package cn.com.bluemoon.jeesite.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.AllClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: BLUE MOON
 * </p>
 * 
 * @author Eric liang
 * @version 1.0
 * @date 2015/07/17
 */

public class HttpUtil {
	private static String TAG_TAG = "testing";
	/**
	 * In milli-second
	 */
	public static int timeoutConnection = 30000;

	/**
	 * In milli-second
	 */
	public static int timeoutSocket = 70000;
	
	private static Logger logger = Logger.getLogger(HttpUtil.class);

	/**
	 * Do HTTP post. Set static class variables timeoutConnection and
	 * timeoutSocket for time out. Default values are 10s
	 * 
	 * @param url
	 *            - The host.
	 * @param params
	 *            - Parameters set. Set null if no parameter. This method
	 *            encoded the parameters with UTF-8 character set. Use the
	 *            follow name value pair code sample to make a parameter object
	 *            for http post.
	 * 
	 *            List<BasicNameValuePair> params = new
	 *            LinkedList<BasicNameValuePair>(); params.add(new
	 *            BasicNameValuePair("msisdn", "+85212345675"));
	 * 
	 * @return An response object.
	 * 
	 *         To get response content: HttpEntity resEntity =
	 *         response.getEntity(); String result =
	 *         EntityUtils.toString(resEntity);
	 */

	public static HttpResponse executeHttpPost(String url,
			List<BasicNameValuePair> params) {
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpPost request = null;
		UrlEncodedFormEntity form = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			client = new DefaultHttpClient(httpParameters);
			request = new HttpPost();
			if (params != null) {
				form = new UrlEncodedFormEntity(params);
				form.setContentEncoding(HTTP.UTF_8);
				request.setEntity(form);
			}
			request.setURI(new URI(url));

			HttpResponse response = client.execute(request);
			return response;
		} catch (Exception e) {
			//Logger.e(TAG_TAG, "executeHttpPost: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
			form = null;
		}
	}
	
	public static String doJsonPost(String strURL, String params) {
		logger.info(strURL);
		logger.info(params);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(strURL);
		StringEntity myEntity = new StringEntity(params, ContentType.APPLICATION_JSON);// 构造请求数据
		post.setEntity(myEntity);// 设置请求体
		String responseContent = null; // 响应内容
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response.close();
		
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}
	
	public static String doHttpsJsonPost(String strURL, String params){
		logger.info(strURL);
		logger.info(params);
		HttpClient client = null;
		HttpPost post = new HttpPost(strURL);
		StringEntity myEntity = new StringEntity(params, ContentType.APPLICATION_JSON);// 构造请求数据
		post.setEntity(myEntity);// 设置请求体
		String responseContent = null; // 响应内容
		HttpResponse response = null;
		
		try {
			client = getHttpClient();
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null)
					response = null;
		
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}
	
	

	/**
	 * Do HTTP post. Set static class variables timeoutConnection and
	 * timeoutSocket for time out. Default values are 10s
	 * 
	 * @param url
	 *            - The host.
	 * @param params
	 *            - Parameters set. Set null if no parameter. This method
	 *            encoded the parameters with UTF-8 character set. Use the
	 *            follow name value pair code sample to make a parameter object
	 *            for http post.
	 * 
	 *            List<BasicNameValuePair> params = new
	 *            LinkedList<BasicNameValuePair>(); params.add(new
	 *            BasicNameValuePair("msisdn", "+85212345675"));
	 * 
	 * @param clazz
	 *            - The JSONObject class.
	 * 
	 *            such as AddressJsonResponse.class
	 * 
	 * @return An response JSONObject.
	 * 
	 */
/*	public static <T> T executeHttpPost(String url,
			List<BasicNameValuePair> params, Class<T> clazz) {
		if ( params != null) {
			StringBuffer strBuff = new StringBuffer();
			strBuff.append(url);
			for (int i = 0; i< params.size(); i++) {
				BasicNameValuePair param = params.get(i);
				String name = param.getName();
				String value = param.getValue();
				if (i == 0) {
					strBuff.append("?"+name+"="+value);
				} else {
					strBuff.append("&"+name+"="+value);
				}
			}
			//LogUtils.d(TAG_TAG, "executeHttpPost: request url = " + strBuff.toString());
		}
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpPost request = null;
		// UrlEncodedFormEntity form = null;
		HttpResponse response = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			//client = new DefaultHttpClient(httpParameters);
			client = getHttpClient();
			request = new HttpPost(url);
			if (params != null) {
				// form = new UrlEncodedFormEntity(params);
				// form.setContentEncoding(HTTP.UTF_8);
				// request.setEntity(form);
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			// request.setURI(new URI(url));

			response = client.execute(request);
			HttpEntity resEntity = response.getEntity();
			String resultJson = EntityUtils.toString(resEntity);
			//LogUtils.d(TAG_TAG, "executeHttpPost: result = " + resultJson);
			//T t = JSONObject.parseObject(resultJson, clazz);
			return t;
		} catch (Exception e) {
			//LogUtils.e(TAG_TAG, "executeHttpPost: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
			// form = null;
			response = null;
		}
	}*/

	/**
	 * Do HTTP get. Set static class variables timeoutConnection and
	 * timeoutSocket for time out. Default values are 10s
	 * 
	 * @param url
	 *            - Parameters could be attached. It should be encoded with
	 *            proper encoding method. For instance,
	 *            http://www.example.com/?para1=%20abc&para2=abc
	 * @return An response object.
	 * 
	 *         To get response content: HttpEntity resEntity =
	 *         response.getEntity(); String result =
	 *         EntityUtils.toString(resEntity);
	 */
	public static HttpResponse executeHttpGet(String url) {
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpGet request = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			client = new DefaultHttpClient(httpParameters);
			request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			//LogUtils.e(TAG_TAG, "executeHttpGet: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
		}
	}

	/**
	 * Do HTTP get. Set static class variables timeoutConnection and
	 * timeoutSocket for time out. Default values are 10s
	 * 
	 * @param url
	 *            - Parameters could be attached. It should be encoded with
	 *            proper encoding method. For instance,
	 *            http://www.example.com/?para1=%20abc&para2=abc
	 * 
	 * @param clazz
	 *            - The JSONObject class.
	 * 
	 *            such as AddressJsonResponse.class
	 * 
	 * @return An response JSONObject.
	 */

	/*@SuppressWarnings("finally")
	public static <T> T executeHttpGet(String url, Class<T> clazz) {
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpGet request = null;
		HttpResponse response = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			client = getHttpClient();
			request = new HttpGet();
			request.setURI(new URI(url));
			response = client.execute(request);
			HttpEntity resEntity = response.getEntity();
			String resultJson = EntityUtils.toString(resEntity);
			//LogUtils.d(TAG_TAG, "executeHttpGet: result = " + resultJson);
			T t = JSON.parseObject(resultJson, clazz);
			
			return t;
		} catch (Exception e) {
			//LogUtils.e(TAG_TAG, "executeHttpGet: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
			response = null;
		}
	}*/

	public static HttpResponse executeHttpPut(String url,
			List<BasicNameValuePair> params) {
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpPut request = null;
		UrlEncodedFormEntity form = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			client = new DefaultHttpClient(httpParameters);
			request = new HttpPut();
			if (params != null) {
				form = new UrlEncodedFormEntity(params);
				form.setContentEncoding(HTTP.UTF_8);
				request.setEntity(form);
			}
			request.setURI(new URI(url));

			HttpResponse response = client.execute(request);
			return response;
		} catch (Exception e) {
			//LogUtils.e(TAG_TAG, "executeHttpPut: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
			form = null;
		}
	}

	public static HttpResponse executeHttpDelete(String url) {
		HttpParams httpParameters = null;
		HttpClient client = null;
		HttpDelete request = null;
		try {
			httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			client = new DefaultHttpClient(httpParameters);
			request = new HttpDelete();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			return response;
		} catch (Exception e) {
			//LogUtils.e(TAG_TAG, "executeHttpDelete: error=" + e.toString(), e);
			return null;
		} finally {
			httpParameters = null;
			client = null;
			request = null;
		}
	}

	private static HttpClient getHttpClient() {
		try {
			KeyStore trustStore;
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			// general setup
			SchemeRegistry supportedSchemes = new SchemeRegistry();

			// Register the "http" and "https" protocol schemes, they are
			// required by the default operator to look up socket factories.
			supportedSchemes.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			supportedSchemes.register(new Scheme("https", sf, 443));

			// prepare parameters
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");
			HttpProtocolParams.setUseExpectContinue(params, true);
			// ClientConnectionManager ccm = new
			// ThreadSafeClientConnManager(params,
			// supportedSchemes);
			ClientConnectionManager ccm = new SingleClientConnManager(params,
					supportedSchemes);

			DefaultHttpClient client = new DefaultHttpClient(ccm, params);

			client.getParams().setParameter(AllClientPNames.USER_AGENT,
					"0060e Android");
			client.getParams()
					.setIntParameter(AllClientPNames.CONNECTION_TIMEOUT,
							timeoutConnection /* milliseconds */);
			client.getParams().setIntParameter(AllClientPNames.SO_TIMEOUT,
					timeoutSocket /* milliseconds */);
			return client;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}

	}

	// ��ȡsign
	public static String createSign(SortedMap<String, String> packageParams,
			String key, String charsetName) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Encode(sb.toString(), charsetName).toUpperCase();
		return sign;

	}

	// MD5����
	public static String MD5Encode(String inStr, String charsetName) {
		try {
			MessageDigest md5 = null;

			md5 = MessageDigest.getInstance("MD5");

			byte[] byteArray = inStr.getBytes(charsetName);
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}

	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
