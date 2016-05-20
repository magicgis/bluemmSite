/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.sys.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 单表生成Entity
 * @author mij
 * @version 2016-01-04
 */
public class MallSysLogInfo extends DataEntity<MallSysLogInfo> {
	
	private static final long serialVersionUID = 1L;
	private String logId;		// 日志id
	private String module;		// 模块
	private Date operTime;		// 操作时间
	private String userIp;		// 来源ip
	private String cuid;		// 设备号
	private String clientType;		// 来源入口渠道
	private String methods;		// 操作说明（调用方法）
	private String description;		// 操作结果
	private String broswer;		// broswer
	private User user;		// user_id
	private String version;		// version
	private Date beginOperTime;		// 开始 操作时间
	private Date endOperTime;		// 结束 操作时间
	
	public MallSysLogInfo() {
		super();
	}

	public MallSysLogInfo(String id){
		super(id);
	}

	@Length(min=1, max=32, message="日志id长度必须介于 1 和 32 之间")
	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
	
	@Length(min=0, max=32, message="模块长度必须介于 0 和 32 之间")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	
	@Length(min=0, max=32, message="来源ip长度必须介于 0 和 32 之间")
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	
	@Length(min=0, max=32, message="设备号长度必须介于 0 和 32 之间")
	public String getCuid() {
		return cuid;
	}

	public void setCuid(String cuid) {
		this.cuid = cuid;
	}
	
	@Length(min=0, max=32, message="来源入口渠道长度必须介于 0 和 32 之间")
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	@Length(min=0, max=32, message="操作说明（调用方法）长度必须介于 0 和 32 之间")
	public String getMethods() {
		return methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}
	
	@Length(min=0, max=5000, message="操作结果长度必须介于 0 和 5000 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=256, message="broswer长度必须介于 0 和 256 之间")
	public String getBroswer() {
		return broswer;
	}

	public void setBroswer(String broswer) {
		this.broswer = broswer;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=356, message="version长度必须介于 0 和 356 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public Date getBeginOperTime() {
		return beginOperTime;
	}

	public void setBeginOperTime(Date beginOperTime) {
		this.beginOperTime = beginOperTime;
	}
	
	public Date getEndOperTime() {
		return endOperTime;
	}

	public void setEndOperTime(Date endOperTime) {
		this.endOperTime = endOperTime;
	}
		
}