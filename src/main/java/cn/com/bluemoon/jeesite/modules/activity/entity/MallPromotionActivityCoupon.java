/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.math.BigInteger;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * mall_promotion_Entity
 * @author linyihao
 * @version 2016-03-24
 */
public class MallPromotionActivityCoupon extends DataEntity<MallPromotionActivityCoupon> {
	
	
	private static final long serialVersionUID = 1L;
	private BigInteger activityCouponId;		// id
	private BigInteger activityId;		// 活动编码
	private String sendCouponType;		// 发券方式（系统发放、自助领取、发工发放）
	private String conditionType;		// 满足条件（发券方式为系统发放时）
	private String position;		// 活动排序（发券方式为自助领取时）
	
	public MallPromotionActivityCoupon() {
		super();
	}

	public MallPromotionActivityCoupon(String id){
		super(id);
	}

	@Length(min=0, max=64, message="发券方式（系统发放、自助领取、发工发放）长度必须介于 0 和 64 之间")
	public String getSendCouponType() {
		return sendCouponType;
	}

	public BigInteger getActivityCouponId() {
		return activityCouponId;
	}

	public void setActivityCouponId(BigInteger activityCouponId) {
		this.activityCouponId = activityCouponId;
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public void setSendCouponType(String sendCouponType) {
		this.sendCouponType = sendCouponType;
	}
	
	@Length(min=0, max=64, message="满足条件（发券方式为系统发放时）长度必须介于 0 和 64 之间")
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	
	@Length(min=0, max=11, message="活动排序（发券方式为自助领取时）长度必须介于 0 和 11 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}