/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.appversion.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.appversion.entity.MallAppVersionUpdate;
import cn.com.bluemoon.jeesite.modules.appversion.service.MallAppVersionUpdateService;

/**
 * APP版本信息管理Controller
 * @author linyihao
 * @version 2016-01-20
 */
@Controller
@RequestMapping(value = "${adminPath}/appversion/mallAppVersionUpdate")
public class MallAppVersionUpdateController extends BaseController {

	@Autowired
	private MallAppVersionUpdateService mallAppVersionUpdateService;
	
	@ModelAttribute
	public MallAppVersionUpdate get(@RequestParam(required=false) String id) {
		MallAppVersionUpdate entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallAppVersionUpdateService.get(id);
		}
		if (entity == null){
			entity = new MallAppVersionUpdate();
		}
		return entity;
	}
	
	@RequiresPermissions("appversion:mallAppVersionUpdate:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallAppVersionUpdate mallAppVersionUpdate, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallAppVersionUpdate> page = mallAppVersionUpdateService.findPage(new Page<MallAppVersionUpdate>(request, response), mallAppVersionUpdate); 
		model.addAttribute("page", page);
		return "modules/appversion/mallAppVersionUpdateList";
	}

	@RequiresPermissions("appversion:mallAppVersionUpdate:view")
	@RequestMapping(value = "form")
	public String form(MallAppVersionUpdate mallAppVersionUpdate, Model model) {
		model.addAttribute("mallAppVersionUpdate", mallAppVersionUpdate);
		return "modules/appversion/mallAppVersionUpdateForm";
	}

	@RequiresPermissions("appversion:mallAppVersionUpdate:edit")
	@RequestMapping(value = "save")
	public String save(MallAppVersionUpdate mallAppVersionUpdate, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallAppVersionUpdate)){
			return form(mallAppVersionUpdate, model);
		}
		mallAppVersionUpdateService.save(mallAppVersionUpdate);
		addMessage(redirectAttributes, "保存版本信息成功");
		return "redirect:"+Global.getAdminPath()+"/appversion/mallAppVersionUpdate/?repage";
	}
	
	@RequiresPermissions("appversion:mallAppVersionUpdate:edit")
	@RequestMapping(value = "delete")
	public String delete(MallAppVersionUpdate mallAppVersionUpdate, RedirectAttributes redirectAttributes) {
		mallAppVersionUpdateService.delete(mallAppVersionUpdate);
		addMessage(redirectAttributes, "删除版本信息成功");
		return "redirect:"+Global.getAdminPath()+"/appversion/mallAppVersionUpdate/?repage";
	}

}