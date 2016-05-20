/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayRefund;

/**
 * 第三方支付退款记录DAO接口
 * @author liao
 * @version 2016-03-09
 */
@MyBatisDao
public interface MallPayRefundDao extends CrudDao<MallPayRefund> {

	public List<MallPayRefund> findListByOutTradeNos(@Param(value="outTradeNos")String outTradeNos) ;

	public List<MallPayRefund> findListByTransactionIds(@Param(value="transactionIds")String transactionIds) ;

	public List<MallPayRefund> findListByTransCodeMsg(@Param(value="transCodeMsg")String transCodeMsg) ;
	
	public int updateValidateByOutTradeNo(@Param(value="payStatus")String payStatus, @Param(value="validate")String validate , @Param(value="outTradeNo")String outTradeNo) ;

}