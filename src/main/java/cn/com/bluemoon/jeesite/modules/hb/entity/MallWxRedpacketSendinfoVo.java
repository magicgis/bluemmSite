/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * 红包发送详情Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketSendinfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String mchBillno;	// 商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）
	private String appid;		// 商户openid
	private String mchId;		// 商户号微信-支付分配的商户号
	private String redpackno;	// 红包单号
	private String detailId;	// 红包单号detail_id
	private String openid;		// 领取红包的openid
	private String referrerNo;	// 推荐人编号
	private Date sendTime;		// 红包发送时间
	private String status;		// 红包状态SENDING:发放中1;//SENT:已发放待领取2&nbsp;;//FAILED：发放失败3&nbsp;;//RECEIVED:已领取4;//REFUND:已退款5
	private String reason;		// 发送失败原因
	private String refundAmount;	// 红包退款金额
	private Date refundTime;		// 红包的退款时间（如果其未领取的退款）
	private String refundResendCount;		// 退款的红包重发次数&lt;2
	private String actName;		// 活动名称
	private String wishing;		// 红包祝福语
	private String totalAmount;		// 红包金额红包总金额（单位元）
	private String totalNum;		// 红包个数
	private String hbType;		// 红包类型GROUP:裂变红包&nbsp;;//NORMAL:普通红包
	private String sendType;	// 发放类型API:通过API接口发放&nbsp;;//UPLOAD:通过上传文件方式发放&nbsp;;//ACTIVITY:通过活动方式发放
	private String hblist;		// 领取列表-裂变红包的领取列表
	private String amount;		// 领取金额-裂变红包
	private Date rcvTime;		// 领取时间-裂变红包的领取
	private String remark;		// 活动描述，低版本微信可见
	private String isdeleted;		// 是否被删除0否1是
	private String operator;		// 操作人
	private Date operatortime;		// 操作时间
	
	private String applyid;
	
	public MallWxRedpacketSendinfoVo() {
		super();
	}

	public String getMchBillno() {
		return mchBillno;
	}

	public void setMchBillno(String mchBillno) {
		this.mchBillno = mchBillno;
	}
	
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	
	public String getRedpackno() {
		return redpackno;
	}

	public void setRedpackno(String redpackno) {
		this.redpackno = redpackno;
	}
	
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getReferrerNo() {
		return referrerNo;
	}

	public void setReferrerNo(String referrerNo) {
		this.referrerNo = referrerNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
	public String getRefundResendCount() {
		return refundResendCount;
	}

	public void setRefundResendCount(String refundResendCount) {
		this.refundResendCount = refundResendCount;
	}
	
	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}
	
	public String getWishing() {
		return wishing;
	}

	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	
	public String getHbType() {
		return hbType;
	}

	public void setHbType(String hbType) {
		this.hbType = hbType;
	}
	
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	public String getHblist() {
		return hblist;
	}

	public void setHblist(String hblist) {
		this.hblist = hblist;
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getRcvTime() {
		return rcvTime;
	}

	public void setRcvTime(Date rcvTime) {
		this.rcvTime = rcvTime;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public Date getOperatortime() {
		return operatortime;
	}

	public void setOperatortime(Date operatortime) {
		this.operatortime = operatortime;
	}

	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}
	
}