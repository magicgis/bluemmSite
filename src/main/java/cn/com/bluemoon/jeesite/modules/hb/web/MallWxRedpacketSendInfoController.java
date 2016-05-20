/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.DateUtils;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.utils.excel.ExportExcel;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketSendinfo;
import cn.com.bluemoon.jeesite.modules.hb.service.MallWxRedpacketSendInfoService;



/**
 * 红包申请Controller
 * @author linyihao
 * @version 2016-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/hb/mallWxRedpacketSendInfo")
public class MallWxRedpacketSendInfoController extends BaseController {
	
	@Autowired
	private MallWxRedpacketSendInfoService mallWxRedpacketSendinfoService;
	
	@ModelAttribute
	public MallWxRedpacketSendinfo get(@RequestParam(required=false) String id) {
		MallWxRedpacketSendinfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallWxRedpacketSendinfoService.get(id);
		}
		if (entity == null){
			entity = new MallWxRedpacketSendinfo();
		}
		return entity;
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(MallWxRedpacketSendinfo mallWxRedpacketSendinfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		String url = "modules/hb/mallWxRedpacketSendInfoList";
		Page<MallWxRedpacketSendinfo> page = mallWxRedpacketSendinfoService.findPage(new Page<MallWxRedpacketSendinfo>(request, response), mallWxRedpacketSendinfo); 
		model.addAttribute("page", page);
		return url;
	}

	/**
	 * 导出用户数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(MallWxRedpacketSendinfo mallWxRedpacketSendinfo, RedirectAttributes redirectAttributes, 
    		HttpServletRequest request, HttpServletResponse response) {
		try {
            String fileName = "红包明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            List<MallWxRedpacketSendinfo> list = mallWxRedpacketSendinfoService.findList(mallWxRedpacketSendinfo);
          	for (MallWxRedpacketSendinfo temp : list) {
          		String status = temp.getStatus();
          		if ("1".equalsIgnoreCase(status)) {
					status = "发送中"; // 发送中
				} else if ("2".equalsIgnoreCase(status)) {
					status = "已发送待领取"; // 已发送待领取
				} else if ("3".equalsIgnoreCase(status)) {
					status = "发送失败"; // 发送失败
				} else if ("4".equalsIgnoreCase(status)) {
					status = "已领取"; // 已领取
				} else if ("5".equalsIgnoreCase(status)) {
					status = "已退款"; // 已退款
				} else {
					status = "未发放";
				}
          		temp.setStatus(status);
			}
            String agent = request.getHeader("USER-AGENT").toLowerCase();
    		new ExportExcel(null, MallWxRedpacketSendinfo.class,2).setDataList(list).write(agent,response, fileName).dispose();
//    		new ExportExcel("发门票", MallTicketSendTemp.class).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
    }

	
	
}