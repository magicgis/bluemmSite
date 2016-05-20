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
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityMjsDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityMjsLevelDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityRangeDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
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
public class MallPromotionActivityInfoMJSService extends CrudService<MallPromotionActivityInfoDao, MallPromotionActivityInfo> {
	
	@Autowired
	private MallPromotionCouponInfoService couponService;
	@Autowired
	private MallPromotionActivityRangeDao rangeDao;
	@Autowired
	private MallPromotionActivityMjsLevelDao levelDao;
	@Autowired
	private MallPromotionActivityMjsDao mjsDao;


	public MallPromotionActivityInfo get(String id) {
		return super.get(id);
	}
	
	public List<MallPromotionActivityInfo> findList(MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findList(mallPromotionActivityInfo);
	}
	
	public Page<MallPromotionActivityInfo> findPage(Page<MallPromotionActivityInfo> page, MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findPage(page, mallPromotionActivityInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPromotionActivityInfo entity) {
		User user = UserUtils.getUser();
		if (entity.getActivityId()==null){
			entity.setActivityId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
			entity.setActivityCode(getCouponCode());
			entity.setCreateBy(user);
			entity.setCreateDate(new Date());
			entity.setUpdateBy(user);
			entity.setUpdateDate(new Date());
			entity.setStatus(1);
			entity.setActivityType("mjs");
			entity.setDelFlag("0");
			dao.insert(entity);
			//保存满足条件
			MallPromotionActivityMjs mjs = new MallPromotionActivityMjs();
			mjs.setActivityId(entity.getActivityId());
			mjs.setActivityMjsId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
			mjs.setConditionType(entity.getCondition());
			mjsDao.insert(mjs);
			//保存活动范围
			List<MallPromotionActivityRange> listRange = entity.getRange();
			if(listRange.size()>0){
				for (MallPromotionActivityRange range : listRange) {
					range.setActivityRangeId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
					range.setActivityId(entity.getActivityId());
					rangeDao.insert(range);
				}
			}
			//保存优惠级别
			List<MallPromotionActivityMjsLevel> listLevel = entity.getLevel();
			if(listLevel.size()>0){
				for (MallPromotionActivityMjsLevel level : listLevel) {
					level.setMjsLevelId(Long.valueOf(SerialNo.getSerialforDB()));
					level.setActivityId(entity.getActivityId());
					if("amount_over".equals(entity.getCondition())){
						level.setConditionValue((int)(level.getConditionValueDouble()*100));
					}else{
						level.setConditionValue((int)level.getConditionValueDouble());
					}
					if("less_money".equals(level.getPreferentialType())){
						level.setPreferentialValue((int)(level.getPreferentialValueDouble()*100));
					}else if("discount".equals(level.getPreferentialType())){
						level.setPreferentialValue((int)(level.getPreferentialValueDouble()*10));
					}
					//TODO
					if(level.getGiftPriceFloat()!=0){
						level.setGiftPrice((int)(level.getGiftPriceFloat()*100));
					}
					levelDao.insert(level);
				}
			}
		}else{
			entity.setUpdateBy(user);
			entity.setStatus(1);
			entity.setUpdatBy(user.getLoginName());
			entity.setUpdateDate(new Date());
			dao.update(entity);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPromotionActivityInfo mallPromotionActivityInfo) {
		super.delete(mallPromotionActivityInfo);
	}
	
	private String getCouponCode(){
		String commandType = "MJS";
		String commandName = "满就送活动";
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
	
	public List<MallPromotionActivityMjsLevel> getLevelByActivityId(MallPromotionActivityMjsLevel level){
		List<MallPromotionActivityMjsLevel> list = levelDao.findList(level);
		if(list.size()>0){
			return list;
		}else{
			logger.error("查询券级别优惠信息出错，一共有"+list.size()+"条数据!");
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
}