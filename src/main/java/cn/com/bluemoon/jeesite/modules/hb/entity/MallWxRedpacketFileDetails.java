/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 导入文件信息Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketFileDetails extends DataEntity<MallWxRedpacketFileDetails> {
	
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
	
	public MallWxRedpacketFileDetails() {
		super();
	}

	public MallWxRedpacketFileDetails(String id){
		super(id);
	}

	@Length(min=1, max=13, message="文件编号长度必须介于 1 和 13 之间")
	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	
	@Length(min=0, max=30, message="商户订单号:商户号+年（4位）+月（2位）+日（2位）+序号（10位）长度必须介于 0 和 30 之间")
	public String getMerchantOrderNo() {
		return merchantOrderNo;
	}

	public void setMerchantOrderNo(String merchantOrderNo) {
		this.merchantOrderNo = merchantOrderNo;
	}
	
	@Length(min=0, max=20, message="红包编号hb20150709001000001长度必须介于 0 和 20 之间")
	@ExcelField(title="红包编号", align=2, sort=10)
	public String getRedpacketNo() {
		return redpacketNo;
	}

	public void setRedpacketNo(String redpacketNo) {
		this.redpacketNo = redpacketNo;
	}
	
	@Length(min=0, max=8, message="推荐人编码长度必须介于 0 和 8 之间")
	@ExcelField(title="推荐人编号", align=2, sort=20)
	public String getReferrerNo() {
		return referrerNo;
	}

	public void setReferrerNo(String referrerNo) {
		this.referrerNo = referrerNo;
	}
	
	@Length(min=0, max=10, message="推荐人姓名长度必须介于 0 和 10 之间")
	@ExcelField(title="推荐人姓名", align=2, sort=30)
	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}
	
	@Length(min=0, max=50, message="用户微信账号openid长度必须介于 0 和 50 之间")
	@ExcelField(title="用户微信openid", align=2, sort=40)
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
	
	@Length(min=0, max=10, message="红包金额(分)长度必须介于 0 和 10 之间")
	@ExcelField(title="付款金额（分）", align=2, sort=50)
	public String getRedpacketAmountFen() {
		return redpacketAmountFen;
	}

	public void setRedpacketAmountFen(String redpacketAmountFen) {
		this.redpacketAmountFen = redpacketAmountFen;
	}
	
	@Length(min=0, max=10, message="最小红包金额(分)长度必须介于 0 和 10 之间")
	public String getRedpacketMinAmount() {
		return redpacketMinAmount;
	}

	public void setRedpacketMinAmount(String redpacketMinAmount) {
		this.redpacketMinAmount = redpacketMinAmount;
	}
	
	@Length(min=0, max=10, message="最大红包金额(分)长度必须介于 0 和 10 之间")
	public String getRedpacketMaxAmount() {
		return redpacketMaxAmount;
	}

	public void setRedpacketMaxAmount(String redpacketMaxAmount) {
		this.redpacketMaxAmount = redpacketMaxAmount;
	}
	
	@Length(min=0, max=10, message="红包发放人数长度必须介于 0 和 10 之间")
	@ExcelField(title="红包发放总人数", align=2, sort=60)
	public String getSendTotalPeople() {
		return sendTotalPeople;
	}

	public void setSendTotalPeople(String sendTotalPeople) {
		this.sendTotalPeople = sendTotalPeople;
	}
	
	@Length(min=0, max=50, message="红包祝福语长度必须介于 0 和 50 之间")
	@ExcelField(title="红包祝福语", align=2, sort=70)
	public String getRedpacketGreetings() {
		return redpacketGreetings;
	}

	public void setRedpacketGreetings(String redpacketGreetings) {
		this.redpacketGreetings = redpacketGreetings;
	}
	
	@Length(min=0, max=50, message="活动名称长度必须介于 0 和 50 之间")
	@ExcelField(title="活动名称", align=2, sort=80)
	public String getHdName() {
		return hdName;
	}

	public void setHdName(String hdName) {
		this.hdName = hdName;
	}
	
	@Length(min=0, max=250, message="备注长度必须介于 0 和 250 之间")
	@ExcelField(title="备注", align=2, sort=90)
	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}
	
}