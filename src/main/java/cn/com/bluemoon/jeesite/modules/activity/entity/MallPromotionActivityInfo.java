/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * mall_promotion_Entity
 * @author linyihao
 * @version 2016-03-24
 */
public class MallPromotionActivityInfo extends DataEntity<MallPromotionActivityInfo> {
	
	private static final long serialVersionUID = 1L;
	private BigInteger activityId;		// id
	private String activityCode;		// 活动编码
	private String activityTheme;		// 所属活动类别（活动大类）
	private String activityType;		// activity_type
	private String activityName;		// 活动名称
	private String activitySName;		// 活动简称
	private Date startTime;		// 活动开始时间
	private Date endTime;		// 活动结束时间
	private String activityDesc;		// 活动说明
	private String rangeType;		// 活动范围标识
	private Integer status;		// status
	private String createCode;		// 创建人编码
	private String updatBy; //更新人
	
	//额外字段
	private String createName;
	private String condition;
	private String sendCouponType;
	private List<MallPromotionActivityRange> range;
	private MallPromotionActivityCoupon coupon;
	private List<MallPromotionActivityCouponLevel> couponLevel;
	private List<MallPromotionActivityCouponLevelDetail> couponLevelDetail;
	private List<MallPromotionActivityCouponMap> couponMap;
	private JSONArray rangeJson;
	private List<MallPromotionActivityMjsLevel> level;
	private JSONArray levelJson;
	private MallPromotionActivityItemInfo itemInfo;
	private BigInteger activityItemId;
	private String activityItemType;
	private Integer activityItemValue;
	private String restrictionType;
	private Integer restrictionValue;
	private int pageCount;
	
	
	public MallPromotionActivityInfo() {
		super();
	}

	public MallPromotionActivityInfo(String id){
		super(id);
	}

	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	@Length(min=0, max=64, message="所属活动类别（活动大类）长度必须介于 0 和 64 之间")
	public String getActivityTheme() {
		return activityTheme;
	}

	public void setActivityTheme(String activityTheme) {
		this.activityTheme = activityTheme;
	}
	
	@Length(min=0, max=32, message="activity_type长度必须介于 0 和 32 之间")
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	@Length(min=0, max=64, message="活动名称长度必须介于 0 和 64 之间")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	@Length(min=0, max=64, message="活动简称长度必须介于 0 和 64 之间")
	public String getActivitySName() {
		return activitySName;
	}

	public void setActivitySName(String activitySName) {
		this.activitySName = activitySName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=256, message="活动说明长度必须介于 0 和 256 之间")
	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	
	@Length(min=0, max=32, message="活动范围标识长度必须介于 0 和 32 之间")
	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(min=0, max=64, message="创建人编码长度必须介于 0 和 64 之间")
	public String getCreateCode() {
		return createCode;
	}

	public void setCreateCode(String createCode) {
		this.createCode = createCode;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public List<MallPromotionActivityRange> getRange() {
		return range;
	}

	public void setRange(List<MallPromotionActivityRange> range) {
		this.range = range;
	}

	public JSONArray getRangeJson() {
		return rangeJson;
	}

	public void setRangeJson(JSONArray rangeJson) {
		this.rangeJson = rangeJson;
	}

	public List<MallPromotionActivityMjsLevel> getLevel() {
		return level;
	}

	public void setLevel(List<MallPromotionActivityMjsLevel> level) {
		this.level = level;
	}

	public JSONArray getLevelJson() {
		return levelJson;
	}

	public void setLevelJson(JSONArray levelJson) {
		this.levelJson = levelJson;
	}

	public String getSendCouponType() {
		return sendCouponType;
	}

	public void setSendCouponType(String sendCouponType) {
		this.sendCouponType = sendCouponType;
	}

	public MallPromotionActivityCoupon getCoupon() {
		return coupon;
	}

	public void setCoupon(MallPromotionActivityCoupon coupon) {
		this.coupon = coupon;
	}

	public List<MallPromotionActivityCouponLevel> getCouponLevel() {
		return couponLevel;
	}

	public void setCouponLevel(List<MallPromotionActivityCouponLevel> couponLevel) {
		this.couponLevel = couponLevel;
	}

	public List<MallPromotionActivityCouponLevelDetail> getCouponLevelDetail() {
		return couponLevelDetail;
	}

	public void setCouponLevelDetail(List<MallPromotionActivityCouponLevelDetail> couponLevelDetail) {
		this.couponLevelDetail = couponLevelDetail;
	}

	public List<MallPromotionActivityCouponMap> getCouponMap() {
		return couponMap;
	}

	public void setCouponMap(List<MallPromotionActivityCouponMap> couponMap) {
		this.couponMap = couponMap;
	}

	public MallPromotionActivityItemInfo getItemInfo() {
		return itemInfo;
	}

	public void setItemInfo(MallPromotionActivityItemInfo itemInfo) {
		this.itemInfo = itemInfo;
	}

	public BigInteger getActivityItemId() {
		return activityItemId;
	}

	public void setActivityItemId(BigInteger activityItemId) {
		this.activityItemId = activityItemId;
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

	public String getUpdatBy() {
		return updatBy;
	}

	public void setUpdatBy(String updatBy) {
		this.updatBy = updatBy;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
}