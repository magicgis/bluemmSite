/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.parameter.dao;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.parameter.entity.MallSysParameter;

/**
 * 系统参数DAO接口
 * @author mij
 * @version 2015-12-28
 */
@MyBatisDao
public interface MallSysParameterDao extends CrudDao<MallSysParameter> {
	
	public MallSysParameter getSysParameterByCode(@Param(value="paraType")String paraType,  @Param(value="paraCode")String paraCode);
	
}