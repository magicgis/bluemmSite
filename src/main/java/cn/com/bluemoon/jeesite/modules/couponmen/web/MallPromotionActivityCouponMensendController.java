/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.couponmen.web;

import java.math.BigInteger;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.mapper.JsonMapper;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.DateUtils;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.utils.excel.ExportExcel;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponMap;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityCouponMapVo;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoService;
import cn.com.bluemoon.jeesite.modules.couponmen.entity.MallPromotionActivityCouponMensend;
import cn.com.bluemoon.jeesite.modules.couponmen.entity.MallPromotionActivityCouponMensendDetail;
import cn.com.bluemoon.jeesite.modules.couponmen.service.MallPromotionActivityCouponMensendService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 人工派券Controller
 * @author linyihao
 * @version 2016-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/couponmen/mallPromotionActivityCouponMensend")
public class MallPromotionActivityCouponMensendController extends BaseController {

	@Autowired
	private MallPromotionActivityCouponMensendService mallPromotionActivityCouponMensendService;
	@Autowired
	private MallPromotionActivityInfoService activityInfoService;

	
	@ModelAttribute
	public MallPromotionActivityCouponMensend get(@RequestParam(required=false) String id) {
		MallPromotionActivityCouponMensend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPromotionActivityCouponMensendService.get(id);
		}
		if (entity == null){
			entity = new MallPromotionActivityCouponMensend();
		}
		return entity;
	}
	
	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallPromotionActivityCouponMensend> page = mallPromotionActivityCouponMensendService.findPage(new Page<MallPromotionActivityCouponMensend>(request, response), mallPromotionActivityCouponMensend); 
//		//TODO 
//		List<MallPromotionActivityCouponMensend> list = page.getList();
//		for (MallPromotionActivityCouponMensend temp : list) {
//			MallPromotionActivityCouponMensendDetail detail = new MallPromotionActivityCouponMensendDetail();
//			detail.setMensendId(temp.getMensendId());
//			
//		}
		model.addAttribute("page", page);
		return "modules/couponmen/mallPromotionActivityCouponMensendList";
	}

	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionActivityCouponMensend entity, Model model) {
		if(StringUtils.isBlank(entity.getActivityCode())){
			User user = UserUtils.getUser();
			entity.setCreatCode(user.getLoginName());
			entity.setCreatBy(user.getName());
			entity.setCreatTime(new Date());
		}else{
			
		}
		model.addAttribute("mallPromotionActivityCouponMensend", entity);
		return "modules/couponmen/mallPromotionActivityCouponMensendForm";
	}

	@SuppressWarnings("unchecked")
	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:edit")
	@RequestMapping(value = "save")
	public String save(MallPromotionActivityCouponMensend entity, Model model, 
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, entity)){
			return form(entity, model);
		}
		String couponData = request.getParameter("couponData");
		if(StringUtils.isBlank(couponData)){
			return form(entity, model);
		}
		String consumerData = request.getParameter("consumerData");
		if(StringUtils.isBlank(consumerData)){
			return form(entity, model);
		}
		JSONArray json = JSONArray.fromObject(couponData);
		List<MallPromotionActivityCouponMensendDetail> listDetail = 
				(List<MallPromotionActivityCouponMensendDetail>)JSONArray.toCollection(json, MallPromotionActivityCouponMensendDetail.class);
		entity.setListDetail(listDetail);
		entity.setUserMobile(consumerData);
		mallPromotionActivityCouponMensendService.save(entity);
		addMessage(redirectAttributes, "保存手动推券成功");
		return "redirect:"+Global.getAdminPath()+"/couponmen/mallPromotionActivityCouponMensend/?repage";
	}
	
	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend, RedirectAttributes redirectAttributes) {
		mallPromotionActivityCouponMensendService.delete(mallPromotionActivityCouponMensend);
		addMessage(redirectAttributes, "删除手动推券成功");
		return "redirect:"+Global.getAdminPath()+"/couponmen/mallPromotionActivityCouponMensend/?repage";
	}
	
	/**
	 * 获取自助领取优惠券活动列表
	 * @param mallPromotionActivityCouponMensend
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:edit")
	@RequestMapping(value = "getSelfActivity")
	public String getSelfActivity(MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend, HttpServletRequest request, HttpServletResponse response, Model model){
		List<MallPromotionActivityInfo> list =  activityInfoService.getSelfActivityList();
		model.addAttribute("activityList", list);
		return "modules/couponmen/activityList";
	}
	
	/**
	 * 通过活动ID获取可发送券
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "getCouponList")
	@ResponseBody
	public Map<String, Object> getCouponList(@RequestParam(required=false) String activityId){
		MallPromotionActivityInfo activityInfo =  activityInfoService.getCouponActivity(activityId);
		String activityInfoJson = JsonMapper.toJsonString(activityInfo);
		Map<String, Object> map = new HashMap<String, Object>();
		MallPromotionActivityCouponMap couponMap = new MallPromotionActivityCouponMap();
		couponMap.setActivityId(new BigInteger(activityId));
		List<MallPromotionActivityCouponMap> listMap = activityInfoService.getCouponByActivityId(couponMap);
		List<MallPromotionActivityCouponMapVo> listVo = new ArrayList<MallPromotionActivityCouponMapVo>();
		for (MallPromotionActivityCouponMap po : listMap) {
			MallPromotionActivityCouponMapVo vo = new MallPromotionActivityCouponMapVo(po);
			listVo.add(vo);
		}
		JSONArray json = null;
		json = JSONArray.fromObject(listVo);
		map.put("json", json);
		map.put("activity", activityInfoJson);
		return map;
	}
	
	/**
	 * 导出推券信息
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("couponmen:mallPromotionActivityCouponMensend:edit")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MallPromotionActivityCouponMensend entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "推券信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MallPromotionActivityCouponMensend> page = mallPromotionActivityCouponMensendService.findExcelPage(
            		new Page<MallPromotionActivityCouponMensend>(request, response), entity); 
            List<MallPromotionActivityCouponMensend> list = page.getList();
            for (MallPromotionActivityCouponMensend temp : list) {
            	String isSend = temp.getIsSend();
            	if("0".equals(isSend)){
            		temp.setIsSend("失败");
            	}else if("1".equals(isSend)){
            		temp.setIsSend("成功");
            	}else if("2".equals(isSend)){
            		temp.setIsSend("等待");
            	}
			}
            String agent = request.getHeader("USER-AGENT").toLowerCase();
//

    		new ExportExcel("推券信息", MallPromotionActivityCouponMensend.class).setDataList(page.getList()).write(agent,response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出数据失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/couponmen/mallPromotionActivityCouponMensend/?repage";
    }
}