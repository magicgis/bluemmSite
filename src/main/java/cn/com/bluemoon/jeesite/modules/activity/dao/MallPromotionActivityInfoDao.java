/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;

/**
 * mall_promotion_DAO接口
 * @author linyihao
 * @version 2016-03-24
 */
@MyBatisDao
public interface MallPromotionActivityInfoDao extends CrudDao<MallPromotionActivityInfo> {
	
	public List<MallPromotionActivityInfo> checkList(@Param(value="activityType")String activityType,
			@Param(value="endTime")Date endTime,@Param(value="startTime")Date startTime,
			@Param(value="giftSku")String giftSku);
	
	public List<MallPromotionActivityInfo> checkSholeshop(@Param(value="activityType")String activityType,
			@Param(value="endTime")Date endTime,@Param(value="startTime")Date startTime);
	
	public List<MallPromotionActivityInfo> checkSholeshops(@Param(value="activityType")String activityType,
			@Param(value="endTime")Date endTime,@Param(value="startTime")Date startTime);
	
	public List<MallPromotionActivityInfo> findCouponList(MallPromotionActivityInfo mallPromotionActivityInfo);
	
	public List<MallPromotionActivityInfo> findSpList(MallPromotionActivityInfo mallPromotionActivityInfo);
	
	public List<MallPromotionActivityInfo> checkCouponList(@Param(value="activityType")String activityType,
			@Param(value="endTime")Date endTime,@Param(value="startTime")Date startTime,
			@Param(value="giftSku")String giftSku);
	
	public List<MallPromotionActivityInfo> checkItemList(@Param(value="activityType")String activityType,
			@Param(value="endTime")Date endTime,@Param(value="startTime")Date startTime,
			@Param(value="giftSku")String giftSku);
	
	public MallPromotionActivityInfo getCouponActivity(String id);
	
	public List<MallPromotionActivityInfo> getCouponListBySendCouponType(@Param(value="sendCouponType")String sendCouponType);
	
	public List<MallPromotionActivityInfo> getSelfActivityList();
	
	public void updateMallPromotionActivityInfo(MallPromotionActivityInfo mallPromotionActivityInfo);
	
}