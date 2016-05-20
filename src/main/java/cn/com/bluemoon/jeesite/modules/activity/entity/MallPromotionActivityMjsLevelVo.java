package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;

public class MallPromotionActivityMjsLevelVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger activityId;

	private int conditionValue;

	private String giftName;

	private int giftNum;

	private String giftSku;

	private String giftType;
	
	private int giftPrice;

	private int level;

	private Long mjsLevelId;

	private String preferentialType;

	private int preferentialValue;
	
	private String giftPicture;
	
	//额外字段
	private double conditionValueDouble;
	private double preferentialValueDouble;
	private float giftPriceFloat;

	public MallPromotionActivityMjsLevelVo() {
	}

	public MallPromotionActivityMjsLevelVo(MallPromotionActivityMjsLevel po) {
		this.activityId = po.getActivityId();
		this.conditionValue= po.getConditionValue();
		this.giftName= po.getGiftName();
		this.giftNum= po.getGiftNum();
		this.giftSku= po.getGiftSku();
		this.giftType= po.getGiftType();
		this.giftPrice= po.getGiftPrice();
		this.level= po.getLevel();
		this.mjsLevelId= po.getMjsLevelId();
		this.preferentialType= po.getPreferentialType();
		this.preferentialValue= po.getPreferentialValue();
		this.giftPicture= po.getGiftPicture();
		this.conditionValueDouble = po.getConditionValue();
		this.preferentialValueDouble = po.getPreferentialValue();
	}
	
	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public int getConditionValue() {
		return this.conditionValue;
	}

	public void setConditionValue(int conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getGiftName() {
		return this.giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public int getGiftNum() {
		return this.giftNum;
	}

	public void setGiftNum(int giftNum) {
		this.giftNum = giftNum;
	}

	public String getGiftSku() {
		return this.giftSku;
	}

	public void setGiftSku(String giftSku) {
		this.giftSku = giftSku;
	}

	public String getGiftType() {
		return giftType;
	}

	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Long getMjsLevelId() {
		return mjsLevelId;
	}

	public void setMjsLevelId(Long mjsLevelId) {
		this.mjsLevelId = mjsLevelId;
	}

	public String getPreferentialType() {
		return this.preferentialType;
	}

	public void setPreferentialType(String preferentialType) {
		this.preferentialType = preferentialType;
	}

	public int getPreferentialValue() {
		return this.preferentialValue;
	}

	public void setPreferentialValue(int preferentialValue) {
		this.preferentialValue = preferentialValue;
	}

	public int getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(int giftPrice) {
		this.giftPrice = giftPrice;
	}

	public String getGiftPicture() {
		return giftPicture;
	}

	public void setGiftPicture(String giftPicture) {
		this.giftPicture = giftPicture;
	}

	public double getPreferentialValueDouble() {
		return preferentialValueDouble;
	}

	public void setPreferentialValueDouble(double preferentialValueDouble) {
		this.preferentialValueDouble = preferentialValueDouble;
	}

	public double getConditionValueDouble() {
		return conditionValueDouble;
	}

	public void setConditionValueDouble(double conditionValueDouble) {
		this.conditionValueDouble = conditionValueDouble;
	}

	public float getGiftPriceFloat() {
		return giftPriceFloat;
	}

	public void setGiftPriceFloat(float giftPriceFloat) {
		this.giftPriceFloat = giftPriceFloat;
	}

}