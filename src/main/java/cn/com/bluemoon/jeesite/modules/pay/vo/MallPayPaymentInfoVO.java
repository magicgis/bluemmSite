package cn.com.bluemoon.jeesite.modules.pay.vo;

import java.io.Serializable;

/**
 * 第三方支付记录 VO
 * 
 * @Author liaol
 * @Time 2015-11-24
 * @Version 1.0.1
 * 
 */
public class MallPayPaymentInfoVO implements Serializable {

	private static final long serialVersionUID = 9014352465521338672L;

	private String paymentId; // 主键id
	private String outTradeNo; // 订单id
	private String transactionId; // 交易流水号
	private Integer payStatus; // 支付状态
	private Integer totalFee; // 支付金额

	private String payPlatform; // 支付平台
	private String tradeType; // 订单类型
	private String mchId; // 商家编号
	private String prepayId; // 预付款编号
	private String createTime; // 创建时间

	private String responseMsg; // 接口返回信息

	public MallPayPaymentInfoVO() {
		super();
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

}
