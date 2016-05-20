/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendOperInfo;

/**
 * 推荐码明细DAO接口
 * @author xgb
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallRecommendOperInfoDao extends CrudDao<MallRecommendOperInfo> {
	
}