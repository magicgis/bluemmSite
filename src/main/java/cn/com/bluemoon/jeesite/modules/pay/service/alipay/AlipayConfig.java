package cn.com.bluemoon.jeesite.modules.pay.service.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088021154716593";
	
	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串
	public static String seller_id = partner;
	// 商户的私钥
	public static String private_key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANbjbzSDjyQMrK/uCO+OpH2GU2Oug8CQGp1ZwwsbFUOGboRq4vRA9XdPcxoFonHR9oZXfFw/+slrdLolNXjzOu1G9WCtM5v2iyB/AcrwUD0PbyJN8lChWUYeHnj6JDRHLSAp5Ox5uhKsAmSlrMgONuRk61+3JGIxntuEet3iPKJtAgMBAAECgYEAnJirPpqs4nl2EtMloeXSS0NrW5Yen2KkcoyqTVqUOVwrB8aY06TO/P5iA9GdWtLGk4oHr3ZUfRoYu1pISHcANfmCygHepuUYXTbS8I0tT74hgAXMCKTfMmzHvJmHF2qownlicXV2xx7DzD2O/s6k8LWUWMcTYpAplCoNcnKOuIUCQQD39NEDj6TmWISvIJfE0Cea0R1QePI368KGFpKiSmuLGyYQDIGgXKNb2tRXVnqaDwsT7LT3U9Yq2NULxVJLY7pXAkEA3dwAY/WIgRKGoffmBO8gicR6etK6IwmzBX3YN64gDezz+TJR/R81vxtSJC1x3ilHNvhyewJ4CQCs40BiOWVW2wJBAKSBMAqEyopoP99QaGCTGTOFeUVieJDRQYVkhHRXm3H22RHcukOCMn9Gd4paPYzoXhkh+g38+SIEKZRdnXOW/pECQQC/euivXTQCSm3+jslsysInhXc8WHGkWfOzsqiRbexmeHMdSavvriciGL9tT/euCaxYKlgMCNZ21qlgvanI5cjvAkBbISNFvQvKVH66bZ2XrFzpVQd/Otc58hbo635omxcRpJIWuE9nwDKtGuBrD2VDEfA8eKwa7NAneWP6gRvZkkrx";
	
	// 支付宝的公钥，无需修改该值
	//public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
