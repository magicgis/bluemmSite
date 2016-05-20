package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * The persistent class for the mall_promotion_activity_mjs database table.
 * 
 */
public class MallPromotionActivityMj extends cn.com.bluemoon.jeesite.common.persistence.DataEntity<MallPromotionActivityMj> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger activityId;

	private BigInteger activityMjsId;

	private String conditionType;

	public MallPromotionActivityMj() {
	}

	public BigInteger getActivityId() {
		return this.activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public BigInteger getActivityMjsId() {
		return this.activityMjsId;
	}

	public void setActivityMjsId(BigInteger activityMjsId) {
		this.activityMjsId = activityMjsId;
	}

	public String getConditionType() {
		return this.conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

}