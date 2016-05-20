/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPaySearchRecord;

/**
 * 第三方同步记录DAO接口
 * @author liao
 * @version 2016-03-07
 */
@MyBatisDao
public interface MallPaySearchRecordDao extends CrudDao<MallPaySearchRecord> {
	
}