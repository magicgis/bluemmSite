package com.weixin.bm.redPacket.model;

/**
 * 这里放置各种配置数据
 * @author wangzhe
 *
 */
public class Configure {

	// sdk 版本号
	private static final String sdkVersion = "java sdk 1.0.1";
	
	// =======【基本信息设置】=======
	// 微信公众号身份的唯一标识。审核通过后，在微信发送的邮件中查看
	public static String APPID = "wx2b5f8f135210796d";
	// 受理商ID，身份标识
	public static String MCHID = "10021546";
	// 商户支付密钥key，审核通过后，在微信发送的邮件中查看
	public static String KEY = "441558a4e7d4098286ab6bcfe18e5ef8";
	// JSAPI接口中获取openid，审核后在公众平台开启开发模式后可查看
	public static String APPSECRET = "00983328f72e40ab5799848414938305";
	
	// =======【证书路径设置】=====================================
	// 证书路径,注意应该填写绝对路径
	public static String SSLCERT_PATH = "D:/apiclient_cert.p12";
	//public static String SSLCERT_PATH = "/usr/onlinepay/wxpay/apiclient_cert.p12"; // 测试机-生产机
	public static String SSLKEY_PATH = "http://webchat.tunnel.mobi/weixin_bm/WxPayPubHelper/cacert/apiclient_key.pem";

	// =======【API路径设置】=====================================
	//1）被扫支付API
	public static String PAY_API = "https://api.mch.weixin.qq.com/pay/micropay";
	//2）被扫支付查询API
	public static String PAY_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
	//3）退款API
	public static String REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//4）退款查询API
	public static String REFUND_QUERY_API = "https://api.mch.weixin.qq.com/pay/refundquery";
	//5）撤销API
	public static String REVERSE_API = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
	//6）下载对账单API
	public static String DOWNLOAD_BILL_API = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//7) 统计上报API
	public static String REPORT_API = "https://api.mch.weixin.qq.com/payitil/report";
	//8) 红包发送API
	public static String SEND_REDPACK_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	//9) 红包查询API
	public static String REDPACK_GET = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
}
