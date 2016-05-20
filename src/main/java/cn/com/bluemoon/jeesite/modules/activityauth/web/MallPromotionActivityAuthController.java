/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.web;

import java.math.BigInteger;
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
import cn.com.bluemoon.jeesite.common.utils.HttpUtil;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activity.service.MallPromotionActivityInfoService;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.AngelUser;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuth;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuthAngel;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuthAngelVo;
import cn.com.bluemoon.jeesite.modules.activityauth.service.MallPromotionActivityAuthAngelService;
import cn.com.bluemoon.jeesite.modules.activityauth.service.MallPromotionActivityAuthService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 活动权限表Controller
 * @author linyihao
 * @version 2016-04-19
 */
@Controller
@RequestMapping(value = "${adminPath}/activityauth/mallPromotionActivityAuth")
public class MallPromotionActivityAuthController extends BaseController {

	@Autowired
	private MallPromotionActivityAuthService mallPromotionActivityAuthService;
	
	@Autowired
	private MallPromotionActivityAuthAngelService angelService;
	
	@Autowired
	private MallPromotionActivityInfoService activityService;
	
	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallPromotionActivityAuth get(@RequestParam(required=false) String id) {
		MallPromotionActivityAuth entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPromotionActivityAuthService.get(id);
		}
		if (entity == null){
			entity = new MallPromotionActivityAuth();
		}
		return entity;
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPromotionActivityAuth mallPromotionActivityAuth, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallPromotionActivityAuth> page = mallPromotionActivityAuthService.findPage(new Page<MallPromotionActivityAuth>(request, response), mallPromotionActivityAuth); 
		model.addAttribute("page", page);
		return "modules/activityauth/mallPromotionActivityAuthList";
	}

	@RequiresPermissions("activityauth:mallPromotionActivityAuth:view")
	@RequestMapping(value = "form")
	public String form(MallPromotionActivityAuth mallPromotionActivityAuth, Model model) {
		if(StringUtils.isBlank(mallPromotionActivityAuth.getActivityCode())){
			User user = UserUtils.getUser();
			mallPromotionActivityAuth.setCreateCode(user.getLoginName());
			mallPromotionActivityAuth.setCreateName(user.getName());
			mallPromotionActivityAuth.setCreateDate(new Date());
			model.addAttribute("mallPromotionActivityInfo", mallPromotionActivityAuth);
			return "modules/activityauth/mallPromotionActivityAuthForm";
		}
		model.addAttribute("mallPromotionActivityAuth", mallPromotionActivityAuth);
		return "modules/activityauth/mallPromotionActivityAuthForm";
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:view")
	@RequestMapping(value = "info")
	public String info(MallPromotionActivityAuth mallPromotionActivityAuth, Model model) {
		mallPromotionActivityAuth = mallPromotionActivityAuthService.get(mallPromotionActivityAuth.getAuthId());
		if(mallPromotionActivityAuth != null){
			List<MallPromotionActivityAuthAngel> angelList = angelService.findListByAuthId(mallPromotionActivityAuth.getAuthId(),0);
			mallPromotionActivityAuth.setAngelUserList(angelList);
		}
		model.addAttribute("mallPromotionActivityAuth", mallPromotionActivityAuth);
		
		return "modules/activityauth/mallPromotionActivityAuthFormInfo";
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:view")
	@RequestMapping(value = "modify")
	public String modify(MallPromotionActivityAuth mallPromotionActivityAuth, Model model) {
		mallPromotionActivityAuth = mallPromotionActivityAuthService.get(mallPromotionActivityAuth.getAuthId());
		User user = UserUtils.getUser();
		mallPromotionActivityAuth.setCreateCode(user.getLoginName());
		mallPromotionActivityAuth.setCreateName(user.getName());
		mallPromotionActivityAuth.setCreateDate(new Date());
		if(mallPromotionActivityAuth != null){
			List<MallPromotionActivityAuthAngel> angelList = angelService.findListByAuthId(mallPromotionActivityAuth.getAuthId(),0);
			mallPromotionActivityAuth.setAngelUserList(angelList);
			/*List<MallPromotionActivityAuthAngelVo> newAngelList = new ArrayList<MallPromotionActivityAuthAngelVo>();
			mallPromotionActivityAuth.setAngelUserList(angelList);
			if(angelList != null && angelList.size() > 0){
				for(int i=0;i<angelList.size();i++){
					MallPromotionActivityAuthAngel al = angelList.get(i);
					MallPromotionActivityAuthAngelVo vo = new MallPromotionActivityAuthAngelVo(al.getMapId(),al.getAuthId(),al.getAngelId(),
							al.getAngelName(),al.getAngelMobile());
					newAngelList.add(vo);
				}
				JSONArray angelJson = JSONArray.fromObject(newAngelList);
				mallPromotionActivityAuth.setAngelJson(angelJson);
			}*/

		}
		model.addAttribute("mallPromotionActivityAuth", mallPromotionActivityAuth);
		return "modules/activityauth/mallPromotionActivityAuthFormModify";
	}

	@RequiresPermissions("activityauth:mallPromotionActivityAuth:edit")
	@RequestMapping(value = "save")
	@SuppressWarnings("unchecked")
	public String save(HttpServletRequest request,MallPromotionActivityAuth mallPromotionActivityAuth, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPromotionActivityAuth)){
			return form(mallPromotionActivityAuth, model);
		}
		JSONArray json = null;
		String angelItemData = request.getParameter("angelItemData");
		if(StringUtils.isBlank(angelItemData)){
			return form(mallPromotionActivityAuth, model);
		}
		json = JSONArray.fromObject(angelItemData);
		List<MallPromotionActivityAuthAngel> list = (List<MallPromotionActivityAuthAngel>)JSONArray.toCollection(json,MallPromotionActivityAuthAngel.class);
		mallPromotionActivityAuth.setAngelUserList(list);
		mallPromotionActivityAuthService.save(mallPromotionActivityAuth);
		addMessage(redirectAttributes, "保存推券活动授权成功");
		return "redirect:"+Global.getAdminPath()+"/activityauth/mallPromotionActivityAuth/?repage";
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:edit")
	@RequestMapping(value = "update")
	@SuppressWarnings("unchecked")
	public String update(HttpServletRequest request,MallPromotionActivityAuth mallPromotionActivityAuth, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPromotionActivityAuth)){
			return form(mallPromotionActivityAuth, model);
		}
		JSONArray json = null;
		String angelItemData = request.getParameter("angelItemData");
		if(StringUtils.isBlank(angelItemData)){
			return form(mallPromotionActivityAuth, model);
		}
		json = JSONArray.fromObject(angelItemData);
		List<MallPromotionActivityAuthAngel> list = (List<MallPromotionActivityAuthAngel>)JSONArray.toCollection(json,MallPromotionActivityAuthAngel.class);
		
		List<MallPromotionActivityAuthAngel> oldList = angelService.findListByAuthId(mallPromotionActivityAuth.getAuthId(),null);
		if(oldList != null && oldList.size() > 0){
			for(int i=0;i<oldList.size();i++){
				oldList.get(i).setOpCode(mallPromotionActivityAuth.getCreateCode());
				oldList.get(i).setStatus(2);
			}
		}
		if(list != null && list.size() > 0 ){
			if(oldList != null && oldList.size() > 0){
				for(int i=0;i<list.size();i++){
					boolean isNew = true;
					MallPromotionActivityAuthAngel ma = list.get(i);
					for(int j=0;j<oldList.size();j++){
						MallPromotionActivityAuthAngel oma = oldList.get(j);
						if(ma.getAngelId().equals(oma.getAngelId())){
							//oma.setStatus(0);
							isNew = false;
							continue;
						}
					}
					if(isNew){
						BigInteger ids = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB())+i);
						ma.setMapId(ids+"");
						ma.setAuthId(mallPromotionActivityAuth.getAuthId());
						ma.setStatus(1);
						ma.setOpCode(mallPromotionActivityAuth.getCreateCode());
						ma.setOpTime(new Date());
						oldList.add(ma);
					}
				}
			}else{
				oldList = list;
			}
		}
		angelService.saveOrUpdate(oldList);
		mallPromotionActivityAuth.setUpdateTime(new Date());
		mallPromotionActivityAuthService.updateUser(mallPromotionActivityAuth);
		addMessage(redirectAttributes, "保存推券活动授权成功");
		return "redirect:"+Global.getAdminPath()+"/activityauth/mallPromotionActivityAuth/?repage";
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPromotionActivityAuth mallPromotionActivityAuth, RedirectAttributes redirectAttributes) {
		mallPromotionActivityAuthService.delete(mallPromotionActivityAuth);
		addMessage(redirectAttributes, "删除推券活动授权成功");
		return "redirect:"+Global.getAdminPath()+"/activityauth/mallPromotionActivityAuth/?repage";
	}
	
	@RequiresPermissions("activityauth:mallPromotionActivityAuth:edit")
	@RequestMapping(value = "cancel")
	public String cancel(MallPromotionActivityAuth mallPromotionActivityAuth, RedirectAttributes redirectAttributes) {
		mallPromotionActivityAuthService.cancel(mallPromotionActivityAuth);
		addMessage(redirectAttributes, "取消推券活动授权成功");
		return "redirect:"+Global.getAdminPath()+"/activityauth/mallPromotionActivityAuth/?repage";
	}

	/**
	 * 获取活动
	 */
	@RequestMapping(value = "getActivityItem")
	public String getActivityItem(HttpServletRequest request, HttpServletResponse response,Model model) {
		List<MallPromotionActivityInfo> listActivity = activityService.getCouponListBySendCouponType("man_send");
		model.addAttribute("listActivity", listActivity);
		return "modules/activityauth/activityForm";
	}
	
	/**
	 * 获取活动
	 */
	@RequestMapping(value = "getAngelByCode")
	@ResponseBody
	public Map<String, Object> getAngel(HttpServletRequest request, HttpServletResponse response,Model model) {
		Map<String, Object> map = null;
		String angelCode = request.getParameter("angelCode");
		String angelAddress = mallSysParameterService.getSysParameterByTypeAndCode("HTTP", "HTTP_ANGEL_ADDRESS");
		Map<String,String> jsonMap = new HashMap<String,String>();
		jsonMap.put("userCode", angelCode);
		String value = HttpUtil.doJsonPost(angelAddress+"user/getUserInfoByCode?client=ios&cuid=78771ECF-8D45-4E16-82AE-DDF0A9C4280F&format=json&sign=455e2b1c29cc7210df943acaa454630e&time=1459999388&version=1.0.0",
				JSONObject.fromObject(jsonMap).toString());
		JSONObject ob = JSONObject.fromObject(value);
		if(ob.getBoolean("isSuccess") && 0 == ob.getInt("responseCode")){
			map = new HashMap<String, Object>();
			AngelUser as = (AngelUser)JSONObject.toBean(ob.getJSONObject("user"), AngelUser.class);
			String json = JsonMapper.toJsonString(as);
			map.put("json", json);
			map.put("flag", "success");
		}
		return map;
	}
	
	/**
	 * 获取更多天使账号信息
	 * @return
	 */
	@RequestMapping(value = "getMoreData")
	@ResponseBody
	public Map<String,Object> getMoreData(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		int pageNo = 1;
		String pageNoStr = request.getParameter("pageNo");
		String authId = request.getParameter("authId");
		if(!StringUtils.isEmpty(pageNoStr)){
			pageNo = Integer.valueOf(pageNoStr);
		}else{
			pageNo = 2;
		}
		if(authId == null || "".equals(authId)){
			map.put("flag", "error");
			return map;
		}
		int begin = (pageNo-1)*20;
		List<MallPromotionActivityAuthAngel> list = angelService.findListByAuthId(authId,begin);
		if(list != null && list.size()>0){
			List<MallPromotionActivityAuthAngelVo> newAngelList = new ArrayList<MallPromotionActivityAuthAngelVo>();
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i).getAuthId()+"-"+list.get(i).getAngelName());
				MallPromotionActivityAuthAngel al = list.get(i);
				MallPromotionActivityAuthAngelVo vo = new MallPromotionActivityAuthAngelVo(al.getMapId(),al.getAuthId(),al.getAngelId(),
						al.getAngelName(),al.getAngelMobile(),al.getOpTime());
				newAngelList.add(vo);
			}
			JSONArray json = JSONArray.fromObject(newAngelList);
			map.put("flag", "success");
			map.put("pageNo", pageNo+1);
			map.put("json", json);
		}
	
		return map;
	}
	
	/**
	 * 保存天使账号信息
	 * @return
	 */
	@RequestMapping(value = "saveAngel")
	@ResponseBody
	public Map<String,Object> saveAngel(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String angelId = request.getParameter("angelId");
		String angelName = request.getParameter("angelName");
		String angelMobile = request.getParameter("angelMobile");
		String authId = request.getParameter("authId");
		if(angelId == null || "".equals(angelId)){
			map.put("flag", "false");
			map.put("msg", "月亮天使账号错误");
			return map;
		}
		if(angelName == null || "".equals(angelName)){
			map.put("flag", "false");
			map.put("msg", "天使姓名错误");
			return map;
		}
		if(angelMobile == null || "".equals(angelMobile)){
			map.put("flag", "false");
			map.put("msg", "天使联系错误");
			return map;
		}
		if(authId == null || "".equals(authId)){
			map.put("flag", "false");
			map.put("msg", "活动授权错误");
			return map;
		}
		MallPromotionActivityAuthAngel maa = angelService.getAngelByAuthIdAndAngelId(authId, angelId);
		if(maa != null){
			map.put("flag", "false");
			map.put("msg", "该账户已授权，请勿重复添加");
			return map;
		}
		MallPromotionActivityAuthAngel mallPromotionActivityAuthAngel = new MallPromotionActivityAuthAngel();
		mallPromotionActivityAuthAngel.setAuthId(authId);
		mallPromotionActivityAuthAngel.setAngelMobile(angelMobile);
		mallPromotionActivityAuthAngel.setAngelId(angelId);
		mallPromotionActivityAuthAngel.setAngelName(angelName);
		mallPromotionActivityAuthService.saveAngel(mallPromotionActivityAuthAngel);
		map.put("flag", "success");
		map.put("msg", "天使账号保存成功");
		return map;
	}
	
	/**
	 * 保存天使账号信息
	 * @return
	 */
	@RequestMapping(value = "deleteAngel")
	@ResponseBody
	public Map<String,Object> deleteAngel(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String angelId = request.getParameter("angelId");
		String authId = request.getParameter("authId");
		if(angelId == null || "".equals(angelId)){
			map.put("flag", "false");
			map.put("msg", "月亮天使账号错误");
			return map;
		}
		if(authId == null || "".equals(authId)){
			map.put("flag", "false");
			map.put("msg", "活动授权错误");
			return map;
		}
		int result = angelService.deleteAngel(authId,angelId);
		if(result > 0){
			map.put("flag", "success");
			map.put("msg", "天使账号删除成功");
		}else{
			map.put("flag", "false");
			map.put("msg", "天使账号删失败");
		}
		return map;
	}
}