package cn.com.bluemoon.jeesite.modules.pay.dao;

public class PayRecordPO {
	private String merchant_out_order_no;//商户外部订单号;
	private String pay_platform;//支付平台;//alipay wxpay unionpay
	private String trade_no;//支付平台订单号;
	private String trade_date;//交易时间;
	private String trade_status;//交易状态;//SUCCESS REFUND REVOKED
	private String total_fee;//支付总金额;
	private String seller_id;//卖家id
	private String buyer_id;//买家id
	public String getMerchant_out_order_no() {
		return merchant_out_order_no;
	}
	public void setMerchant_out_order_no(String merchant_out_order_no) {
		this.merchant_out_order_no = merchant_out_order_no;
	}
	public String getPay_platform() {
		return pay_platform;
	}
	public void setPay_platform(String pay_platform) {
		this.pay_platform = pay_platform;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getTrade_date() {
		return trade_date;
	}
	public void setTrade_date(String trade_date) {
		this.trade_date = trade_date;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	@Override
	public String toString() {
		return "PayRecordPO [merchant_out_order_no=" + merchant_out_order_no
				+ ", pay_platform=" + pay_platform + ", trade_no=" + trade_no
				+ ", trade_date=" + trade_date + ", trade_status="
				+ trade_status + ", total_fee=" + total_fee + ", seller_id="
				+ seller_id + ", buyer_id=" + buyer_id + "]";
	}
	

}
