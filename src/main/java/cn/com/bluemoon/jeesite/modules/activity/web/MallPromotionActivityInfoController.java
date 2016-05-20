/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activity.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.mapper.JsonMapper;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCoupon;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponLevel;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponLevelDetail;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponMap;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjsLevel;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRange;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRangeVo;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoService;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;

/**
 * 优惠券活动管理Controller
 * @author linyihao
 * @version 2016-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/mallPromotionActivityInfo")
public class MallPromotionActivityInfoController extends BaseController {

	@Autowired
	private MallPromotionActivityInfoService activityService;
	@Autowired
	private ItemService itemService;
	
	@ModelAttribute
	public MallPromotionActivityInfo get(@RequestParam(required=false) String id) {
		MallPromotionActivityInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = activityService.getCouponActivity(id);
		}
		if (entity == null){
			entity = new MallPromotionActivityInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("activity:mallPromotionActivityInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionActivityInfo mallPromotionActivityInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		mallPromotionActivityInfo.setActivityType("coupon");
		Page<MallPromotionActivityInfo> page = activityService.findPageCoupon(new Page<MallPromotionActivityInfo>(request, response), mallPromotionActivityInfo); 
		model.addAttribute("page", page);
		return "modules/activity/mallPromotionActivityInfoList";
	}

	@RequiresPermissions("activity:mallPromotionActivityInfo:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionActivityInfo entity, Model model) {
		if(StringUtils.isBlank(entity.getActivityCode())){
			User user = UserUtils.getUser();
			entity.setActivityCode("系统自动生成");
			entity.setCreateCode(user.getLoginName());
			entity.setCreateName(user.getName());
			entity.setRangeType("part");
			entity.setCreateDate(new Date());
			model.addAttribute("mallPromotionActivityInfo", entity);
			return "modules/activity/mallPromotionActivityInfoForm";
		}else{
			//活动范围
			if("part".equals(entity.getRangeType())){
				List<MallPromotionActivityRange> listRange = activityService.getRangeByActivityId(new MallPromotionActivityRange(entity.getActivityId()));
				List<MallPromotionActivityRangeVo> listVo = new ArrayList<MallPromotionActivityRangeVo>();
				if(listRange!=null&&listRange.size()>0){
					entity.setRange(listRange);
					for (MallPromotionActivityRange po : listRange) {
						MallPromotionActivityRangeVo vo = new MallPromotionActivityRangeVo(po);
						listVo.add(vo);
					}
					JSONArray jsonRange = JSONArray.fromObject(listVo);
					entity.setRangeJson(jsonRange);
				}
			}
			//TODO  发券方式
			MallPromotionActivityCoupon activityCoupon = new MallPromotionActivityCoupon();
			activityCoupon.setActivityId(entity.getActivityId());
			activityCoupon = activityService.getCouponTypeByActivityId(activityCoupon);
			boolean isAmountOver = true;
			if(!"amount_over".equals(activityCoupon.getConditionType())){
				isAmountOver = false;
			}
			entity.setCoupon(activityCoupon);
			//	可发送券
			MallPromotionActivityCouponMap couponMap = new MallPromotionActivityCouponMap();
			couponMap.setActivityId(entity.getActivityId());
			List<MallPromotionActivityCouponMap> listMap = activityService.getCouponByActivityId(couponMap);
			entity.setCouponMap(listMap);
			// 级别优惠
			if("sys".equals(activityCoupon.getSendCouponType())){
				MallPromotionActivityCouponLevel couponLevel = new MallPromotionActivityCouponLevel();
				couponLevel.setActivityId(entity.getActivityId());
				List<MallPromotionActivityCouponLevel> listLevel = activityService.getCouponByActivityId(couponLevel);
				if(isAmountOver){
					if(listLevel != null && listLevel.size() > 0){
						for(int i=0;i<listLevel.size();i++){
							double f = (double)(Integer.parseInt(listLevel.get(i).getConditionValue())/100);  
							BigDecimal b = new BigDecimal(f);  
							double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
							listLevel.get(i).setConditionValue(Double.valueOf(f1).toString());
						}
					}
					
				}
				entity.setCouponLevel(listLevel);
			}
			model.addAttribute("mallPromotionActivityInfo", entity);
			return "modules/activity/mallPromotionActivityInfoFormQuery";
		}
	}
	
	@RequiresPermissions("activity:mallPromotionActivityInfo:view")
	@RequestMapping(value = "modify")
	public String modify(MallPromotionActivityInfo entity, Model model) {
		User user = UserUtils.getUser();
		entity.setUpdatBy(user.getLoginName());
		entity.setCreateName(user.getName());
		entity.setCreateDate(new Date());
		//活动范围
		if("part".equals(entity.getRangeType())){
			List<MallPromotionActivityRange> listRange = activityService.getRangeByActivityId(new MallPromotionActivityRange(entity.getActivityId()));
			List<MallPromotionActivityRangeVo> listVo = new ArrayList<MallPromotionActivityRangeVo>();
			if(listRange!=null&&listRange.size()>0){
				entity.setRange(listRange);
				for (MallPromotionActivityRange po : listRange) {
					MallPromotionActivityRangeVo vo = new MallPromotionActivityRangeVo(po);
					listVo.add(vo);
				}
				JSONArray jsonRange = JSONArray.fromObject(listVo);
				entity.setRangeJson(jsonRange);
			}
		}
		//TODO  发券方式
		MallPromotionActivityCoupon activityCoupon = new MallPromotionActivityCoupon();
		activityCoupon.setActivityId(entity.getActivityId());
		activityCoupon = activityService.getCouponTypeByActivityId(activityCoupon);
		boolean isAmountOver = true;
		if(!"amount_over".equals(activityCoupon.getConditionType())){
			isAmountOver = false;
		}
		entity.setCoupon(activityCoupon);
		//	可发送券
		MallPromotionActivityCouponMap couponMap = new MallPromotionActivityCouponMap();
		couponMap.setActivityId(entity.getActivityId());
		List<MallPromotionActivityCouponMap> listMap = activityService.getCouponByActivityId(couponMap);
		entity.setCouponMap(listMap);
		// 级别优惠
		if("sys".equals(activityCoupon.getSendCouponType())){
			MallPromotionActivityCouponLevel couponLevel = new MallPromotionActivityCouponLevel();
			couponLevel.setActivityId(entity.getActivityId());
			List<MallPromotionActivityCouponLevel> listLevel = activityService.getCouponByActivityId(couponLevel);
			if(isAmountOver){
				if(listLevel != null && listLevel.size() > 0){
					for(int i=0;i<listLevel.size();i++){
						double f = (double)(Integer.parseInt(listLevel.get(i).getConditionValue())/100);  
						BigDecimal b = new BigDecimal(f);  
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						listLevel.get(i).setConditionValue(Double.valueOf(f1).toString());
					}
				}
				
			}
			entity.setCouponLevel(listLevel);
		}
		model.addAttribute("mallPromotionActivityInfo", entity);
		return "modules/activity/mallPromotionActivityInfoFormModify";
	}

	@RequiresPermissions("activity:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "save")
	@SuppressWarnings("unchecked")
	public String save(HttpServletRequest request,MallPromotionActivityInfo mallPromotionActivityInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPromotionActivityInfo)){
			return form(mallPromotionActivityInfo, model);
		}
		String itemData = request.getParameter("itemData");
		String couponMap = request.getParameter("couponMapData");
		MallPromotionActivityCoupon coupon = new MallPromotionActivityCoupon();
		JSONArray json = null;
		if("part".equals(mallPromotionActivityInfo.getRangeType())){
			if(StringUtils.isBlank(itemData)){
				return form(mallPromotionActivityInfo, model);
			}
			json = JSONArray.fromObject(itemData);
			List<MallPromotionActivityRange> list = (List<MallPromotionActivityRange>)JSONArray.toCollection(json, MallPromotionActivityRange.class);
			mallPromotionActivityInfo.setRange(list);
		}
		coupon.setSendCouponType(mallPromotionActivityInfo.getSendCouponType());
		if("sys".equals(mallPromotionActivityInfo.getSendCouponType())){
			String couponLevel = request.getParameter("couponLevelData");
			String conditionType = request.getParameter("condition");
			coupon.setConditionType(conditionType);
			if(StringUtils.isBlank(couponMap)){
				return form(mallPromotionActivityInfo, model);
			}
			if(StringUtils.isBlank(couponLevel)){
				return form(mallPromotionActivityInfo, model);
			}
			json = JSONArray.fromObject(couponMap);
			List<MallPromotionActivityCouponMap> cmList = (List<MallPromotionActivityCouponMap>)JSONArray.toCollection(json,MallPromotionActivityCouponMap.class);
			mallPromotionActivityInfo.setCouponMap(cmList);
			json = JSONArray.fromObject(couponLevel);
			List<MallPromotionActivityCouponLevel> clList = (List<MallPromotionActivityCouponLevel>)JSONArray.toCollection(json, MallPromotionActivityCouponLevel.class);
			mallPromotionActivityInfo.setCouponLevel(clList);
			boolean isAmountOver = true;
			if(!"amount_over".equals(conditionType)){
				isAmountOver = false;
			}
			for(int i=0;i<clList.size();i++){
				String[] detailStr = clList.get(i).getLevelDetailStr().split(";");
				List<MallPromotionActivityCouponLevelDetail> cdList = new ArrayList<MallPromotionActivityCouponLevelDetail>();
				String mo = clList.get(i).getConditionValue().substring(0, clList.get(i).getConditionValue().length()-1);
				if(isAmountOver){
					double money = Double.parseDouble(mo);
					clList.get(i).setConditionValue((int)(money*100)+"");
				}else{
					clList.get(i).setConditionValue(mo);
				}
				for(int j=0;j<detailStr.length;j++){
					MallPromotionActivityCouponLevelDetail cd = new MallPromotionActivityCouponLevelDetail();
					String[] type = detailStr[j].split(":");
					Long id = Long.valueOf(type[0]);
					int sendNum = Integer.valueOf(type[1]);
					cd.setCouponId(BigInteger.valueOf(id));
					cd.setSendNum(sendNum);
					cdList.add(cd);
				}
				clList.get(i).setLevelDetail(cdList);
			}
		}else if("self".equals(mallPromotionActivityInfo.getSendCouponType())){
			String position = request.getParameter("position");
			coupon.setPosition(position);
			json = JSONArray.fromObject(couponMap);
			List<MallPromotionActivityCouponMap> cmList = (List<MallPromotionActivityCouponMap>)JSONArray.toCollection(json,MallPromotionActivityCouponMap.class);
			mallPromotionActivityInfo.setCouponMap(cmList);
			coupon.setConditionType("");
		}else if("man_send".equals(mallPromotionActivityInfo.getSendCouponType())){
			json = JSONArray.fromObject(couponMap);
			List<MallPromotionActivityCouponMap> cmList = (List<MallPromotionActivityCouponMap>)JSONArray.toCollection(json,MallPromotionActivityCouponMap.class);
			mallPromotionActivityInfo.setCouponMap(cmList);
			coupon.setConditionType("");
		}else if("sys_regist".equals(mallPromotionActivityInfo.getSendCouponType())){
			json = JSONArray.fromObject(couponMap);
			List<MallPromotionActivityCouponMap> cmList = (List<MallPromotionActivityCouponMap>)JSONArray.toCollection(json,MallPromotionActivityCouponMap.class);
			mallPromotionActivityInfo.setCouponMap(cmList);
			coupon.setConditionType("");
		}
		mallPromotionActivityInfo.setCoupon(coupon);
		activityService.save(mallPromotionActivityInfo);
		addMessage(redirectAttributes, "保存优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activity/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activity:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "update")
	@SuppressWarnings("unchecked")
	public String update(HttpServletRequest request,MallPromotionActivityInfo mallPromotionActivityInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPromotionActivityInfo)){
			return form(mallPromotionActivityInfo, model);
		}
		activityService.update(mallPromotionActivityInfo);
		addMessage(redirectAttributes, "修改优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activity/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activity:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionActivityInfo mallPromotionActivityInfo, RedirectAttributes redirectAttributes) {
		activityService.delete(mallPromotionActivityInfo);
		addMessage(redirectAttributes, "取消优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activity/mallPromotionActivityInfo/?repage";
	}

	/**
	 * 添加级别优惠
	 */
	@RequestMapping(value = "addLevel")
	public String addLevel(MallPromotionActivityMjsLevel level,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String condition = request.getParameter("condition");
		level = new MallPromotionActivityMjsLevel();
		model.addAttribute("mallPromotionActivityMjsLevel", level);
		model.addAttribute("condition", condition);
		return "modules/activity/levelForm";
	}
	
	/**
	 * 通过商品sku获取商品
	 * @throws Exception 
	 */
	@RequestMapping(value = "getItemBySku")
	@ResponseBody
	public Map<String, Object> getItemList(MallPromotionActivityInfo entity, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		//过滤出售的
		Map<String, Object> map = null;
		String json = "";
		String itemSku = request.getParameter("itemSku");
		String startDateStr = request.getParameter("startTime");
		String endDateStr = request.getParameter("endTime");
		
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(startDateStr);
		Date endDate = sdf.parse(endDateStr);
		Timestamp startTime = new Timestamp(startDate.getTime());
		Timestamp endTime = new Timestamp(endDate.getTime());
		List<MallItemInfo> list = itemService.findItemBySku(itemSku.trim());
		
		if(list != null && list.size()>0){
			map = new HashMap<String, Object>();
			MallItemInfo itemInfo = list.get(0);
			//判断同一商品不能包含在不同的活动中
			List<MallPromotionActivityInfo> listActivity = activityService.checkCouponList(startTime, endTime, itemSku);
			if(listActivity.size()>0){
				MallPromotionActivityInfo info = listActivity.get(0);
				json = "sku为"+itemSku+"的商品已经存在编码为"+info.getActivityCode()+"的活动中";
				map.put("flag", "error");
			}else{
				json = JsonMapper.toJsonString(itemInfo);
				map.put("flag", "success");
			}
			map.put("json", json);
		}
		return map;
	}
}