/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.single.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.FtpUploadUtil;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.item.entity.MallProductInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ProductService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;

/**
 * 单品表主数据管理Controller
 * @author linyihao
 * @version 2016-01-21
 */
@Controller
@RequestMapping(value = "${adminPath}/single/mallProductInfo")
public class MallProductInfoController extends BaseController {

	@Autowired
	private ProductService mallProductInfoService;
	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallProductInfo get(@RequestParam(required=false) String productId) {
		MallProductInfo entity = null;
		if (StringUtils.isNotBlank(productId)){
			entity = mallProductInfoService.getById(productId);
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			entity.setPicUrl(ftpUrl+entity.getPicUrl());
			entity.setMarketPrice(entity.getMarketPrice()/100);
		}
		if (entity == null){
			entity = new MallProductInfo();
		}
		return entity;
	}
	
	@RequiresPermissions("single:mallProductInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallProductInfo mallProductInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		float marketPrice = mallProductInfo.getMarketPrice();
		if (marketPrice != 0) {
			mallProductInfo.setMarketPrice((int)(marketPrice * 100));
		}
		Page<MallProductInfo> page = mallProductInfoService.findPage(new Page<MallProductInfo>(request, response), mallProductInfo); 
		mallProductInfo.setMarketPrice(marketPrice);
		model.addAttribute("page", page);
		return "modules/single/mallProductInfoList";
	}

	@RequiresPermissions("single:mallProductInfo:view")
	@RequestMapping(value = "form")
	public String form(MallProductInfo mallProductInfo, Model model) {
		model.addAttribute("mallProductInfo", mallProductInfo);
		return "modules/single/mallProductInfoForm";
	}

	@RequiresPermissions("single:mallProductInfo:edit")
	@RequestMapping(value = "save")
	public String save(MallProductInfo mallProductInfo, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		if (!beanValidator(model, mallProductInfo)){
			return form(mallProductInfo, model);
		}
		String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
		if(StringUtils.isBlank(mallProductInfo.getProductId())){
			String productId = SerialNo.getUNID();
			mallProductInfo.setProductId(productId);
			mallProductInfo.setMarketPrice((int)Math.round(mallProductInfo.getMarketPrice()*100));
			mallProductInfo.setStatus(1);
			mallProductInfo.setIsNewRecord(true);
			String imgPath = uploadImage(mallProductInfo.getProductSku(), mallProductInfo.getPicUrl(), request, 1);
			mallProductInfo.setPicUrl(imgPath);
		}else{
			MallProductInfo productInfo = mallProductInfoService.get(mallProductInfo.getProductId());
			mallProductInfo.setId(mallProductInfo.getProductId());
			mallProductInfo.setMarketPrice((int)Math.round(mallProductInfo.getMarketPrice()*100));
			mallProductInfo.setIsNewRecord(false);
			if(!StringUtils.equals(ftpUrl+productInfo.getPicUrl(), mallProductInfo.getPicUrl()) ){
				String imgPath = uploadImage(mallProductInfo.getProductSku(), mallProductInfo.getPicUrl(), request, 1);
				mallProductInfo.setPicUrl(imgPath);
			}else{
				mallProductInfo.setPicUrl(productInfo.getPicUrl());
			}
		}
		mallProductInfoService.save(mallProductInfo);
		addMessage(redirectAttributes, "保存产品成功");
		return "redirect:"+Global.getAdminPath()+"/single/mallProductInfo/?repage";
	}
	
	@RequiresPermissions("single:mallProductInfo:edit")
	@RequestMapping(value = "delete")
	public String delete(MallProductInfo mallProductInfo, RedirectAttributes redirectAttributes) {
		mallProductInfoService.delete(mallProductInfo);
		addMessage(redirectAttributes, "删除产品成功");
		return "redirect:"+Global.getAdminPath()+"/single/mallProductInfo/?repage";
	}
	
	/**
	 * 校验商品SKU
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkProductSku")
	@ResponseBody
	public boolean checkProductSku(MallProductInfo mallProductInfo,HttpServletRequest request,
			HttpServletResponse response){
		boolean flag = false;
		List<MallProductInfo> list = mallProductInfoService.findAllList(mallProductInfo);
		if(list.size()<=0){
			flag = true;
		}
		return flag;
	}  
	
	/**
	 * 上传图片
	 * @param productSku
	 * @param fileName
	 * @param request
	 * @param position
	 * @return
	 */
	private String uploadImage(String productSku,String fileName,HttpServletRequest request,int position){
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
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PRODUCT");
		String mainPath = FtpUploadUtil.getNewFilePath(productSku,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}

}