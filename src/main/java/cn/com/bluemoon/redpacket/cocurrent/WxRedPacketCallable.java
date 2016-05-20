package cn.com.bluemoon.redpacket.cocurrent;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.weixin.bm.redPacket.model.Configure;
import com.weixin.bm.redPacket.model.RedPackRespone;
import com.weixin.bm.util.HttpsUtil;
import com.weixin.bm.util.XmlUtil;

public class WxRedPacketCallable implements Callable<HashMap<String, String>>{
	private HttpsUtil httpsUtils;
	private String xml;
	public WxRedPacketCallable(){
		
	}
	public WxRedPacketCallable(HttpsUtil httpsUtils,String xml){
		this.httpsUtils=httpsUtils;
		this.xml=xml;
	}
	public HashMap<String, String> call() throws KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IOException, InterruptedException {
		HashMap<String, String> resultMap=new HashMap<String, String>();
		//Thread.sleep(10*1000);
		String msg = httpsUtils.sendPost(Configure.SEND_REDPACK_API, xml);//发红包
		RedPackRespone sendPacketResult = XmlUtil.XmlToObject(msg, RedPackRespone.class);
		System.out.println("红包发放结果:"+sendPacketResult.toString());
		String returncode=sendPacketResult.getReturn_code();
		String resultcode=sendPacketResult.getResult_code();
		if(returncode.equals("SUCCESS")&&resultcode.equals("SUCCESS")){
			resultMap.put("returncode","SUCCESS");//是否成功
			resultMap.put("send_time", sendPacketResult.getSend_time());//发放时间
			resultMap.put("send_listid", sendPacketResult.getSend_listid());//流水号
			resultMap.put("mch_billno", sendPacketResult.getMch_billno());//商户订单号
		}else{
			resultMap.put("returncode","FAIL");
			resultMap.put("mch_billno", sendPacketResult.getMch_billno());//商户订单号
		}
		return resultMap;
	}
	
}
