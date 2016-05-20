package cn.com.bluemoon.jeesite.modules.item.entity;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 商品分类表(中间关联表)
 * @author linyihao
 *
 */
public class MallCategoryItem extends DataEntity<MallCategoryItem>{
	
	private static final long serialVersionUID = 1L;
	
	private String ciId;
	private String cid;
	private String itemId;
	private int categoryPosition;
	private int status;
	
	public String getCiId() {
		return ciId;
	}
	public void setCiId(String ciId) {
		this.ciId = ciId;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCategoryPosition() {
		return categoryPosition;
	}
	public void setCategoryPosition(int categoryPosition) {
		this.categoryPosition = categoryPosition;
	}
	
}
