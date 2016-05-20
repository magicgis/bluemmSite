/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.couponmen.entity;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 人工派券内容Entity
 * @author linyihao
 * @version 2016-04-19
 */
public class MallPromotionActivityCouponMensendDetail extends DataEntity<MallPromotionActivityCouponMensendDetail> {
	
	private static final long serialVersionUID = 1L;
	private String detailId;		// 主键
	private String mensendId;		// 人工发券ID
	private String couponCode;		// 券编码
	private String couponSName;		// 券简称
	
	public MallPromotionActivityCouponMensendDetail() {
		super();
	}

	public MallPromotionActivityCouponMensendDetail(String id){
		super(id);
	}

	@Length(min=1, max=32, message="主键长度必须介于 1 和 32 之间")
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	@Length(min=0, max=32, message="人工发券ID长度必须介于 0 和 32 之间")
	public String getMensendId() {
		return mensendId;
	}

	public void setMensendId(String mensendId) {
		this.mensendId = mensendId;
	}
	
	@Length(min=0, max=32, message="券编码长度必须介于 0 和 32 之间")
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	@Length(min=0, max=64, message="券简称长度必须介于 0 和 64 之间")
	public String getCouponSName() {
		return couponSName;
	}

	public void setCouponSName(String couponSName) {
		this.couponSName = couponSName;
	}
	
}