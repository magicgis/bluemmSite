/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import net.sf.json.JSONArray;

/**
 * 活动权限表Entity
 * @author linyihao
 * @version 2016-04-19
 */
public class MallPromotionActivityAuth extends DataEntity<MallPromotionActivityAuth> {
	
	private static final long serialVersionUID = 1L;
	private String authId;		// 主键
	private BigInteger activityId;		// 活动ID
	private String activityCode;	//活动编码
	private Integer status;		// 是否有效
	private String creatCode;		// 创建人编号
	private String creatBy;		// 创建人
	private Date creatTime;		// 创建时间
	private String updateCode;		// 修改人编号
	private Date updateTime;		// 修改时间
	private String createCode;		// 创建人编码
	private String createName;     //创建人名称
	private String updatBy; //修改人
	
	//扩展数学系
	private List<MallPromotionActivityAuthAngel> angelUserList; //天使账号
	private String activityName; //活动名称
	private String activitySName; //活动简称
	private Date startTime; //活动开始时间
	private Date endTime; //活动结束时间
	private JSONArray angelJson;
	
	public MallPromotionActivityAuth() {
		super();
	}

	public MallPromotionActivityAuth(String id){
		super(id);
	}

	@Length(min=1, max=32, message="主键长度必须介于 1 和 32 之间")
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}
	
	public BigInteger getActivityId() {
		return activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Length(min=0, max=16, message="创建人编号长度必须介于 0 和 16 之间")
	public String getCreatCode() {
		return creatCode;
	}

	public void setCreatCode(String creatCode) {
		this.creatCode = creatCode;
	}
	
	@Length(min=0, max=16, message="创建人长度必须介于 0 和 16 之间")
	public String getCreatBy() {
		return creatBy;
	}

	public void setCreatBy(String creatBy) {
		this.creatBy = creatBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	@Length(min=0, max=16, message="修改人编号长度必须介于 0 和 16 之间")
	public String getUpdateCode() {
		return updateCode;
	}

	public void setUpdateCode(String updateCode) {
		this.updateCode = updateCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

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

	public List<MallPromotionActivityAuthAngel> getAngelUserList() {
		return angelUserList;
	}

	public void setAngelUserList(List<MallPromotionActivityAuthAngel> angelUserList) {
		this.angelUserList = angelUserList;
	}

	public String getActivitySName() {
		return activitySName;
	}

	public void setActivitySName(String activitySName) {
		this.activitySName = activitySName;
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

	public String getUpdatBy() {
		return updatBy;
	}

	public void setUpdatBy(String updatBy) {
		this.updatBy = updatBy;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public JSONArray getAngelJson() {
		return angelJson;
	}

	public void setAngelJson(JSONArray angelJson) {
		this.angelJson = angelJson;
	}

}