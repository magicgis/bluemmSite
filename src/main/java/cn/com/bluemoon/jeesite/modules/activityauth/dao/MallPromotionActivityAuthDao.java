/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.dao;


import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuth;

/**
 * 活动权限表DAO接口
 * @author linyihao
 * @version 2016-04-19
 */
@MyBatisDao
public interface MallPromotionActivityAuthDao extends CrudDao<MallPromotionActivityAuth> {
	public void cancel(MallPromotionActivityAuth mallPromotionActivityAuth);
	
	public void updateUser(MallPromotionActivityAuth mallPromotionActivityAuth);
}