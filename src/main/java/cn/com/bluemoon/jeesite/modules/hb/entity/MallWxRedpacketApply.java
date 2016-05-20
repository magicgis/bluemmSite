/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.util.Date;

import net.sf.json.JSONArray;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 红包申请Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketApply extends DataEntity<MallWxRedpacketApply> {
	
	private static final long serialVersionUID = 1L;
	private String applyid;		// 申请编号
	private String processinstid;		// 流程实例号
	private String merchantNo;		// 商户号
	private String merchantName;		// 商户名称
	private String fileid;		// 导入文件id
	private String redpacketTotalCount;		// 红包总个数
	private String redpacketTotalAmount;		// 红包总金额
	private String applystatus;		// 申请状态1未提交2提交3审核通过4审核退回5已发放
	private String applyercode;		// 申请人
	private String applyername;		// 申请人名称
	private Date applydate;			// 申请日期
	private String checkercode;		// 审核人
	private String checkername;		// checkername
	private Date checkdate;		// 审核时间
	private String backReason;		// 不同意原因
	private String sendStatus;		// 发放状态1未发放2发放中3已发放4锁定
	private String isdeleted;		// 是否被删除0否1是
	private String operator;		// 操作人
	private Date operatortime;		// 操作时间
	
	//额外字段
	private Date startTime;			//申请开始时间(查询)
	private Date endTime;			//申请结束时间(查询)
	private String provider;		//提供方名称
	private String webchatPublicNo;	//公众号
	private JSONArray merchantJson;
	private String checkValue;		//审核是否通过
	private String sendCode;		//发放人编码
	private String sendName;		//发放人
	private Date startAduitTime;	//审核开始时间(查询)
	private Date endAduitTime;	//审核结束时间(查询)
	
	public MallWxRedpacketApply() {
		super();
	}

	public MallWxRedpacketApply(String id){
		super(id);
	}

	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}
	
	public String getProcessinstid() {
		return processinstid;
	}

	public void setProcessinstid(String processinstid) {
		this.processinstid = processinstid;
	}
	
	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	
	public String getRedpacketTotalCount() {
		return redpacketTotalCount;
	}

	public void setRedpacketTotalCount(String redpacketTotalCount) {
		this.redpacketTotalCount = redpacketTotalCount;
	}
	
	public String getRedpacketTotalAmount() {
		return redpacketTotalAmount;
	}

	public void setRedpacketTotalAmount(String redpacketTotalAmount) {
		this.redpacketTotalAmount = redpacketTotalAmount;
	}
	
	public String getApplystatus() {
		return applystatus;
	}

	public void setApplystatus(String applystatus) {
		this.applystatus = applystatus;
	}
	
	public String getApplyercode() {
		return applyercode;
	}

	public void setApplyercode(String applyercode) {
		this.applyercode = applyercode;
	}
	
	public String getApplyername() {
		return applyername;
	}

	public void setApplyername(String applyername) {
		this.applyername = applyername;
	}
	
	public Date getApplydate() {
		return applydate;
	}

	public void setApplydate(Date applydate) {
		this.applydate = applydate;
	}
	
	public String getCheckercode() {
		return checkercode;
	}

	public void setCheckercode(String checkercode) {
		this.checkercode = checkercode;
	}
	
	public String getCheckername() {
		return checkername;
	}

	public void setCheckername(String checkername) {
		this.checkername = checkername;
	}
	
	public Date getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(Date checkdate) {
		this.checkdate = checkdate;
	}

	public String getBackReason() {
		return backReason;
	}

	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	
	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
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

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getWebchatPublicNo() {
		return webchatPublicNo;
	}

	public void setWebchatPublicNo(String webchatPublicNo) {
		this.webchatPublicNo = webchatPublicNo;
	}

	public JSONArray getMerchantJson() {
		return merchantJson;
	}

	public void setMerchantJson(JSONArray merchantJson) {
		this.merchantJson = merchantJson;
	}

	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	public String getSendCode() {
		return sendCode;
	}

	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public Date getStartAduitTime() {
		return startAduitTime;
	}

	public void setStartAduitTime(Date startAduitTime) {
		this.startAduitTime = startAduitTime;
	}

	public Date getEndAduitTime() {
		return endAduitTime;
	}

	public void setEndAduitTime(Date endAduitTime) {
		this.endAduitTime = endAduitTime;
	}

}