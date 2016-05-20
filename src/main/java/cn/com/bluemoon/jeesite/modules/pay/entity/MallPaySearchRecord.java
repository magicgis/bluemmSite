/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 第三方同步记录Entity
 * 
 * @author liao
 * @version 2016-03-07
 */
public class MallPaySearchRecord extends DataEntity<MallPaySearchRecord> {

	private static final long serialVersionUID = 1L;
	private String payrecordId; // 主键ID
	private String outTradeNo; // 订单号
	private String payPlatform; // 支付平台
	private Date createTime; // 创建时间
	private Date payTime; // 支付时间
	private String transactionId; // 交易流水号
	private String payStatus; // 支付状态
	private String totalFee; // 支付金额
	private String responseMsg; // 接口返回信息
	private String validate; // 是否认证
	private String transCodeMsg; // 类型
	private String buyerAccount; // 买方
	private String sellerAccount; // 卖方
	private String dataDate; // 数据日期
	private String tradeType; // 订单类型
	
	
	private Date payTime1; // 支付时间
	private Date payTime2; // 支付时间
	private Date createTime1; // 创建时间
	private Date createTime2; // 创建时间
	
	

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

	public Date getCreateTime1() {
		return createTime1;
	}

	public void setCreateTime1(Date createTime1) {
		this.createTime1 = createTime1;
	}

	public Date getCreateTime2() {
		return createTime2;
	}

	public void setCreateTime2(Date createTime2) {
		this.createTime2 = createTime2;
	}

	public MallPaySearchRecord() {
		super();
	}

	public MallPaySearchRecord(String id) {
		super(id);
	}

	@Length(min = 1, max = 32, message = "主键ID长度必须介于 1 和 32 之间")
	public String getPayrecordId() {
		return payrecordId;
	}

	public void setPayrecordId(String payrecordId) {
		this.payrecordId = payrecordId;
	}

	@Length(min = 0, max = 32, message = "订单号长度必须介于 0 和 32 之间")
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Length(min = 0, max = 32, message = "支付平台长度必须介于 0 和 32 之间")
	public String getPayPlatform() {
		return payPlatform;
	}

	public void setPayPlatform(String payPlatform) {
		this.payPlatform = payPlatform;
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

	@Length(min = 0, max = 11, message = "支付金额长度必须介于 0 和 11 之间")
	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
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

	@Length(min = 0, max = 36, message = "类型长度必须介于 0 和 36 之间")
	public String getTransCodeMsg() {
		return transCodeMsg;
	}

	public void setTransCodeMsg(String transCodeMsg) {
		this.transCodeMsg = transCodeMsg;
	}

	@Length(min = 0, max = 36, message = "买方长度必须介于 0 和 36 之间")
	public String getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	@Length(min = 0, max = 36, message = "卖方长度必须介于 0 和 36 之间")
	public String getSellerAccount() {
		return sellerAccount;
	}

	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}

	@Length(min = 0, max = 16, message = "数据日期长度必须介于 0 和 16 之间")
	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	@Length(min = 0, max = 32, message = "订单类型长度必须介于 0 和 32 之间")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

}