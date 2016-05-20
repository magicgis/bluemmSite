package cn.com.bluemoon.jeesite.modules.pay.vo;

import java.io.Serializable;

public class MallPayPaymentVo implements Serializable {
	private static final long serialVersionUID = 2159212660645116129L;
	private String payName;
	private String payCnName;

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayCnName() {
		return payCnName;
	}

	public void setPayCnName(String payCnName) {
		this.payCnName = payCnName;
	}

	public MallPayPaymentVo(String payName, String payCnName) {
		super();
		this.payName = payName;
		this.payCnName = payCnName;
	}

	public MallPayPaymentVo() {
		super();
	}

}
