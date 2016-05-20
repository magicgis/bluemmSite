package cn.com.bluemoon.jeesite.modules.item.entity;

import java.util.Date;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 推荐商品表
 * @author linyihao
 *
 */
public class ItemRecommend extends DataEntity<ItemRecommend>{
	
	private static final long serialVersionUID = 1L;
	
	private String itemId;			//商品ID
	private String rePicUrl;		//商品推荐图片
	private String recommendType;	//推荐类型
	private Date onRecomDate;		//上推荐时间
	private Date offRecomDate;		//下推荐时间
	private int position;			//排序
	private Integer status;			//状态
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getRePicUrl() {
		return rePicUrl;
	}
	public void setRePicUrl(String rePicUrl) {
		this.rePicUrl = rePicUrl;
	}
	public String getRecommendType() {
		return recommendType;
	}
	public void setRecommendType(String recommendType) {
		this.recommendType = recommendType;
	}
	public Date getOnRecomDate() {
		return onRecomDate;
	}
	public void setOnRecomDate(Date onRecomDate) {
		this.onRecomDate = onRecomDate;
	}
	public Date getOffRecomDate() {
		return offRecomDate;
	}
	public void setOffRecomDate(Date offRecomDate) {
		this.offRecomDate = offRecomDate;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
