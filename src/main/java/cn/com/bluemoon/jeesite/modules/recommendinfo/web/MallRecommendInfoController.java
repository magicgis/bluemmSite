/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.web;

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
import cn.com.bluemoon.jeesite.common.utils.FtpUploadUtil;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.activity.entity.MallPromotionActivityInfo;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuth;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallOrderInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendOperInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.service.MallRecommendInfoService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 推荐码申请审核Controller
 * @author xgb
 * @version 2016-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/recommendinfo/mallRecommendInfo")
public class MallRecommendInfoController extends BaseController {

	@Autowired
	private MallRecommendInfoService mallRecommendInfoService;
	
	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallRecommendInfo get(@RequestParam(required=false) String id) {
		MallRecommendInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallRecommendInfoService.get(id);
		}
		if (entity == null){
			entity = new MallRecommendInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallRecommendInfo mallRecommendInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallRecommendInfo> page = mallRecommendInfoService.findPage(new Page<MallRecommendInfo>(request, response), mallRecommendInfo);
		int type = -1;
		User user = UserUtils.getUser();
		String office = user.getOffice().toString();
		if("IT部".endsWith(office) || "公司管理员".endsWith(office)){
			type = 0;
		}else if("客服部".equals(office)){
			type = 1;
		}else if("财务部".equals(office)){
			type = 2;
		}
		if(page != null && page.getList() != null && page.getList().size() > 0){
			List<MallRecommendInfo> reList = page.getList();
			for(int i=0;i<reList.size();i++){
				MallRecommendInfo re = reList.get(i);
				if(re.getOldRecommend() == null || "".equals(re.getOldRecommend())){
					re.setOldRecommend("(未填推荐码)");
				}else{
					re.setOldRecommend(re.getOldRecommend());
				}
				String s = re.getOrderMoney();
				if(s.lastIndexOf("00") != -1){
					s = s.substring(0,s.length()-2);
				}else{
					double d = Double.valueOf(s);
					d = d / 100;
					s = d +"";
				}
				re.setOrderMoney(s);
				MallRecommendOperInfo ro = null;
				try {
					/*ro = mallRecommendInfoService.getOperInfoByReId(re.getId(),mallRecommendInfo.getHaveRecommend(),mallRecommendInfo.getOldRecommend(),mallRecommendInfo.getAduitStatus());
					if(ro != null){
						re.setAduitBy(ro.getAduitBy());
						re.setAduitReason(ro.getAduitReason());
						re.setAduitStatus(ro.getAduitStatus());
						re.setAduitTime(ro.getAduitTime());
						re.setUpdatBy(ro.getUpdatBy());
						re.setUpdateTime(ro.getUpdateTime());
						if(ro.getOldRecommend() == null || "".equals(ro.getOldRecommend())){
							re.setOldRecommend("(未填推荐码)");
						}else{
							re.setOldRecommend(ro.getOldRecommend());
						}
						re.setNewRecommend(ro.getNewRecommend());
					}else{
						reList.remove(re);
						i--;
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		model.addAttribute("page", page);
		model.addAttribute("office", type);
		return "modules/recommendinfo/mallRecommendInfoList";
	}

	@RequiresPermissions("recommendinfo:mallRecommendInfo:view")
	@RequestMapping(value = "form")
	public String form(MallRecommendInfo mallRecommendInfo, Model model) {
		User user = UserUtils.getUser();
		mallRecommendInfo.setUpdatBy(user.getLoginName());
		mallRecommendInfo.setCreateDate(new Date());
		model.addAttribute("mallRecommendInfo", mallRecommendInfo);
		return "modules/recommendinfo/mallRecommendInfoForm";
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:view")
	@RequestMapping(value = "modify")
	public String modify(MallRecommendInfo mallRecommendInfo, Model model,HttpServletRequest request) {
		User user = UserUtils.getUser();
		String id = request.getParameter("id");
		mallRecommendInfo = mallRecommendInfoService.get(id);
		try {
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			ftpUrl = ftpUrl.substring(0, ftpUrl.length()-1);
			MallRecommendOperInfo ro = mallRecommendInfoService.getOperInfoByReId(mallRecommendInfo.getId(), null, null, null);
			mallRecommendInfo.setAduitBy(ro.getAduitBy());
			mallRecommendInfo.setAduitReason(ro.getAduitReason());
			mallRecommendInfo.setAduitStatus(ro.getAduitStatus());
			mallRecommendInfo.setAduitTime(ro.getAduitTime());
			mallRecommendInfo.setOldRecommend(ro.getOldRecommend());
			mallRecommendInfo.setNewRecommend(ro.getNewRecommend());
			mallRecommendInfo.setPictureFirst(ftpUrl+ro.getPictureFirst());
			mallRecommendInfo.setPictureSecond(ftpUrl+ro.getPictureSecond());
			mallRecommendInfo.setUpdatBy(ro.getUpdatBy());
			mallRecommendInfo.setUpdateTime(ro.getUpdateTime());
			String s = mallRecommendInfo.getOrderMoney();
			if(s.lastIndexOf("00") != -1){
				s = s.substring(0,s.length()-2);
			}else{
				double d = Double.valueOf(s);
				d = d / 100;
				s = d +"";
			}
			mallRecommendInfo.setOrderMoney(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mallRecommendInfo.setUpdatBy(user.getLoginName());
		mallRecommendInfo.setCreateDate(new Date());
		model.addAttribute("mallRecommendInfo", mallRecommendInfo);
		return "modules/recommendinfo/mallRecommendInfoFormModify";
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:view")
	@RequestMapping(value = "check")
	public String check(MallRecommendInfo mallRecommendInfo, Model model,HttpServletRequest request) {
		User user = UserUtils.getUser();
		String id = request.getParameter("id");
		mallRecommendInfo = mallRecommendInfoService.get(id);
		try {
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			ftpUrl = ftpUrl.substring(0, ftpUrl.length()-1);
			MallRecommendOperInfo ro = mallRecommendInfoService.getOperInfoByReId(mallRecommendInfo.getId(), null, null, null);
			mallRecommendInfo.setAduitBy(ro.getAduitBy());
			mallRecommendInfo.setAduitReason(ro.getAduitReason());
			mallRecommendInfo.setAduitStatus(ro.getAduitStatus());
			mallRecommendInfo.setAduitTime(ro.getAduitTime());
			mallRecommendInfo.setOldRecommend(ro.getOldRecommend());
			mallRecommendInfo.setNewRecommend(ro.getNewRecommend());
			mallRecommendInfo.setPictureFirst(ftpUrl+ro.getPictureFirst());
			mallRecommendInfo.setPictureSecond(ftpUrl+ro.getPictureSecond());
			mallRecommendInfo.setUpdatBy(ro.getUpdatBy());
			mallRecommendInfo.setUpdateTime(ro.getUpdateTime());
			String s = mallRecommendInfo.getOrderMoney();
			if(s.lastIndexOf("00") != -1){
				s = s.substring(0,s.length()-2);
			}else{
				double d = Double.valueOf(s);
				d = d / 100;
				s = d +"";
			}
			mallRecommendInfo.setOrderMoney(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mallRecommendInfo.setAduitBy(user.getLoginName());
		mallRecommendInfo.setAduitTime(new Date());
		model.addAttribute("mallRecommendInfo", mallRecommendInfo);
		return "modules/recommendinfo/mallRecommendInfoFormCheck";
	}

	@RequiresPermissions("recommendinfo:mallRecommendInfo:edit")
	@RequestMapping(value = "save")
	public String save(MallRecommendInfo mallRecommendInfo, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, mallRecommendInfo)){
			return form(mallRecommendInfo, model);
		}
		String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
		String imgPath = uploadImage(mallRecommendInfo.getOrderCode(), mallRecommendInfo.getPictureFirst(), request, 1);
		String imgPath2 = uploadImage(mallRecommendInfo.getOrderCode(), mallRecommendInfo.getPictureSecond(), request, 1);
		if("".equals(imgPath)){
			addMessage(redirectAttributes, "凭证1上传失败");
			return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
		}
		if("".equals(imgPath2)){
			addMessage(redirectAttributes, "凭证2上传失败");
			return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
		}
		double d = Double.parseDouble(mallRecommendInfo.getOrderMoney());
		mallRecommendInfo.setOrderMoney(d * 100+"");
		mallRecommendInfo.setPictureFirst(imgPath);
		mallRecommendInfo.setPictureSecond(imgPath2);
		mallRecommendInfoService.save(mallRecommendInfo);
		addMessage(redirectAttributes, "保存推荐码成功");
		return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:edit")
	@RequestMapping(value = "update")
	public String update(MallRecommendInfo mallRecommendInfo, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, mallRecommendInfo)){
			return form(mallRecommendInfo, model);
		}
		String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
		if(mallRecommendInfo.getPictureFirst().indexOf("/recommendPhoto") != -1){
			String imgPath = uploadImage(mallRecommendInfo.getOrderCode(), mallRecommendInfo.getPictureFirst(), request, 1);
			mallRecommendInfo.setPictureFirst(imgPath);
		}else{
			String p1 = mallRecommendInfo.getPictureFirst();
			int pI = p1.indexOf(ftpUrl);
			p1 = p1.substring(pI+ftpUrl.length()-1, p1.length());
			mallRecommendInfo.setPictureFirst(p1);
		}
		if(mallRecommendInfo.getPictureSecond().indexOf("/recommendPhoto") != -1){
			String imgPath2 = uploadImage(mallRecommendInfo.getOrderCode(), mallRecommendInfo.getPictureSecond(), request, 1);
			mallRecommendInfo.setPictureSecond(imgPath2);
		}else{
			String p2 = mallRecommendInfo.getPictureSecond();
			int pI = p2.indexOf(ftpUrl);
			p2 = p2.substring(pI+ftpUrl.length()-1, p2.length());
			mallRecommendInfo.setPictureSecond(p2);
		}
		double d = Double.parseDouble(mallRecommendInfo.getOrderMoney());
		mallRecommendInfo.setOrderMoney(d * 100+"");
		mallRecommendInfoService.update(mallRecommendInfo);
		addMessage(redirectAttributes, "保存推荐码成功");
		return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:edit")
	@RequestMapping(value = "checkUpdate")
	public String checkUpdate(MallRecommendInfo mallRecommendInfo, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		if (!beanValidator(model, mallRecommendInfo)){
			return form(mallRecommendInfo, model);
		}
		String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
		double d = Double.parseDouble(mallRecommendInfo.getOrderMoney());
		mallRecommendInfo.setOrderMoney(d * 100+"");
		String p1 = mallRecommendInfo.getPictureFirst();
		String p2 = mallRecommendInfo.getPictureSecond();
		p1 = p1.substring(ftpUrl.length()-1, p1.length());
		p2 = p2.substring(ftpUrl.length()-1, p2.length());
		mallRecommendInfo.setPictureFirst(p1);
		mallRecommendInfo.setPictureSecond(p2);
		mallRecommendInfoService.checkUpdate(mallRecommendInfo);
		addMessage(redirectAttributes, "保存推荐码成功");
		return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
	}
	
	@RequiresPermissions("recommendinfo:mallRecommendInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallRecommendInfo mallRecommendInfo, RedirectAttributes redirectAttributes) {
		mallRecommendInfoService.delete(mallRecommendInfo);
		addMessage(redirectAttributes, "删除推荐码成功");
		return "redirect:"+Global.getAdminPath()+"/recommendinfo/mallRecommendInfo/?repage";
	}
	
	/**
	 * 获取订单
	 */
	@RequestMapping(value = "getOrder")
	@ResponseBody
	public Map<String, Object> getOrder(HttpServletRequest request, HttpServletResponse response,Model model) {
		Map<String, Object> map = null;
		String orderCode = request.getParameter("orderCode");
		try {
			MallRecommendInfo info = mallRecommendInfoService.getRecommendByOrderCode(orderCode);
			if(info != null){
				map = new HashMap<String, Object>();
				map.put("flag", "already");
				return map;
			}
			MallOrderInfo order = mallRecommendInfoService.getOrderByOrderCode(orderCode);
			if(order != null ){
				map = new HashMap<String, Object>();
				if(order.getParentCode() != null && !"".equals(order.getParentCode())){
					order = mallRecommendInfoService.getOrderByOrderCode(order.getParentCode());
				}
				String json = JsonMapper.toJsonString(order);
				map.put("json", json);
				map.put("flag", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 上传图片
	 * @param productSku
	 * @param fileName
	 * @param request
	 * @param position
	 * @return
	 */
	private String uploadImage(String orderCode,String fileName,HttpServletRequest request,int position){
		String ftp = "FTP";
		String rootPath = request.getSession().getServletContext().getRealPath("/userfiles");
		if(fileName.contains("userfiles")){
			fileName = fileName.substring(fileName.indexOf("userfiles")+"userfiles".length());
		}
		String filePath =  rootPath+fileName;
		String FTP_USERNAME = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_USERNAME");
		String FTP_PASSWORD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PASSWORD");
		String FTP_URL = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_URL");
		int FTP_PORT = Integer.valueOf(mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PORT"));
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_RECOMMEND_REVIEW");
		String mainPath = FtpUploadUtil.getNewFilePathByRecommend(orderCode,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}
	
	/**
	 * 获取操作详细
	 */
	@RequestMapping(value = "getOperItem")
	public String getOperItem(HttpServletRequest request, HttpServletResponse response,Model model) {
		String id = request.getParameter("id");
		List<MallRecommendOperInfo> list = null;
		try {
			list = mallRecommendInfoService.getOperInfoById(id);
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			ftpUrl = ftpUrl.substring(0, ftpUrl.length()-1);
			if(list != null && list.size() > 0){
				for(int i=0;i<list.size();i++){
					list.get(i).setIndex(list.size()-i);
					list.get(i).setPictureFirst(ftpUrl+list.get(i).getPictureFirst());
					list.get(i).setPictureSecond(ftpUrl+list.get(i).getPictureSecond());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("listOper", list);
		return "modules/recommendinfo/recommendOperList";
	}
	
}