/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.sys.entity.Log;
import cn.com.bluemoon.jeesite.modules.sys.entity.MallSysLogInfo;
import cn.com.bluemoon.jeesite.modules.sys.service.LogService;
import cn.com.bluemoon.jeesite.modules.sys.service.MallSysLogInfoService;

/**
 * 日志Controller
 * @author ThinkGem
 * @version 2013-6-2
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/log")
public class LogController extends BaseController {

	@Autowired
	private LogService logService;
	
	@Autowired
	private MallSysLogInfoService mallSysLogInfoService;
	
	@RequiresPermissions("sys:log:view")
	@RequestMapping(value = {"list", ""})
	public String list(Log log, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Log> page = logService.findPage(new Page<Log>(request, response), log); 
        model.addAttribute("page", page);
		return "modules/sys/logList";
	}
	
	@RequiresPermissions("gateway:log:view")
	@RequestMapping(value = {"gatewayList", ""})
	public String gatewayList(MallSysLogInfo mallSysLogInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<MallSysLogInfo> page = mallSysLogInfoService.findGatewayPage(new Page<MallSysLogInfo>(request, response), mallSysLogInfo); 
        model.addAttribute("page", page);
		return "modules/sys/mallSysLogInfoList";
	}

}
