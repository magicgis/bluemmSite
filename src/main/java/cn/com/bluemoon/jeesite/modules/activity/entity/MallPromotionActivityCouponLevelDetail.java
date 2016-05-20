package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;



/**
 * The persistent class for the mall_promotion_activity_coupon_level_detail database table.
 * 
 */
public class MallPromotionActivityCouponLevelDetail extends cn.com.bluemoon.jeesite.common.persistence.DataEntity<MallPromotionActivityCouponLevelDetail> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String couponLevelDetailId;

	private BigInteger couponId;

	private BigInteger couponLevelId;

	private int sendNum;
	
	private String couponSName;

	public MallPromotionActivityCouponLevelDetail() {
	}

	public String getCouponLevelDetailId() {
		return this.couponLevelDetailId;
	}

	public void setCouponLevelDetailId(String couponLevelDetailId) {
		this.couponLevelDetailId = couponLevelDetailId;
	}

	public BigInteger getCouponId() {
		return this.couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}

	public BigInteger getCouponLevelId() {
		return this.couponLevelId;
	}

	public void setCouponLevelId(BigInteger couponLevelId) {
		this.couponLevelId = couponLevelId;
	}

	public int getSendNum() {
		return this.sendNum;
	}

	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}

	public String getCouponSName() {
		return couponSName;
	}

	public void setCouponSName(String couponSName) {
		this.couponSName = couponSName;
	}

}