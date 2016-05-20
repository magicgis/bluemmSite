/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallticketsendtemp.entity;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 派发门票Entity
 * @author lingyihao
 * @version 2016-02-26
 */
public class MallTicketSendTemp extends DataEntity<MallTicketSendTemp> {
	
	private static final long serialVersionUID = 1L;
	private String mobile;		// 收票人手机号
	private String sendNum;		// 发票数
	private String isSend;		// 发送是否成功
	private String type;		// 发票类型
	private String actId;
	
	private String couponSName;	//券名称
	
	public MallTicketSendTemp() {
		super();
	}

	public MallTicketSendTemp(String id){
		super(id);
	}

	@Length(min=0, max=16, message="收票人手机号长度必须介于 0 和 16 之间")
	@ExcelField(title="手机号", align=1, sort=20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	@Length(min=0, max=11, message="发票数长度必须介于 0 和 11 之间")
	@ExcelField(title="发送数量", align=1, sort=25)
	public String getSendNum() {
		return sendNum;
	}

	public void setSendNum(String sendNum) {
		this.sendNum = sendNum;
	}
	
	@Length(min=0, max=11, message="发送是否成功长度必须介于 0 和 11 之间")
	@ExcelField(title="是否发送", align=1, sort=30)
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	
	@Length(min=0, max=16, message="发票类型长度必须介于 0 和 16 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=32, message="请填写活动编号")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getCouponSName() {
		return couponSName;
	}

	public void setCouponSName(String couponSName) {
		this.couponSName = couponSName;
	}
	
	
}