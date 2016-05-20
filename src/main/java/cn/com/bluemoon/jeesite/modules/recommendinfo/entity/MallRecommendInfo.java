/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 推荐码申请审核Entity
 * @author xgb
 * @version 2016-05-03
 */
public class MallRecommendInfo extends DataEntity<MallRecommendInfo> {
	
	private static final long serialVersionUID = 1L;
	private String orderCode;		// 订单编号
	private String oldRecommend;		// 原推荐码
	private String newRecommend;		// 修改后推荐码
	private String updatBy; //修改人
	private Date updateTime;		// 修改时间
	private String aduitStatus;		// 审核状态:0,提交未审核,1:审核通过,2:审核未通过
	private String aduitReason;		// 审核原因说明
	private String aduitBy;		// 审核人
	private Date aduitTime;		// 审核时间
	private Date orderPayTime;		// 订单付款时间
	private String orderMoney;		// 订单金额,单位分
	private String registrant;		// 订单下单时对应的注册账户昵称
	private String registrantMobile;		// 注册人手机号
	private String pictureFirst; //上传凭证1
	private String pictureSecond;//上传凭证2
	
	//新增
	private String haveRecommend;//是否有推荐码，0，没有，1有
	private Date startTime;//
	private Date endTime;
	
	
	public MallRecommendInfo() {
		super();
	}

	public MallRecommendInfo(String id){
		super(id);
	}

	@Length(min=0, max=32, message="订单编号长度必须介于 0 和 32 之间")
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	@Length(min=0, max=32, message="原推荐码长度必须介于 0 和 32 之间")
	public String getOldRecommend() {
		return oldRecommend;
	}

	public void setOldRecommend(String oldRecommend) {
		this.oldRecommend = oldRecommend;
	}
	
	@Length(min=0, max=32, message="修改后推荐码长度必须介于 0 和 32 之间")
	public String getNewRecommend() {
		return newRecommend;
	}

	public void setNewRecommend(String newRecommend) {
		this.newRecommend = newRecommend;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Length(min=0, max=2, message="审核状态:0,提交未审核,1:审核通过,2:审核未通过长度必须介于 0 和 2 之间")
	public String getAduitStatus() {
		return aduitStatus;
	}

	public void setAduitStatus(String aduitStatus) {
		this.aduitStatus = aduitStatus;
	}
	
	@Length(min=0, max=500, message="审核原因说明长度必须介于 0 和 500 之间")
	public String getAduitReason() {
		return aduitReason;
	}

	public void setAduitReason(String aduitReason) {
		this.aduitReason = aduitReason;
	}
	
	@Length(min=0, max=32, message="审核人长度必须介于 0 和 32 之间")
	public String getAduitBy() {
		return aduitBy;
	}

	public void setAduitBy(String aduitBy) {
		this.aduitBy = aduitBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAduitTime() {
		return aduitTime;
	}

	public void setAduitTime(Date aduitTime) {
		this.aduitTime = aduitTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Date orderPayTime) {
		this.orderPayTime = orderPayTime;
	}
	
	@Length(min=0, max=11, message="订单金额,单位分长度必须介于 0 和 11 之间")
	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}
	
	@Length(min=0, max=32, message="订单下单时对应的注册账户昵称长度必须介于 0 和 32 之间")
	public String getRegistrant() {
		return registrant;
	}

	public void setRegistrant(String registrant) {
		this.registrant = registrant;
	}
	
	@Length(min=0, max=11, message="注册人手机号长度必须介于 0 和 11 之间")
	public String getRegistrantMobile() {
		return registrantMobile;
	}

	public void setRegistrantMobile(String registrantMobile) {
		this.registrantMobile = registrantMobile;
	}

	public String getPictureFirst() {
		return pictureFirst;
	}

	public void setPictureFirst(String pictureFirst) {
		this.pictureFirst = pictureFirst;
	}

	public String getPictureSecond() {
		return pictureSecond;
	}

	public void setPictureSecond(String pictureSecond) {
		this.pictureSecond = pictureSecond;
	}

	public String getUpdatBy() {
		return updatBy;
	}

	public void setUpdatBy(String updatBy) {
		this.updatBy = updatBy;
	}

	public String getHaveRecommend() {
		return haveRecommend;
	}

	public void setHaveRecommend(String haveRecommend) {
		this.haveRecommend = haveRecommend;
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