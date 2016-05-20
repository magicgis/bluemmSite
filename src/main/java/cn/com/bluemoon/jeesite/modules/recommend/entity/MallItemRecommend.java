/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommend.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 商品推荐位Entity
 * @author linyihao
 * @version 2016-01-15
 */
public class MallItemRecommend extends DataEntity<MallItemRecommend> {
	
	private static final long serialVersionUID = 1L;
	private String itemId;		// 商品id
	private String rePicUrl;		// 推荐图片
	private String recommendType;		// 推荐类型
	private Date onRecomDate;		// 上推荐时间
	private Date offRecomDate;		// 下推荐时间
	private String position;		// 排序序号
	private String status;		// 是否有效
	
	//额外字段
	private String itemStatus;
	private String itemSku;
	private String itemName;
	private float memberPrice;
	private float marketPrice;
	private java.sql.Date onRecomTime;
	private java.sql.Date offRecomTime;
	
	public MallItemRecommend() {
		super();
	}

	public MallItemRecommend(String id){
		super(id);
	}

	@Length(min=0, max=32, message="商品id长度必须介于 0 和 32 之间")
	@NotNull(message="请选择推荐商品")
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	@Length(min=0, max=200, message="推荐图片长度必须介于 0 和 200 之间")
	@NotNull(message="请添加推荐图片")
	public String getRePicUrl() {
		return rePicUrl;
	}

	public void setRePicUrl(String rePicUrl) {
		this.rePicUrl = rePicUrl;
	}
	
	@Length(min=0, max=32, message="推荐类型长度必须介于 0 和 32 之间")
	@NotNull(message="商品类型不能为空")
	public String getRecommendType() {
		return recommendType;
	}

	public void setRecommendType(String recommendType) {
		this.recommendType = recommendType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="上架时间不能为空")
	public Date getOnRecomDate() {
		return onRecomDate;
	}

	public void setOnRecomDate(Date onRecomDate) {
		this.onRecomDate = onRecomDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="下架时间不能为空")
	public Date getOffRecomDate() {
		return offRecomDate;
	}

	public void setOffRecomDate(Date offRecomDate) {
		this.offRecomDate = offRecomDate;
	}
	
	@Length(min=0, max=11, message="排序序号长度必须介于 0 和 11 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@Length(min=0, max=11, message="是否有效长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getItemSku() {
		return itemSku;
	}

	public void setItemSku(String itemSku) {
		this.itemSku = itemSku;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public float getMemberPrice() {
		return memberPrice;
	}

	public void setMemberPrice(float memberPrice) {
		this.memberPrice = memberPrice;
	}

	public float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}

	public java.sql.Date getOnRecomTime() {
		return onRecomTime;
	}

	public void setOnRecomTime(java.sql.Date onRecomTime) {
		this.onRecomTime = onRecomTime;
	}

	public java.sql.Date getOffRecomTime() {
		return offRecomTime;
	}

	public void setOffRecomTime(java.sql.Date offRecomTime) {
		this.offRecomTime = offRecomTime;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	
}