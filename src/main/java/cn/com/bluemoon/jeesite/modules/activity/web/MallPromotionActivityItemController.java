package cn.com.bluemoon.jeesite.modules.activity.web;

import java.math.BigDecimal;
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
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityItemInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRange;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRangeVo;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoItemService;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoService;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;

/**
 * 商品活动管理Controller
 * @author linyihao
 * @version 2016-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/activityItem/mallPromotionActivityInfo")
public class MallPromotionActivityItemController extends BaseController {

	@Autowired
	private MallPromotionActivityInfoItemService activityService;
	
	@Autowired
	private MallPromotionActivityInfoService activityInfoService;
	
	@Autowired
	private ItemService itemService;
	
	@ModelAttribute
	public MallPromotionActivityInfo get(@RequestParam(required=false) String id) {
		MallPromotionActivityInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = activityService.get(id);
		}
		if (entity == null){
			entity = new MallPromotionActivityInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("activityItem:mallPromotionActivityInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionActivityInfo mallPromotionActivityInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		mallPromotionActivityInfo.setActivityType("item");
		Page<MallPromotionActivityInfo> page = activityService.findPageSp(new Page<MallPromotionActivityInfo>(request, response), mallPromotionActivityInfo); 
		for (MallPromotionActivityInfo temp : page.getList()) {
			if(temp.getStatus()==1&&temp.getEndTime()!=null
					&&temp.getEndTime().before(new Date())){
				temp.setStatus(2);
			}
		}
		model.addAttribute("page", page);
		return "modules/activityItem/mallPromotionActivityInfoList";
	}

	@RequiresPermissions("activityItem:mallPromotionActivityInfo:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionActivityInfo entity, Model model) {
		if(StringUtils.isBlank(entity.getActivityCode())){
			User user = UserUtils.getUser();
			entity.setActivityCode("系统自动生成");
			entity.setCreateCode(user.getLoginName());
			entity.setCreateName(user.getName());
			entity.setRangeType("part");
			entity.setCreateDate(new Date());
		}
		model.addAttribute("mallPromotionActivityInfo", entity);
		return "modules/activityItem/mallPromotionActivityInfoForm";
	}
	
	@RequiresPermissions("activityItem:mallPromotionActivityInfo:view")
	@RequestMapping(value = "info")
	public String info(MallPromotionActivityInfo entity, Model model) {
		entity = activityService.get(entity.getActivityId()+"");
		User user = UserUtils.getUser();
		entity.setUpdatBy(user.getLoginName());
		entity.setCreateName(user.getName());
		entity.setCreateDate(new Date());
		//活动范围
		if("part".equals(entity.getRangeType())){
			List<MallPromotionActivityRange> listRange = activityService.getRangeByActivityId(new MallPromotionActivityRange(entity.getActivityId()));
			List<MallPromotionActivityRangeVo> listVo = new ArrayList<MallPromotionActivityRangeVo>();
			if(listRange!=null&&listRange.size()>0){
				for (MallPromotionActivityRange po : listRange) {
					MallPromotionActivityRangeVo vo = new MallPromotionActivityRangeVo(po);
					listVo.add(vo);
				}
				JSONArray jsonRange = JSONArray.fromObject(listVo);
				entity.setRangeJson(jsonRange);
			}
		}
		String itemValue = "";
		if("limited_price".equals(entity.getActivityItemType())){
			double mon = Double.valueOf(entity.getActivityItemValue())/100.00;
			itemValue = mon+"";
		}else if("lmited_discount".equals(entity.getActivityItemType())){
			double dis =  Double.valueOf(entity.getActivityItemValue())/10.0;
			 if(dis % 1.0 == 0){
				 itemValue = (int)dis+"";
		    }else{
		    	itemValue = dis+"";
		    }
			
		}
		model.addAttribute("mallPromotionActivityInfo", entity);
		model.addAttribute("itemValue",itemValue);
		return "modules/activityItem/mallPromotionActivityInfoFormInfo";
	}
	
	@RequiresPermissions("activityItem:mallPromotionActivityInfo:view")
	@RequestMapping(value = "modify")
	public String modify(MallPromotionActivityInfo entity, Model model) {
		entity = activityService.get(entity.getActivityId()+"");
		String itemValue = "";
		if("limited_price".equals(entity.getActivityItemType())){
			double mon = Double.valueOf(entity.getActivityItemValue())/100.00;
			itemValue = mon+"";
		}else if("lmited_discount".equals(entity.getActivityItemType())){
			double dis =  Double.valueOf(entity.getActivityItemValue())/10.0;
			 if(dis % 1.0 == 0){
				 itemValue = (int)dis+"";
		    }else{
		    	itemValue = dis+"";
		    }
			
		}
		User user = UserUtils.getUser();
		entity.setUpdatBy(user.getLoginName());
		entity.setCreateName(user.getName());
		entity.setCreateDate(new Date());
		//活动范围
		if("part".equals(entity.getRangeType())){
			List<MallPromotionActivityRange> listRange = activityService.getRangeByActivityId(new MallPromotionActivityRange(entity.getActivityId()));
			List<MallPromotionActivityRangeVo> listVo = new ArrayList<MallPromotionActivityRangeVo>();
			if(listRange!=null&&listRange.size()>0){
				for (MallPromotionActivityRange po : listRange) {
					MallPromotionActivityRangeVo vo = new MallPromotionActivityRangeVo(po);
					listVo.add(vo);
				}
				JSONArray jsonRange = JSONArray.fromObject(listVo);
				entity.setRangeJson(jsonRange);
			}
		}
		model.addAttribute("mallPromotionActivityInfo", entity);
		model.addAttribute("itemValue",itemValue);
		return "modules/activityItem/mallPromotionActivityInfoFormModify";
	}

	@RequiresPermissions("activityItem:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "save")
	@SuppressWarnings("unchecked")
	public String save(MallPromotionActivityInfo entity, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		String itemData = request.getParameter("itemData");
		if(StringUtils.isBlank(itemData)){
			return form(entity, model);
		}
		String activityItemType = request.getParameter("activityItemType");
		if(StringUtils.isBlank(activityItemType)){
			return form(entity, model);
		}
		String restrictionType = request.getParameter("restrictionType");
		if(StringUtils.isBlank(activityItemType)){
			return form(entity, model);
		}
		String activityItemValue = request.getParameter("activityItemValues");
		if(StringUtils.isBlank(activityItemValue)){
			return form(entity, model);
		}
		String restrictionValue = request.getParameter("restrictionValue");
		Integer rValue = null;
		if(!StringUtils.isBlank(restrictionValue)){
			rValue = Integer.valueOf(restrictionValue);
		}
		JSONArray json = JSONArray.fromObject(itemData);
		List<MallPromotionActivityRange> list = (List<MallPromotionActivityRange>)JSONArray.toCollection(json, MallPromotionActivityRange.class);
		entity.setRange(list);
		MallPromotionActivityItemInfo itemInfo = new MallPromotionActivityItemInfo();
		itemInfo.setActivityItemType(activityItemType);
		if("limited_price".equals(activityItemType)){
			double mon = Double.valueOf(activityItemValue)*100;
			int money = (int)mon;
			itemInfo.setActivityItemValue(money);
		}else if("lmited_discount".equals(activityItemType)){
			double dis = Double.valueOf(activityItemValue)*10;
			int count = (int)dis;
			itemInfo.setActivityItemValue(count);
		}
		itemInfo.setRestrictionType(restrictionType);
		itemInfo.setRestrictionValue(rValue);
		entity.setItemInfo(itemInfo);
		activityService.save(entity);
		addMessage(redirectAttributes, "保存商品活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityItem/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activityItem:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "update")
	@SuppressWarnings("unchecked")
	public String update(MallPromotionActivityInfo entity, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		activityInfoService.update(entity);
		addMessage(redirectAttributes, "修改商品活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityItem/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activityItem:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionActivityInfo mallPromotionActivityInfo, RedirectAttributes redirectAttributes) {
		activityService.delete(mallPromotionActivityInfo);
		addMessage(redirectAttributes, "删除商品活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityItem/mallPromotionActivityInfo/?repage";
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
		Map<String, Object> map = new HashMap<String, Object>();
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
		if(list.size()>0){
			MallItemInfo itemInfo = list.get(0);
			//判断同一商品不能包含在不同的活动中
			List<MallPromotionActivityInfo> listActivity = activityInfoService.checkItemList(startTime, endTime, itemSku);
			if(listActivity.size()>0){
				MallPromotionActivityInfo info = listActivity.get(0);
				json = "sku为"+itemSku+"的商品已经存在编码为"+info.getActivityCode()+"的活动中";
				map.put("flag", "error");
			}else{
				json = JsonMapper.toJsonString(itemInfo);
				map.put("flag", "success");
			}
		}else{
			json = "该商品不存在或者已下架，请重新确认";
			map.put("flag", "error");
		}
		map.put("json", json);
		return map;
	}
	
	/**
	 * 通过商品sku获取商品
	 */
	@RequestMapping(value = "checkSholeshop")
	@ResponseBody
	@SuppressWarnings("deprecation")
	public Map<String, Object> checkSholeshop(MallPromotionActivityInfo entity, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		//过滤出售的
		Map<String, Object> map = new HashMap<String, Object>();
		Date startDate = new Date(request.getParameter("startTime"));
		Date endDate = new Date(request.getParameter("endTime"));
		
		Timestamp startTime = new Timestamp(startDate.getTime());
		Timestamp endTime = new Timestamp(endDate.getTime());
			//判断同一商品不能包含在不同的活动中
		boolean flag = activityService.checkSholeshop("item",startTime, endTime);
		if(flag){
			map.put("flag", "success");
		}else{
			map.put("flag", "error");
		}
		return map;
	}
}