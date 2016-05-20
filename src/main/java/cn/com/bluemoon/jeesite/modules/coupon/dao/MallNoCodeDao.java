/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.coupon.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallNoCode;

/**
 * 券主数据管理DAO接口
 * @author linyihao
 * @version 2016-03-22
 */
@MyBatisDao
public interface MallNoCodeDao extends CrudDao<MallNoCode> {
	
}