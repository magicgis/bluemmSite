/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 支付记录Entity
 * 
 * @author liao
 * @version 2016-03-07
 */
public class MallPayPaymentInfo extends DataEntity<MallPayPaymentInfo> {

	private static final long serialVersionUID = 1L;
	private String paymentId; // 主键ID
	private String outTradeNo; // 订单号
	private String transactionId; // 交易流水号
	private String payStatus; // 支付状态
	private Integer totalFee; // 支付金额
	private String payPlatform; // 支付平台
	private String tradeType; // 订单类型
	private String mchId; // 商家编号
	private String prepayId; // 预付款编号
	private Date createTime; // 创建时间
	private Date payTime; // 支付时间
	private String responseMsg; // 接口返回信息
	private String validate; // 是否认证
	

	private Double totalFee1; // 支付金额
	private Double totalFee2; // 支付金额
	
	private Date payTime1; // 支付时间
	private Date payTime2; // 支付时间
	

	public Double getTotalFee1() {
		return totalFee1;
	}

	public void setTotalFee1(Double totalFee1) {
		this.totalFee1 = totalFee1;
	}

	public Double getTotalFee2() {
		return totalFee2;
	}

	public void setTotalFee2(Double totalFee2) {
		this.totalFee2 = totalFee2;
	}

	public Date getPayTime1() {
		return payTime1;
	}

	public void setPayTime1(Date payTime1) {
		this.payTime1 = payTime1;
	}

	public Date getPayTime2() {
		return payTime2;
	}

	public void setPayTime2(Date payTime2) {
		this.payTime2 = payTime2;
	}

	public MallPayPaymentInfo() {
		super();
	}

	public MallPayPaymentInfo(String id) {
		super(id);
	}

	@Length(min = 1, max = 32, message = "主键ID长度必须介于 1 和 32 之间")
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	@Length(min = 0, max = 32, message = "订单号长度必须介于 0 和 32 之间")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Length(min = 0, max = 32, message = "交易流水号长度必须介于 0 和 32 之间")
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Length(min = 0, max = 32, message = "支付状态长度必须介于 0 和 32 之间")
	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	@Length(min = 0, max = 32, message = "支付平台长度必须介于 0 和 32 之间")
	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
	}

	@Length(min = 0, max = 32, message = "订单类型长度必须介于 0 和 32 之间")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@Length(min = 0, max = 32, message = "商家编号长度必须介于 0 和 32 之间")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	@Length(min = 0, max = 32, message = "预付款编号长度必须介于 0 和 32 之间")
	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Length(min = 0, max = 1000, message = "接口返回信息长度必须介于 0 和 1000 之间")
	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	@Length(min = 0, max = 6, message = "是否认证长度必须介于 0 和 6 之间")
	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

}