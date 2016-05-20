/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.DateUtil;
import cn.com.bluemoon.jeesite.common.utils.StringUtil;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayPaymentInfo;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayRefund;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPayPaymentInfoService;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPayRefundService;
import cn.com.bluemoon.jeesite.modules.pay.service.wxpay.HttpsUtil;
import cn.com.bluemoon.jeesite.modules.pay.util.HttpProperties;

/**
 * 第三方支付退款记录Controller
 * @author liao
 * @version 2016-03-09
 */
@Controller
@RequestMapping(value = "${adminPath}/pay/mallPayRefund")
public class MallPayRefundController extends BaseController {

	private static Logger log = (Logger) LoggerFactory.getLogger(MallPayRefundController.class);

	@Autowired
	private MallPayRefundService mallPayRefundService;
	@Autowired
	private MallPayPaymentInfoService mallPayPaymentInfoService;
	
	@ModelAttribute
	public MallPayRefund get(@RequestParam(required=false) String id) {
		MallPayRefund entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPayRefundService.get(id);
		}
		if (entity == null){
			entity = new MallPayRefund();
		}
		return entity;
	}
	
	@RequiresPermissions("pay:mallPayRefund:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPayRefund mallPayRefund, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallPayRefund> page = mallPayRefundService.findPage(new Page<MallPayRefund>(request, response, 15), mallPayRefund); 
		model.addAttribute("page", page);
		return "modules/pay/mallPayRefundList";
	}

	@RequiresPermissions("pay:mallPayRefund:view")
	@RequestMapping(value = "form")
	public String form(MallPayRefund mallPayRefund, Model model) {
		model.addAttribute("mallPayRefund", mallPayRefund);
		return "modules/pay/mallPayRefundForm";
	}

	@RequiresPermissions("pay:mallPayRefund:edit")
	@RequestMapping(value = "save")
	public String save(MallPayRefund mallPayRefund, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPayRefund)){
			return form(mallPayRefund, model);
		}
		mallPayRefundService.save(mallPayRefund);
		addMessage(redirectAttributes, "保存支付退款记录成功");
		return "redirect:"+Global.getAdminPath()+"/pay/mallPayRefund/?repage";
	}
//	
//	@RequiresPermissions("pay:mallPayRefund:edit")
//	@RequestMapping(value = "delete")
//	public String delete(MallPayRefund mallPayRefund, RedirectAttributes redirectAttributes) {
//		mallPayRefundService.delete(mallPayRefund);
//		addMessage(redirectAttributes, "删除支付退款记录成功");
//		return "redirect:"+Global.getAdminPath()+"/pay/mallPayRefund/?repage";
//	}

	@ResponseBody
	@RequiresPermissions("pay:mallPayRefund:edit")
	@RequestMapping(value = "delete")
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> reuslt = new HashMap<String, Object>();
		reuslt.put("responseMsg", "请求失败");
		reuslt.put("responseCode", -1);
		reuslt.put("isSuccess", Boolean.FALSE);
		String ids = StringUtil.trimToEmpty(request.getParameter("ids"));
		String [] idarr = ids.split(",");
		MallPayRefund mallPayRefund = new MallPayRefund();
		int i = 0;
		for (String string : idarr) {
			++i;
			mallPayRefund.setId(string);
			mallPayRefundService.delete(mallPayRefund);
		}
		if(i==idarr.length){
			reuslt.put("responseMsg", "删除支付退款记录成功");
			reuslt.put("responseCode", 100);
			reuslt.put("isSuccess", Boolean.TRUE);
		} else if (i<idarr.length){
			reuslt.put("responseMsg", "删除支付退款记录部分成功");
			reuslt.put("responseCode", 100);
			reuslt.put("isSuccess", Boolean.TRUE);
		}
		return reuslt;
	}

	@ResponseBody
	@RequiresPermissions("pay:mallPayRefund:refund")
	@RequestMapping(value = "refund_alipay")
	public Map<String, Object> refund_alipay(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> reuslt = new HashMap<String, Object>();
		
		reuslt.put("responseMsg", "请求失败");
		reuslt.put("responseCode", -1);
		reuslt.put("isSuccess", Boolean.FALSE);
		String outTradeNos = StringUtil.trimToEmpty(request.getParameter("outTradeNos"));
		String arrs[] = outTradeNos.split(",");
		outTradeNos = "";
		for (String string : arrs) {
			string = StringUtil.trimToEmpty(string);
			if("".equals(string)) continue;
			outTradeNos+="'"+string+"',";
		}
		if("".equals(outTradeNos)){
			reuslt.put("responseMsg", "请选择订单号!");
			return reuslt;
		}
		if(!"".equals(outTradeNos)){
			outTradeNos = outTradeNos.substring(0, outTradeNos.length()-1);
		}
		
		//验证是否退款过
		List<MallPayRefund> listVlidate = mallPayRefundService.findListByOutTradeNos(outTradeNos);
		if(null!=listVlidate && listVlidate.size()>0){
			String refund_url="";
			for (MallPayRefund mallPayRefund : listVlidate) {
				String outTradeNo = StringUtil.trimToEmpty(mallPayRefund.getOutTradeNo());
				String payStatus = StringUtil.trimToEmpty(mallPayRefund.getPayStatus());
				refund_url = StringUtil.trimToEmpty(mallPayRefund.getResponseMsg());
				if ("WAIT".equalsIgnoreCase(payStatus) && !"".equals(refund_url)){
					reuslt.put("refund_url", refund_url);
					reuslt.put("responseMsg", "订单号:"+outTradeNo+"已经操作了退款,请前往支付宝操作退款!");
					reuslt.put("responseCode", 100);
					reuslt.put("isSuccess", Boolean.TRUE);
					return reuslt;
				}
				if("SUCCESS".equalsIgnoreCase(payStatus)){
					reuslt.put("responseMsg", "订单号:"+outTradeNo+"已经退款成功!");
					mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo("REFUND", mallPayRefund.getOutTradeNo());
					return reuslt;
				}
			}
		}
		
		List<MallPayPaymentInfo> listPay = mallPayPaymentInfoService.findListByOutTradeNos(outTradeNos);
		if(null==listPay || listPay.size()<1){
			reuslt.put("responseMsg", "订单号不存在支付记录中（如果是当天的支付订单请在第二天10点后再操作）!");
			return reuslt;
		}
		Map<String,MallPayPaymentInfo> mapOutTradeNo = new HashMap<String,MallPayPaymentInfo>(listPay.size());
		for (MallPayPaymentInfo mallPayPaymentInfo : listPay) {
			String payStatus = StringUtil.trimToEmpty(mallPayPaymentInfo.getPayStatus());
			String outTradeNo = StringUtil.trimToEmpty(mallPayPaymentInfo.getOutTradeNo());
			String payPlatform = StringUtil.trimToEmpty(mallPayPaymentInfo.getPayPlatform());
			if(!"alipay".equalsIgnoreCase(payPlatform)){
				reuslt.put("responseMsg", "订单号不是支付宝支付的!");
				return reuslt;
			}
			if(!"".equals(outTradeNo) && "SUCCESS".equalsIgnoreCase(payStatus)){
				mapOutTradeNo.put(outTradeNo, mallPayPaymentInfo);
			} else {
				reuslt.put("responseMsg", "订单号:"+outTradeNo+"不是支付成功状态,不允许退款!");
				return reuslt;
			}
		}
		for (String string : arrs) {
			string = StringUtil.trimToEmpty(string);
			if(!mapOutTradeNo.containsKey(string)){
				reuslt.put("responseMsg", "订单号:"+string+"在支付成功记录中不存在,不允许退款!");
				return reuslt;
			}
		}
		Date now = new Date();
//		当天日期[8位]+序列号[3至24位]，如：201008010000001   
		String batch_no = DateUtil.getDateString(now, "yyyyMMddHHmmssSSS");
		int batch_num = 0 ; 
		String detail_data = "";
		//支付宝交易订单号^退款金额^退款描述#
		List<MallPayRefund> mallPayRefunds = new ArrayList<MallPayRefund>();
		for (Map.Entry<String,MallPayPaymentInfo> entry: mapOutTradeNo.entrySet()) {
			MallPayPaymentInfo mallPayPaymentInfo = entry.getValue();
			String outTradeNo = StringUtil.trimToEmpty(mallPayPaymentInfo.getOutTradeNo());
			String transactionId = StringUtil.trimToEmpty(mallPayPaymentInfo.getTransactionId());
			Integer totalFee = mallPayPaymentInfo.getTotalFee();
			if(null==totalFee || totalFee<=0){
				reuslt.put("responseMsg", "订单号:"+outTradeNo+"退款金额为"+totalFee+",金额为空或小于0,不允许退款!");
				return reuslt;
			} 
			if("".equals(transactionId)){
				reuslt.put("responseMsg", "订单号:"+outTradeNo+",支付流水号 transactionId 错误,不允许退款!");
				return reuslt;
			}
			BigDecimal pay = new BigDecimal(totalFee);
			BigDecimal money = pay.divide(new BigDecimal("100"));
			detail_data+=transactionId+"^"+String.valueOf(money)+"^订单号:"+outTradeNo+"(买家取消订单)退款.#";
			++batch_num;
			
			MallPayRefund mallPayRefund= new MallPayRefund();
			mallPayRefund.setOutTradeNo(mallPayPaymentInfo.getOutTradeNo()); // 订单号
			mallPayRefund.setPayPlatform(mallPayPaymentInfo.getPayPlatform()); // 支付平台
			mallPayRefund.setCreateTime(now); // 创建时间
			mallPayRefund.setPayTime(now); // 退款时间
			mallPayRefund.setTransactionId(mallPayPaymentInfo.getTransactionId()); // 交易流水号
			mallPayRefund.setPayStatus("WAIT"); // 支付状态
			mallPayRefund.setTotalFee(mallPayPaymentInfo.getTotalFee()); // 支付金额
			mallPayRefund.setResponseMsg(""); // 接口返回信息
			mallPayRefund.setValidate("FALSE"); // 是否认证
			mallPayRefund.setTransCodeMsg(batch_no); // 类型
			mallPayRefund.setTradeType(mallPayPaymentInfo.getTradeType()); // 订单类型
			mallPayRefunds.add(mallPayRefund);
		}
		detail_data = detail_data.substring(0, detail_data.length()-1); 		
		//封装参数
		Map<String,String> params = new HashMap<String,String>();
		params.put("batch_no", batch_no);
		params.put("batch_num", String.valueOf(batch_num));
		params.put("detail_data", detail_data);
		params.put("refund_date", DateUtil.getDateString(now, "yyyy-MM-dd HH:mm:ss"));

		//创建请求
		String url = HttpProperties.getMoonMallPayUrl() + "/refund/ali?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
		HttpsUtil ht = new HttpsUtil();
		Map<String, Object> rlt = ht.sendPost(url, params);
		String refund_url = "";
		{
			if(!rlt.containsKey("refund_url")){
				reuslt.put("responseMsg", "请求生成退款地址时返回信息错误,请重试!");
				return reuslt;
			}
			refund_url = rlt.get("refund_url").toString();
			if("".equals(refund_url)){
				reuslt.put("responseMsg", "请求生成退款地址时返回信息为空,请重试!");
				return reuslt;
			}
		}
		for (MallPayRefund mallPayRefund : mallPayRefunds) {
			mallPayRefund.setResponseMsg(refund_url); // 接口返回信息
			mallPayRefundService.save(mallPayRefund);
		}
		reuslt.put("refund_url", refund_url);
		reuslt.put("responseMsg", "");
		reuslt.put("responseCode", 100);
		reuslt.put("isSuccess", Boolean.TRUE);
		return reuslt;
	}

	@ResponseBody
	@RequiresPermissions("pay:mallPayRefund:refund")
	@RequestMapping(value = "refund_wxpay")
	public Map<String, Object> refund_wxpay(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		Map<String, Object> reuslt = new HashMap<String, Object>();
		reuslt.put("responseMsg", "请求失败");
		reuslt.put("responseCode", -1);
		reuslt.put("isSuccess", Boolean.FALSE);
		String outTradeNos = StringUtil.trimToEmpty(request.getParameter("outTradeNos"));
		String arrs[] = outTradeNos.split(",");
		outTradeNos = "";
		for (String string : arrs) {
			string = StringUtil.trimToEmpty(string);
			if("".equals(string)) continue;
			outTradeNos+="'"+string+"',";
		}
		if("".equals(outTradeNos)){
			reuslt.put("responseMsg", "请选择订单号!");
			return reuslt;
		}
		if(!"".equals(outTradeNos)){
			outTradeNos = outTradeNos.substring(0, outTradeNos.length()-1);
		}
		
		//验证是否退款过
		List<MallPayRefund> listVlidate = mallPayRefundService.findListByOutTradeNos(outTradeNos);
		if(null!=listVlidate && listVlidate.size()>0){
			String refund_url="";
			for (MallPayRefund mallPayRefund : listVlidate) {
				String outTradeNo = StringUtil.trimToEmpty(mallPayRefund.getOutTradeNo());
				String payStatus = StringUtil.trimToEmpty(mallPayRefund.getPayStatus());
				refund_url = StringUtil.trimToEmpty(mallPayRefund.getResponseMsg());
				if ("WAIT".equalsIgnoreCase(payStatus) && !"".equals(refund_url)){
					reuslt.put("refund_url", refund_url);
					reuslt.put("responseMsg", "订单号:"+outTradeNo+"已经操作了退款,请前往支付宝操作退款!");
					reuslt.put("responseCode", 100);
					reuslt.put("isSuccess", Boolean.TRUE);
					return reuslt;
				}
				if("SUCCESS".equalsIgnoreCase(payStatus)){
					reuslt.put("responseMsg", "订单号:"+outTradeNo+"已经退款成功!");
					mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo("REFUND", mallPayRefund.getOutTradeNo());
					return reuslt;
				}
			}
		}
		
		List<MallPayPaymentInfo> listPay = mallPayPaymentInfoService.findListByOutTradeNos(outTradeNos);
		if(null==listPay || listPay.size()<1){
			reuslt.put("responseMsg", "订单号不存在,请在[退款操作]查看订单退款状态!");
			return reuslt;
		}
		Map<String,MallPayPaymentInfo> mapOutTradeNo = new HashMap<String,MallPayPaymentInfo>(listPay.size());
		for (MallPayPaymentInfo mallPayPaymentInfo : listPay) {
			String payStatus = StringUtil.trimToEmpty(mallPayPaymentInfo.getPayStatus());
			String outTradeNo = StringUtil.trimToEmpty(mallPayPaymentInfo.getOutTradeNo());
			String payPlatform = StringUtil.trimToEmpty(mallPayPaymentInfo.getPayPlatform());
			if(!"wxpay".equalsIgnoreCase(payPlatform)){
				reuslt.put("responseMsg", "订单号不是微信支付的!");
				return reuslt;
			}
			if(!"".equals(outTradeNo) && "SUCCESS".equalsIgnoreCase(payStatus)){
				mapOutTradeNo.put(outTradeNo, mallPayPaymentInfo);
			} else {
				reuslt.put("responseMsg", "订单号:"+outTradeNo+"不是支付成功状态,不允许退款!");
				return reuslt;
			}
		}
		for (String string : arrs) {
			string = StringUtil.trimToEmpty(string);
			if(!mapOutTradeNo.containsKey(string)){
				reuslt.put("responseMsg", "订单号:"+string+"在支付成功记录中不存在,不允许退款!");
				return reuslt;
			}
		}
		Date now = new Date();
		List<MallPayRefund> mallPayRefunds = new ArrayList<MallPayRefund>();
		for (Map.Entry<String,MallPayPaymentInfo> entry: mapOutTradeNo.entrySet()) {
			MallPayPaymentInfo mallPayPaymentInfo = entry.getValue();
			String outTradeNo = StringUtil.trimToEmpty(mallPayPaymentInfo.getOutTradeNo());
			String transactionId = StringUtil.trimToEmpty(mallPayPaymentInfo.getTransactionId());
			Integer totalFee = mallPayPaymentInfo.getTotalFee();
			if(null==totalFee || totalFee<=0){
				reuslt.put("responseMsg", "订单号:"+outTradeNo+"退款金额为"+totalFee+",金额为空或小于0,不允许退款!");
				return reuslt;
			} 
			if("".equals(transactionId)){
				reuslt.put("responseMsg", "订单号:"+outTradeNo+",支付流水号 transactionId 错误,不允许退款!");
				return reuslt;
			}
			
			MallPayRefund mallPayRefund= new MallPayRefund();
			mallPayRefund.setOutTradeNo(outTradeNo); // 订单号
			mallPayRefund.setPayPlatform(mallPayPaymentInfo.getPayPlatform()); // 支付平台
			mallPayRefund.setCreateTime(now); // 创建时间
			mallPayRefund.setPayTime(now); // 退款时间
			mallPayRefund.setTransactionId(mallPayPaymentInfo.getTransactionId()); // 交易流水号
			mallPayRefund.setPayStatus("WAIT"); // 支付状态
			mallPayRefund.setTotalFee(mallPayPaymentInfo.getTotalFee()); // 支付金额
			mallPayRefund.setResponseMsg(""); // 接口返回信息
			mallPayRefund.setValidate("FALSE"); // 是否认证
			mallPayRefund.setTransCodeMsg(outTradeNo+"01"); // 类型
			mallPayRefund.setTradeType(mallPayPaymentInfo.getTradeType()); // 订单类型
			mallPayRefunds.add(mallPayRefund);
		}
		//封装参数
		Map<String,String> params = new HashMap<String,String>();
		int i = mallPayRefunds.size(), size = mallPayRefunds.size();
		for (MallPayRefund mallPayRefund : mallPayRefunds) {
//			params.put("mch_id", "");
//			params.put("appid", "");
//			params.put("nonce_str", "");
//			params.put("sign", "");
//			params.put("transaction_id", "");
			params.put("out_trade_no", mallPayRefund.getOutTradeNo());
			params.put("out_refund_no", mallPayRefund.getTransCodeMsg());
			params.put("total_fee", String.valueOf(mallPayRefund.getTotalFee()));
			params.put("refund_fee", String.valueOf(mallPayRefund.getTotalFee()));
//			params.put("op_user_id", "");
			params.put("trade_type", String.valueOf(mallPayRefund.getTradeType()));

			//创建请求
			
			String url = HttpProperties.getMoonMallPayUrl() + "/refund/wx?client=pc&version=v3.1&cuid=5E-E0-C5-FA-2B-48&format=json&time=356345634563456365&sign=795b8d6c8d5e5f7440c9c42f6e1346bb";
			HttpsUtil ht = new HttpsUtil();
			Map<String, Object> rlt = ht.sendPost(url, params);
			{
				String out_trade_no = StringUtil.trimObjToEmpty(rlt.get("out_trade_no"));
				String return_code = StringUtil.trimObjToEmpty(rlt.get("return_code"));
				String refund_fee = StringUtil.trimObjToEmpty(rlt.get("refund_fee"));
				String return_msg = StringUtil.trimObjToEmpty(rlt.get("return_msg"));
				if("SUCCESS".equalsIgnoreCase(return_code)){
					mallPayRefund.setTotalFee(Integer.valueOf(refund_fee));
					mallPayRefund.setPayStatus("SUCCESS"); // 支付状态 ： 退款成功
					mallPayRefund.setReturnMsg(return_msg);
					mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo("REFUND", mallPayRefund.getOutTradeNo());
					--i;
				} else {
					reuslt.put("responseMsg", return_msg);
					log.error(mallPayRefund.toString() + "请求退款时返回信息错误,请重试!");
					mallPayRefund.setPayStatus("ERROR"); // 支付状态 ： 退款失败
					mallPayRefund.setReturnMsg(return_msg);
					mallPayRefund.setResponseMsg(rlt.toString()); // 接口返回信息
					mallPayRefundService.save(mallPayRefund);
					mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo("REFUND", mallPayRefund.getOutTradeNo());
					return reuslt;
				}
			}
			mallPayRefund.setResponseMsg(rlt.toString()); // 接口返回信息
			mallPayRefundService.save(mallPayRefund);
		}
		if(i==0){
			reuslt.put("responseMsg", "微信支付退款记录成功");
		} else if (i>0){
			reuslt.put("responseMsg", "微信支付退款记录有条"+ (size-i) +"成功,条"+i+"失败");
		} else if (i==size){
			reuslt.put("responseMsg", "微信支付退款记录失败");
		}
		reuslt.put("responseCode", 100);
		reuslt.put("isSuccess", Boolean.FALSE);
		return reuslt;
	}
	
}