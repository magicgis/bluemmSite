/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import org.hibernate.validator.constraints.Length;

import cn.com.bluemoon.jeesite.common.persistence.DataEntity;

/**
 * 红包发放商户名称Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketMerchantInfo extends DataEntity<MallWxRedpacketMerchantInfo> {
	
	private static final long serialVersionUID = 1L;
	private String merchantNo;		// 商户号
	private String merchantName;		// 商户名
	private String webchatPublicNo;		// 微信公众账号
	private String provider;		// 提供方名称
	
	public MallWxRedpacketMerchantInfo() {
		super();
	}

	public MallWxRedpacketMerchantInfo(String id){
		super(id);
	}

	@Length(min=1, max=11, message="商户号长度必须介于 1 和 11 之间")
	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	@Length(min=0, max=20, message="商户名长度必须介于 0 和 20 之间")
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	@Length(min=0, max=50, message="微信公众账号长度必须介于 0 和 50 之间")
	public String getWebchatPublicNo() {
		return webchatPublicNo;
	}

	public void setWebchatPublicNo(String webchatPublicNo) {
		this.webchatPublicNo = webchatPublicNo;
	}
	
	@Length(min=0, max=20, message="提供方名称长度必须介于 0 和 20 之间")
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}