package cn.com.bluemoon.jeesite.modules.recommendinfo.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;


/**
 * The persistent class for the mall_order_info database table.
 * 
 */

public class MallOrderInfo extends DataEntity<MallOrderInfo>{
	private static final long serialVersionUID = 1L;

	private String orderId; //订单Id

	private String outerCode; //订单编号

	private String parentCode; //父订单外部编码

	private Date payTime; //付款时间

	private String recommendCode; //推荐码
	
	private String state; //状态
	
	private String nickName;// 用户昵称
	
	private String userMobile;//注册人手机号
	
	private int payTotal;//订单金额

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOuterCode() {
		return outerCode;
	}

	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getRecommendCode() {
		return recommendCode;
	}

	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

}