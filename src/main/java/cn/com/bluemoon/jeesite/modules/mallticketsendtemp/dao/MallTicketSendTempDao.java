/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallticketsendtemp.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.mallticketsendtemp.entity.MallTicketSendTemp;

/**
 * 派发门票DAO接口
 * @author lingyihao
 * @version 2016-02-26
 */
@MyBatisDao
public interface MallTicketSendTempDao extends CrudDao<MallTicketSendTemp> {
	
}