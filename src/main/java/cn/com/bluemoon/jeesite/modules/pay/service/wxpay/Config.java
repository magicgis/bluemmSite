package cn.com.bluemoon.jeesite.modules.pay.service.wxpay;

import cn.com.bluemoon.jeesite.modules.pay.util.HttpProperties;


public class Config {
	/**	*/
	// 商户号
	public static final String MCH_ID = "1262141101";
	// API密钥，在商户平台设置
	public static final String AppSecret = "f460032df4259add790824d3a3307f6b";
	
	//月亮小屋的商务号
	// appid
	public static final String AppID = "wxe60f77c2d0e17028";
	// 商户支付密钥key，审核通过后，在微信发送的邮件中查看
	public static String KEY = "flaj32kanflnz8falmzq23mgbsiefae6";
	
	//1)统一订单的URL
	public static final String UnifiedOrder_API="https://api.mch.weixin.qq.com/pay/unifiedorder";
	//public static final String UnifiedOrder_API="http://192.168.236.2/wxPay/";
	//2)查询订单
	public static final String OrderQuery_API="https://api.mch.weixin.qq.com/pay/orderquery";
	//3)下载对账单
	public static final String downloadBill_API="https://api.mch.weixin.qq.com/pay/downloadbill";

//	request.setAppid("wx2b5f8f135210796d");
//	request.setMch_id("10021546");
//	public static final String isMP_treu_Key="441558a4e7d4098286ab6bcfe18e5ef8";

//	//正试公众号isMP_true
//	public static final String GZH_Key="441558a4e7d4098286ab6bcfe18e5ef8";
//	public static final String GZH_Appid = "wx2b5f8f135210796d";
//	public static final String GZH_Mch_id = "10021546";
 
	public static final String GZH_Key= HttpProperties.getVal("GZH_Key");
	public static final String GZH_Appid = HttpProperties.getVal("GZH_Appid");
	public static final String GZH_Mch_id = HttpProperties.getVal("GZH_Mch_id");
	public static final String GZH_APPSECRET = HttpProperties.getVal("GZH_APPSECRET");
}
