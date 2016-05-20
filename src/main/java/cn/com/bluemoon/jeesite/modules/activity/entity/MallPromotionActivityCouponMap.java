package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;

import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponInfo;


/**
 * The persistent class for the mall_promotion_activity_coupon_map database table.
 * 
 */
public class MallPromotionActivityCouponMap extends cn.com.bluemoon.jeesite.common.persistence.DataEntity<MallPromotionActivityCouponMap> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String mapId;

	private BigInteger activityId;

	private BigInteger couponId;

	private int totalLimit;

	private int userLimit;

	private int userUsableLimit;
	
	//额外字段
	private MallPromotionCouponInfo coupon; 

	public MallPromotionActivityCouponMap() {
	}

	public String getMapId() {
		return this.mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public BigInteger getActivityId() {
		return this.activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public BigInteger getCouponId() {
		return this.couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}

	public int getTotalLimit() {
		return this.totalLimit;
	}

	public void setTotalLimit(int totalLimit) {
		this.totalLimit = totalLimit;
	}

	public int getUserLimit() {
		return this.userLimit;
	}

	public void setUserLimit(int userLimit) {
		this.userLimit = userLimit;
	}

	public int getUserUsableLimit() {
		return this.userUsableLimit;
	}

	public void setUserUsableLimit(int userUsableLimit) {
		this.userUsableLimit = userUsableLimit;
	}

	public MallPromotionCouponInfo getCoupon() {
		return coupon;
	}

	public void setCoupon(MallPromotionCouponInfo coupon) {
		this.coupon = coupon;
	}

}