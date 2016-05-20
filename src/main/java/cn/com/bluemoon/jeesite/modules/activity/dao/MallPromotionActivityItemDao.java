/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.dao;

import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityItemInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjs;

/**
 * mall_promotion_DAO接口
 * @author linyihao
 * @version 2016-03-24
 */
@MyBatisDao
public interface MallPromotionActivityItemDao extends CrudDao<MallPromotionActivityItemInfo> {
	public List<MallPromotionActivityInfo> findSpList(MallPromotionActivityInfo mallPromotionActivityInfo);
	
	public MallPromotionActivityInfo getSpActivity(String id);
}