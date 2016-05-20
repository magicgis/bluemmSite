package cn.com.bluemoon.jeesite.modules.suite.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.FtpUploadUtil;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemSuite;
import cn.com.bluemoon.jeesite.modules.item.entity.MallProductInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ProductService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetail;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetailGoodVo;
import cn.com.bluemoon.jeesite.modules.suite.service.SuiteInfoService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/suite/mallItemSuite")
public class MallSuiteInfoController extends BaseController{
	@Autowired  
	private SuiteInfoService suiteInfoService;
	@Autowired  
	private ProductService productService;
	@Autowired  
	private MallSysParameterService mallSysParameterService;
	
	@ModelAttribute
	public MallItemSuite get(@RequestParam(required=false) String suiteId) {
		MallItemSuite entity = null;
		if (StringUtils.isNotBlank(suiteId)){
			entity = suiteInfoService.querySuiteInfoBySuiteId(suiteId);
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			entity.setPicUrl(ftpUrl+entity.getPicUrl());
			entity.setMarketPriceFloat((float)entity.getMarketPrice()/100);
		}
		if (entity == null){
			entity = new MallItemSuite();
		}
		return entity;
	}
	
	@RequiresPermissions("suite:mallItemSuite:view")
	@RequestMapping(value = {"suiteList", ""})
	public String list(MallItemSuite mallItemSuite, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<MallItemSuite> page = new Page<MallItemSuite>(request, response);
		page.setPageSize(10);
		page = suiteInfoService.findPage(page, mallItemSuite); 
		model.addAttribute("page", page);
		return "modules/suite/suiteList";
	}
	
	@RequiresPermissions("suite:mallItemSuite:view")
	@RequestMapping(value = "form")
	public String form(MallItemSuite mallItemSuite, Model model) {
		model.addAttribute("mallItemSuite", mallItemSuite);
		List<MallSuiteDetailGoodVo> mallItemSuiteDetails = null;
		if(StringUtils.isNotBlank(mallItemSuite.getSuiteId())){
			String suiteId = mallItemSuite.getSuiteId();
			mallItemSuiteDetails = suiteInfoService.searchSuiteProductList(suiteId);
			JSONArray json = JSONArray.fromObject(mallItemSuiteDetails);
			model.addAttribute("mallItemSuiteDetails", json);
		}else{
			model.addAttribute("mallItemSuiteDetails", null);
		}
		return "modules/suite/suiteForm";
	}
						  
	/**
	 * 套餐保存
	 * 
	 */
	@RequiresPermissions("suite:mallItemSuite:edit")
	@RequestMapping(value = "/save")
	@Transactional 
	public String save(MallItemSuite mallItemSuite ,RedirectAttributes redirectAttributes, 
			HttpServletRequest request,Model model){
		String goodDetail=request.getParameter("goodDetail");
		JSONArray list=null;
		List<MallItemDetail> mallItemDetails = null;
		User user = UserUtils.getUser();
		MallSuiteDetail mallSuiteDetail=new MallSuiteDetail();
		if(StringUtils.isNotBlank(goodDetail)){
			list=JSONArray.fromObject(goodDetail);
			mallItemDetails = (List<MallItemDetail>)JSONArray.toCollection(list, MallItemDetail.class);
		}
		if(StringUtils.isBlank(mallItemSuite.getSuiteId())){//新增
			mallItemSuite.setSuiteId(SerialNo.getUNID());//套餐sku
			mallItemSuite.setIsParent(0);//是否有子套装
			mallItemSuite.setStatus(1);//是否有效
			mallItemSuite.setMarketPrice((int)Math.round(mallItemSuite.getMarketPriceFloat()*100));
			mallItemSuite.setCreateByWho(user.getName());//创建人编号
			mallItemSuite.setCreateTime(new Date());//创建时间
			String imgPath = uploadImage(mallItemSuite.getSuiteSku(), mallItemSuite.getPicUrl(), request, 1);
			mallItemSuite.setPicUrl(imgPath);
			suiteInfoService.save(mallItemSuite);//保存套餐信息
			for(MallItemDetail mallItemDetail : mallItemDetails){//保存子类商品信息
				mallSuiteDetail.setSdId(SerialNo.getUNID());//主键ID
				mallSuiteDetail.setSuiteId(mallItemSuite.getSuiteId());//套装id
				mallSuiteDetail.setSgoodId(mallItemDetail.getGoodId());//货物id
				mallSuiteDetail.setSgoodSku(mallItemDetail.getGoodSku());//货物sku
				mallSuiteDetail.setSgoodType("single");//套装or单品
				mallSuiteDetail.setMarketPrice((int)Math.round(mallItemDetail.getMarketPrice()*100));//套装内价(分)
				mallSuiteDetail.setNum(mallItemDetail.getNum());//数量
				mallSuiteDetail.setCreateByWho(user.getId());//创建人
				mallSuiteDetail.setCreateTime(new Date());//创建时间
				suiteInfoService.saveDetails(mallSuiteDetail);
			}
		}else {//修改
			MallItemSuite mallItemSuiteOld = suiteInfoService.findById(mallItemSuite);
			mallItemSuite.setId(mallItemSuite.getSuiteId());//修改
			mallItemSuite.setIsParent(0);//是否有子套装
			mallItemSuite.setStatus(1);//是否有效
			mallItemSuite.setMarketPrice((int)Math.round(mallItemSuite.getMarketPriceFloat()*100));
			String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
			if(!StringUtils.equals(ftpUrl+mallItemSuiteOld.getPicUrl(), mallItemSuite.getPicUrl())){
				String imgPath = uploadImage(mallItemSuite.getSuiteSku(), mallItemSuite.getPicUrl(), request, 1);
				mallItemSuite.setPicUrl(imgPath);
			}else{
				mallItemSuite.setPicUrl(mallItemSuiteOld.getPicUrl());
			}
			mallItemSuite.setModifyBy(user.getName());//修改人编号
			mallItemSuite.setModifyTime(new Date());//修改时间
			mallItemSuite.setIsNewRecord(false);
			suiteInfoService.save(mallItemSuite);//保存套餐信息
			suiteInfoService.deleteSuiteDetail(mallItemSuite.getSuiteId());
			for(MallItemDetail mallItemDetail : mallItemDetails){//保存子类商品信息
				mallSuiteDetail.setSdId(SerialNo.getUNID());//主键ID
				mallSuiteDetail.setSuiteId(mallItemSuite.getSuiteId());//套装id
				mallSuiteDetail.setSgoodId(mallItemDetail.getGoodId());//货物id
				mallSuiteDetail.setSgoodSku(mallItemDetail.getGoodSku());//货物sku
				mallSuiteDetail.setSgoodType("single");//套装or单品
				mallSuiteDetail.setMarketPrice((int)Math.round(mallItemDetail.getMarketPrice()*100));//套装内价(分)
				mallSuiteDetail.setNum(mallItemDetail.getNum());//数量
				mallSuiteDetail.setCreateByWho(user.getName());//创建人
				mallSuiteDetail.setCreateTime(new Date());//创建时间
				suiteInfoService.saveDetails(mallSuiteDetail);
			}
			
		}
		addMessage(redirectAttributes, "保存产品成功");
		return "redirect:"+Global.getAdminPath()+"/suite/mallItemSuite/?repage";
	}
	
	@RequiresPermissions("suite:mallItemSuite:edit")
	@RequestMapping(value = "delete")
	public String delete(MallItemSuite mallItemSuite, RedirectAttributes redirectAttributes) {
		MallItemSuite mallItemSuiteOld = suiteInfoService.findById(mallItemSuite);
		mallItemSuiteOld.setId("update");
		mallItemSuiteOld.setStatus(0);
		suiteInfoService.save(mallItemSuiteOld);
		addMessage(redirectAttributes, "删除产品成功");
		return "redirect:"+Global.getAdminPath()+"/suite/mallItemSuite/?repage";
	}
	
	@ResponseBody
	@RequestMapping(value= {"suiteDetail", ""}, method = RequestMethod.POST)     
	public List<MallSuiteDetailGoodVo> suiteSearch(HttpServletRequest request,Model model) {
		String suiteId=request.getParameter("suiteId");
		List<MallSuiteDetailGoodVo> list = suiteInfoService.searchSuiteProductList(suiteId);
		return list;
	}
	
	/**
	 * 单品产品列表
	 */
	@RequestMapping(value = "single")
	public String getSingleList(MallProductInfo mallProductInfo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<MallProductInfo> page = productService.find(
				new Page<MallProductInfo>(request, response), mallProductInfo);
		model.addAttribute("page", page);
		return "modules/item/singleList";
	}
	
	/**
	 * 校验商品SKU
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkSuiteSku")
	@ResponseBody
	public boolean checkProductSku(MallItemSuite mallItemSuite,HttpServletRequest request,
			HttpServletResponse response){
		boolean flag = false;
		List<MallItemSuite> list = suiteInfoService.findSameSkuList(mallItemSuite);
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
	private String uploadImage(String suiteSku,String fileName,HttpServletRequest request,int position){
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
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_SUITE");
		String mainPath = FtpUploadUtil.getNewFilePath(suiteSku,filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		return mainPath;
	}
	
//	@RequestMapping(value = {"suiteAdd", ""})
//	public String suiteAdd(MallItemSuite mallSuiteInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
//		//Page<MallItemSuite> page = suiteInfoService.findPage(new Page<MallItemSuite>(request, response), mallSuiteInfo); 
//		//model.addAttribute("page", page);
//		return "modules/suite/suiteAdd";
//	}
//	@RequestMapping(value = {"suiteEdit", ""})
//	public String suiteEdit(String suiteId, HttpServletRequest request, HttpServletResponse response, Model model) {
//		MallItemSuite mallItemSuite = suiteInfoService.querySuiteInfoBySuiteId(suiteId); 
//		List<MallItemDetailVo> list= suiteInfoService.querySuiteDetailBySuiteId(suiteId);
//		for(MallItemDetailVo vo :list){
//			vo.setPrice(vo.getPrice()/100);
//			vo.setMarketPrice(vo.getMarketPrice()/100);
//		}
//		JSONArray json = JSONArray.fromObject(list);
//		model.addAttribute("mallItemSuite", mallItemSuite);
//		model.addAttribute("list",json);
//		return "modules/suite/suiteAdd";
//	}
}
