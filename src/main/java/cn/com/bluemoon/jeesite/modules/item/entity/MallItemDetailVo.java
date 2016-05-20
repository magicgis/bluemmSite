package cn.com.bluemoon.jeesite.modules.item.entity;

import java.io.Serializable;


public class MallItemDetailVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String detailId;	//主键
	private String itemId;		//商品ID
	private String goodId;		//产品ID
	private String goodType;	//产品类型
	private float price;			//产品价格
	private int num;			//产品数量
	
	//额外字段
	private String goodName;	//产品名称
	private String goodSku;		//产品SKU
	private float marketPrice;	//产品市场价
	
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public String getGoodType() {
		return goodType;
	}
	public void setGoodType(String goodType) {
		this.goodType = goodType;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		if(num==0)
			num = 1;
		this.num = num;
	}
	public String getGoodSku() {
		return goodSku;
	}
	public void setGoodSku(String goodSku) {
		this.goodSku = goodSku;
	}
	public float getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(float marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
}
