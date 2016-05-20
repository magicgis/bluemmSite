package cn.com.bluemoon.jeesite.modules.suite.entity;

import java.io.Serializable;

public class MallSuiteDetailGoodVo implements Serializable {
	
	private static final long serialVersionUID = 3967128128276505646L;

	private String sdId;//套装详情id
	
	private String suiteId;//套装id
	
	private String sgoodId;//货物id
	
	private String sgoodSku;//货物sku
	
	private String sgoodType;//套装or单品
	
	private int realPrice;//套装内价 
	
	private int marketPrice;//市场价
	
	private int num;//数量
	
	private String itemName;//单品名称
	
	private String type;

	public String getSdId() {
		return sdId;
	}

	public void setSdId(String sdId) {
		this.sdId = sdId;
	}

	public String getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}

	public String getSgoodId() {
		return sgoodId;
	}

	public void setSgoodId(String sgoodId) {
		this.sgoodId = sgoodId;
	}

	public String getSgoodSku() {
		return sgoodSku;
	}

	public void setSgoodSku(String sgoodSku) {
		this.sgoodSku = sgoodSku;
	}

	public String getSgoodType() {
		return sgoodType;
	}

	public void setSgoodType(String sgoodType) {
		this.sgoodType = sgoodType;
	}

	public int getRealPrice() {
		return realPrice;
	}

	public void setRealPrice(int realPrice) {
		this.realPrice = realPrice;
	}

	public int getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(int marketPrice) {
		this.marketPrice = marketPrice;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
