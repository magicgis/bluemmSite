/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.coupon.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.DateUtil;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtil;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.coupon.dao.MallNoCodeDao;
import cn.com.bluemoon.jeesite.modules.coupon.dao.MallPromotionCouponInfoDao;
import cn.com.bluemoon.jeesite.modules.coupon.dao.MallPromotionCouponRangeDao;
import cn.com.bluemoon.jeesite.modules.coupon.dao.MallPromotionCouponRuleDao;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallNoCode;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponInfo;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponRange;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponRule;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 券主数据管理Service
 * @author linyihao
 * @version 2016-03-22
 */
@Service
@Transactional(readOnly = true)
public class MallPromotionCouponInfoService extends CrudService<MallPromotionCouponInfoDao, MallPromotionCouponInfo> {

	@Autowired
	private MallNoCodeDao mallNoCodeDao;
	@Autowired
	MallPromotionCouponRuleDao ruleDao;
	@Autowired
	MallPromotionCouponRangeDao rangeDao;
	@Autowired
	private MallPromotionCouponInfoDao couponInfoDao;
	
	public MallPromotionCouponInfo get(String id) {
		return super.get(id);
	}
	
	public List<MallPromotionCouponInfo> findList(MallPromotionCouponInfo mallCouponInfo) {
		return super.findList(mallCouponInfo);
	}
	
	public MallPromotionCouponInfo getCouponByCouponCode(String couponCode) {
		return couponInfoDao.getCouponByCouponCode(couponCode);
	}
	
	public Page<MallPromotionCouponInfo> findPage(Page<MallPromotionCouponInfo> page, MallPromotionCouponInfo mallCouponInfo) {
		String couponType = mallCouponInfo.getCouponType();
		if(StringUtil.isNotEmpty(mallCouponInfo.getCouponType())){
			String[] arr = couponType.split("_");
			mallCouponInfo.setCouponType(arr[0]);
			mallCouponInfo.setGiftType(arr[1]);;
		}
		return super.findPage(page, mallCouponInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPromotionCouponInfo entity) {
		User user = UserUtils.getUser();
		if (entity.getCouponId()==null){
			if(entity.getEndTime()!=null&&entity.getEndTime().before(new Date())){
				entity.setStatus(2);
			}else{
				entity.setStatus(1);
			}
			entity.setDelFlag("0");
			entity.setCouponId(new BigInteger(SerialNo.getSerialforDB()));
			entity.setCreateDate(new Date());
			entity = getCouponCode(entity);		//系统自动生成couponCode
			if(!"validTime".equals(entity.getTimeType())){
				entity.setValidDays(null);
			}
			dao.insert(entity);
			//保存使用范围
			List<MallPromotionCouponRange> list = entity.getListRange();
			if(list.size()>0){
				for (MallPromotionCouponRange range : list) {
					range.setCouponRangeId(new BigInteger(SerialNo.getSerialforDB()));
					range.setCouponId(entity.getCouponId());
					rangeDao.insert(range);
				}
			}
		}else{
			entity.setUpdateBy(user);
			dao.update(entity);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPromotionCouponInfo mallCouponInfo) {
		super.delete(mallCouponInfo);
	}
	
	
	/**
	 * 获取ID
	 * @param commandType	类型
	 * @param commandName	名称
	 * @param typeCode		对应字段名
	 * @param format		时间规格	
	 * @return
	 */
	public String generateCouponCardCode(String commandType, String commandName, 
			String typeCode, String format){
		StringBuffer cardId = new StringBuffer();
		try {
			cardId = new StringBuffer();
			String newNum = "";
			String increasseRule = DateUtil.getDateString(new Date(), format);
			MallNoCode mallNoCode = new MallNoCode();
			mallNoCode.setCommandType(commandType);
			mallNoCode.setIncreasseRule(increasseRule);
			List<MallNoCode> mallNoCodeList = mallNoCodeDao.findList(mallNoCode);
			if(mallNoCodeList.size()==1){
				mallNoCode = mallNoCodeList.get(0);
				String codeNum = mallNoCode.getCodeNum();
				int num = Integer.parseInt(codeNum);
				num++;
				newNum = String.valueOf(num);
				while (newNum.length()<5) {
					newNum = "0"+newNum;
				}
				mallNoCode.setCodeNum(newNum);
				mallNoCodeDao.update(mallNoCode);
			}else if(mallNoCodeList.size()==0){
				mallNoCode = new MallNoCode();
				int codeNum = 1;
				newNum = String.valueOf(codeNum);
				while (newNum.length()<5) {
					newNum = "0"+newNum;
				}
				mallNoCode.setCommandName(commandName);
				mallNoCode.setTypeCode(typeCode);
				mallNoCode.setCommandType(commandType);
				mallNoCode.setIncreasseRule(increasseRule);
				mallNoCode.setLastTime(new Date());
				mallNoCode.setCodeNum(newNum);
				mallNoCodeDao.insert(mallNoCode);
			}else{
				System.out.println("------------存在多条 : "+commandType + " " +increasseRule +" ------------------");
			}
			//拼接字符串
			cardId.append(commandType);
			cardId.append(increasseRule);
			cardId.append(newNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardId.toString();
	}
	
	private MallPromotionCouponInfo getCouponCode(MallPromotionCouponInfo entity){
		String commandType = "";
		String commandName = "";
		String typeCode = "couponCode";
		String format = "yyyyMMdd";
		MallPromotionCouponRule rule = entity.getRule();
		if("online_discount".equals(entity.getCouponType())){
			commandType = "DKQ";
			commandName = "网店抵扣券";
			entity.setCouponType("online");
			entity.setGiftType("discount");
			//保存使用规则信息rule
			rule.setCouponRuleId(new BigInteger(SerialNo.getSerialforDB()));
			rule.setCouponId(entity.getCouponId());
			rule.setConditionType("amount_over");
			rule.setConditionValue((int)(rule.getConditionValueDouble()*100));
			rule.setDenomination((int)(rule.getDenominationDouble()*100));
			rule.setGiftNum(0);
			rule.setGiftName("");
			ruleDao.insert(rule);
		}else if("online_gift".equals(entity.getCouponType())){
			commandType = "ZPQ";
			commandName = "网店赠品券";
			entity.setCouponType("online");
			entity.setGiftType("gift");
			//保存使用规则信息rule
			rule.setCouponRuleId(new BigInteger(SerialNo.getSerialforDB()));
			rule.setCouponId(entity.getCouponId());
			rule.setConditionType("");
			rule.setConditionValue(0);
			ruleDao.insert(rule);
		}else if("offline_gift".equals(entity.getCouponType())){
			commandType = "DHQ";
			commandName = "线下兑换券";
			rule = new MallPromotionCouponRule();
			rule.setCouponRuleId(new BigInteger(SerialNo.getSerialforDB()));
			rule.setCouponId(entity.getCouponId());
			ruleDao.insert(rule);
			entity.setCouponType("offline");
			entity.setGiftType("gift");
		}
		String couponCode = generateCouponCardCode(commandType, commandName, typeCode, format);
		entity.setCouponCode(couponCode);
		return entity;
	}
	
	public MallPromotionCouponRule getRule(MallPromotionCouponRule rule){
		List<MallPromotionCouponRule> list = ruleDao.findList(rule);
		if(list.size()==1){
			return list.get(0);
		}else{
			logger.error("查询券使用规则信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	public List<MallPromotionCouponRange> getRange(MallPromotionCouponRange range){
		List<MallPromotionCouponRange> list = rangeDao.findList(range);
		if(list.size()>0){
			return list;
		}else{
			logger.error("查询券适用范围信息出错，一共有"+list.size()+"条数据!");
			return null;
		}
	}
	
	@Transactional(readOnly = false)
	public boolean cancelCoupon(MallPromotionCouponInfo entity){
		List<MallPromotionActivityInfo> list = dao.getActivityByCouponId(entity.getCouponId());
		if(list.size()>0){
			return false;
		}else{
			User user = UserUtils.getUser();
			dao.cancelCoupon(entity.getCouponId(),user.getLoginName());
			return true;
		}
	}
	
}