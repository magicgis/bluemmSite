/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.entity;

import java.io.Serializable;

/**
 * 红包发放商户名称Entity
 * @author linyihao
 * @version 2016-05-03
 */
public class MallWxRedpacketMerchantInfoVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String merchantNo;		// 商户号
	private String merchantName;		// 商户名
	private String webchatPublicNo;		// 微信公众账号
	private String provider;		// 提供方名称
	
	public MallWxRedpacketMerchantInfoVo() {
	}
	
	public MallWxRedpacketMerchantInfoVo(MallWxRedpacketMerchantInfo po) {
		this.merchantNo = po.getMerchantNo();
		this.merchantName = po.getMerchantName();
		this.webchatPublicNo = po.getWebchatPublicNo();
		this.provider = po.getProvider();
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	
	public String getWebchatPublicNo() {
		return webchatPublicNo;
	}

	public void setWebchatPublicNo(String webchatPublicNo) {
		this.webchatPublicNo = webchatPublicNo;
	}
	
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}