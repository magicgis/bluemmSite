package cn.com.bluemoon.jeesite.modules.item.entity;

import java.util.Date;
import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 商品表
 * @author linyihao
 *
 */
public class MallItemInfo extends DataEntity<MallItemInfo>{
	
	private static final long serialVersionUID = 1L;
	
	private String itemId;		//商品ID
	private String itemSku;		//商品SKU
	private String itemName;	//商品名称
	private int memberPrice;	//体验价
	private int marketPrice;	//市场价
	private int stock;			//库存
	private Date onlineTime;	//上架时间
	private Date offlineTime;	//下架时间
	private Integer status;		//是否上架(0下架，1上架)
	private int isValid;		//是否有效（0无效，1有效）
	private Integer hasPresale;		//是否包含预售
	private int position;		//排序
	private String sellPoint;	//卖点描述
	private String itemDesc;	//商品描述
	private String picUrl;		//图片
	
	//额外字段
	private float memberPriceFloat;
	private float marketPriceFloat;
	private String categoryId;	//分类ID
	private String categoryName;//分类名称
	private java.sql.Date onlineDate;
	private java.sql.Date offlineDate;
	private String mainPicUrl;	//主图
	private String introPicUrl;	//细节图
	private String artMainPicUrl;	//详情图
	private int categoryPosition;
	
	private List<MallItemDetail> mallItemDetails;
	private List<MallItemDetailVo> mallItemDetailVo;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
	public int getMemberPrice() {
		return memberPrice;
	}
	public void setMemberPrice(int memberPrice) {
		this.memberPrice = memberPrice;
	}
	public int getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public Date getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	public Date getOfflineTime() {
		return offlineTime;
	}
	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public Integer getHasPresale() {
		return hasPresale;
	}
	public void setHasPresale(Integer hasPresale) {
		this.hasPresale = hasPresale;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public java.sql.Date getOnlineDate() {
		return onlineDate;
	}
	public void setOnlineDate(java.sql.Date onlineDate) {
		this.onlineDate = onlineDate;
	}
	public java.sql.Date getOfflineDate() {
		return offlineDate;
	}
	public void setOfflineDate(java.sql.Date offlineDate) {
		this.offlineDate = offlineDate;
	}
	public String getSellPoint() {
		return sellPoint;
	}
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getMainPicUrl() {
		return mainPicUrl;
	}
	public void setMainPicUrl(String mainPicUrl) {
		this.mainPicUrl = mainPicUrl;
	}
	public String getArtMainPicUrl() {
		return artMainPicUrl;
	}
	public void setArtMainPicUrl(String artMainPicUrl) {
		this.artMainPicUrl = artMainPicUrl;
	}
	public String getIntroPicUrl() {
		return introPicUrl;
	}
	public void setIntroPicUrl(String introPicUrl) {
		this.introPicUrl = introPicUrl;
	}
	public List<MallItemDetail> getMallItemDetails() {
		return mallItemDetails;
	}
	public void setMallItemDetails(List<MallItemDetail> mallItemDetails) {
		this.mallItemDetails = mallItemDetails;
	}
	public List<MallItemDetailVo> getMallItemDetailVo() {
		return mallItemDetailVo;
	}
	public void setMallItemDetailVo(List<MallItemDetailVo> mallItemDetailVo) {
		this.mallItemDetailVo = mallItemDetailVo;
	}
	public float getMemberPriceFloat() {
		return memberPriceFloat;
	}
	public void setMemberPriceFloat(float memberPriceFloat) {
		this.memberPriceFloat = memberPriceFloat;
	}
	public float getMarketPriceFloat() {
		return marketPriceFloat;
	}
	public void setMarketPriceFloat(float marketPriceFloat) {
		this.marketPriceFloat = marketPriceFloat;
	}
	public int getCategoryPosition() {
		return categoryPosition;
	}
	public void setCategoryPosition(int categoryPosition) {
		this.categoryPosition = categoryPosition;
	}
	
}
