/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 授权天使Entity
 * @author linyihao
 * @version 2016-04-19
 */
public class MallPromotionActivityAuthAngel extends DataEntity<MallPromotionActivityAuthAngel> {
	
	private static final long serialVersionUID = 1L;
	private String mapId;		// 主键
	private String authId;		// 权限表ID
	private String angelId;		// 天使编号
	private String angelName;		// 天使名称
	private String angelMobile;		// 天使手机
	private Integer status;		// 是否有效
	private String opCode;		// 操作人
	private Date opTime;		// 操作时间
	
	public MallPromotionActivityAuthAngel() {
		super();
	}

	public MallPromotionActivityAuthAngel(String id){
		super(id);
	}

	@Length(min=1, max=32, message="主键长度必须介于 1 和 32 之间")
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	@Length(min=0, max=16, message="天使编号长度必须介于 0 和 16 之间")
	public String getAngelId() {
		return angelId;
	}

	@Length(min=1, max=32, message="权限主键长度必须介于 1 和 32 之间")
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setAngelId(String angelId) {
		this.angelId = angelId;
	}
	
	@Length(min=0, max=32, message="天使名称长度必须介于 0 和 32 之间")
	public String getAngelName() {
		return angelName;
	}

	public void setAngelName(String angelName) {
		this.angelName = angelName;
	}
	
	@Length(min=0, max=16, message="天使手机长度必须介于 0 和 16 之间")
	public String getAngelMobile() {
		return angelMobile;
	}

	public void setAngelMobile(String angelMobile) {
		this.angelMobile = angelMobile;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Length(min=0, max=16, message="操作人长度必须介于 0 和 16 之间")
	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
}