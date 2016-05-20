package cn.com.bluemoon.jeesite.modules.coupon.entity;

import java.io.Serializable;
import java.math.BigInteger;

public class MallPromotionCouponRangeVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger couponRangeId;

	private BigInteger couponId;

	private String sku;

	private String name;

	private int num;

	private int priority;
	
	public MallPromotionCouponRangeVo(MallPromotionCouponRange po) {
		this.couponRangeId = po.getCouponId();
		this.couponId = po.getCouponId();
		this.sku = po.getSku();
		this.name = po.getName();
		this.num = po.getNum();
		this.priority = po.getPriority();
	}

	public MallPromotionCouponRangeVo() {
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