/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.coupon.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponInfo;

/**
 * mall_promotion_DAO接口
 * @author linyihao
 * @version 2016-03-24
 */
@MyBatisDao
public interface MallPromotionCouponInfoDao extends CrudDao<MallPromotionCouponInfo> {
	public int cancelCoupon(@Param(value="couponId")BigInteger couponId,@Param(value="updateName")String updateName);
	
	public MallPromotionCouponInfo getCouponByCouponCode(@Param(value="couponCode")String couponCode);
	
	public List<MallPromotionActivityInfo> getActivityByCouponId(@Param(value="couponId")BigInteger couponId);
	
}