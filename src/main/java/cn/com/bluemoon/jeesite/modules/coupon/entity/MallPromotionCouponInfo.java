package cn.com.bluemoon.jeesite.modules.coupon.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

public class MallPromotionCouponInfo extends DataEntity<MallPromotionCouponInfo> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigInteger couponId;

	private String couponCode;

	private String couponName;

	private String couponSName;

	private String couponType;

	private int deleteFlag;

	private String direction;

	private Date endTime;

	private String giftType;

	private String op;

	private String opCode;

	private Date opTime;

	private String remarks;

	private Date startTime;

	private Integer status;

	private String timeType;

	private Date updateDate;
	
	private Integer validDays;
	
	private String createUserName;
	
	private String createUserCode;
	
	//额外字段
	private MallPromotionCouponRule rule;
	private List<MallPromotionCouponRange> listRange;
	private JSONArray listRangeJson;
	private String couponStatus;
	private String rangeType;
	private String validDaysStr;
	
	public MallPromotionCouponInfo() {
	}

	public BigInteger getCouponId() {
		return couponId;
	}

	public void setCouponId(BigInteger couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return this.couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponName() {
		return this.couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponSName() {
		return this.couponSName;
	}

	public void setCouponSName(String couponSName) {
		this.couponSName = couponSName;
	}

	public String getCouponType() {
		return this.couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getDirection() {
		return this.direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getGiftType() {
		return this.giftType;
	}

	public void setGiftType(String giftType) {
		this.giftType = giftType;
	}

	public String getOp() {
		return this.op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getOpCode() {
		return this.opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public Date getOpTime() {
		return this.opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getStatus() {
		return this.status==null?-1:this.status;//mybaties不支持0
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Integer getValidDays() {
		return validDays==null?0:validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public String getRangeType() {
		return rangeType;
	}

	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserCode() {
		return createUserCode;
	}

	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public MallPromotionCouponRule getRule() {
		return rule;
	}

	public void setRule(MallPromotionCouponRule rule) {
		this.rule = rule;
	}

	public List<MallPromotionCouponRange> getListRange() {
		return listRange;
	}

	public void setListRange(List<MallPromotionCouponRange> listRange) {
		this.listRange = listRange;
	}

	public JSONArray getListRangeJson() {
		return listRangeJson;
	}

	public void setListRangeJson(JSONArray listRangeJson) {
		this.listRangeJson = listRangeJson;
	}

	public String getValidDaysStr() {
		return validDaysStr;
	}

	public void setValidDaysStr(String validDaysStr) {
		this.validDaysStr = validDaysStr;
	}

	
}