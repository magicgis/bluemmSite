/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.couponmen.dao;

import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.couponmen.entity.MallPromotionActivityCouponMensend;

/**
 * 人工派券DAO接口
 * @author linyihao
 * @version 2016-04-19
 */
@MyBatisDao
public interface MallPromotionActivityCouponMensendDao extends CrudDao<MallPromotionActivityCouponMensend> {
	public List<MallPromotionActivityCouponMensend> findExcelList(MallPromotionActivityCouponMensend entity);
	
}