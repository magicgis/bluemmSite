/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.web;

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
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPaySearchRecord;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPaySearchRecordService;

/**
 * 第三方同步记录Controller
 * @author liao
 * @version 2016-03-07
 */
@Controller
@RequestMapping(value = "${adminPath}/pay/mallPaySearchRecord")
public class MallPaySearchRecordController extends BaseController {

	@Autowired
	private MallPaySearchRecordService mallPaySearchRecordService;
	
	@ModelAttribute
	public MallPaySearchRecord get(@RequestParam(required=false) String id) {
		MallPaySearchRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPaySearchRecordService.get(id);
		}
		if (entity == null){
			entity = new MallPaySearchRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("pay:mallPaySearchRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPaySearchRecord mallPaySearchRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallPaySearchRecord> page = mallPaySearchRecordService.findPage(new Page<MallPaySearchRecord>(request, response), mallPaySearchRecord); 
		model.addAttribute("page", page);
		return "modules/pay/mallPaySearchRecordList";
	}

	@RequiresPermissions("pay:mallPaySearchRecord:view")
	@RequestMapping(value = "form")
	public String form(MallPaySearchRecord mallPaySearchRecord, Model model) {
		model.addAttribute("mallPaySearchRecord", mallPaySearchRecord);
		return "modules/pay/mallPaySearchRecordForm";
	}

	@RequiresPermissions("pay:mallPaySearchRecord:edit")
	@RequestMapping(value = "save")
	public String save(MallPaySearchRecord mallPaySearchRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPaySearchRecord)){
			return form(mallPaySearchRecord, model);
		}
		mallPaySearchRecordService.save(mallPaySearchRecord);
		addMessage(redirectAttributes, "保存第三方同步记录成功");
		return "redirect:"+Global.getAdminPath()+"/pay/mallPaySearchRecord/?repage";
	}
	
	@RequiresPermissions("pay:mallPaySearchRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPaySearchRecord mallPaySearchRecord, RedirectAttributes redirectAttributes) {
		mallPaySearchRecordService.delete(mallPaySearchRecord);
		addMessage(redirectAttributes, "删除第三方同步记录成功");
		return "redirect:"+Global.getAdminPath()+"/pay/mallPaySearchRecord/?repage";
	}

}