/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * mall_promotion_Entity
 * @author linyihao
 * @version 2016-03-24
 */
public class MallPromotionActivityCouponLevel extends DataEntity<MallPromotionActivityCouponLevel> {
	
	
	private static final long serialVersionUID = 1L;
	private BigInteger couponLevelId;		// id
	private BigInteger activityId;		// activity_id
	private String level;		// 优惠级别
	private String conditionValue;		// 优惠条件
	private List<MallPromotionActivityCouponLevelDetail> levelDetail;
	private String levelDetailStr;
	
	public MallPromotionActivityCouponLevel() {
		super();
	}

	public MallPromotionActivityCouponLevel(String id){
		super(id);
	}

	
	
	public BigInteger getCouponLevelId() {
		return couponLevelId;
	}

	public void setCouponLevelId(BigInteger couponLevelId) {
		this.couponLevelId = couponLevelId;
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	@Length(min=0, max=11, message="优惠级别长度必须介于 0 和 11 之间")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	@Length(min=0, max=11, message="优惠条件长度必须介于 0 和 11 之间")
	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public List<MallPromotionActivityCouponLevelDetail> getLevelDetail() {
		return levelDetail;
	}

	public void setLevelDetail(List<MallPromotionActivityCouponLevelDetail> levelDetail) {
		this.levelDetail = levelDetail;
	}

	public String getLevelDetailStr() {
		return levelDetailStr;
	}

	public void setLevelDetailStr(String levelDetailStr) {
		this.levelDetailStr = levelDetailStr;
	}

}