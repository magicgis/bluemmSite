/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayPaymentInfo;

/**
 * 支付记录DAO接口
 * @author liao
 * @version 2016-03-07
 */
@MyBatisDao
public interface MallPayPaymentInfoDao extends CrudDao<MallPayPaymentInfo> {

	public List<MallPayPaymentInfo> findListByOutTradeNos(@Param(value="outTradeNos")String outTradeNos) ;

	public List<MallPayPaymentInfo> findListByTransactionIds(@Param(value="transactionIds")String transactionIds) ;

	public int updateRefundPaystatusById(@Param(value="payStatus")String payStatus, @Param(value="id")String id) ;

	public int updateRefundPaystatusByOutTradeNo(@Param(value="payStatus")String payStatus, @Param(value="outTradeNo")String outTradeNo) ;

	public int updateValidateByOutTradeNo(@Param(value="payStatus")String payStatus, @Param(value="validate")String validate , @Param(value="outTradeNo")String outTradeNo) ;

}