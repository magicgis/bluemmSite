package cn.com.bluemoon.jeesite.modules.coupon.entity;

import java.io.Serializable;
import java.math.BigInteger;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;


/**
 * The persistent class for the mall_promotion_coupon_rule database table.
 * 
 */
public class MallPromotionCouponRule extends DataEntity<MallPromotionCouponRule> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger couponRuleId;

	private String conditionType;

	private int conditionValue;

	private BigInteger couponId;

	private Integer denomination;//折扣

	private int giftNum;
	
	private String giftName;

	private String rangeType;//适用范围
	
	//额外字段
	private Double denominationDouble;//折扣字符串
	private Double conditionValueDouble;//满足条件
	
	public MallPromotionCouponRule(BigInteger couponId){
		this.couponId = couponId;
	}

	public MallPromotionCouponRule() {
	}

	public BigInteger getCouponRuleId() {
		return couponRuleId;
	}

	public void setCouponRuleId(BigInteger couponRuleId) {
		this.couponRuleId = couponRuleId;
	}

	public String getConditionType() {
		return this.conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public int getConditionValue() {
		return this.conditionValue;
	}

	public void setConditionValue(int conditionValue) {
		this.conditionValue = conditionValue;
	}

	public BigInteger getCouponId() {
		return couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}


	public Integer getDenomination() {
		return denomination==null?0:denomination;
	}

	public void setDenomination(Integer denomination) {
		this.denomination = denomination;
	}

	public int getGiftNum() {
		return this.giftNum;
	}

	public void setGiftNum(int giftNum) {
		this.giftNum = giftNum;
	}

	public String getRangeType() {
		return this.rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public Double getDenominationDouble() {
		return denominationDouble==null?0:denominationDouble;
	}

	public void setDenominationDouble(Double denominationDouble) {
		this.denominationDouble = denominationDouble;
	}

	public Double getConditionValueDouble() {
		return conditionValueDouble==null?0:conditionValueDouble;
	}

	public void setConditionValueDouble(Double conditionValueDouble) {
		this.conditionValueDouble = conditionValueDouble;
	}

}