/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 红包发送详情Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketSendinfo extends DataEntity<MallWxRedpacketSendinfo> {
	
	private static final long serialVersionUID = 1L;
	private String mchBillno;	// 商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）
	private String appid;		// 商户openid????
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
	
	private String applyid;		//申请单号
	private Date startTime;
	private Date endTime;
	
	public MallWxRedpacketSendinfo() {
		super();
	}
	
	public MallWxRedpacketSendinfo(MallWxRedpacketApplyDetails detail,MallWxRedpacketApply apply) {
		this.mchBillno = detail.getMerchantOrderNo();
//		this.appid = apply.getMerchantNo(); // 商户openid
		this.mchId = apply.getMerchantNo();	// 商户号微信-支付分配的商户号
		this.redpackno = detail.getRedpacketNo();	// 红包单号
//		this.detailId;	// 红包单号detail_id 
		this.openid = detail.getUserOpenid();		// 领取红包的openid
		this.referrerNo = detail.getReferrerNo();	// 推荐人编号
		this.status = "0";		// 红包状态SENDING:发放中1;//SENT:已发放待领取2&nbsp;;//FAILED：发放失败3&nbsp;;//RECEIVED:已领取4;//REFUND:已退款5
		this.actName = detail.getHdName();		// 活动名称
		this.wishing = detail.getRedpacketGreetings();		// 红包祝福语
		this.totalAmount = detail.getRedpacketAmountYuan();		// 红包金额红包总金额（单位元）
		this.totalNum = detail.getSendTotalPeople();		// 红包个数
		this.operator = detail.getOperator();		// 操作人
		this.operatortime = detail.getOperatortime();		// 操作时间
//		this.sendTime;		// 红包发送时间
//		this.reason;		// 发送失败原因
//		this.refundAmount;	// 红包退款金额
//		this.refundTime;		// 红包的退款时间（如果其未领取的退款）
//		this.refundResendCount;		// 退款的红包重发次数&lt;2
//		this.hbType;		// 红包类型GROUP:裂变红包&nbsp;;//NORMAL:普通红包
//		this.sendType;	// 发放类型API:通过API接口发放&nbsp;;//UPLOAD:通过上传文件方式发放&nbsp;;//ACTIVITY:通过活动方式发放
//		this.hblist;		// 领取列表-裂变红包的领取列表
//		this.amount;		// 领取金额-裂变红包
//		this.rcvTime;		// 领取时间-裂变红包的领取
//		this.remark;		// 活动描述，低版本微信可见
//		this.isdeleted;		// 是否被删除0否1是
	}

	public MallWxRedpacketSendinfo(String id){
		super(id);
	}

	@Length(min=1, max=29, message="商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）长度必须介于 1 和 29 之间")
	@ExcelField(title="商户订单号", align=2, sort=40)
	public String getMchBillno() {
		return mchBillno;
	}

	public void setMchBillno(String mchBillno) {
		this.mchBillno = mchBillno;
	}
	
	@Length(min=0, max=30, message="商户openid长度必须介于 0 和 30 之间")
	@ExcelField(title="发放appID", align=2, sort=100)
	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
	
	@Length(min=0, max=10, message="商户号微信-支付分配的商户号长度必须介于 0 和 10 之间")
	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	
	@ExcelField(title="内部红包编号", align=2, sort=20)
	public String getRedpackno() {
		return redpackno;
	}

	public void setRedpackno(String redpackno) {
		this.redpackno = redpackno;
	}
	
	@Length(min=0, max=28, message="红包单号detail_id长度必须介于 0 和 28 之间")
	@ExcelField(title="微信红包编号", align=2, sort=30)
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	@Length(min=0, max=50, message="领取红包的openid长度必须介于 0 和 50 之间")
	@ExcelField(title="用户微信openID", align=2, sort=90)
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Length(min=0, max=10, message="推荐人编号长度必须介于 0 和 10 之间")
	@ExcelField(title="推荐人编码", align=2, sort=70)
	public String getReferrerNo() {
		return referrerNo;
	}

	public void setReferrerNo(String referrerNo) {
		this.referrerNo = referrerNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发放时间", align=2, sort=50)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	@Length(min=0, max=1, message="红包状态SENDING:发放中1;//SENT:已发放待领取2&nbsp;;//FAILED：发放失败3&nbsp;;//RECEIVED:已领取4;//REFUND:已退款5长度必须介于 0 和 1 之间")
	@ExcelField(title="发放状态", align=2, sort=60)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Length(min=0, max=100, message="发送失败原因长度必须介于 0 和 100 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Length(min=0, max=6, message="红包退款金额长度必须介于 0 和 6 之间")
	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="退款时间", align=2, sort=110)
	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
	@Length(min=0, max=11, message="退款的红包重发次数&lt;2长度必须介于 0 和 11 之间")
	public String getRefundResendCount() {
		return refundResendCount;
	}

	public void setRefundResendCount(String refundResendCount) {
		this.refundResendCount = refundResendCount;
	}
	
	@Length(min=0, max=50, message="活动名称长度必须介于 0 和 50 之间")
	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}
	
	@Length(min=0, max=50, message="红包祝福语长度必须介于 0 和 50 之间")
	public String getWishing() {
		return wishing;
	}

	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	
	@Length(min=0, max=6, message="红包金额红包总金额（单位元）长度必须介于 0 和 6 之间")
	@ExcelField(title="红包金额（元）", align=2, sort=80)
	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Length(min=0, max=10, message="红包个数长度必须介于 0 和 10 之间")
	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	
	@Length(min=0, max=10, message="红包类型GROUP:裂变红包&nbsp;;//NORMAL:普通红包长度必须介于 0 和 10 之间")
	public String getHbType() {
		return hbType;
	}

	public void setHbType(String hbType) {
		this.hbType = hbType;
	}
	
	@Length(min=0, max=10, message="发放类型API:通过API接口发放&nbsp;;//UPLOAD:通过上传文件方式发放&nbsp;;//ACTIVITY:通过活动方式发放长度必须介于 0 和 10 之间")
	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	@Length(min=0, max=10, message="领取列表-裂变红包的领取列表长度必须介于 0 和 10 之间")
	public String getHblist() {
		return hblist;
	}

	public void setHblist(String hblist) {
		this.hblist = hblist;
	}
	
	@Length(min=0, max=5, message="领取金额-裂变红包长度必须介于 0 和 5 之间")
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
	
	@Length(min=0, max=255, message="活动描述，低版本微信可见长度必须介于 0 和 255 之间")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Length(min=0, max=1, message="是否被删除0否1是长度必须介于 0 和 1 之间")
	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}
	
	@Length(min=0, max=10, message="操作人长度必须介于 0 和 10 之间")
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

	@ExcelField(title="申请单号", align=2, sort=10)
	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}