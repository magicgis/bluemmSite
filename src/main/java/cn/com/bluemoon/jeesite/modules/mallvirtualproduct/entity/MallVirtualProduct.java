/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallvirtualproduct.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 虚拟产品信息管理Entity
 * @author linyihao
 * @version 2016-02-29
 */
public class MallVirtualProduct extends DataEntity<MallVirtualProduct> {
	
	private static final long serialVersionUID = 1L;
	private String vpId;		// 虚拟产品id
	private String vpSku;		// 虚拟产品sku
	private String vpName;		// 虚拟产品名称
	private int marketPrice;		// 市场价（以分为单位）
	private String picUrl;		// 图片
	private String type;		// 类型
	private String status;		// 是否有效
	private Date createTime;		// 创建时间
	private String modifyBy;		// 修改人
	private Date modifyTime;		// 修改时间
	
	private float marketPriceFloat;
	
	public MallVirtualProduct() {
		super();
	}

	public MallVirtualProduct(String id){
		super(id);
	}

	public String getVpId() {
		return vpId;
	}

	public void setVpId(String vpId) {
		this.vpId = vpId;
	}
	
	@Length(min=8, max=8, message="虚拟产品sku长度必须为8位数字")
	public String getVpSku() {
		return vpSku;
	}

	public void setVpSku(String vpSku) {
		this.vpSku = vpSku;
	}
	
	@Length(min=0, max=500, message="虚拟产品名称长度必须介于 0 和 500 之间")
	public String getVpName() {
		return vpName;
	}

	public void setVpName(String vpName) {
		this.vpName = vpName;
	}
	
	@Length(min=0, max=500, message="图片长度必须介于 0 和 500 之间")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	@Length(min=0, max=32, message="类型长度必须介于 0 和 32 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=11, message="是否有效长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=32, message="修改人长度必须介于 0 和 32 之间")
	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}

	public float getMarketPriceFloat() {
		return marketPriceFloat;
	}

	public void setMarketPriceFloat(float marketPriceFloat) {
		this.marketPriceFloat = marketPriceFloat;
	}
	
}