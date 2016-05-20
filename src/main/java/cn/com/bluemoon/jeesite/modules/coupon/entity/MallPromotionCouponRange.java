package cn.com.bluemoon.jeesite.modules.coupon.entity;

import java.io.Serializable;
import java.math.BigInteger;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;


/**
 * The persistent class for the mall_promotion_coupon_range database table.
 * 
 */
public class MallPromotionCouponRange extends DataEntity<MallPromotionCouponRange> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger couponRangeId;

	private BigInteger couponId;

	private String sku;

	private String name;

	private int num;

	private int priority;
	
	public MallPromotionCouponRange(BigInteger couponId) {
		this.couponId = couponId;
	}

	public MallPromotionCouponRange() {
	}

	public BigInteger getCouponRangeId() {
		return couponRangeId;
	}

	public void setCouponRangeId(BigInteger couponRangeId) {
		this.couponRangeId = couponRangeId;
	}

	public BigInteger getCouponId() {
		return couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}