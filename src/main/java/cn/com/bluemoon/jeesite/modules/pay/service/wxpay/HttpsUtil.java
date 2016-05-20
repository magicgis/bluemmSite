package cn.com.bluemoon.jeesite.modules.pay.service.wxpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import  org.apache.commons.lang.exception.NestableRuntimeException;
import net.sf.ezmorph.Morpher;
/**
 * @author kouzhiqiang
 *
 */
public class HttpsUtil {

	private static Logger logger = (Logger) LoggerFactory
			.getLogger(HttpsUtil.class);

	public interface ResultListener{
		public void onConnectionPoolTimeoutError();
	}
	// 链接超时时间，默认10秒
	private int socketTimeout = 10000;
	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;
	// 请求器的配置
	private RequestConfig requestConfig;
	// HTTP请求器
	private CloseableHttpClient httpClient;
	
	public HttpsUtil() {
			httpClient=HttpClients.createDefault();
	}

	public Map<String, Object> sendPost(String url, Map<String, String> params) {
		Map<String, Object> reuslt = new HashMap<String, Object>();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		logger.info("请求："+url );
		HttpPost method = new HttpPost(url);
		JSONObject json = new JSONObject();
		// // 设置代理
		// if (IS_NEED_PROXY.equals("1")) {
		// HttpHost proxy = new HttpHost("192.168.13.19", 7777);
		// httpClient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
		// proxy);
		// }
		reuslt.put("responseMsg", "请求失败");
		reuslt.put("responseCode", -1);
		reuslt.put("isSuccess", Boolean.FALSE);

		if (params == null && params.size() <1 ) {
			reuslt.put("responseMsg", "参数错误");
			reuslt.put("responseCode", -1);
			reuslt.put("isSuccess", Boolean.FALSE);
			return reuslt;
		}
		logger.info("数据列表大小params.size={}", params != null ? params.size() : 0);
		// 开始循环组装post请求参数,使用倒序进行处理
		// 接收参数json列表
		JSONObject jsonParam = new JSONObject();
		jsonParam.putAll(params);
		logger.info("数据列表：", jsonParam.toString());

//			StringEntity postEntity = new StringEntity(xml, "UTF-8");
//			postEntity.setContentType("application/x-www-form-urlencoded");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		method.setEntity(entity);
		// 这边使用适用正常的表单提交

		HttpResponse reusltOjb = null;
		try {
			reusltOjb = httpClient.execute(method);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			reuslt.put("responseMsg", "网络异常");
			reuslt.put("responseCode", 100);
			reuslt.put("isSuccess", Boolean.FALSE);
			return reuslt;
		} catch (IOException e) {
			e.printStackTrace();
			reuslt.put("responseMsg", "网络异常");
			reuslt.put("responseCode", 100);
			reuslt.put("isSuccess", Boolean.FALSE);
			return reuslt;
		}
		if(params.containsKey("rtl")){
			reuslt.put("responseMsg", "请求成功");
			reuslt.put("responseCode", 0);
			reuslt.put("isSuccess", Boolean.TRUE);
			return reuslt;
		}
		if(null!=reusltOjb &&
				null!=reusltOjb.getStatusLine() &&
				reusltOjb.getStatusLine().getStatusCode()==200){
			logger.info("请求成功......");
		} else {
			reuslt.put("responseMsg", "网络异常");
			reuslt.put("responseCode", 100);
			reuslt.put("isSuccess", Boolean.FALSE);
			return reuslt;
		}
		JSONObject resJson = JSONObject.fromObject(reusltOjb.getEntity());
		logger.info("请求返回结果集"+resJson.toString());
		// 得到httpResponse的实体数据
        HttpEntity httpEntity = reusltOjb.getEntity();
        if (httpEntity != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(httpEntity.getContent(),"UTF-8"), 8 * 1024);
                StringBuilder rlt = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                	rlt.append(line + "/n");
                }
                logger.info(rlt.toString());
                if(rlt.length()>0){
                    if(rlt.length()>=2) rlt.delete(rlt.length()-2, rlt.length());
                    logger.info(rlt.toString());
                    JSONObject resJson2 = JSONObject.fromObject(rlt.toString());
                    reuslt.putAll(resJson2);
                    logger.info("resJson2"+resJson2.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//		reuslt.put("responseMsg", "请求成功");
//		reuslt.put("responseCode", 0);
//		reuslt.put("isSuccess", Boolean.TRUE);
		return reuslt;
	}


	
	public String sendPost(String url, String xml) {
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
        System.out.println();
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
