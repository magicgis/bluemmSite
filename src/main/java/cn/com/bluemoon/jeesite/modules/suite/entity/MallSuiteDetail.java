package cn.com.bluemoon.jeesite.modules.suite.entity;

import java.util.Date;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 套装详情表(中间关联表)
 * goodID对应单品表和套餐表的ID
 * @author linyihao
 *
 */
public class MallSuiteDetail extends DataEntity<MallSuiteDetail>{
	
	private static final long serialVersionUID = 1L;
	
	private String sdId;	//主键
	private String suiteId;		//商品ID
	private String sgoodId;		//产品ID
	private String sgoodType;	//产品类型
	private String sgoodSku;		//产品SKU
	private int realPrice;		//产品SKU
	private int marketPrice;			//产品价格
	private int num;			//产品数量
	private String CreateByWho;
	private Date CreateTime;
	
	//额外字段
	private String goodName;	//产品名称
	private float marketPriceFloat;	//产品市场价
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
	public String getSgoodType() {
		return sgoodType;
	}
	public void setSgoodType(String sgoodType) {
		this.sgoodType = sgoodType;
	}
	public String getSgoodSku() {
		return sgoodSku;
	}
	public void setSgoodSku(String sgoodSku) {
		this.sgoodSku = sgoodSku;
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
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public float getMarketPriceFloat() {
		return marketPriceFloat;
	}
	public void setMarketPriceFloat(float marketPriceFloat) {
		this.marketPriceFloat = marketPriceFloat;
	}
	public String getCreateByWho() {
		return CreateByWho;
	}
	public void setCreateByWho(String createByWho) {
		CreateByWho = createByWho;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	
}
