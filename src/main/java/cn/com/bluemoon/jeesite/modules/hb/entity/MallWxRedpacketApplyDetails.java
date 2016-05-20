/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.util.Date;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 红包明细Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketApplyDetails extends DataEntity<MallWxRedpacketApplyDetails> {
	
	private static final long serialVersionUID = 1L;
	private String applyid;		// 申请编号===========
	private String merchantOrderNo;		// 商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）
	private String sendListid;		// send_listid
	private Date sendTime;		// 发放成功时间
	private String processinstid;		// 流程实例号
	private String redpacketNo;		// 红包编号hb20150709001000001==============
	private String referrerNo;		// 推荐人编码
	private String referrerName;		// 推荐人姓名
	private String userOpenid;		// 用户微信账号openid
	private String redpacketAmountYuan;		// 红包金额(元)
	private String redpacketAmountFen;		// 红包金额(分)
	private String redpacketMinAmount;		// 最小红包金额(分)
	private String redpacketMaxAmount;		// 最大红包金额(分)
	private String sendTotalPeople;		// 红包发放人数
	private String redpacketGreetings;		// 红包祝福语
	private String redpacketStatus;		// 红包的状态-1未发放2已发放
	private String hdName;		// 活动名称
	private String marker;		// 备注
	private String isdeleted;		// 是否被删除0否1是
	private String operator;		// 操作人
	private Date operatortime;		// 操作时间
	
	private String redpacketNoStr;
	
	public MallWxRedpacketApplyDetails() {
		super();
	}

	public MallWxRedpacketApplyDetails(String id){
		super(id);
	}

	
	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}
	
	
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	
	
	public String getSendListid() {
		return sendListid;
	}

	public void setSendListid(String sendListid) {
		this.sendListid = sendListid;
	}
	
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getProcessinstid() {
		return processinstid;
	}

	public void setProcessinstid(String processinstid) {
		this.processinstid = processinstid;
	}
	
	
	public String getRedpacketNo() {
		return redpacketNo;
	}

	public void setRedpacketNo(String redpacketNo) {
		this.redpacketNo = redpacketNo;
	}
	
	
	public String getReferrerNo() {
		return referrerNo;
	}

	public void setReferrerNo(String referrerNo) {
		this.referrerNo = referrerNo;
	}
	
	
	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}
	
	
	public String getUserOpenid() {
		return userOpenid;
	}

	public void setUserOpenid(String userOpenid) {
		this.userOpenid = userOpenid;
	}
	
	public String getRedpacketAmountYuan() {
		return redpacketAmountYuan;
	}

	public void setRedpacketAmountYuan(String redpacketAmountYuan) {
		this.redpacketAmountYuan = redpacketAmountYuan;
	}
	
	
	public String getRedpacketAmountFen() {
		return redpacketAmountFen;
	}

	public void setRedpacketAmountFen(String redpacketAmountFen) {
		this.redpacketAmountFen = redpacketAmountFen;
	}
	
	
	public String getRedpacketMinAmount() {
		return redpacketMinAmount;
	}

	public void setRedpacketMinAmount(String redpacketMinAmount) {
		this.redpacketMinAmount = redpacketMinAmount;
	}
	
	
	public String getRedpacketMaxAmount() {
		return redpacketMaxAmount;
	}

	public void setRedpacketMaxAmount(String redpacketMaxAmount) {
		this.redpacketMaxAmount = redpacketMaxAmount;
	}
	
	
	public String getSendTotalPeople() {
		return sendTotalPeople;
	}

	public void setSendTotalPeople(String sendTotalPeople) {
		this.sendTotalPeople = sendTotalPeople;
	}
	
	
	public String getRedpacketGreetings() {
		return redpacketGreetings;
	}

	public void setRedpacketGreetings(String redpacketGreetings) {
		this.redpacketGreetings = redpacketGreetings;
	}
	
	
	public String getRedpacketStatus() {
		return redpacketStatus;
	}

	public void setRedpacketStatus(String redpacketStatus) {
		this.redpacketStatus = redpacketStatus;
	}
	
	
	public String getHdName() {
		return hdName;
	}

	public void setHdName(String hdName) {
		this.hdName = hdName;
	}
	
	
	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
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

	public String getRedpacketNoStr() {
		return redpacketNoStr;
	}

	public void setRedpacketNoStr(String redpacketNoStr) {
		this.redpacketNoStr = redpacketNoStr;
	}
	
}