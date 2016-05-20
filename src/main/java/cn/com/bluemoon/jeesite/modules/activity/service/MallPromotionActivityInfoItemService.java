/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityInfoDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityItemDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityMjsDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityMjsLevelDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityRangeDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityItemInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjs;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjsLevel;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRange;
import cn.com.bluemoon.jeesite.modules.coupon.service.MallPromotionCouponInfoService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 满就送活动管理Service
 * @author linyihao
 * @version 2016-03-29
 */
@Service
@Transactional(readOnly = true)
public class MallPromotionActivityInfoItemService extends CrudService<MallPromotionActivityInfoDao, MallPromotionActivityInfo> {
	
	@Autowired
	private MallPromotionCouponInfoService couponService;
	@Autowired
	private MallPromotionActivityRangeDao rangeDao;

	@Autowired
	private MallPromotionActivityItemDao itemDao;


	public MallPromotionActivityInfo get(String id) {
		return itemDao.getSpActivity(id);
	}
	
	public List<MallPromotionActivityInfo> findList(MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findList(mallPromotionActivityInfo);
	}
	
	public Page<MallPromotionActivityInfo> findPage(Page<MallPromotionActivityInfo> page, MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findPage(page, mallPromotionActivityInfo);
	}
	
	public Page<MallPromotionActivityInfo> findPageSp(Page<MallPromotionActivityInfo> page, MallPromotionActivityInfo mallPromotionActivityInfo) {
		if(page.getPageNo()>mallPromotionActivityInfo.getPageCount()){
			page.setPageNo(mallPromotionActivityInfo.getPageCount());
		}
		mallPromotionActivityInfo.setPage(page);
		page.setList(itemDao.findSpList(mallPromotionActivityInfo));
		mallPromotionActivityInfo.setPageCount(page.getTotalPage());
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(MallPromotionActivityInfo entity) {
		User user = UserUtils.getUser();
		entity.setActivityId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
		entity.setActivityCode(getCouponCode());
		entity.setCreateBy(user);
		entity.setCreateDate(new Date());
		entity.setUpdateBy(user);
		entity.setUpdateDate(new Date());
		entity.setStatus(1);
		entity.setActivityType("item");
		entity.setDelFlag("0");
		dao.insert(entity);
		//保存满足条件
		MallPromotionActivityItemInfo item = entity.getItemInfo();
		item.setActivityId(entity.getActivityId());
		item.setActivityItemId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
		itemDao.insert(item);
		//保存活动范围
		List<MallPromotionActivityRange> listRange = entity.getRange();
		if(listRange.size()>0){
			for (MallPromotionActivityRange range : listRange) {
				range.setActivityRangeId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
				range.setActivityId(entity.getActivityId());
				rangeDao.insert(range);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void update(MallPromotionActivityInfo entity) {
		entity.setStatus(1);
		entity.setUpdateDate(new Date());
		
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPromotionActivityInfo mallPromotionActivityInfo) {
		super.delete(mallPromotionActivityInfo);
	}
	
	private String getCouponCode(){
		String commandType = "SPJ";
		String commandName = "商品活动";
		String typeCode = "activityCode";
		String format = "yyyyMMdd";
		String couponCode = couponService.generateCouponCardCode(commandType, commandName, typeCode, format);
		return couponCode;
	}
	
	public List<MallPromotionActivityRange> getRangeByActivityId(MallPromotionActivityRange range){
		List<MallPromotionActivityRange> list = rangeDao.findList(range);
		if(list.size()>0){
			return list;
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	public List<MallPromotionActivityInfo> checkList(Date startTime,Date endTime,String giftSku){
		List<MallPromotionActivityInfo> list = dao.checkList("mjs", endTime, startTime, giftSku);
		return list;
	}
	
	public boolean checkSholeshop(Date startTime,Date endTime){
		List<MallPromotionActivityInfo> list = dao.checkSholeshop("mjs", endTime, startTime);
		if(list.size()>0){
			logger.error(list.get(0).getActivityCode()+" 的活动已经适用于全部商品！！！");
			return false;
		}else{
			return true;
		}
	}
	
	public boolean checkSholeshop(String rangType,Date startTime,Date endTime){
		List<MallPromotionActivityInfo> list = dao.checkSholeshops(rangType, endTime, startTime);
		if(list.size()>0){
			logger.error(list.get(0).getActivityCode()+" 的活动已经适用于全部商品！！！");
			return false;
		}else{
			return true;
		}
	}
}