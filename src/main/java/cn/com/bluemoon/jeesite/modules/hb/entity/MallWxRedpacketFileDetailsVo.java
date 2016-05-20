/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.io.Serializable;

/**
 * 导入文件信息Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketFileDetailsVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String fileid;		// 文件编号
	private String merchantOrderNo;		// 商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）
	private String redpacketNo;		// 红包编号hb20150709001000001
	private String referrerNo;		// 推荐人编码
	private String referrerName;		// 推荐人姓名
	private String userOpenid;		// 用户微信账号openid
	private String redpacketAmountYuan;		// 红包金额(元)
	private String redpacketAmountFen;		// 红包金额(分)
	private String redpacketMinAmount;		// 最小红包金额(分)
	private String redpacketMaxAmount;		// 最大红包金额(分)
	private String sendTotalPeople;		// 红包发放人数
	private String redpacketGreetings;		// 红包祝福语
	private String hdName;		// 活动名称
	private String marker;		// 备注
	
	public MallWxRedpacketFileDetailsVo() {
		
	}

	public MallWxRedpacketFileDetailsVo(MallWxRedpacketFileDetails po) {
		this.fileid = po.getFileid();
		this.merchantOrderNo = po.getMerchantOrderNo();
		this.redpacketNo = po.getRedpacketNo();
		this.referrerNo = po.getReferrerNo();
		this.referrerName = po.getReferrerName();
		this.userOpenid = po.getUserOpenid();
		this.redpacketAmountYuan = po.getRedpacketAmountYuan();
		this.redpacketAmountFen = po.getRedpacketAmountFen();
		this.redpacketMinAmount = po.getRedpacketMinAmount();
		this.redpacketMaxAmount = po.getRedpacketMaxAmount();
		this.sendTotalPeople = po.getSendTotalPeople();
		this.redpacketGreetings = po.getRedpacketGreetings();
		this.hdName = po.getHdName();
		this.marker = po.getMarker();
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
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
	
}