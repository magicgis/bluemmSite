package cn.com.bluemoon.jeesite.modules.item.entity;

import java.util.Date;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;
import cn.com.bluemoon.jeesite.modules.oa.entity.OaNotify;

/**
 * 套餐表
 * @author linyihao
 *
 */
public class MallItemSuite extends DataEntity<MallItemSuite> {
	
	private static final long serialVersionUID = 1L;
	
	private String suiteId;		//套餐ID
	private String suiteName;	//套餐名称
	private String suiteSku;	//套餐SKU
	private int marketPrice;	//市场价
	private String picUrl;		//主图
	private int isParent;		//是否包含子套餐
	private int status;		//是否有效
	private String createByWho;//创建人
	private Date createTime;//创建时间
	private String modifyBy;//修改人
	private Date modifyTime;//修改时间
	private String type;		//产品类型
	
	
	//额外字段
	private float marketPriceFloat;//市场价
	
	public String getSuiteId() {
		return suiteId;
	}
	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}
	public String getSuiteName() {
		return suiteName;
	}
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	public String getSuiteSku() {
		return suiteSku;
	}
	public void setSuiteSku(String suiteSku) {
		this.suiteSku = suiteSku;
	}
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
	public int getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	public String getCreateByWho() {
		return createByWho;
	}
	public void setCreateByWho(String createByWho) {
		this.createByWho = createByWho;
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
	public float getMarketPriceFloat() {
		return marketPriceFloat;
	}
	public void setMarketPriceFloat(float marketPriceFloat) {
		this.marketPriceFloat = marketPriceFloat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
