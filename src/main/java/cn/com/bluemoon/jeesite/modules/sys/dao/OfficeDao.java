/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.sys.dao;

import cn.com.bluemoon.jeesite.common.persistence.TreeDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.sys.entity.Office;

/**
 * 机构DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {
	
}
