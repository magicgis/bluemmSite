/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 红包申请Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketOrderNo extends DataEntity<MallWxRedpacketOrderNo> {
	
	private static final long serialVersionUID = 1L;
	private String applydate;		// 申请日期
	private String no;				// 下次编号
	private String type;			// 类型 1表示申请单 2表示红包
	private String isdeleted;		// 是否被删除0否1是
	private String operator;		// 操作人
	private Date operatortime;		// 操作时间
	
	public MallWxRedpacketOrderNo() {
		super();
	}

	public MallWxRedpacketOrderNo(String id){
		super(id);
	}

	@Length(min=1, max=8, message="申请日期长度必须介于 1 和 8 之间")
	public String getApplydate() {
		return applydate;
	}

	public void setApplydate(String applydate) {
		this.applydate = applydate;
	}
	
	@Length(min=1, max=10, message="下次编号长度必须介于 1 和 10 之间")
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@Length(min=1, max=5, message="类型 1表示申请单 2表示红包长度必须介于 1 和 5 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=1, message="是否被删除0否1是长度必须介于 0 和 1 之间")
	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}
	
	@Length(min=0, max=10, message="操作人长度必须介于 0 和 10 之间")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperatortime() {
		return operatortime;
	}

	public void setOperatortime(Date operatortime) {
		this.operatortime = operatortime;
	}
	
}