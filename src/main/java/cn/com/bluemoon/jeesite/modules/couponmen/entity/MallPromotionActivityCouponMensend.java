/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.couponmen.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.common.utils.excel.annotation.ExcelField;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 人工派券Entity
 * @author linyihao
 * @version 2016-04-19
 */
public class MallPromotionActivityCouponMensend extends DataEntity<MallPromotionActivityCouponMensend> {
	
	private static final long serialVersionUID = 1L;
	private String mensendId;		// 主键
	private String userMobile;		// 用户手机号
	private String activityCode;		// 活动编码
	private String isSend;		// 推送状态（0失败，1成功，2等待）
	private String creatCode;		// 创建人编码
	private String creatBy;		// 创建人
	private Date creatTime;		// 创建时间
	
	//额外字段
	private String couponNum;	//数量（列表展示）
	private Date startTime;		//开始时间（查询）
	private Date endTime;		//结束时间（查询）
	private String activityName;//活动名称（列表展示）
	private String activitySName;//活动简称
	private String couponStr;	//推送券（列表展示）
	private MallPromotionActivityCouponMensendDetail detail;
	private List<MallPromotionActivityCouponMensendDetail> listDetail;
	
	public MallPromotionActivityCouponMensend() {
		super();
	}

	public MallPromotionActivityCouponMensend(String id){
		super(id);
	}

	@Length(min=1, max=32, message="主键长度必须介于 1 和 32 之间")
	public String getMensendId() {
		return mensendId;
	}
	
	public void setMensendId(String mensendId) {
		this.mensendId = mensendId;
	}
	
	@Length(min=0, max=16, message="用户手机号长度必须介于 0 和 16 之间")
	@ExcelField(title="消费者手机号", align=2, sort=1)
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	
	@Length(min=0, max=32, message="活动编码长度必须介于 0 和 32 之间")
	@ExcelField(title="活动编码", align=2, sort=2)
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	
	@Length(min=0, max=11, message="推送状态（0失败，1成功，2等待）长度必须介于 0 和 11 之间")
	@ExcelField(title="推送状态", align=2, sort=6)
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	
	@Length(min=0, max=16, message="创建人编码长度必须介于 0 和 16 之间")
	@ExcelField(title="操作人编号", align=2, sort=7)
	public String getCreatCode() {
		return creatCode;
	}

	public void setCreatCode(String creatCode) {
		this.creatCode = creatCode;
	}
	
	@Length(min=0, max=16, message="创建人长度必须介于 0 和 16 之间")
	@ExcelField(title="操作人", align=2, sort=8)
	public String getCreatBy() {
		return creatBy;
	}

	public void setCreatBy(String creatBy) {
		this.creatBy = creatBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作时间", align=2, sort=9)
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@ExcelField(title="推送券", align=2, sort=5)
	public String getCouponStr() {
		return couponStr;
	}

	public void setCouponStr(String couponStr) {
		this.couponStr = couponStr;
	}

	public MallPromotionActivityCouponMensendDetail getDetail() {
		return detail;
	}

	public void setDetail(MallPromotionActivityCouponMensendDetail detail) {
		this.detail = detail;
	}

	@ExcelField(title="活动名称", align=2, sort=3)
	public String getActivitySName() {
		return activitySName;
	}

	public void setActivitySName(String activitySName) {
		this.activitySName = activitySName;
	}

	public List<MallPromotionActivityCouponMensendDetail> getListDetail() {
		return listDetail;
	}

	public void setListDetail(
			List<MallPromotionActivityCouponMensendDetail> listDetail) {
		this.listDetail = listDetail;
	}

	@ExcelField(title="推送券数", align=2, sort=4)
	public String getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(String couponNum) {
		this.couponNum = couponNum;
	}
	
}