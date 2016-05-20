package cn.com.bluemoon.jeesite.modules.pay.model;

/**
 * 付款异常信息
 * @author kouzhiqiang
 * @createTime 2015年10月22日 下午4:05:08
 * 
 */
public class PayExceptionInfo {
	private String order_code;//订单编号
	private String order_price;//订单金额
	private String order_status;//订单当前状态
	private String pay_out_order_no;//支付平台的外部编号
	private String pay_platform_from_order;//订单中支付方式
	private String pay_platform_from_payrecord;//支付记录中的支付方式
	private String pay_total_fee;//支付金额
	private String pay_trade_no;//支付流水号
	private String pay_trade_date;//支付日期
	private String pay_status;//付款状态
	private Boolean is_abnormal;//是否异常
	private Boolean is_visited;//是否遍历过
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getOrder_price() {
		return order_price;
	}
	public void setOrder_price(String order_price) {
		this.order_price = order_price;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getPay_out_order_no() {
		return pay_out_order_no;
	}
	public void setPay_out_order_no(String pay_out_order_no) {
		this.pay_out_order_no = pay_out_order_no;
	}
	
	public String getPay_platform_from_order() {
		return pay_platform_from_order;
	}
	public void setPay_platform_from_order(String pay_platform_from_order) {
		this.pay_platform_from_order = pay_platform_from_order;
	}
	public String getPay_platform_from_payrecord() {
		return pay_platform_from_payrecord;
	}
	public void setPay_platform_from_payrecord(String pay_platform_from_payrecord) {
		this.pay_platform_from_payrecord = pay_platform_from_payrecord;
	}
	public String getPay_total_fee() {
		return pay_total_fee;
	}
	public void setPay_total_fee(String pay_total_fee) {
		this.pay_total_fee = pay_total_fee;
	}
	public String getPay_trade_no() {
		return pay_trade_no;
	}
	public void setPay_trade_no(String pay_trade_no) {
		this.pay_trade_no = pay_trade_no;
	}
	public String getPay_trade_date() {
		return pay_trade_date;
	}
	public void setPay_trade_date(String pay_trade_date) {
		this.pay_trade_date = pay_trade_date;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public Boolean getIs_abnormal() {
		return is_abnormal;
	}
	public void setIs_abnormal(Boolean is_abnormal) {
		this.is_abnormal = is_abnormal;
	}
	public Boolean getIs_visited() {
		return is_visited;
	}
	public void setIs_visited(Boolean is_visited) {
		this.is_visited = is_visited;
	}

	
}
