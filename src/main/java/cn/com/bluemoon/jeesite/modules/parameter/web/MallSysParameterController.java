/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.parameter.web;

import java.util.List;

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
import cn.com.bluemoon.jeesite.common.utils.JedisUtils;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.parameter.entity.MallSysParameter;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.sys.entity.Dict;

/**
 * 系统参数Controller
 * @author mij
 * @version 2015-12-28
 */
@Controller
@RequestMapping(value = "${adminPath}/parameter/mallSysParameter")
public class MallSysParameterController extends BaseController {

	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallSysParameter get(@RequestParam(required=false) String id) {
		MallSysParameter entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallSysParameterService.get(id);
		}
		if (entity == null){
			entity = new MallSysParameter();
		}
		return entity;
	}
	
	@RequiresPermissions("parameter:mallSysParameter:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallSysParameter mallSysParameter, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallSysParameter> page = mallSysParameterService.findPage(new Page<MallSysParameter>(request, response), mallSysParameter); 
		model.addAttribute("page", page);
//		System.out.println(JedisUtils.keys());
//		String paraType="FTP", paraCode ="FTP_PASSWORD";
//		String ss=mallSysParameterService.getSysParameterByTypeAndCode(paraType,paraCode);
		return "modules/parameter/mallSysParameterList";
	}

	@RequiresPermissions("parameter:mallSysParameter:view")
	@RequestMapping(value = "form")
	public String form(MallSysParameter mallSysParameter, Model model) {
		model.addAttribute("mallSysParameter", mallSysParameter);
		return "modules/parameter/mallSysParameterForm";
	}

	@RequiresPermissions("parameter:mallSysParameter:edit")
	@RequestMapping(value = "save")
	public String save(MallSysParameter mallSysParameter, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallSysParameter)){
			return form(mallSysParameter, model);
		}
		mallSysParameterService.save(mallSysParameter);
		addMessage(redirectAttributes, "保存系统参数成功");
		JedisUtils.del(mallSysParameter.getParaType()+mallSysParameter.getParaCode());//删除参数KEY
		return "redirect:"+Global.getAdminPath()+"/parameter/mallSysParameter/?repage";
	}
	
	@RequiresPermissions("parameter:mallSysParameter:edit")
	@RequestMapping(value = "delete")
	public String delete(MallSysParameter mallSysParameter, RedirectAttributes redirectAttributes) {
		mallSysParameterService.delete(mallSysParameter);
		addMessage(redirectAttributes, "删除系统参数成功");
		JedisUtils.del(mallSysParameter.getParaType()+mallSysParameter.getParaCode());//删除参数KEY
		return "redirect:"+Global.getAdminPath()+"/parameter/mallSysParameter/?repage";
	}

}