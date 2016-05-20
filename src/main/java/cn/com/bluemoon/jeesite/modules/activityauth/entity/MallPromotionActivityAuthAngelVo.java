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
public class MallPromotionActivityAuthAngelVo {
	
	private static final long serialVersionUID = 1L;
	private String mapId;		// 主键
	private String authId;		// 权限表ID
	private String angelId;		// 天使编号
	private String angelName;		// 天使名称
	private String angelMobile;		// 天使手机
	private String status;		// 是否有效
	private String opCode;		// 操作人
	private Date opTime;		// 操作时间
	
	public MallPromotionActivityAuthAngelVo() {
		super();
	}

	public MallPromotionActivityAuthAngelVo(String mapId, String authId, String angelId, String angelName,
			String angelMobile) {
		this.mapId = mapId;
		this.authId = authId;
		this.angelId = angelId;
		this.angelName = angelName;
		this.angelMobile = angelMobile;
	}
	
	public MallPromotionActivityAuthAngelVo(String mapId, String authId, String angelId, String angelName,
			String angelMobile,Date opTime) {
		this.mapId = mapId;
		this.authId = authId;
		this.angelId = angelId;
		this.angelName = angelName;
		this.angelMobile = angelMobile;
		this.opTime = opTime;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	public String getAngelId() {
		return angelId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public void setAngelId(String angelId) {
		this.angelId = angelId;
	}
	
	public String getAngelName() {
		return angelName;
	}

	public void setAngelName(String angelName) {
		this.angelName = angelName;
	}
	
	public String getAngelMobile() {
		return angelMobile;
	}

	public void setAngelMobile(String angelMobile) {
		this.angelMobile = angelMobile;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	
	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}
	
}