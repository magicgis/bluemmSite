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
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPayPaymentInfo;
import cn.com.bluemoon.jeesite.modules.pay.service.MallPayPaymentInfoService;

/**
 * 支付记录Controller
 * @author liao
 * @version 2016-03-07
 */
@Controller
@RequestMapping(value = "${adminPath}/pay/mallPayPaymentInfo")
public class MallPayPaymentInfoController extends BaseController {

	@Autowired
	private MallPayPaymentInfoService mallPayPaymentInfoService;
	
	@ModelAttribute
	public MallPayPaymentInfo get(@RequestParam(required=false) String id) {
		MallPayPaymentInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallPayPaymentInfoService.get(id);
		}
		if (entity == null){
			entity = new MallPayPaymentInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("pay:mallPayPaymentInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallPayPaymentInfo mallPayPaymentInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallPayPaymentInfo> page = mallPayPaymentInfoService.findPage(new Page<MallPayPaymentInfo>(request, response, 15), mallPayPaymentInfo); 
		model.addAttribute("page", page);
		return "modules/pay/mallPayPaymentInfoList";
	}

	@RequiresPermissions("pay:mallPayPaymentInfo:view")
	@RequestMapping(value = "form")
	public String form(MallPayPaymentInfo mallPayPaymentInfo, Model model) {
		model.addAttribute("mallPayPaymentInfo", mallPayPaymentInfo);
		return "modules/pay/mallPayPaymentInfoForm";
	}

	@RequiresPermissions("pay:mallPayPaymentInfo:edit")
	@RequestMapping(value = "save")
	public String save(MallPayPaymentInfo mallPayPaymentInfo, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallPayPaymentInfo)){
			return form(mallPayPaymentInfo, model);
		}
		mallPayPaymentInfoService.save(mallPayPaymentInfo);
		addMessage(redirectAttributes, "保存支付记录成功");
		return "redirect:"+Global.getAdminPath()+"/pay/mallPayPaymentInfo/?repage";
	}
	
	@RequiresPermissions("pay:mallPayPaymentInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallPayPaymentInfo mallPayPaymentInfo, RedirectAttributes redirectAttributes) {
		mallPayPaymentInfoService.delete(mallPayPaymentInfo);
		addMessage(redirectAttributes, "删除支付记录成功");
		return "redirect:"+Global.getAdminPath()+"/pay/mallPayPaymentInfo/?repage";
	}

}