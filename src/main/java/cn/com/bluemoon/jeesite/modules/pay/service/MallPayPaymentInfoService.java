/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.pay.dao.MallPayPaymentInfoDao;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayPaymentInfo;

/**
 * 支付记录Service
 * @author liao
 * @version 2016-03-07
 */
@Service
@Transactional(readOnly = true)
public class MallPayPaymentInfoService extends CrudService<MallPayPaymentInfoDao, MallPayPaymentInfo> {

	public MallPayPaymentInfo get(String id) {
		return super.get(id);
	}
	
	public List<MallPayPaymentInfo> findList(MallPayPaymentInfo mallPayPaymentInfo) {
		return super.findList(mallPayPaymentInfo);
	}
	
	public Page<MallPayPaymentInfo> findPage(Page<MallPayPaymentInfo> page, MallPayPaymentInfo mallPayPaymentInfo) {
		return super.findPage(page, mallPayPaymentInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPayPaymentInfo mallPayPaymentInfo) {
		super.save(mallPayPaymentInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPayPaymentInfo mallPayPaymentInfo) {
		super.delete(mallPayPaymentInfo);
	}


	@Autowired
	private MallPayPaymentInfoDao mallPayPaymentInfoDao;
	
	public List<MallPayPaymentInfo> findListByOutTradeNos(String outTradeNos) {
		return mallPayPaymentInfoDao.findListByOutTradeNos(outTradeNos);
	}
	
	public List<MallPayPaymentInfo> findListByTransactionIds(String transactionIds) {
		return mallPayPaymentInfoDao.findListByTransactionIds(transactionIds);
	}
	

	@Transactional(readOnly = false)
	public int updateRefundPaystatusById(String payStatus, String id) {
		return mallPayPaymentInfoDao.updateRefundPaystatusById(payStatus, id);
	}

	

	@Transactional(readOnly = false)
	public int updateRefundPaystatusByOutTradeNo(String payStatus, String outTradeNo) {
		return mallPayPaymentInfoDao.updateRefundPaystatusByOutTradeNo(payStatus, outTradeNo);
	}
	
	@Transactional(readOnly = false)
	public int updateValidateByOutTradeNo(String payStatus, String validate , String outTradeNo) {
		return mallPayPaymentInfoDao.updateValidateByOutTradeNo(payStatus,validate, outTradeNo);
	}
	
}