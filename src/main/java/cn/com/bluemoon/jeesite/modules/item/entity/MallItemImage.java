package cn.com.bluemoon.jeesite.modules.item.entity;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 图片表(中间关联表)
 * @author linyihao
 *
 */
public class MallItemImage extends DataEntity<MallItemImage>{
	
	private static final long serialVersionUID = 1L;
	
	public static final String IMAGE_TYPE_MAIN = "main";
	public static final String IMAGE_TYPE_INTRO = "intro";
	public static final String IMAGE_TYPE_ARTMAIN = "artMain";
	
	private String itemId;
	private String itmeImgUrl;
	private String imgType;
	private int position;
	private int status;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItmeImgUrl() {
		return itmeImgUrl;
	}
	public void setItmeImgUrl(String itmeImgUrl) {
		this.itmeImgUrl = itmeImgUrl;
	}
	public String getImgType() {
		return imgType;
	}
	public void setImgType(String imgType) {
		this.imgType = imgType;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
