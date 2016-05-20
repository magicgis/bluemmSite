/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallvirtualproduct.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.entity.MallVirtualProduct;

/**
 * 虚拟产品信息管理DAO接口
 * @author linyihao
 * @version 2016-02-29
 */
@MyBatisDao
public interface MallVirtualProductDao extends CrudDao<MallVirtualProduct> {
	
}