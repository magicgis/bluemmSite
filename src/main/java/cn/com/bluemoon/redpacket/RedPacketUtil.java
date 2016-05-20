/**
 * 
 */
package cn.com.bluemoon.redpacket;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.bluemoon.redpacket.util.RequestHandler;

import com.weixin.bm.redPacket.model.Configure;
import com.weixin.bm.redPacket.model.CreateRedPacket;
import com.weixin.bm.redPacket.model.RedPackInfo;
import com.weixin.bm.redPacket.model.RedPackRespone;
import com.weixin.bm.util.HttpsUtil;
import com.weixin.bm.util.XmlUtil;

/**
 * @author dakou
 * @date 2015-07-16 12:08:36
 *
 */
public class RedPacketUtil {
	
	
	public static HashMap getWeixinNoInfor(String mchid){
		HashMap<String, Object> resultMap=new HashMap<String, Object>();
		String appid="",key="",p12path="";
		if("122269960".equals(mchid)||"10021546".equals(mchid)){//月亮小屋
			appid="wx2b5f8f135210796d";
			key="441558a4e7d4098286ab6bcfe18e5ef8";
			p12path="/data/pay_certificate/10021546.p12";
		} else if("1286224401".equals(mchid)){//蓝月亮的世界
			appid="wx547eeee78eb998f9";
			key="w6t78ikl0oplyujnm6yhntg43eert56y";
			p12path="/data/pay_certificate/1286224401.p12";
		}else{
			resultMap.put("returncode","FAIL");
			return resultMap;
		}
		resultMap.put("appid", appid);
		resultMap.put("key", key);
		resultMap.put("p12path", p12path);
		resultMap.put("mchid", mchid);
		return resultMap;
	}
	
	
//	String datas = para.has("datas")?para.getString("datas"):""; //订单号
//	String p12path = para.has("p12path")?para.getString("p12path"):""; //證書地址
//	String appid = para.has("appid")?para.getString("appid"):""; //appid点
//	String key = para.has("key")?para.getString("key"):""; //key
//	String mchid=para.has("mchid")?para.getString("mchid"):""; //商戶號
//	List<String> datalist = (List<String>)JSONArray.toCollection(JSONArray.fromObject(datas), String.class);
	
 
	/**
	 * 红包查询
	 * @throws IOException 
	 */
	public HashMap<String, Object> searchRedPacket(String mch_billno,String mchid) throws IOException{
		RedPackInfo redPackInfo=null;
		HashMap<String, Object> resultMap=new HashMap<String, Object>();
		String appid="",key="",p12path="";
		if("122269960".equals(mchid)){//月亮小屋
			appid="wx2b5f8f135210796d";
			key="441558a4e7d4098286ab6bcfe18e5ef8";
			p12path="/data/pay_certificate/10021546.p12";
			//p12path="d:/10021546.p12";/data/pay_certificate/
			//p12path="/opt/moonMall/primeton/eos7_2/onlinepay/weixin/10021546.p12";
			//
		}else if("1286224401".equals(mchid)){//蓝月亮的世界
			appid="wx547eeee78eb998f9";
			key="w6t78ikl0oplyujnm6yhntg43eert56y";
			p12path="/data/pay_certificate/1286224401.p12";
			//p12path="d:/1286224401.p12";
			//p12path="/opt/moonMall/primeton/eos7_2/onlinepay/weixin/1286224401.p12";
			
		}else{
			resultMap.put("returncode","FAIL");
			return resultMap;
		}
		try {
			SortedMap<String, String> signParams = new TreeMap<String, String>();
			signParams.put("appid",appid);
			signParams.put("mch_id",mchid);
			signParams.put("nonce_str", getRandomStringByLength(32));
			signParams.put("mch_billno", mch_billno);//商户发放红包的商户订单号
			signParams.put("bill_type", "MCHT");
			String sign = getSign(null, null, signParams,appid,key);//获取签名
			signParams.put("sign", sign);
			RequestHandler reqHandler = new RequestHandler(null, null);
			String xml = reqHandler.parseXML(signParams);
			HttpsUtil httpsUtil = new HttpsUtil(mchid,p12path);
			//System.out.println(xml);
			String msg = httpsUtil.sendPost(Configure.REDPACK_GET, xml);//查询红包
			//System.out.println(msg);
			redPackInfo = XmlUtil.XmlToObject(msg, RedPackInfo.class);
			System.out.println(redPackInfo);
			String returncode=redPackInfo.getReturn_code();
			String resultcode=redPackInfo.getResult_code();
			if(returncode.equals("SUCCESS")&&resultcode.equals("SUCCESS")){
				resultMap.put("returncode","SUCCESS");
				resultMap.put("redPackInfo",redPackInfo);
			}else{
				resultMap.put("returncode","FAIL");
			}
			Thread.sleep(2*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 红包发送
	 * @throws IOException 
	 */
	public HashMap<String, String> sendRedPacket(CreateRedPacket createRedPacket,String appid,String mchid,String key,String p12path) throws IOException{
		RedPackRespone sendPacketResult=null;
		HashMap<String, String> resultMap=new HashMap<String, String>();
		try {
			SortedMap<String, String> signParams = getSignParams(createRedPacket,appid,mchid);
			String sign = getSign(null, null, signParams,appid,key);//获取签名
			signParams.put("sign", sign);
			RequestHandler reqHandler = new RequestHandler(null, null);
			String xml = reqHandler.parseXML(signParams);//将HashMap转化为xml
			HttpsUtil httpsUtil = new HttpsUtil(mchid,p12path);
			String msg = httpsUtil.sendPost(Configure.SEND_REDPACK_API, xml);//发红包
			sendPacketResult = XmlUtil.XmlToObject(msg, RedPackRespone.class);
			//System.out.println(msg);
			System.out.println(sendPacketResult.toString());
			String returncode=sendPacketResult.getReturn_code();
			String resultcode=sendPacketResult.getResult_code();
			if(returncode.equals("SUCCESS")&&resultcode.equals("SUCCESS")){
				resultMap.put("returncode","SUCCESS");
				resultMap.put("send_time", sendPacketResult.getSend_time());
				resultMap.put("send_listid", sendPacketResult.getSend_listid());
				resultMap.put("wxappid", sendPacketResult.getWxappid());//商户appid
			}else{
				resultMap.put("returncode","FAIL");
			}
			Thread.sleep(2*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 设置实体参数
	 * @param createRedPacket
	 * @return
	 */
	private CreateRedPacket set_Object(CreateRedPacket createRedPacket){
		createRedPacket.setWxappid(Configure.APPID);
		createRedPacket.setMch_id(Configure.MCHID);
		createRedPacket.setNonce_str(getRandomStringByLength(32));
		return createRedPacket;
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
	public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
	/*	CreateRedPacket createRedPacket=new CreateRedPacket();
		createRedPacket.setMch_billno("1222699601201507150000000000");
		createRedPacket.setNick_name("月亮小屋");
		createRedPacket.setSend_name("月亮小屋");
		createRedPacket.setRe_openid("ormn7t-_XWFX2_HH-C-pxT6VprhQ");
		createRedPacket.setTotal_amount(100);
		createRedPacket.setMin_value(100);
		createRedPacket.setMax_value(100);
		createRedPacket.setTotal_num(1);
		createRedPacket.setWishing("这是一条测试数据。");
		createRedPacket.setClient_ip("172.16.21.84");
		createRedPacket.setAct_name("测试数据");
		createRedPacket.setRemark("测试");
		new RedPacketUtil().sendRedPacket(createRedPacket);*/
		new RedPacketUtil().searchRedPacket("10021546201605110000020161","122269960");
    }
	
}
