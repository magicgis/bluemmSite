package cn.com.bluemoon.jeesite.modules.pay.service.wxpay;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.DocumentException;

import cn.com.bluemoon.jeesite.modules.pay.dao.PayRecordPO;
import cn.com.bluemoon.jeesite.modules.pay.service.alipay.AlipayQueryService;
import cn.com.bluemoon.jeesite.modules.pay.util.XmlUtil;

//
public class WeixinQueryService {
	//("单个查询")
	public List<HashMap<String, String>> queryOrderFromWXAndAlipy(String transaction_id,String out_trade_no) {
		String[]out_trade_nos=null;
		List<HashMap<String, String>> results=new ArrayList<HashMap<String,String>>();
		if(out_trade_no!=null){
			out_trade_nos=out_trade_no.split(",");
		}
		for(String o:out_trade_nos){
			HashMap<String, String>result1=orderquery(transaction_id,o,"false");
			if(result1.get("result_code").equals("SUCCESS")){
				if(result1.get("transaction_id")!=null&&result1.get("payed_time")!=null){
					HashMap<String, String>  resultHashMap =new HashMap<String, String>();
					resultHashMap.put("pay_platform", "微信支付");
					resultHashMap.put("trade_date",result1.get("payed_time"));
					resultHashMap.put("trade_no", result1.get("transaction_id"));
					resultHashMap.put("out_trade_no", o);
					resultHashMap.put("result_code","SUCCESS");
					results.add(resultHashMap);
				}
			}
			HashMap<String, String>result2=orderquery(transaction_id,o,"true");
			if(result2.get("result_code").equals("SUCCESS")){
				if(result2.get("transaction_id")!=null&&result2.get("payed_time")!=null){
					HashMap<String, String>  resultHashMap =new HashMap<String, String>();
					resultHashMap.put("pay_platform", "微信支付");
					resultHashMap.put("trade_date",result2.get("payed_time"));
					resultHashMap.put("trade_no", result2.get("transaction_id"));
					resultHashMap.put("out_trade_no", o);
					resultHashMap.put("result_code","SUCCESS");
					results.add(resultHashMap);
				}
			}

			AlipayQueryService aliQuery = new AlipayQueryService();
			HashMap<String, String> reulst3 = aliQuery.single_trade_query(transaction_id, o);
			if(reulst3.get("result_code").equals("SUCCESS")){
				HashMap<String, String>  resultHashMap =new HashMap<String, String>();
				resultHashMap.put("pay_platform", "支付宝支付");
				resultHashMap.put("trade_date",reulst3.get("payed_time"));
				resultHashMap.put("trade_no", reulst3.get("transaction_id"));
				resultHashMap.put("out_trade_no", o);
				resultHashMap.put("result_code","SUCCESS");
				results.add(resultHashMap);
			}
			if(!(reulst3.get("result_code").equals("SUCCESS")||result2.get("result_code").equals("SUCCESS")||result1.get("result_code").equals("SUCCESS"))){
				HashMap<String, String>  resultHashMap =new HashMap<String, String>();
				resultHashMap.put("pay_platform", "该订单未支付");
				resultHashMap.put("trade_date","");
				resultHashMap.put("trade_no","");
				resultHashMap.put("out_trade_no", o);
				resultHashMap.put("result_code","FAIL");
				results.add(resultHashMap);
			}
		}
		
		return results;
	}
	//("刷数据")
	public void flushDataFromWXPAY(int startday ,int endday){
		System.out.println("[支付信息同步][微信支付]同步开始");
		List<PayRecordPO> records=new ArrayList<PayRecordPO>();
		for(;startday<=endday;startday++){
			records.addAll(downloadBill_open(String.valueOf(startday),"true"));
			records.addAll(downloadBill_open(String.valueOf(startday),"false"));
		}
		try {
			//new PayRecordsDAO().savePayRecords(records);
			System.out.println("[支付信息同步][微信支付]同步正常结束,同步数量:"+records.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[支付信息同步][微信支付]同步异常");
		}
	}
	//("对账单下载接口_开放平台_公共平台")
	public List<PayRecordPO> downloadBill_open(String date,String isMP){
		SortedMap<String, String> requestPacket = new TreeMap<String, String>();
		requestPacket.put("appid",Config.AppID);
		requestPacket.put("mch_id",Config.MCH_ID);
		boolean isMPs=false;
		if(isMP!=null&&isMP.equals("true")){
			requestPacket.put("appid","wx2b5f8f135210796d");
			requestPacket.put("mch_id","10021546");
			isMPs=true;
		}
		requestPacket.put("nonce_str",getRandomStringByLength(32));
		requestPacket.put("bill_date",date);
		requestPacket.put("bill_type","ALL");
		requestPacket.put("sign",RequestUtil.createSign(requestPacket,"UTF-8",isMPs));
		String requestXMLStr = XmlUtil.simpleMapToXMLMessage(requestPacket);
		HttpsUtil ss=new HttpsUtil();
		String respondxml=ss.sendPost(Config.downloadBill_API,requestXMLStr);
		List<PayRecordPO> records=new ArrayList<PayRecordPO>();
		try{
			if(!respondxml.contains("return_code")){
				List<String> resultList=new ArrayList<String>();
				BufferedReader reader=new BufferedReader(new StringReader(respondxml));
				String str=null;
				while((str=reader.readLine())!=null){
					resultList.add(str);
				 }
				int lineNum=resultList.size();
				for(int i=0;i<lineNum;i++){
					if(i==0||i==lineNum-2||i==lineNum-1){//跳过第一行 倒数第二行 倒数第一行
						continue;
					}
					records.add(parseTradeStr2PayRecordPO(resultList.get(i)));
				}
				//new PayRecordsDAO().savePayRecords(records);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return records;
	}
	private static PayRecordPO parseTradeStr2PayRecordPO(String trade){
		PayRecordPO po=null;
		if(trade==null){
			return po;
		}
		/*
		 * 交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,0-6
		 * 用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,7-13
		 * 微信退款单号,商户退款单号,退款金额,企业红包退款金额,退款类型14-18
		 * ,退款状态,商品名称,商户数据包,手续费,费率19-23
			`2015-09-30 19:57:02,`wxe60f77c2d0e17028,
			`1262141101,`0,`,`1000160406201509301046941631,
			`C201509301956007098,`oje1suByYxGWBw18B6pixkl1Gs_8
			,`APP,`SUCCESS,`CFT,`CNY,`10.80,`0.00,`0,`0,`0,`0,`,
			`,`C201509301956007098,`,`0.06000,`0.60
		 */
		String[] infos=trade.split(",");
		po=new PayRecordPO();
		po.setMerchant_out_order_no(infos[6].replace("`", ""));
		po.setPay_platform("wxpay");
		po.setTrade_no(infos[5].replace("`", ""));
		po.setTrade_date(infos[0].replace("`", ""));
		String status=infos[9].replace("`", "");
		po.setTrade_status(status);
		if(status.equals("REFUND")){
			po.setTotal_fee(infos[16].replace("`", ""));
		}else{
			po.setTotal_fee(infos[12].replace("`", ""));
		}
		po.setSeller_id(infos[2].replace("`", ""));
		po.setBuyer_id(infos[7].replace("`", ""));
		return po;
	}
	//("订单查询接口")
	public HashMap<String, String> orderquery(String transaction_id,String out_trade_no,String isMP){
		HashMap<String, String> result=new HashMap<String, String>();
		SortedMap<String, String> requestPacket = new TreeMap<String, String>();
		requestPacket.put("appid",Config.AppID);
		requestPacket.put("mch_id",Config.MCH_ID);
		boolean isMPs=false;
		if(isMP!=null&&isMP.equals("true")){
			requestPacket.put("appid","wx2b5f8f135210796d");
			requestPacket.put("mch_id","10021546");
			isMPs=true;
		}
		requestPacket.put("nonce_str",getRandomStringByLength(32));
		requestPacket.put("transaction_id",transaction_id);
		requestPacket.put("out_trade_no",out_trade_no);
		requestPacket.put("sign",RequestUtil.createSign(requestPacket,"UTF-8",isMPs));
		String requestXMLStr = XmlUtil.simpleMapToXMLMessage(requestPacket);
		HttpsUtil ss=new HttpsUtil();
		String respondxml=ss.sendPost(Config.OrderQuery_API,requestXMLStr);
		//System.out.println(respondxml);
		UnifiedOrderRespond respond = XmlUtil.XmlToObject(respondxml, UnifiedOrderRespond.class);
		String result_code=respond.getResult_code();
		if(null!=result_code && result_code.equals("SUCCESS")){
			result.put("result_code", "SUCCESS");
			result.put("return_code", "SUCCESS");
			result.put("appid",respond.getAppid());
			result.put("mch_id",respond.getMch_id());
			result.put("nonce_str",respond.getNonce_str());
			result.put("transaction_id",respond.getTransaction_id());
			result.put("out_trade_no",respond.getOut_trade_no());
			result.put("pay_platform","wxpay");
			result.put("payed_time",respond.getTime_end());
			result.put("response_message","");//respondxml
		}else{
			result.put("result_code", "FAIL");
			result.put("return_code", "FAIL");
			result.put("return_msg", respond.getReturn_msg());
			result.put("error_code", respond.getErr_code());
			result.put("error_desc", respond.getErr_code_des());
			
		}
		return result;
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
