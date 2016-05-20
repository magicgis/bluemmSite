package cn.com.bluemoon.jeesite.modules.pay.service.alipay;


import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.bluemoon.jeesite.modules.pay.vo.AlipayTrade;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AlipayQueryService {

	private static Logger logger = LoggerFactory.getLogger(AlipayQueryService.class);
	
	/**
	 * 检查 out_trade_no 是否已经在支付宝支付
	 * 如果result_code == SUCCESS 表示已经在支付宝支付成功
	 * 否则在支付宝支付不成功
	 * 
	 * @param trade_no 支付宝交易订单（支付流水号）
	 * @param out_trade_no 商户订单号，就是 C开头的订单
	 * @return
	 * @throws DocumentException
	 */
	//@Bizlet("单笔订单查询接口")
	// 如果结果中result_code == SUCCESS , 则表示已经使用阿里支付了
	public HashMap<String, String> single_trade_query(String trade_no, String out_trade_no) {
		logger.info("支付宝支付查询：支付宝交易号:" + trade_no + ", 商户订单号: " + out_trade_no );
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "single_trade_query");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", "utf-8");
		sParaTemp.put("trade_no", trade_no);
		sParaTemp.put("out_trade_no", out_trade_no);
		String sHtmlText="";
		try {
			sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Document document = null;
		try {
			document = DocumentHelper.parseText(sHtmlText);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        Element root = document.getRootElement();
       // System.out.println("Root: " + root.getName());
        Element is_success=root.element("is_success");
        Element response=root.element("response");
        HashMap<String, String> result=new HashMap<String, String>();
        result.put("result_code", "FAIL");
        if(is_success.getData().equals("T")&&response!=null){
             Element tradeElement=response.element("trade");
             XStream xStream=new XStream(new DomDriver());
             xStream.alias("trade", AlipayTrade.class);
             AlipayTrade aliTrade=(AlipayTrade)xStream.fromXML(tradeElement.asXML());
             //System.out.println(aliTrade.toString());
             //System.out.println(aliTrade.toString());
             result.put("result_code", "SUCCESS");
             result.put("pay_platform","alipay");
             result.put("total_fee", aliTrade.getTotal_fee());
             result.put("out_trade_no", aliTrade.getOut_trade_no());
             result.put("transaction_id", aliTrade.getTrade_no());
             result.put("payed_time", aliTrade.getGmt_payment());
        }
        logger.info("支付宝支付查询：返回结果" + result.toString() );
        return result;
	}
}
