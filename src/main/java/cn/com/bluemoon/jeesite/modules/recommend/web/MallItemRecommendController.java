/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommend.web;

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
import cn.com.bluemoon.jeesite.common.utils.JedisUtils;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemImageService;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.recommend.entity.MallItemRecommend;
import cn.com.bluemoon.jeesite.modules.recommend.service.MallItemRecommendService;

/**
 * 商品推荐位Controller
 * @author linyihao
 * @version 2016-01-15
 */
@Controller
@RequestMapping(value = "${adminPath}/recommend/mallItemRecommend")
public class MallItemRecommendController extends BaseController {

	@Autowired
	private MallItemRecommendService mallItemRecommendService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private MallSysParameterService mallSysParameterService;
	@Autowired
	private ItemImageService itemImageService;
	
	@ModelAttribute
	public MallItemRecommend get(@RequestParam(required=false) String id) {
		MallItemRecommend entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallItemRecommendService.get(id);
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			entity.setRePicUrl(ftpUrl+entity.getRePicUrl());
		}
		if (entity == null){
			entity = new MallItemRecommend();
		}
		return entity;
	}
	
	@RequiresPermissions("recommend:mallItemRecommend:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallItemRecommend mallItemRecommend, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallItemRecommend> page = mallItemRecommendService.findPage(new Page<MallItemRecommend>(request, response), mallItemRecommend); 
		model.addAttribute("page", page);
		return "modules/recommend/mallItemRecommendList";
	}

	@RequiresPermissions("recommend:mallItemRecommend:view")
	@RequestMapping(value = "form")
	public String form(MallItemRecommend mallItemRecommend, Model model) {
		model.addAttribute("mallItemRecommend", mallItemRecommend);
		return "modules/recommend/mallItemRecommendForm";
	}

	@RequiresPermissions("recommend:mallItemRecommend:edit")
	@RequestMapping(value = "save")
	public String save(MallItemRecommend mallItemRecommend, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		if(StringUtils.isBlank(mallItemRecommend.getItemId())){
			mallItemRecommend.setItemId(null);
		}
		if(StringUtils.isBlank(mallItemRecommend.getRePicUrl())){
			mallItemRecommend.setRePicUrl(null);
		}
		if(mallItemRecommend.getOnRecomDate().after(mallItemRecommend.getOffRecomDate())){
			addMessage(model, "结束时间必须在开始时间之后");
			return form(mallItemRecommend, model);
		}
		if (!beanValidator(model, mallItemRecommend)){
			return form(mallItemRecommend, model);
		}
		if(StringUtils.isBlank(mallItemRecommend.getId())){
			String imgPath = uploadImage(mallItemRecommend.getItemSku(), mallItemRecommend.getRePicUrl(), request, 1);
			mallItemRecommend.setRePicUrl(imgPath);
		}else{
			MallItemRecommend recommend = mallItemRecommendService.get(mallItemRecommend.getId());
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			if(!StringUtils.equals(ftpUrl+recommend.getRePicUrl(), mallItemRecommend.getRePicUrl()) ){
				String imgPath = uploadImage(mallItemRecommend.getItemSku(), mallItemRecommend.getRePicUrl(), request, 1);
				mallItemRecommend.setRePicUrl(imgPath);
			}else{
				mallItemRecommend.setRePicUrl(recommend.getRePicUrl());
			}
		}
		mallItemRecommendService.updatePosition(mallItemRecommend);
		mallItemRecommendService.save(mallItemRecommend);
		JedisUtils.del("itemall");
		addMessage(redirectAttributes, "保存推荐商品成功");
		return "redirect:"+Global.getAdminPath()+"/recommend/mallItemRecommend/?repage";
	}
	
	@RequiresPermissions("recommend:mallItemRecommend:edit")
	@RequestMapping(value = "delete")
	public String delete(MallItemRecommend mallItemRecommend, RedirectAttributes redirectAttributes) {
		mallItemRecommendService.delete(mallItemRecommend);
		addMessage(redirectAttributes, "删除推荐商品成功");
		JedisUtils.del("itemall");
		return "redirect:"+Global.getAdminPath()+"/recommend/mallItemRecommend/?repage";
	}
	
	/**
	 * 在售商品列表
	 */
	@RequestMapping(value = "itemList")
	public String getItemList(MallItemInfo mallItemInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		//过滤出售的
		mallItemInfo.setStatus(1);
		Page<MallItemInfo> page = itemService.findRecommendItem(new Page<MallItemInfo>(
				request, response), mallItemInfo);
		model.addAttribute("page", page);
		return "modules/recommend/itemList";
	}
	
	/**
	 * 在售商品列表
	 */
	@RequestMapping(value = "getItemById")
	@ResponseBody
	public String getItemById(HttpServletRequest request, 
			HttpServletResponse response) {
		String itemId = request.getParameter("itemId");
		MallItemInfo item = itemService.getById(itemId);
		if(item!=null){
			String jsonStr = JsonMapper.toJsonString(item);
			return jsonStr;
		}
		return "";
	}
	
	public String uploadImage(String itemSku,String fileName,HttpServletRequest request,int position){
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
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_RECOMMEND");
		String mainPath = FtpUploadUtil.getNewFilePath(itemSku,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}
	
}