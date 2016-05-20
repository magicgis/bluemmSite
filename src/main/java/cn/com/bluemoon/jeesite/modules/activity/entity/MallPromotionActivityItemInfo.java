package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;



public class MallPromotionActivityItemInfo extends cn.com.bluemoon.jeesite.common.persistence.DataEntity<MallPromotionActivityItemInfo> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger activityItemId;

	private BigInteger activityId;

	private String activityItemType;
	
	private Integer activityItemValue;
	
	private String restrictionType;
	
	private Integer restrictionValue;

	public MallPromotionActivityItemInfo() {
	}

	public BigInteger getActivityItemId() {
		return activityItemId;
	}

	public void setActivityItemId(BigInteger activityItemId) {
		this.activityItemId = activityItemId;
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public String getActivityItemType() {
		return activityItemType;
	}

	public void setActivityItemType(String activityItemType) {
		this.activityItemType = activityItemType;
	}

	public Integer getActivityItemValue() {
		return activityItemValue;
	}

	public void setActivityItemValue(Integer activityItemValue) {
		this.activityItemValue = activityItemValue;
	}

	public String getRestrictionType() {
		return restrictionType;
	}

	public void setRestrictionType(String restrictionType) {
		this.restrictionType = restrictionType;
	}

	public Integer getRestrictionValue() {
		return restrictionValue;
	}

	public void setRestrictionValue(Integer restrictionValue) {
		this.restrictionValue = restrictionValue;
	}

}