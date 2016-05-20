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
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.pay.dao.MallPayRefundDao;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayRefund;

/**
 * 第三方支付退款记录Service
 * @author liao
 * @version 2016-03-09
 */
@Service
@Transactional(readOnly = true)
public class MallPayRefundService extends CrudService<MallPayRefundDao, MallPayRefund> {

	@Autowired
	private MallPayRefundDao mallPayRefundDao;
	
	public MallPayRefund get(String id) {
		return super.get(id);
	}
	
	public List<MallPayRefund> findList(MallPayRefund mallPayRefund) {
		return super.findList(mallPayRefund);
	}
	
	public Page<MallPayRefund> findPage(Page<MallPayRefund> page, MallPayRefund mallPayRefund) {
		return super.findPage(page, mallPayRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPayRefund mallPayRefund) {
		mallPayRefund.setId(SerialNo.getUNID());
		mallPayRefundDao.insert(mallPayRefund);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPayRefund mallPayRefund) {
		super.delete(mallPayRefund);
	}

	
	public List<MallPayRefund> findListByOutTradeNos(String outTradeNos) {
		return mallPayRefundDao.findListByOutTradeNos(outTradeNos);
	}

	public List<MallPayRefund> findListByTransactionIds(String transactionIds) {
		return mallPayRefundDao.findListByTransactionIds(transactionIds);
	}

	public List<MallPayRefund> findListByTransCodeMsg(String transCodeMsg) {
		return mallPayRefundDao.findListByTransCodeMsg(transCodeMsg);
	}

	public int updateValidateByOutTradeNo(String payStatus, String validate , String outTradeNo) {
		return mallPayRefundDao.updateValidateByOutTradeNo(payStatus, validate, outTradeNo);
	}
}