package cn.com.bluemoon.jeesite.modules.item.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.modules.oa.entity.OaNotify;

/**
 * 单品表
 * @author linyihao
 *
 */
public class MallProductInfo extends DataEntity<MallProductInfo> {
	
	private static final long serialVersionUID = 1L;

	private String productId;	// 产品id
	private String productName;	// 产品名称
	private String productSku;	// 产品sku
	private String barCode;		// 条码
	private Integer carton;		// 箱规
	private float marketPrice;	// 市场价（以分为单位）
	private String picUrl;		// 产品图片
	private int status;			// 是否有效
	private Date createTime;		// 创建时间
	private String modifyBy;		// 修改人
	private Date modifyTime;		// 修改时间
	private String type;		//产品类型

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	
	public float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	@Length(min=0, max=200, message="产品图片长度必须介于 0 和 200 之间")
	@NotNull(message="请添加产品图片")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public Integer getCarton() {
		return carton;
	}

	public void setCarton(Integer carton) {
		this.carton = carton;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
