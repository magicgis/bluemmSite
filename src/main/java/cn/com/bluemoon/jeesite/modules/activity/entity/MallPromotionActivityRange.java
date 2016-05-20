package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * The persistent class for the mall_promotion_activity_range database table.
 * 
 */
public class MallPromotionActivityRange extends cn.com.bluemoon.jeesite.common.persistence.DataEntity<MallPromotionActivityRange> implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger activityRangeId;

	private BigInteger activityId;

	private String itemName;

	private String itemSku;
	
	public MallPromotionActivityRange(BigInteger activityId) {
		this.activityId = activityId;
	}

	public MallPromotionActivityRange() {
	}

	public BigInteger getActivityRangeId() {
		return activityRangeId;
	}

	public void setActivityRangeId(BigInteger activityRangeId) {
		this.activityRangeId = activityRangeId;
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemSku() {
		return this.itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

}