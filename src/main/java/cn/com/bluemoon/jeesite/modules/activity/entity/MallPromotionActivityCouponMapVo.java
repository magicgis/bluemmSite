package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * The persistent class for the mall_promotion_activity_coupon_map database table.
 * 
 */
public class MallPromotionActivityCouponMapVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger couponId;
	private String couponCode;
	private String couponName;
	private String couponSName;
	
	public MallPromotionActivityCouponMapVo() {
	}

	public MallPromotionActivityCouponMapVo(MallPromotionActivityCouponMap po) {
		this.couponId = po.getCouponId();
		this.couponCode = po.getCoupon().getCouponCode();
		this.couponName = po.getCoupon().getCouponName();
		this.couponSName = po.getCoupon().getCouponSName();
	}
	

	public BigInteger getCouponId() {
		return couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponSName() {
		return couponSName;
	}

	public void setCouponSName(String couponSName) {
		this.couponSName = couponSName;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

}