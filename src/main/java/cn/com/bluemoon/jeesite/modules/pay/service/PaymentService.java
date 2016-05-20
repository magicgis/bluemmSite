package cn.com.bluemoon.jeesite.modules.pay.service;

import java.util.List;

import cn.com.bluemoon.jeesite.modules.pay.vo.MallPayPaymentVo;

public interface PaymentService {

	/**
	 * 获取所有支付方式
	 * @return
	 */
	public List<MallPayPaymentVo> getAllPayment();
}
