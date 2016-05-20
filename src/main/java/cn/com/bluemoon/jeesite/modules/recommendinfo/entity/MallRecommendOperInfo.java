/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 推荐码明细Entity
 * @author xgb
 * @version 2016-05-03
 */
public class MallRecommendOperInfo extends DataEntity<MallRecommendOperInfo> {
	
	private static final long serialVersionUID = 1L;
	private String recommendInfoId;		// 推荐码信息id
	private String updatBy;			//修改人
	private Date updateTime;		// 修改时间
	private String oldRecommend;		// 原推荐码
	private String newRecommend;		// 修改后推荐码
	private String pictureFirst;		// 上传凭证1
	private String pictureSecond;		// 上传凭证2
	private String aduitStatus;		// 审核状态:0,提交未审核,1:审核通过,2:审核未通过
	private String aduitReason;		// 审核原因说明
	private String aduitBy;		// 审核人
	private Date aduitTime;		// 审核时间
	
	private int index;
	
	public MallRecommendOperInfo() {
		super();
	}

	public MallRecommendOperInfo(String id){
		super(id);
	}

	@Length(min=0, max=32, message="推荐码信息id长度必须介于 0 和 32 之间")
	public String getRecommendInfoId() {
		return recommendInfoId;
	}

	public void setRecommendInfoId(String recommendInfoId) {
		this.recommendInfoId = recommendInfoId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Length(min=0, max=32, message="原推荐码长度必须介于 0 和 32 之间")
	public String getOldRecommend() {
		return oldRecommend;
	}

	public void setOldRecommend(String oldRecommend) {
		this.oldRecommend = oldRecommend;
	}
	
	@Length(min=0, max=32, message="修改后推荐码长度必须介于 0 和 32 之间")
	public String getNewRecommend() {
		return newRecommend;
	}

	public void setNewRecommend(String newRecommend) {
		this.newRecommend = newRecommend;
	}
	
	@Length(min=0, max=500, message="上传凭证1长度必须介于 0 和 500 之间")
	public String getPictureFirst() {
		return pictureFirst;
	}

	public void setPictureFirst(String pictureFirst) {
		this.pictureFirst = pictureFirst;
	}
	
	@Length(min=0, max=500, message="上传凭证2长度必须介于 0 和 500 之间")
	public String getPictureSecond() {
		return pictureSecond;
	}

	public void setPictureSecond(String pictureSecond) {
		this.pictureSecond = pictureSecond;
	}
	
	@Length(min=0, max=10, message="审核状态:0,提交未审核,1:审核通过,2:审核未通过长度必须介于 0 和 10 之间")
	public String getAduitStatus() {
		return aduitStatus;
	}

	public void setAduitStatus(String aduitStatus) {
		this.aduitStatus = aduitStatus;
	}
	
	@Length(min=0, max=500, message="审核原因说明长度必须介于 0 和 500 之间")
	public String getAduitReason() {
		return aduitReason;
	}

	public void setAduitReason(String aduitReason) {
		this.aduitReason = aduitReason;
	}
	
	@Length(min=0, max=10, message="审核人长度必须介于 0 和 10 之间")
	public String getAduitBy() {
		return aduitBy;
	}

	public void setAduitBy(String aduitBy) {
		this.aduitBy = aduitBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAduitTime() {
		return aduitTime;
	}

	public void setAduitTime(Date aduitTime) {
		this.aduitTime = aduitTime;
	}

	public String getUpdatBy() {
		return updatBy;
	}

	public void setUpdatBy(String updatBy) {
		this.updatBy = updatBy;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}