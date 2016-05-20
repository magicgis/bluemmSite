/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 导入文件临时数据（红包明细）Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketExcel extends DataEntity<MallWxRedpacketExcel> {
	
	private static final long serialVersionUID = 1L;
	private String clientpath;		// clientpath
	private String clientfilename;		// clientfilename
	private String serverfilepath;		// serverfilepath
	private String serverfilename;		// serverfilename
	private String uploaduser;		// uploaduser
	private Date uploadtime;		// uploadtime
	private String isdeleted;		// 是否已删除
	private String operator;		// operator
	private Date operatetime;		// operatetime
	
	public MallWxRedpacketExcel() {
		super();
	}

	public MallWxRedpacketExcel(String id){
		super(id);
	}

	@Length(min=1, max=255, message="clientpath长度必须介于 1 和 255 之间")
	public String getClientpath() {
		return clientpath;
	}

	public void setClientpath(String clientpath) {
		this.clientpath = clientpath;
	}
	
	@Length(min=0, max=100, message="clientfilename长度必须介于 0 和 100 之间")
	public String getClientfilename() {
		return clientfilename;
	}

	public void setClientfilename(String clientfilename) {
		this.clientfilename = clientfilename;
	}
	
	@Length(min=1, max=255, message="serverfilepath长度必须介于 1 和 255 之间")
	public String getServerfilepath() {
		return serverfilepath;
	}

	public void setServerfilepath(String serverfilepath) {
		this.serverfilepath = serverfilepath;
	}
	
	@Length(min=0, max=100, message="serverfilename长度必须介于 0 和 100 之间")
	public String getServerfilename() {
		return serverfilename;
	}

	public void setServerfilename(String serverfilename) {
		this.serverfilename = serverfilename;
	}
	
	@Length(min=0, max=10, message="uploaduser长度必须介于 0 和 10 之间")
	public String getUploaduser() {
		return uploaduser;
	}

	public void setUploaduser(String uploaduser) {
		this.uploaduser = uploaduser;
	}
	
	
	
	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
	}

	@Length(min=0, max=11, message="是否已删除长度必须介于 0 和 11 之间")
	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}
	
	@Length(min=0, max=10, message="operator长度必须介于 0 和 10 之间")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperatetime() {
		return operatetime;
	}
	
}