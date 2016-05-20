package com.weixin.bm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.weixin.bm.redPacket.model.Configure;


/**
 * 
 * @author wangzhe
 *
 */
public class HttpsUtil {
	
	public interface ResultListener{
		public void onConnectionPoolTimeoutError();
	}
	
	// 表示请求器是否已经做了初始化工作
	private boolean hasInit = false;
	// 链接超时时间，默认10秒
	private int socketTimeout = 10000;
	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;
	// 请求器的配置
	private RequestConfig requestConfig;
	// HTTP请求器
	private CloseableHttpClient httpClient;
	//添加与2015-11-18 多商户同事发红包
	String mchid;
	String p12path;
	public  void closeClient(){
		if(this.httpClient!=null){
			try {
				this.httpClient.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	public HttpsUtil() throws Exception{
		//init();
	}
	public HttpsUtil(String mchidT,String p12pathT) throws Exception{
		//init();
		this.mchid=mchidT;
		this.p12path=p12pathT;
		init(mchidT, p12pathT);
	}
	public void init(String mchid,String p12path) throws KeyStoreException, IOException, KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException{
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		///FileInputStream instream = new FileInputStream(new File(Configure.SSLCERT_PATH));// 加载本地的证书进行https加密传输
		FileInputStream instream = new FileInputStream(new File(p12path));// 加载本地的证书进行https加密传输
		try {
			keyStore.load(instream,mchid.toCharArray());// 设置证书密码
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			instream.close();
		}
		
		// 信任自己的CA和自签名的证书
		SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore,mchid.toCharArray()).build();
		//
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		
		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		
		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
		
		hasInit = true;
	}
	
	
	public String sendPost(String url, String xml) throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IOException{
		if(!hasInit){
			//init();
			init(mchid, p12path);
		}
		String result = "";
		
		HttpPost httpPost = new HttpPost(url);
		
		// 解决XStream对出现双下划线的bug
		//XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		
		// 将要提交给API的数据对象转换成xml格式数据Post给API
		//String postDataXML = xStream.toXML(xmlObj);
		
		// 得致命使用UTF-8编码，否则到API服务器的XML中文不能被成功识别
		StringEntity postEntity = new StringEntity(xml, "UTF-8");
		postEntity.setContentType("application/x-www-form-urlencoded");
		
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);
		
		// 设置请求器的配置
		//httpPost.setConfig(requestConfig);
		
	//	Util.log("executing request: " + httpPost.getRequestLine());
		
		try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ConnectionPoolTimeoutException e) {

        } catch (ConnectTimeoutException e) {

        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            httpPost.abort();
        }
        return result;
	}
	
	/**
     * 设置连接超时时间
     *
     * @param socketTimeout 连接时长，默认10秒
     */
    public void setSocketTimeout(int socketTimeout) {
        socketTimeout = socketTimeout;
        resetRequestConfig();
    }

    /**
     * 设置传输超时时间
     *
     * @param connectTimeout 传输时长，默认30秒
     */
    public void setConnectTimeout(int connectTimeout) {
        connectTimeout = connectTimeout;
        resetRequestConfig();
    }

    private void resetRequestConfig(){
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
    }

    /**
     * 允许商户自己做更高级更复杂的请求器配置
     *
     * @param requestConfig 设置HttpsRequest的请求器配置
     */
    public void setRequestConfig(RequestConfig requestConfig) {
        requestConfig = requestConfig;
    }
}
