/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallvirtualproduct.web;

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
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.common.utils.FtpUploadUtil;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.entity.MallVirtualProduct;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.service.MallVirtualProductService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;

/**
 * 虚拟产品信息管理Controller
 * @author linyihao
 * @version 2016-02-29
 */
@Controller
@RequestMapping(value = "${adminPath}/mallvirtualproduct/mallVirtualProduct")
public class MallVirtualProductController extends BaseController {

	@Autowired
	private MallVirtualProductService mallVirtualProductService;
	
	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallVirtualProduct get(@RequestParam(required=false) String vpId) {
		MallVirtualProduct entity = null;
		if (StringUtils.isNotBlank(vpId)){
			entity = mallVirtualProductService.get(vpId);
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			entity.setPicUrl(ftpUrl+entity.getPicUrl());
			entity.setMarketPriceFloat((float)entity.getMarketPrice()/100);
		}
		if (entity == null){
			entity = new MallVirtualProduct();
		}
		return entity;
	}
	
	@RequiresPermissions("mallvirtualproduct:mallVirtualProduct:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallVirtualProduct mallVirtualProduct, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(mallVirtualProduct.getMarketPriceFloat()!=0){
			mallVirtualProduct.setMarketPrice(Math.round(mallVirtualProduct.getMarketPriceFloat()*100));
		}
		Page<MallVirtualProduct> page = mallVirtualProductService.findPage(new Page<MallVirtualProduct>(request, response), mallVirtualProduct); 
		model.addAttribute("page", page);
		return "modules/mallvirtualproduct/mallVirtualProductList";
	}

	@RequiresPermissions("mallvirtualproduct:mallVirtualProduct:view")
	@RequestMapping(value = "form")
	public String form(MallVirtualProduct mallVirtualProduct, Model model) {
		model.addAttribute("mallVirtualProduct", mallVirtualProduct);
		return "modules/mallvirtualproduct/mallVirtualProductForm";
	}

	@RequiresPermissions("mallvirtualproduct:mallVirtualProduct:edit")
	@RequestMapping(value = "save")
	public String save(MallVirtualProduct mallVirtualProduct, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request ) {
		if (!beanValidator(model, mallVirtualProduct)){
			return form(mallVirtualProduct, model);
		}
		if(StringUtils.isBlank(mallVirtualProduct.getVpId())){
			String newPicUrl = uploadImage(mallVirtualProduct.getVpSku(), mallVirtualProduct.getPicUrl(), request, 1);
			mallVirtualProduct.setPicUrl(newPicUrl);
			mallVirtualProduct.setId(mallVirtualProduct.getVpId());
		}else{
			mallVirtualProduct.setIsNewRecord(false);
			mallVirtualProduct.setId(mallVirtualProduct.getVpId());
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			MallVirtualProduct product = mallVirtualProductService.get(mallVirtualProduct.getVpId());
			if(!StringUtils.equals(ftpUrl+product.getPicUrl(), mallVirtualProduct.getPicUrl()) ){
				String imgPath = uploadImage(mallVirtualProduct.getVpSku(), mallVirtualProduct.getPicUrl(), request, 1);
				mallVirtualProduct.setPicUrl(imgPath);
			}else{
				mallVirtualProduct.setPicUrl(product.getPicUrl());
			}
		}
		mallVirtualProduct.setStatus("1");
		mallVirtualProduct.setMarketPrice(Math.round(mallVirtualProduct.getMarketPriceFloat()*100));
		mallVirtualProductService.save(mallVirtualProduct);
		addMessage(redirectAttributes, "保存虚拟产品成功");
		return "redirect:"+Global.getAdminPath()+"/mallvirtualproduct/mallVirtualProduct/?repage";
	}
	
	@RequiresPermissions("mallvirtualproduct:mallVirtualProduct:edit")
	@RequestMapping(value = "delete")
	public String delete(MallVirtualProduct mallVirtualProduct, RedirectAttributes redirectAttributes) {
		MallVirtualProduct product = mallVirtualProductService.get(mallVirtualProduct.getVpId());
		product.setStatus("0");
		product.setIsNewRecord(false);
		product.setId(mallVirtualProduct.getVpId());
		mallVirtualProductService.save(product);
		addMessage(redirectAttributes, "删除虚拟产品成功");
		return "redirect:"+Global.getAdminPath()+"/mallvirtualproduct/mallVirtualProduct/?repage";
	}
	/**
	 * 校验商品SKU
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkProductSku")
	@ResponseBody
	public boolean checkProductSku(MallVirtualProduct mallVirtualProduct,HttpServletRequest request,
			HttpServletResponse response){
		boolean flag = false;
		List<MallVirtualProduct> list = mallVirtualProductService.findList(mallVirtualProduct);
		if(list.size()<=0){
			flag = true;
		}else{
			flag = true;
			for (MallVirtualProduct product : list) {
				if(!StringUtils.equals(product.getVpId(), mallVirtualProduct.getVpId())){
					flag = false;
					break;
				}
			}
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
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_VIRTUAL");
		String mainPath = FtpUploadUtil.getNewFilePath(productSku,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}

}