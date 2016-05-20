/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallOrderInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendOperInfo;

/**
 * 推荐码申请审核DAO接口
 * @author xgb
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallRecommendInfoDao extends CrudDao<MallRecommendInfo> {
	public MallOrderInfo getOrderByOrderCode(@Param(value="orderCode")String orderCode) throws Exception;
	
	public MallRecommendOperInfo getOperInfoByReId(@Param(value="recommendInfoId")String recommendInfoId,
			@Param(value="haveRecommend")String haveRecommend,@Param(value="oldRecommend")String oldRecommend,@Param(value="aduitStatus")String aduitStatus) throws Exception;
	
	public List<MallRecommendOperInfo> getOperInfoById(@Param(value="id")String id)throws Exception;
	
	public MallRecommendInfo getRecommendByOrderCode(@Param(value="orderCode")String orderCode) throws Exception;
	
	public void updateOrderRecomment(@Param(value="orderCode")String orderCode,@Param(value="recommendCode")String recommendCode) throws Exception;
}