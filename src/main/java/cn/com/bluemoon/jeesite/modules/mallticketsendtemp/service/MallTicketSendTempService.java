/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallticketsendtemp.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.HttpProperties;
import cn.com.bluemoon.jeesite.common.utils.HttpUtil;
import cn.com.bluemoon.jeesite.modules.mallticketsendtemp.dao.MallTicketSendTempDao;
import cn.com.bluemoon.jeesite.modules.mallticketsendtemp.entity.MallTicketSendTemp;

/**
 * 派发门票Service
 * @author lingyihao
 * @version 2016-02-26
 */
@Service
@Transactional(readOnly = true)
public class MallTicketSendTempService extends CrudService<MallTicketSendTempDao, MallTicketSendTemp> {

	public MallTicketSendTemp get(String id) {
		return super.get(id);
	}
	
	public List<MallTicketSendTemp> findList(MallTicketSendTemp mallTicketSendTemp) {
		return super.findList(mallTicketSendTemp);
	}
	
	public Page<MallTicketSendTemp> findPage(Page<MallTicketSendTemp> page, MallTicketSendTemp mallTicketSendTemp) {
		return super.findPage(page, mallTicketSendTemp);
	}
	
	@Transactional(readOnly = false)
	public void save(MallTicketSendTemp mallTicketSendTemp) {
//		String uri = HttpProperties.getVal("moonMallUrl");
//		String url = uri + "/sendTicket";
//		JSONObject params = new JSONObject();
//		params.put("mobile", mallTicketSendTemp.getMobile());
//		params.put("num", mallTicketSendTemp.getSendNum());
//		params.put("actId", mallTicketSendTemp.getActId());
//		String response = HttpUtil.doJsonPost(url, params.toString());
//		JSONObject json = JSONObject.fromObject(response);
//		if(json.getBoolean("isSuccess") == true && json.getInt("responseCode") == 0){
//			mallTicketSendTemp.setIsSend("1");
//		}
		super.save(mallTicketSendTemp);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallTicketSendTemp mallTicketSendTemp) {
		super.delete(mallTicketSendTemp);
	}
	
}