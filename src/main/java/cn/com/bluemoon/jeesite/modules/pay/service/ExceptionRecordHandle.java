package cn.com.bluemoon.jeesite.modules.pay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.bluemoon.jeesite.modules.pay.model.PayExceptionInfo;

public class ExceptionRecordHandle {
	/**
	 * @param orders  mall_app_orderdetail
	 * @param zhifujuans mall_app_user_prepaid
	 * @param payrecord		mall_app_order_pay_records
	 * @return
	 */
	
	public List<PayExceptionInfo> handleRecord(Object[] orders,Object[] zhifujuans,Object[] payrecord){
		HashMap<String, PayExceptionInfo> ordersHashMap=parse2Into(orders,"order_code");
		HashMap<String, PayExceptionInfo> zhifujuansHashMap=parse2Into(zhifujuans,"order_code");
		HashMap<String, PayExceptionInfo> payrecordHashMap=parse2Into(payrecord,"pay_out_order_no");
		List<PayExceptionInfo> result=getResult(ordersHashMap,zhifujuansHashMap,payrecordHashMap);
		return result;
	}
	
	private List<PayExceptionInfo> getResult(HashMap<String, PayExceptionInfo> ordersHashMap,
			HashMap<String, PayExceptionInfo> zhifujuansHashMap,
			HashMap<String, PayExceptionInfo> payrecordHashMap) {
		List<PayExceptionInfo> result=new ArrayList<PayExceptionInfo>();//最后的结果
		//处理普通订单开始       遍历订单记录，查找对应的支付记录，如果没有找到，属于异常订单，不然就是正常的，合并信息
		Iterator ordersIterator=ordersHashMap.entrySet().iterator();
		while(ordersIterator.hasNext()){
			Map.Entry<String, PayExceptionInfo> order=(Map.Entry<String, PayExceptionInfo>)ordersIterator.next();
			String key=order.getKey();
			PayExceptionInfo value=order.getValue();
			value.setIs_visited(true);//处理过
			PayExceptionInfo payRecord=payrecordHashMap.get(key);
			if(payRecord!=null){//表示找到支付信息，不是异常
				value.setIs_abnormal(false);
				value.setPay_out_order_no(payRecord.getPay_out_order_no());
				value.setPay_platform_from_payrecord(payRecord.getPay_platform_from_payrecord());
				value.setPay_status(payRecord.getPay_status());
				value.setPay_trade_date(payRecord.getPay_trade_date());
				value.setPay_total_fee(payRecord.getPay_total_fee());
				value.setPay_trade_no(payRecord.getPay_trade_no());
				//=========操作 符合的支付记录 is_visited=true is_abnormal=false
				payRecord.setIs_abnormal(false);
				payRecord.setIs_visited(true);
			}else{
				//如果该订单没有找到,表示异常
				value.setIs_abnormal(true);
			}
			result.add(value);
		}
		//处理支付卷开始  遍历订单记录，查找对应的支付记录，如果没有找到，属于异常订单，不然就是正常的，合并信息
		Iterator zhifujuanIterator = zhifujuansHashMap.entrySet().iterator();
		while (zhifujuanIterator.hasNext()) {
			Map.Entry<String, PayExceptionInfo> zhifujuan = (Map.Entry<String, PayExceptionInfo>) zhifujuanIterator.next();
			String key = zhifujuan.getKey();
			PayExceptionInfo value = zhifujuan.getValue();
			value.setIs_visited(true);// 处理过
			PayExceptionInfo payRecord = payrecordHashMap.get(key);
			if (payRecord != null) {// 表示找到支付信息
				value.setIs_abnormal(false);
				value.setPay_out_order_no(payRecord.getPay_out_order_no());
				value.setPay_platform_from_payrecord(payRecord.getPay_platform_from_payrecord());
				value.setPay_status(payRecord.getPay_status());
				value.setPay_trade_date(payRecord.getPay_trade_date());
				value.setPay_total_fee(payRecord.getPay_total_fee());
				value.setPay_trade_no(payRecord.getPay_trade_no());
				payRecord.setIs_abnormal(false);
				payRecord.setIs_visited(true);
			} else {
				value.setIs_abnormal(true);
			}
			result.add(value);
		}
		// 处理处理最后的支付信息，遍历支付记录，如果is_visited=null 表示未找到对应的订单信息，属于异常订单，添加到结果中，否则，已经遍历了，直接跳过======
		Iterator payRecordIterator = payrecordHashMap.entrySet().iterator();
		while (payRecordIterator.hasNext()) {
			Map.Entry<String, PayExceptionInfo> payrecord = (Map.Entry<String, PayExceptionInfo>) payRecordIterator.next();
			PayExceptionInfo value = payrecord.getValue();
			Boolean is_visited=value.getIs_visited();
			Boolean is_adnormal=value.getIs_abnormal();
			if (is_visited==null||is_adnormal==null) {// 表示未找到对应的订单信,表示异常
				value.setIs_visited(true);
				value.setIs_abnormal(true);
				result.add(value);
			}else{
				continue;
			}
			
		}
		return result;
	}

	/**
	 * 构造HashMap,结构是 {订单号：其他信息}
	 * @param rows
	 * @param key
	 * @return
	 */
	private HashMap<String, PayExceptionInfo> parse2Into(Object[] rows,String key) {
		HashMap<String, PayExceptionInfo> resultHashMap=new HashMap<String, PayExceptionInfo>();
		for(Object row:rows){
			PayExceptionInfo info=(PayExceptionInfo)row;
			if("order_code".equals(key)){//mall_app_orderdetail||mall_app_user_prepaid中的记录
				resultHashMap.put(info.getOrder_code(), info);
			}
			if("pay_out_order_no".equals(key)){//pay_record中的记录
				if("REFUND".equals(info.getPay_status())){//退款的不参与合并
					continue;
				}
				resultHashMap.put(info.getPay_out_order_no(), info);
			}
		}
		return resultHashMap;
	}
}
