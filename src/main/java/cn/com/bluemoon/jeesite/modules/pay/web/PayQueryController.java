package cn.com.bluemoon.jeesite.modules.pay.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.bluemoon.jeesite.common.utils.StringUtil;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayPaymentInfo;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPayPaymentInfoService;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPayRefundService;
import cn.com.bluemoon.jeesite.modules.pay.service.wxpay.HttpsUtil;
import cn.com.bluemoon.jeesite.modules.pay.util.HttpProperties;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;

@Controller
@RequestMapping(value = "${adminPath}/modules/pay")
public class PayQueryController extends BaseController {

	private static Logger log = (Logger) LoggerFactory.getLogger(PayQueryController.class);

	@Autowired
	private MallPayRefundService mallPayRefundService;
	@Autowired
	private MallPayPaymentInfoService mallPayPaymentInfoService;
	
	@ResponseBody
	@RequestMapping(value = "validate")
	public Map<String, Object> validate(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, Object> reuslt = new HashMap<String, Object>();
		reuslt.put("responseMsg", "请求失败");
		reuslt.put("responseCode", -1);
		reuslt.put("isSuccess", Boolean.FALSE);
		
		String transaction_id = request.getParameter("transaction_id");
		String out_trade_no = request.getParameter("out_trade_no");
		List<HashMap<String, String>> results = new ArrayList<HashMap<String,String>>();
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("outerCodes", out_trade_no);
			params.put("transaction_id", transaction_id);
			HttpsUtil ht = new HttpsUtil();
			Map<String, Object> rlt = ht.sendPost(HttpProperties.getVal("MoonMallPayUrl")+ "/pay/query", params);
			if(rlt.containsKey("isSuccess") && "TRUE".equalsIgnoreCase(rlt.get("isSuccess").toString())
					&& rlt.containsKey("outerCodesInfor")){
				String outerCodesInfor = rlt.get("outerCodesInfor").toString();
				JSONArray resJson = JSONArray.fromObject(outerCodesInfor);
				String outTradeNos = "'"+out_trade_no+"'";
				List<MallPayPaymentInfo> listPay = mallPayPaymentInfoService.findListByOutTradeNos(outTradeNos);
				if(null==listPay || listPay.size()<1){
					return reuslt;
				}
				List<MallPayPaymentInfo> listPayTemp = new ArrayList<MallPayPaymentInfo>();
				for (MallPayPaymentInfo mallPayPaymentInfo : listPay) {
					if(!"SUCCESS".equalsIgnoreCase(mallPayPaymentInfo.getValidate())){
						listPayTemp.add(mallPayPaymentInfo);
					}
				}
				if(listPayTemp.size()<1){
					reuslt.put("responseMsg", "订单号已经是同步认证过了!");
					reuslt.put("responseCode", -1);
					reuslt.put("isSuccess", Boolean.FALSE);
					return reuslt;
				}
				boolean flag = false;
				for (MallPayPaymentInfo mallPayPaymentInfo : listPayTemp) {
					for (Object object : resJson) {
						JSONObject jsonObj = (JSONObject)object;
						String return_code = jsonObj.has("return_code")?jsonObj.getString("return_code"):"";
						String payPlatform = jsonObj.has("payPlatform")?jsonObj.getString("payPlatform"):"";
//						String transaction_id_ = jsonObj.has("trade_no")?jsonObj.getString("trade_no"):"";
//						String out_trade_no_ = jsonObj.has("out_trade_no")?jsonObj.getString("out_trade_no"):"";
						String result_code = jsonObj.has("result_code")?jsonObj.getString("result_code"):"";
						String tradeType = jsonObj.has("tradeType")?jsonObj.getString("tradeType"):"";
						String trade_state = jsonObj.has("trade_state")?jsonObj.getString("trade_state"):"";
//						String payed_time = jsonObj.has("payed_time")?jsonObj.getString("payed_time"):"";
						if("FAIL".equalsIgnoreCase(return_code)){
							continue;
						}
						if( "FAIL".equalsIgnoreCase(result_code)){
							continue;
						}
						flag = true;
						//修改支付状态
						if(payPlatform.equalsIgnoreCase(mallPayPaymentInfo.getPayPlatform()) && 
								tradeType.equalsIgnoreCase(mallPayPaymentInfo.getTradeType()) && 
								!trade_state.equalsIgnoreCase(mallPayPaymentInfo.getPayStatus())){
	//								String outTradeNo = StringUtil.trimToEmpty(mallPayPaymentInfo.getOutTradeNo());
							mallPayRefundService.updateValidateByOutTradeNo(result_code, "TRUE", out_trade_no);
							mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo(result_code, out_trade_no);
						}
					}
					if(!flag && "SUCCESS".equalsIgnoreCase(mallPayPaymentInfo.getPayStatus())){
						mallPayRefundService.updateValidateByOutTradeNo("ERROR", "TRUE", out_trade_no);
						mallPayPaymentInfoService.updateRefundPaystatusByOutTradeNo("ERROR", out_trade_no);
					}
				}
				log.error(" 第三方支付 查询代理  成功：" + resJson.toString());
			} else {
				JSONObject resJson = JSONObject.fromObject(rlt);
				log.error(" 第三方支付 查询代理  失败：" + resJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		reuslt.put("responseMsg", "订单号同步成功!");
		reuslt.put("responseCode", 100);
		reuslt.put("isSuccess", Boolean.TRUE);
		return reuslt;
	}
	
	
	@RequiresPermissions("modules.pay.query")
	@RequestMapping(value = {"query"})
	public String query(HttpServletRequest request, HttpServletResponse response) {
		return "modules/pay/query";
	}

	@RequiresPermissions("modules.pay.query")
	@ResponseBody
	@RequestMapping(value = "list")
	public List<HashMap<String, String>> list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		String transaction_id = request.getParameter("transaction_id");
		String out_trade_no = request.getParameter("out_trade_no");
		List<HashMap<String, String>> results = new ArrayList<HashMap<String,String>>();
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("outerCodes", out_trade_no);
			params.put("transaction_id", transaction_id);
			HttpsUtil ht = new HttpsUtil();
			Map<String, Object> rlt = ht.sendPost(HttpProperties.getVal("MoonMallPayUrl")+ "/pay/query", params);
			if(rlt.containsKey("isSuccess") && "TRUE".equalsIgnoreCase(rlt.get("isSuccess").toString())
					&& rlt.containsKey("outerCodesInfor")){
				String outerCodesInfor = rlt.get("outerCodesInfor").toString();
				JSONArray resJson = JSONArray.fromObject(outerCodesInfor);
				for (Object object : resJson) {
					JSONObject jsonObj = (JSONObject)object;
					HashMap<String, String> map = new HashMap<String, String>();
					map.putAll(jsonObj);
					results.add(map);
				}
				log.error(" 第三方支付 查询代理  成功：" + resJson.toString());
			} else {
				JSONObject resJson = JSONObject.fromObject(rlt);
				log.error(" 第三方支付 查询代理  失败：" + resJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
}