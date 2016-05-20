package cn.com.bluemoon.redpacket.config;

public abstract class ConfigInterface {
	public String APPID;// 受理商ID，身份标识
	public String MCHID;// 商户支付密钥key，审核通过后，在微信发送的邮件中查看
	public String KEY;	// JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
	public String APPSECRET;
	public String SSLCERT_PATH; 
	public ConfigInterface() {
	}
	public ConfigInterface(String aPPID, String mCHID, String kEY,String aPPSECRET,String sSLCERT_PATH) {
		APPID = aPPID;
		MCHID = mCHID;
		KEY = kEY;
		APPSECRET = aPPSECRET;
		SSLCERT_PATH=sSLCERT_PATH;
	}
}
