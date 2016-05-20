package cn.com.bluemoon.jeesite.modules.pay.vo;

import java.io.Serializable;

/**
 * 账户支付记录 VO
 * 
 * @Author liaol
 * @Time 2015-11-24
 * @Version 1.0.1
 * 
 */
public class MallPayPrepaidPayVO implements Serializable {

	private static final long serialVersionUID = 9014352465521338672L;

	private String userId; // 用户id
	private String cardId; // 充值卡号
	private String orderId; // 订单id
	private Integer payMoney; // 支付金额
	private Integer odd; // 支付后余额

	private String payTime; // 支付时间

	public MallPayPrepaidPayVO() {
		super();
	}

}
