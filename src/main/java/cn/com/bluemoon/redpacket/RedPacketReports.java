package cn.com.bluemoon.redpacket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import cn.com.bluemoon.redpacket.util.RequestHandler;

import com.weixin.bm.redPacket.model.Configure;
import com.weixin.bm.redPacket.model.CreateRedPacket;
import com.weixin.bm.redPacket.model.RedPackInfo;
import com.weixin.bm.util.HttpsUtil;
import com.weixin.bm.util.XmlUtil;

public class RedPacketReports {
	public static void main(String[] args) {
		new RedPacketReports().searchRedPacket("1222699601201512050000000022","10021546","wx2b5f8f135210796d","441558a4e7d4098286ab6bcfe18e5ef8","d:\\10021546.p12",null);
	}
	/**
	 * 红包查询
	 * @throws IOException 
	 */
	public HashMap<String, Object> searchRedPacket(String mch_billno,String mchid,String appid,String key,String p12path,String urlString){
		RedPackInfo redPackInfo=null;
		HashMap<String, Object> resultMap=new HashMap<String, Object>();
		HttpsUtil httpsUtil=null;
		String responseContent = null; // 响应内容
		CloseableHttpResponse response = null;
		CloseableHttpClient client=null;
		try {
			SortedMap<String, String> signParams = new TreeMap<String, String>();
			signParams.put("appid",appid);
			signParams.put("mch_id",mchid);
			signParams.put("nonce_str",getRandomStringByLength(32));
			signParams.put("mch_billno", mch_billno);//商户发放红包的商户订单号
			signParams.put("bill_type", "MCHT");
			String sign = getSign(null, null, signParams,appid,key);//获取签名
			signParams.put("sign", sign);
			RequestHandler reqHandler = new RequestHandler(null, null);
			String xml = reqHandler.parseXML(signParams);
			//String urlString="http://tmallapi.bluemoon.com.cn/moonMall-proxy/redpacket/search?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
			//String urlString="http://127.0.0.1:8080/moonMall-proxy/redpacket/search?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
			client = HttpClients.createDefault();
			HttpPost post = new HttpPost(urlString);
			JSONObject para=new JSONObject();
			para.put("appid", appid); 
			para.put("mchid", mchid);
			para.put("key", key);
			para.put("p12path", p12path);
			para.put("data", xml);
			//System.out.println(para.toString());
			StringEntity myEntity = new StringEntity(para.toString(), ContentType.APPLICATION_JSON);// 构造请求数据
			post.setEntity(myEntity);// 设置请求体
				response = client.execute(post);
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				//System.err.println(responseContent);
				if (response.getStatusLine().getStatusCode() == 200) {
					JSONObject responseObj=JSONObject.fromObject(responseContent);
					String returncode=responseObj.has("returncode")?responseObj.getString("returncode"):"FAIL";
					if("SUCCESS".equals(returncode)){
						redPackInfo = XmlUtil.XmlToObject(responseObj.getString("info"), RedPackInfo.class);
						//System.out.println(redPackInfo);
						String returncode1=redPackInfo.getReturn_code();
						String resultcode=redPackInfo.getResult_code();
						if(returncode1.equals("SUCCESS")&&resultcode.equals("SUCCESS")){
							resultMap.put("returncode","SUCCESS");
							resultMap.put("redPackInfo",redPackInfo);
						}else{
							resultMap.put("returncode","FAIL");
						}
					}
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
			System.out.println("[红包@See RedPacketReports]红包查询结束，结果:"+resultMap.get("returncode"));
			return resultMap;
	}
	/**
	 * 设置红包签名参数
	 * @return
	 */
	private SortedMap<String, String> getSignParams(CreateRedPacket createRedPacket,String appid,String mchid){
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		/*signParams.put("wxappid", Configure.APPID);
		signParams.put("mch_id", Configure.MCHID);*/
		signParams.put("wxappid",appid);
		signParams.put("mch_id",mchid);
		signParams.put("nonce_str", getRandomStringByLength(32));
		signParams.put("mch_billno", createRedPacket.getMch_billno());
		signParams.put("nick_name", createRedPacket.getNick_name());
		signParams.put("send_name", createRedPacket.getSend_name());
		signParams.put("re_openid", createRedPacket.getRe_openid());
		signParams.put("total_amount", String.valueOf(createRedPacket.getTotal_amount()));
		signParams.put("min_value", String.valueOf(createRedPacket.getMin_value()));
		signParams.put("max_value", String.valueOf(createRedPacket.getMax_value()));
		signParams.put("total_num", String.valueOf(createRedPacket.getTotal_num()));
		signParams.put("wishing", createRedPacket.getWishing());
		signParams.put("client_ip", createRedPacket.getClient_ip());
		signParams.put("act_name", createRedPacket.getAct_name());
		signParams.put("remark", createRedPacket.getRemark());
		
		return signParams;
	}
	
	
	/**
	 * 获取sign字符串
	 * 
	 * @return
	 * @throws IOException 
	 */
	public String getSign(HttpServletRequest request, HttpServletResponse response, SortedMap<String, String> signParams,String appid,String key) {
		RequestHandler reqHandler = new RequestHandler(request, response);
		//reqHandler.init(Configure.APPID, Configure.APPSECRET, Configure.KEY);
		reqHandler.init(appid, null,key);
		String sign = reqHandler.createSign(signParams);
		return sign;
	}
	/**
	 * 取一定长度的随机字符串
	 */
	public static String getRandomStringByLength(int length){
		String base = "abcdefghigklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
