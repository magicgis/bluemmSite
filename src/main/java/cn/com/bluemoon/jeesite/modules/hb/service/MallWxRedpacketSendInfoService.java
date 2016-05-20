/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketApplyDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketApplyDetailsDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketSendinfoDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApply;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetails;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketSendinfo;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

import com.bluemoon.proxy.service.SpringContextUtils;
import com.bluemoon.proxy.service.packet.WxRedPacketService;

/**
 * 红包申请Service
 * 
 * @author linyihao
 * @version 2016-05-03
 */
@Service
@Transactional(readOnly = true)
public class MallWxRedpacketSendInfoService extends
		CrudService<MallWxRedpacketSendinfoDao, MallWxRedpacketSendinfo> {

	@Autowired
	private MallWxRedpacketApplyDao applyDao;
	@Autowired
	private MallWxRedpacketApplyDetailsDao applyDetailsDao;
	@Autowired
	private MallWxRedpacketApplyService applyService;

	public MallWxRedpacketSendinfo get(String id) {
		return super.get(id);
	}

	public List<MallWxRedpacketSendinfo> findList(MallWxRedpacketSendinfo entity) {
		return super.findList(entity);
	}

	@Transactional(readOnly = false)
	public Page<MallWxRedpacketSendinfo> findPage(
			Page<MallWxRedpacketSendinfo> page,
			MallWxRedpacketSendinfo MallWxRedpacketSendinfo) {
		User user = UserUtils.getUser();
		Date date = MallWxRedpacketSendinfo.getEndTime();
		if(date!=null){
			long newTime = date.getTime()+1000*3600*24;
			MallWxRedpacketSendinfo.setEndTime(new Date(newTime));
		}
		List<MallWxRedpacketSendinfo> list = dao.findList(MallWxRedpacketSendinfo);
		MallWxRedpacketSendinfo.setEndTime(date);
		MallWxRedpacketSendinfo.setPage(page);
		page.setList(dao.findList(MallWxRedpacketSendinfo));
		if (page.getList().size() > 0) {

			WxRedPacketService demoService = (WxRedPacketService) SpringContextUtils
					.getBean("wxRedPacketService");
			// SendRedPackedUtil sendRed = new SendRedPackedUtil();

			StringBuffer falseMsg = new StringBuffer();
			int i=1;
			for (MallWxRedpacketSendinfo temp : list) {
				// 查询时调用查询接口更新状态后显示
				if ("1".equals(temp.getStatus())
						|| "5".equals(temp.getStatus())
						|| "2".equals(temp.getStatus())) {
					MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
					detail.setMerchantOrderNo(temp.getMchBillno());
					List<MallWxRedpacketApplyDetails> detailList = applyDetailsDao
							.findList(detail);
					if (detailList.size() > 0) {
						detail = detailList.get(0);
						try {
							JSONObject jsonObj = new JSONObject();// sendRed.searchObj(detail,
																	// temp.getMchId());

							jsonObj.put("mchid", temp.getMchId());
							jsonObj.put("mch_billno", detail.getMerchantOrderNo());// 商户发放红包的商户订单号
							//调用接口返回结果
							String sendPacketResult = demoService.search(jsonObj);
							JSONObject obj = JSONObject.fromObject(sendPacketResult);
							String returncode = obj.containsKey("returncode") ? obj.getString("returncode") : "FAIL";
							JSONObject redPackInfo = obj.containsKey("redPackInfo") ? obj.getJSONObject("redPackInfo") : null;
							
							String result_code = redPackInfo
									.containsKey("result_code") ? redPackInfo
									.getString("result_code") : "";
							String send_time = redPackInfo
									.containsKey("send_time") ? redPackInfo
									.getString("send_time") : "";
							String detail_id = redPackInfo
									.containsKey("detail_id") ? redPackInfo
									.getString("detail_id") : "";
							String mch_billno = redPackInfo
									.containsKey("mch_billno") ? redPackInfo
									.getString("mch_billno") : "";
							String appid = redPackInfo.containsKey("appid") ? redPackInfo
									.getString("appid") : "";
							String reason = redPackInfo
									.containsKey("reason") ? redPackInfo
									.getString("reason") : "";
							String send_type = redPackInfo
									.containsKey("send_type") ? redPackInfo
									.getString("send_type") : "";
							String status = redPackInfo
									.containsKey("status") ? redPackInfo
									.getString("status") : "";
							String refund_time = redPackInfo
									.containsKey("refund_time") ? redPackInfo
									.getString("refund_time") : "";
							String refund_amount = redPackInfo
									.containsKey("refund_amount") ? redPackInfo
									.getString("refund_amount") : "";
							if("REFUND".equalsIgnoreCase(status)){
								// 更新红包明细状态
								detail.setMerchantOrderNo(mch_billno);
								detail.setSendListid(detail_id);
								SimpleDateFormat sdf = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								detail.setSendTime(sdf.parse(send_time));
								detail.setRedpacketStatus("2");//TODO  已退款的状态是已发放还是未发放
								applyService.updateAppluDetail(detail);
								// 更新红包发送信息明细
								temp.setSendTime(sdf.parse(send_time));
								temp.setStatus("5");
								temp.setReason(reason);
								temp.setRefundAmount(refund_amount);
								temp.setRefundTime(sdf.parse(refund_time));
								temp.setOperator(user.getName());
								temp.setOperatortime(new Date());
								temp.setSendType(send_type);
								applyService.updateSendInfo(temp);
							}
							if ("SUCCESS".equalsIgnoreCase(returncode)
								&& redPackInfo != null) {
								if ("SENDING".equalsIgnoreCase(status)) {
									status = "1"; // 发送中
								} else if ("SENT".equalsIgnoreCase(status)) {
									status = "2"; // 已发送待领取
								} else if ("FAILED".equalsIgnoreCase(status)) {
									status = "3"; // 发送失败
								} else if ("RECEIVED".equalsIgnoreCase(status)) {
									status = "4"; // 已领取
								} else if ("REFUND".equalsIgnoreCase(status)) {
									status = "5"; // 已退款
								} else {
									status = "0";
								}

								if (result_code.equalsIgnoreCase("SUCCESS")
										&& !status.equals(temp.getStatus())) {// 发送成功
									// 更新红包明细状态
									detail.setMerchantOrderNo(mch_billno);
									detail.setSendListid(detail_id);
									SimpleDateFormat sdf = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									detail.setSendTime(sdf.parse(send_time));
									detail.setRedpacketStatus("2");
									applyService.updateAppluDetail(detail);
									// 更新红包发送信息明细
									temp.setSendTime(sdf.parse(send_time));
									temp.setStatus(status);
									temp.setReason(reason);
//									temp.setDetailId(send_listid);
									if("5".equals(status)){
										temp.setRefundAmount(refund_amount);
										temp.setRefundTime(sdf.parse(refund_time));
									}
									temp.setOperator(user.getName());
									temp.setOperatortime(new Date());
									temp.setSendType(send_type);
									applyService.updateSendInfo(temp);
								} else {
									falseMsg.append(temp.getRedpackno()
											+ "<br/>");
									logger.error("调用微信接口失败");
								}
							} else {
								// 调用接口失败
								logger.error("调用接口失败");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			if (falseMsg.length() > 0) {
				logger.error(falseMsg.toString() + "有异常!");
			}
		}
		return page;
	}

}