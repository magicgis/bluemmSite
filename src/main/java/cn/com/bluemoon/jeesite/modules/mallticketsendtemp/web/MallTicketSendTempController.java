/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallticketsendtemp.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

import cn.com.bluemoon.jeesite.common.beanvalidator.BeanValidators;
import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.DateUtils;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.utils.excel.ExportExcel;
import cn.com.bluemoon.jeesite.common.utils.excel.ImportExcel;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.mallticketsendtemp.entity.MallTicketSendTemp;
import cn.com.bluemoon.jeesite.modules.mallticketsendtemp.service.MallTicketSendTempService;

/**
 * 派发门票Controller
 * @author lingyihao
 * @version 2016-02-26
 */
@Controller
@RequestMapping(value = "${adminPath}/mallticketsendtemp/mallTicketSendTemp")
public class MallTicketSendTempController extends BaseController {

	@Autowired
	private MallTicketSendTempService mallTicketSendTempService;
	
	@ModelAttribute
	public MallTicketSendTemp get(@RequestParam(required=false) String id) {
		MallTicketSendTemp entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallTicketSendTempService.get(id);
		}
		if (entity == null){
			entity = new MallTicketSendTemp();
		}
		return entity;
	}
	
	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallTicketSendTemp mallTicketSendTemp, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallTicketSendTemp> page = mallTicketSendTempService.findPage(new Page<MallTicketSendTemp>(request, response), mallTicketSendTemp); 
		model.addAttribute("page", page);
		return "modules/mallticketsendtemp/mallTicketSendTempList";
	}

	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:view")
	@RequestMapping(value = "form")
	public String form(MallTicketSendTemp mallTicketSendTemp, Model model) {
		model.addAttribute("mallTicketSendTemp", mallTicketSendTemp);
		return "modules/mallticketsendtemp/mallTicketSendTempForm";
	}

	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:edit")
	@RequestMapping(value = "save")
	public String save(MallTicketSendTemp mallTicketSendTemp, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallTicketSendTemp)){
			return form(mallTicketSendTemp, model);
		}
		if(!isMobile(mallTicketSendTemp.getMobile())){
			addMessage(model, "手机格式不正确");
			return form(mallTicketSendTemp, model);
		}
		//TODO
		mallTicketSendTemp.setIsSend("0");
		mallTicketSendTemp.setType("men");
		mallTicketSendTempService.save(mallTicketSendTemp);
		addMessage(redirectAttributes, "保存派发门票成功");
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
	}
	
	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:edit")
	@RequestMapping(value = "delete")
	public String delete(MallTicketSendTemp mallTicketSendTemp, RedirectAttributes redirectAttributes) {
		mallTicketSendTempService.delete(mallTicketSendTemp);
		addMessage(redirectAttributes, "删除派发门票成功");
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
	}
	
	public boolean isMobile(String str) {   
		Pattern p = null;  
		Matcher m = null;  
        boolean b = false;   
//        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号 
        p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,6,7,8])|(18[0-9]))\\d{8}$");
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  

	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 0, 0);
			List<MallTicketSendTemp> list = ei.getDataList(MallTicketSendTemp.class);
			for (MallTicketSendTemp mallTicketSendTemp : list){
				try{
					if (isMobile(mallTicketSendTemp.getMobile())){
						mallTicketSendTemp.setIsSend("0");
						mallTicketSendTemp.setType("men");
						mallTicketSendTempService.save(mallTicketSendTemp);
						successNum++;
					}else{
						failureMsg.append("<br/>手机号 "+mallTicketSendTemp.getMobile()+" 格式不正确; 请设置单元格格式为文本！");
						failureNum++;
					}
				}catch(ConstraintViolationException ex){
					failureMsg.append("<br/>手机号 "+mallTicketSendTemp.getMobile()+" 导入失败：");
					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
					for (String message : messageList){
						failureMsg.append(message+"; ");
						failureNum++;
					}
				}catch (Exception ex) {
					failureMsg.append("<br/>手机号 "+mallTicketSendTemp.getMobile()+" 导入失败："+ex.getMessage());
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
    }
	
	/**
	 * 导出模板
	 */
	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:edit")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "发门票.xlsx";
    		List<MallTicketSendTemp> list = Lists.newArrayList(); 
    		new ExportExcel(null, MallTicketSendTemp.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
    }
	
	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("mallticketsendtemp:mallTicketSendTemp:edit")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MallTicketSendTemp mallTicketSendTemp, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "发门票"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<MallTicketSendTemp> page = mallTicketSendTempService.findPage(new Page<MallTicketSendTemp>(request, response, -1), mallTicketSendTemp);
            List<MallTicketSendTemp> list = page.getList();
            for (MallTicketSendTemp temp : list) {
            	temp.setIsSend("0".equals(temp.getIsSend())?"否":"是");
			}
    		new ExportExcel("发门票", MallTicketSendTemp.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
    }
	
}