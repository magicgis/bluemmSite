/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.parameter.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 系统参数Entity
 * @author mij
 * @version 2015-12-28
 */
public class MallSysParameter extends DataEntity<MallSysParameter> {
	
	private static final long serialVersionUID = 1L;
	private String paraId;		// 系统参数ID
	private String paraType;		// 参数类型
	private String paraCode;		// 参数编码
	private String paraDesc;		// 描述内容
	private String paraValue;		// 参数值
	private String operUser;		// 操作人
	private Date operDate;		// 操作时间
	
	public MallSysParameter() {
		super();
	}

	public MallSysParameter(String id){
		super(id);
	}

/*	@Length(min=1, max=32, message="系统参数ID长度必须介于 1 和 32 之间")
*/	public String getParaId() {
		return paraId;
	}

	public void setParaId(String paraId) {
		this.paraId = paraId;
	}
	
	@Length(min=0, max=32, message="参数类型长度必须介于 0 和 32 之间")
	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}
	
	@Length(min=0, max=50, message="参数编码长度必须介于 0 和 50 之间")
	public String getParaCode() {
		return paraCode;
	}

	public void setParaCode(String paraCode) {
		this.paraCode = paraCode;
	}
	
	@Length(min=0, max=100, message="描述内容长度必须介于 0 和 100 之间")
	public String getParaDesc() {
		return paraDesc;
	}

	public void setParaDesc(String paraDesc) {
		this.paraDesc = paraDesc;
	}
	
	@Length(min=0, max=1024, message="参数值长度必须介于 0 和 1024 之间")
	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}
	
	@Length(min=0, max=50, message="操作人长度必须介于 0 和 50 之间")
	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOperDate() {
		return operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	
}