package cn.com.bluemoon.jeesite.modules.activity.web;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sun.java2d.pipe.SpanShapeRenderer.Simple;
import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.mapper.JsonMapper;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjsLevel;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityMjsLevelVo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRange;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityRangeVo;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoMJSService;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 满就送活动管理Controller
 * @author linyihao
 * @version 2016-03-29
 */
@Controller
@RequestMapping(value = "${adminPath}/activityMJS/mallPromotionActivityInfo")
public class MallPromotionActivityInfoMJSController extends BaseController {

	@Autowired
	private MallPromotionActivityInfoMJSService activityService;
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
	
	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionActivityInfo mallPromotionActivityInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		mallPromotionActivityInfo.setActivityType("mjs");
		Page<MallPromotionActivityInfo> page = activityService.findPage(new Page<MallPromotionActivityInfo>(request, response), mallPromotionActivityInfo); 
		for (MallPromotionActivityInfo temp : page.getList()) {
			if(temp.getStatus()==1&&temp.getEndTime()!=null
					&&temp.getEndTime().before(new Date())){
				temp.setStatus(2);
			}
		}
		model.addAttribute("page", page);
		return "modules/activityMJS/mallPromotionActivityInfoList";
	}

	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionActivityInfo entity, Model model) {
		if(StringUtils.isBlank(entity.getActivityCode())){
			User user = UserUtils.getUser();
			entity.setActivityCode("系统自动生成");
			entity.setCreateCode(user.getLoginName());
			entity.setCreateName(user.getName());
			entity.setRangeType("part");
			entity.setCreateDate(new Date());
		}else{
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
			//优惠级别
			List<MallPromotionActivityMjsLevel> listLevel = activityService.getLevelByActivityId(
					new MallPromotionActivityMjsLevel(entity.getActivityId()));
			List<MallPromotionActivityMjsLevelVo> listLevelVo = new ArrayList<MallPromotionActivityMjsLevelVo>();
			if(listLevel!=null&&listLevel.size()>0){
				for (MallPromotionActivityMjsLevel po : listLevel) {
					MallPromotionActivityMjsLevelVo vo = new MallPromotionActivityMjsLevelVo(po);
					if("amount_over".equals(entity.getCondition())){
						double f = (double)po.getConditionValue()/100;  
						BigDecimal b = new BigDecimal(f);  
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						vo.setConditionValueDouble(f1);
					}else{
						vo.setConditionValueDouble(po.getConditionValue());
					}
					if("less_money".equals(po.getPreferentialType())){
						double f = (double)po.getPreferentialValue()/100;  
						BigDecimal b = new BigDecimal(f);  
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						vo.setPreferentialValueDouble(f1);
					}else if("discount".equals(po.getPreferentialType())){
						vo.setPreferentialValueDouble((int)po.getPreferentialValue());
					}
					listLevelVo.add(vo);
				}
			}
			JSONArray jsonLevel = JSONArray.fromObject(listLevelVo);
			entity.setLevelJson(jsonLevel);
		}
		model.addAttribute("mallPromotionActivityInfo", entity);
		return "modules/activityMJS/mallPromotionActivityInfoForm";
	}
	
	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "modify")
	public String modify(MallPromotionActivityInfo entity, Model model) {
			User user = UserUtils.getUser();
			entity.setCreateCode(user.getLoginName());
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
			//优惠级别
			List<MallPromotionActivityMjsLevel> listLevel = activityService.getLevelByActivityId(
					new MallPromotionActivityMjsLevel(entity.getActivityId()));
			List<MallPromotionActivityMjsLevelVo> listLevelVo = new ArrayList<MallPromotionActivityMjsLevelVo>();
			if(listLevel!=null&&listLevel.size()>0){
				for (MallPromotionActivityMjsLevel po : listLevel) {
					MallPromotionActivityMjsLevelVo vo = new MallPromotionActivityMjsLevelVo(po);
					if("amount_over".equals(entity.getCondition())){
						double f = (double)po.getConditionValue()/100;  
						BigDecimal b = new BigDecimal(f);  
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						vo.setConditionValueDouble(f1);
					}else{
						vo.setConditionValueDouble(po.getConditionValue());
					}
					if("less_money".equals(po.getPreferentialType())){
						double f = (double)po.getPreferentialValue()/100;  
						BigDecimal b = new BigDecimal(f);  
						double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
						vo.setPreferentialValueDouble(f1);
					}else if("discount".equals(po.getPreferentialType())){
						vo.setPreferentialValueDouble((int)po.getPreferentialValue());
					}
					listLevelVo.add(vo);
				}
			}
			JSONArray jsonLevel = JSONArray.fromObject(listLevelVo);
			entity.setLevelJson(jsonLevel);
		model.addAttribute("mallPromotionActivityInfo", entity);
		return "modules/activityMJS/mallPromotionActivityInfoFormModify";
	}

	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:edit")
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
		String levelData = request.getParameter("levelData");
		if(StringUtils.isBlank(levelData)){
			return form(entity, model);
		}
		JSONArray json = JSONArray.fromObject(itemData);
		List<MallPromotionActivityRange> list = (List<MallPromotionActivityRange>)JSONArray.toCollection(json, MallPromotionActivityRange.class);
		entity.setRange(list);
		
		json = JSONArray.fromObject(levelData);
		List<MallPromotionActivityMjsLevel> listLevel = (List<MallPromotionActivityMjsLevel>)JSONArray.toCollection(json, MallPromotionActivityMjsLevel.class);
		entity.setLevel(listLevel);
		
		activityService.save(entity);
		addMessage(redirectAttributes, "保存优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityMJS/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "update")
	public String update(MallPromotionActivityInfo entity, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		activityService.save(entity);
		addMessage(redirectAttributes, "修改优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityMJS/mallPromotionActivityInfo/?repage";
	}
	
	@RequiresPermissions("activityMJS:mallPromotionActivityInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionActivityInfo mallPromotionActivityInfo, RedirectAttributes redirectAttributes) {
		activityService.delete(mallPromotionActivityInfo);
		addMessage(redirectAttributes, "删除优惠券活动成功");
		return "redirect:"+Global.getAdminPath()+"/activityMJS/mallPromotionActivityInfo/?repage";
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
		return "modules/activityMJS/levelForm";
	}
	
	/**
	 * 添加级别优惠
	 */
	@RequestMapping(value = "getLevel")
	@ResponseBody
	public String getLevel(MallPromotionActivityMjsLevel level,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
//		String condition = request.getParameter("condition");
//		level = new MallPromotionActivityMjsLevel();
//		model.addAttribute("mallPromotionActivityMjsLevel", level);
		String json = JsonMapper.toJsonString(level);
//		model.addAttribute("condition", condition);
		return json;
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
			List<MallPromotionActivityInfo> listActivity = activityService.checkList(startTime, endTime, itemSku);
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
		boolean flag = activityService.checkSholeshop(startTime, endTime);
		if(flag){
			map.put("flag", "success");
		}else{
			map.put("flag", "error");
		}
		return map;
	}
}