package com.weixin.bm.redPacket.model;
/**
 * 
 * @author dakou
 * @createTime 2015年7月28日 上午9:25:39
 * @desc 红包查询信息
 */
public class RedPackInfo {
	private String return_code;//SUCCESS/FAIL	此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
	private String return_msg;//返回信息，如非空，为错误原因 
	private String sign;//签名
	private String result_code;//SUCCESS/FAIL
	private String err_code;//错误码信息
	private String err_code_des;//结果信息描述
	private String appid;//商户openid
	private String mch_id;//商户号微信-支付分配的商户号
	private String mch_billno;//商户订单号-商户使用查询API填写的商户单号的原路返回
	private String detail_id;//红包单号-使用API发放现金红包时返回的红包单号
	private String status;//红包状态SENDING:发放中;//SENT:已发放待领取 ;//FAILED：发放失败 ;//RECEIVED:已领取;//REFUND:已退款
	private String send_type;//发放类型API:通过API接口发放 ;//UPLOAD:通过上传文件方式发放 ;//ACTIVITY:通过活动方式发放
	private String hb_type;//红包类型GROUP:裂变红包 ;//NORMAL:普通红包
	private String total_num;//红包个数红包个数
	private String total_amount;//红包金额红包总金额（单位分）
	private String reason;//失败原因发送失败原因
	private String send_time;//红包发送时间
	private String refund_time;//红包退款时间红包的退款时间（如果其未领取的退款）
	private String refund_amount;//红包退款金额红包退款金额
	private String wishing;//祝福语祝福语
	private String remark;//活动描述活动描述，低版本微信可见
	private String act_name;//活动名称-发红包的活动名称
	private String hblist;//裂变红包-领取列表裂变红包的领取列表
	private String openid;//领取红包的Openid领取红包的openid
	private String amount;//金额领取金额
	private String rcv_time;//领取红包的时间
	
	public String getRcv_time() {
		return rcv_time;
	}
	public void setRcv_time(String rcv_time) {
		this.rcv_time = rcv_time;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getMch_billno() {
		return mch_billno;
	}
	public void setMch_billno(String mch_billno) {
		this.mch_billno = mch_billno;
	}
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getHb_type() {
		return hb_type;
	}
	public void setHb_type(String hb_type) {
		this.hb_type = hb_type;
	}
	public String getTotal_num() {
		return total_num;
	}
	public void setTotal_num(String total_num) {
		this.total_num = total_num;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refund_time) {
		this.refund_time = refund_time;
	}
	public String getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}
	public String getWishing() {
		return wishing;
	}
	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getHblist() {
		return hblist;
	}
	public void setHblist(String hblist) {
		this.hblist = hblist;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "RedPackInfo [return_code=" + return_code + ", return_msg="
				+ return_msg + ", sign=" + sign + ", result_code="
				+ result_code + ", err_code=" + err_code + ", err_code_des="
				+ err_code_des + ", appid=" + appid + ", mch_id=" + mch_id
				+ ", mch_billno=" + mch_billno + ", detail_id=" + detail_id
				+ ", status=" + status + ", send_type=" + send_type
				+ ", hb_type=" + hb_type + ", total_num=" + total_num
				+ ", total_amount=" + total_amount + ", reason=" + reason
				+ ", send_time=" + send_time + ", refund_time=" + refund_time
				+ ", refund_amount=" + refund_amount + ", wishing=" + wishing
				+ ", remark=" + remark + ", act_name=" + act_name + ", hblist="
				+ hblist + ", openid=" + openid + ", amount=" + amount
				+ ", rcv_time=" + rcv_time + "]";
	}
	
}
