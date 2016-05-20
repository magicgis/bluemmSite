/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.coupon.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.mapper.JsonMapper;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.StringUtil;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponInfo;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponRange;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponRangeVo;
import cn.com.bluemoon.jeesite.modules.coupon.entity.MallPromotionCouponRule;
import cn.com.bluemoon.jeesite.modules.coupon.service.MallPromotionCouponInfoService;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;


/**
 * 券主数据管理Controller
 * @author linyihao
 * @version 2016-03-22
 */
@Controller
@RequestMapping(value = "${adminPath}/coupon/mallPromotionCouponInfo")
public class MallPromotionCouponInfoController extends BaseController {

	@Autowired
	private MallPromotionCouponInfoService mallPromotionCouponInfoService;
	@Autowired
	private ItemService itemService;
	
	@ModelAttribute
	public MallPromotionCouponInfo get(@RequestParam(required=false) String id) {
		MallPromotionCouponInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPromotionCouponInfoService.get(id);
		}
		if (entity == null){
			entity = new MallPromotionCouponInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("coupon:mallPromotionCouponInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionCouponInfo mallPromotionCouponInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(mallPromotionCouponInfo.getStatus()==0){
			mallPromotionCouponInfo.setStatus(3);
		}
		Page<MallPromotionCouponInfo> page = mallPromotionCouponInfoService.findPage(new Page<MallPromotionCouponInfo>(request, response), mallPromotionCouponInfo); 
		for (MallPromotionCouponInfo coupon : page.getList()) {
			if(coupon.getStatus()==1&&coupon.getEndTime()!=null&&coupon.getEndTime().before(new Date())){
				coupon.setStatus(2);
			}
		}
		mallPromotionCouponInfo.setCouponType(mallPromotionCouponInfo.getCouponType()+"_"+mallPromotionCouponInfo.getGiftType());
		if(mallPromotionCouponInfo.getStatus()==3){
			mallPromotionCouponInfo.setStatus(0);
		}
		model.addAttribute("page", page);
		return "modules/coupon/mallPromotionCouponInfoList";
	}

	@RequiresPermissions("coupon:mallPromotionCouponInfo:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionCouponInfo entity, Model model) {
		if(StringUtils.isBlank(entity.getCouponCode())){
			User user = UserUtils.getUser();
			entity.setCouponCode("系统自动生成");
			entity.setOpCode(user.getLoginName());
			entity.setOp(user.getName());
			entity.setOpTime(new Date());
			entity.setCouponType("online_discount");//默认券类型
			entity.setTimeType("fixedTime");//默认时间类型
			MallPromotionCouponRule rule = new MallPromotionCouponRule();
			rule.setRangeType("part");
			entity.setRule(rule); 
		}else{
			if("offline_gift".equals(entity.getCouponType())){
				List<MallPromotionCouponRange> listRange = mallPromotionCouponInfoService.getRange(new MallPromotionCouponRange(entity.getCouponId()));
				if(listRange!=null&&listRange.size()>0){
					List<MallPromotionCouponRangeVo> listVo = new ArrayList<MallPromotionCouponRangeVo>();
					for (MallPromotionCouponRange po : listRange) {
						MallPromotionCouponRangeVo vo = new MallPromotionCouponRangeVo(po);
						listVo.add(vo);
					}
					JSONArray json = JSONArray.fromObject(listVo);
					entity.setListRangeJson(json);
				}
			}else{
				MallPromotionCouponRule rule = mallPromotionCouponInfoService.getRule(new MallPromotionCouponRule(entity.getCouponId()));
				if("amount_over".equals(rule.getConditionType())){
					double f = (double)rule.getConditionValue()/100;  
					BigDecimal b = new BigDecimal(f);  
					double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
					rule.setConditionValueDouble(f1);
					f = (double)rule.getDenomination()/100;  
					b = new BigDecimal(f);  
					f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
					rule.setDenominationDouble(f1);
				}
				entity.setRule(rule);
				if("part".equals(rule.getRangeType())){
					List<MallPromotionCouponRange> listRange = mallPromotionCouponInfoService.getRange(new MallPromotionCouponRange(entity.getCouponId()));
					if(listRange!=null&&listRange.size()>0){
						List<MallPromotionCouponRangeVo> listVo = new ArrayList<MallPromotionCouponRangeVo>();
						for (MallPromotionCouponRange po : listRange) {
							MallPromotionCouponRangeVo vo = new MallPromotionCouponRangeVo(po);
							listVo.add(vo);
						}
						JSONArray json = JSONArray.fromObject(listVo);
						entity.setListRangeJson(json);
					}
				}
			}
		}
		model.addAttribute("mallPromotionCouponInfo", entity);
		return "modules/coupon/mallPromotionCouponInfoForm";
	}

	@RequiresPermissions("coupon:mallPromotionCouponInfo:edit")
	@RequestMapping(value = "save")
	public String save(MallPromotionCouponInfo entity, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request ) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		String itemData = request.getParameter("itemData");
		if(StringUtils.isBlank(itemData)){
			return form(entity, model);
		}
		JSONArray json = JSONArray.fromObject(itemData);
		@SuppressWarnings("unchecked")
		List<MallPromotionCouponRange> list = (List<MallPromotionCouponRange>)JSONArray.toCollection(json, MallPromotionCouponRange.class);
		entity.setListRange(list);
		mallPromotionCouponInfoService.save(entity);
		addMessage(redirectAttributes, "保存活动券成功");
		return "redirect:"+Global.getAdminPath()+"/coupon/mallPromotionCouponInfo/?repage";
	}
	
	@RequiresPermissions("coupon:mallPromotionCouponInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionCouponInfo mallPromotionCouponInfo, RedirectAttributes redirectAttributes) {
		mallPromotionCouponInfo = mallPromotionCouponInfoService.get(mallPromotionCouponInfo);
		if(!StringUtil.isEmptyOrSpace(mallPromotionCouponInfo.getCouponCode())){
			boolean flag = mallPromotionCouponInfoService.cancelCoupon(mallPromotionCouponInfo);
			if(flag){
				addMessage(redirectAttributes, "取消活动券成功");
			}else{
				addMessage(redirectAttributes, "活动券已经存在活动中,不可以取消!");
			}
		}else{
			addMessage(redirectAttributes, "该活动券已取消过了");
		}
		return "redirect:"+Global.getAdminPath()+"/coupon/mallPromotionCouponInfo/?repage";
	}
	
	/**
	 * 通过商品SKU获取商品
	 */
	@RequestMapping(value = "getItemBySku")
	@ResponseBody
	public String getItemList(HttpServletRequest request, HttpServletResponse response) {
		//过滤出售的
		String itemSku = request.getParameter("itemSku");
		List<MallItemInfo> list = itemService.findItemBySku(itemSku.trim());
		String json = "";
		if(list.size()>0){
			MallItemInfo itemInfo = list.get(0);
			json = JsonMapper.toJsonString(itemInfo);
		}
		return json;
	}
	
	/**
	 * 通过产品SKU获取产品
	 */
	@RequestMapping(value = "getProductBySku")
	@ResponseBody
	public String getProductBySku(HttpServletRequest request, HttpServletResponse response) {
		//过滤出售的
		String productSku = request.getParameter("itemSku");
		List<MallItemDetail> list = itemService.findGoodBySku(productSku);
		String json = "";
		if(list.size()>0){
			MallItemDetail itemInfo = list.get(0);
			json = JsonMapper.toJsonString(itemInfo);
		}
		return json;
	}
	
	/**
	 * 获取券通过券编码
	 */
	@RequestMapping(value = "getCoupon")
	@ResponseBody
	public String getCoupon(HttpServletRequest request, HttpServletResponse response) {
		//过滤出售的
		String couponCode = request.getParameter("couponCode");
		MallPromotionCouponInfo mpci = mallPromotionCouponInfoService.getCouponByCouponCode(couponCode);
		String json = "";
		if(mpci != null){
			json = JsonMapper.toJsonString(mpci);
		}
		return json;
	}
}