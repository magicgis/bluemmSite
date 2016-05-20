/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityCouponDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityCouponLevelDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityCouponLevelDetailDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityCouponMapDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityInfoDao;
import cn.com.bluemoon.jeesite.modules.activity.dao.MallPromotionActivityRangeDao;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCoupon;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponLevel;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponLevelDetail;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponMap;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRange;
import cn.com.bluemoon.jeesite.modules.coupon.service.MallPromotionCouponInfoService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 优惠券活动管理Service
 * @author linyihao
 * @version 2016-03-29
 */
@Service
@Transactional(readOnly = true)
public class MallPromotionActivityInfoService extends CrudService<MallPromotionActivityInfoDao, MallPromotionActivityInfo> {
	@Autowired
	private MallPromotionActivityRangeDao rangeDao;
	@Autowired
	private MallPromotionCouponInfoService couponService;
	@Autowired
	private MallPromotionActivityCouponDao couponDao;
	@Autowired
	private MallPromotionActivityCouponMapDao couponMapDao;
	@Autowired
	private MallPromotionActivityCouponLevelDao couponLevelDao;
	@Autowired
	private MallPromotionActivityCouponLevelDetailDao couponLevelDetailDao;
	
	
	public MallPromotionActivityInfo get(String id) {
		return super.get(id);
	}
	
	public MallPromotionActivityInfo getCouponActivity(String id) {
		return dao.getCouponActivity(id);
	}
	
	public List<MallPromotionActivityInfo> findList(MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findList(mallPromotionActivityInfo);
	}
	
	public Page<MallPromotionActivityInfo> findPage(Page<MallPromotionActivityInfo> page, MallPromotionActivityInfo mallPromotionActivityInfo) {
		return super.findPage(page, mallPromotionActivityInfo);
	}
	
	public Page<MallPromotionActivityInfo> findPageCoupon(Page<MallPromotionActivityInfo> page, MallPromotionActivityInfo mallPromotionActivityInfo) {
		mallPromotionActivityInfo.setPage(page);
		page.setList(dao.findCouponList(mallPromotionActivityInfo));
		return page;
	}
	
	
	@Transactional(readOnly = false)
	public void save(MallPromotionActivityInfo mallPromotionActivityInfo) {
		User user = UserUtils.getUser();
		if (mallPromotionActivityInfo.getActivityId()==null){
			BigInteger id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
			mallPromotionActivityInfo.setActivityId(id);
			Date date = new Date();
			if(mallPromotionActivityInfo.getEndTime().getTime() < date.getTime()){
				mallPromotionActivityInfo.setStatus(2);
			}else{
				mallPromotionActivityInfo.setStatus(1);
			}
			mallPromotionActivityInfo.setActivityType("coupon");
			mallPromotionActivityInfo.setActivityCode(getCouponCode());
			//保存活动范围
			List<MallPromotionActivityRange> listRange = mallPromotionActivityInfo.getRange();
			if(listRange != null && listRange.size()>0){
				for (MallPromotionActivityRange range : listRange) {
					range.setActivityRangeId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
					range.setActivityId(mallPromotionActivityInfo.getActivityId());
					rangeDao.insert(range);
				}
			}
			//保存活动优惠券
			MallPromotionActivityCoupon coupon = mallPromotionActivityInfo.getCoupon();	
			coupon.setActivityCouponId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
			coupon.setActivityId(mallPromotionActivityInfo.getActivityId());
			couponDao.insert(coupon);
			//保存couponMap
			List<MallPromotionActivityCouponMap> cm = mallPromotionActivityInfo.getCouponMap();
			if(cm != null && cm.size() > 0){
				for(MallPromotionActivityCouponMap cMap : cm){
					BigInteger actId = mallPromotionActivityInfo.getActivityId();
					BigInteger qId = coupon.getActivityCouponId();
					BigInteger bi = new BigInteger(actId.toString());
					cMap.setActivityId((bi));
					cMap.setMapId(Long.valueOf(SerialNo.getSerialforDB()).toString());
					couponMapDao.insert(cMap);
				}
			}
			//保存couponlevel
			List<MallPromotionActivityCouponLevel> cl = mallPromotionActivityInfo.getCouponLevel();
			if(cl != null && cm.size() > 0){
				for(MallPromotionActivityCouponLevel cLevle : cl){
					cLevle.setActivityId(mallPromotionActivityInfo.getActivityId());
					cLevle.setCouponLevelId(BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())));
					couponLevelDao.insert(cLevle);
					List<MallPromotionActivityCouponLevelDetail> cld = cLevle.getLevelDetail();
					if(cld != null && cl.size() > 0){
						for(MallPromotionActivityCouponLevelDetail cd : cld){
							BigInteger ids = cLevle.getCouponLevelId();
							BigInteger bi = new BigInteger(ids.toString());
							cd.setCouponLevelDetailId(Long.valueOf(SerialNo.getSerialforDB()).toString());
							cd.setCouponLevelId(bi); 
							couponLevelDetailDao.insert(cd);
						}
					}
				}
			}
			//保存couponLevelDetail
			/*List<MallPromotionActivityCouponLevelDetail> cld = mallPromotionActivityInfo.getCouponLevelDetail();
			if(cm.size() > 0){
				for(MallPromotionActivityCouponLevelDetail clDetail : cld){
					cLevle.setActivityId(mallPromotionActivityInfo.getActivityId());
					cLevle.setCouponLevelId(Long.valueOf(SerialNo.getSerialforDB()));
					couponLevelDao.insert(cLevle);
				}
			}*/
		}
		super.save(mallPromotionActivityInfo);
	}
	
	@Transactional(readOnly = false)
	public void update(MallPromotionActivityInfo mallPromotionActivityInfo) {
		mallPromotionActivityInfo.setStatus(1);
		mallPromotionActivityInfo.setUpdateDate(new Date());
		dao.update(mallPromotionActivityInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPromotionActivityInfo mallPromotionActivityInfo) {
		super.delete(mallPromotionActivityInfo);
	}
	
	private String getCouponCode(){
		String commandType = "YHQ";
		String commandName = "优惠券活动";
		String typeCode = "activityCode";
		String format = "yyyyMMdd";
		String couponCode = couponService.generateCouponCardCode(commandType, commandName, typeCode, format);
		return couponCode;
	}
	
	public List<MallPromotionActivityInfo> checkList(Date startTime,Date endTime,String giftSku){
		List<MallPromotionActivityInfo> list = dao.checkList("coupon", endTime, startTime, giftSku);
		return list;
	}
	
	public List<MallPromotionActivityInfo> checkCouponList(Date startTime,Date endTime,String giftSku){
		List<MallPromotionActivityInfo> list = dao.checkCouponList("coupon", endTime, startTime, giftSku);
		return list;
	}
	
	public List<MallPromotionActivityInfo> checkItemList(Date startTime,Date endTime,String giftSku){
		List<MallPromotionActivityInfo> list = dao.checkCouponList("item", endTime, startTime, giftSku);
		return list;
	}
	
	/*
	 * 根据发放方式获取有效，未过期活动
	 */
	public List<MallPromotionActivityInfo> getCouponListBySendCouponType(String sendCouponType){
		List<MallPromotionActivityInfo> list = dao.getCouponListBySendCouponType(sendCouponType);
		return list;
	}
	
	//获取活动范围
	public List<MallPromotionActivityRange> getRangeByActivityId(MallPromotionActivityRange range){
		List<MallPromotionActivityRange> list = rangeDao.findList(range);
		if(list.size()>0){
			return list;
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	//获取优惠券发送方式
	public MallPromotionActivityCoupon getCouponTypeByActivityId(MallPromotionActivityCoupon entity){
		List<MallPromotionActivityCoupon> list = couponDao.findList(entity);
		if(list.size()>0){
			return list.get(0);
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	//获取可发送优惠券
	public List<MallPromotionActivityCouponMap> getCouponByActivityId(MallPromotionActivityCouponMap entity){
		List<MallPromotionActivityCouponMap> list = couponMapDao.findList(entity);
		if(list.size()>0){
			return list;
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	//获取优惠券活动级别优惠
	public List<MallPromotionActivityCouponLevel> getCouponByActivityId(MallPromotionActivityCouponLevel entity){
		List<MallPromotionActivityCouponLevel> list = couponLevelDao.findList(entity);
		if(list.size()>0){
			for (MallPromotionActivityCouponLevel level : list) {
				MallPromotionActivityCouponLevelDetail detail = new MallPromotionActivityCouponLevelDetail();
				detail.setCouponLevelId(level.getCouponLevelId());
				List<MallPromotionActivityCouponLevelDetail> listDetail = couponLevelDetailDao.findList(detail);
				String levelDetailStr = "";
				if(listDetail.size()>0){
					for (MallPromotionActivityCouponLevelDetail temp : listDetail) {
						levelDetailStr += temp.getCouponSName()+"*"+temp.getSendNum()+"<br>";
					}
				}
				level.setLevelDetailStr(levelDetailStr);
//				double f = Double.valueOf(level.getConditionValue())/100;  
//				BigDecimal b = new BigDecimal(f);  
//				double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
//				level.setConditionValue(String.valueOf(f1));
			}
			return list;
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	public List<MallPromotionActivityInfo> getSelfActivityList(){
		return dao.getSelfActivityList();
	}
}
