/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.math.BigInteger;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * mall_promotion_Entity
 * @author linyihao
 * @version 2016-03-24
 */
public class MallPromotionActivityMjs extends DataEntity<MallPromotionActivityMjs> {
	
	private static final long serialVersionUID = 1L;
	private BigInteger activityMjsId;		// activity_mjs_id
	private BigInteger activityId;		// activity_id
	private String conditionType;		// condition_type
	
	public MallPromotionActivityMjs() {
		super();
	}
	
	public MallPromotionActivityMjs(String id){
		super(id);
	}

	public BigInteger getActivityMjsId() {
		return activityMjsId;
	}

	public void setActivityMjsId(BigInteger activityMjsId) {
		this.activityMjsId = activityMjsId;
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	@Length(min=0, max=32, message="condition_type长度必须介于 0 和 32 之间")
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	
}