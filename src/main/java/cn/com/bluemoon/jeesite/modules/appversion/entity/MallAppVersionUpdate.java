/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.appversion.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * APP版本信息管理Entity
 * @author linyihao
 * @version 2016-01-20
 */
public class MallAppVersionUpdate extends DataEntity<MallAppVersionUpdate> {
	
	private static final long serialVersionUID = 1L;
	private String buildVersion;		// 内部编号
	private String mustUpdate;		// 是否强制更新
	private String version;		// 外部版本号
	private String download;		// 下载链接
	private String platform;		// 类型
	private String description;		// 更新描述
	private String lastUpdateBy;		// last_update_by
	private Date lastUpdateDate;		// last_update_date
	private String appType;		// app类型
	private String status;		// 状态
	
	public MallAppVersionUpdate() {
		super();
	}

	public MallAppVersionUpdate(String id){
		super(id);
	}

	@Length(min=1, max=32, message="内部编号长度必须介于 1 和 32 之间")
	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	
	@Length(min=1, max=1, message="是否强制更新长度必须介于 1 和 1 之间")
	public String getMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(String mustUpdate) {
		this.mustUpdate = mustUpdate;
	}
	
	@Length(min=1, max=32, message="外部版本号长度必须介于 1 和 32 之间")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Length(min=1, max=255, message="下载链接长度必须介于 1 和 255 之间")
	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}
	
	@Length(min=1, max=32, message="类型长度必须介于 1 和 32 之间")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	@Length(min=1, max=1024, message="更新描述长度必须介于 1 和 1024 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=1, max=64, message="last_update_by长度必须介于 1 和 64 之间")
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	@Length(min=0, max=32, message="app类型长度必须介于 0 和 32 之间")
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	
	@Length(min=0, max=1, message="状态长度必须介于 0 和 1 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}