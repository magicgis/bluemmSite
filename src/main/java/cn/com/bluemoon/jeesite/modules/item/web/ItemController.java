/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.item.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.mapper.JsonMapper;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.FtpUploadUtil;
import cn.com.bluemoon.jeesite.common.utils.JedisUtils;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.item.entity.MallCategoryItem;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetailVo;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemImage;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.item.entity.ItemRecommend;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemSuite;
import cn.com.bluemoon.jeesite.modules.item.entity.MallProductInfo;
import cn.com.bluemoon.jeesite.modules.item.service.ItemCategoryService;
import cn.com.bluemoon.jeesite.modules.item.service.ItemDetailService;
import cn.com.bluemoon.jeesite.modules.item.service.ItemImageService;
import cn.com.bluemoon.jeesite.modules.item.service.ItemRecommendService;
import cn.com.bluemoon.jeesite.modules.item.service.ItemService;
import cn.com.bluemoon.jeesite.modules.item.service.ProductService;
import cn.com.bluemoon.jeesite.modules.item.service.SuiteService;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.entity.MallVirtualProduct;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.service.MallVirtualProductService;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 商品上下架Controller
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/item")
public class ItemController extends BaseController {

	@Autowired
	private ItemService itemService;
	@Autowired
	private ProductService productService;
	@Autowired
	private SuiteService suiteService;
	@Autowired
	private ItemCategoryService categoryService;
	@Autowired
	private ItemDetailService itemDetailService;
	@Autowired
	private ItemImageService itemImageService;
	@Autowired
	private MallSysParameterService mallSysParameterService;
	@Autowired
	private ItemRecommendService itemRecommendService;
	@Autowired
	private MallVirtualProductService mallVirtualProductService;
	
	@ModelAttribute
	public MallItemInfo get(@RequestParam(required = false) String id) {
		MallItemInfo entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = itemService.get(id);
		}
		if (entity == null) {
			entity = new MallItemInfo();
		}
		return entity;
	}

	@RequestMapping(value = { "list", "" })
	public String list(MallItemInfo mallItemInfo, HttpServletRequest request,
			HttpServletResponse response, Model model) throws Exception {
		{
			try {
				float memberPrice = mallItemInfo.getMemberPriceFloat();
				if (memberPrice != 0) {
					mallItemInfo.setMemberPrice((int)(memberPrice * 100));
				}
			} catch (Exception e) {
				mallItemInfo.setMemberPrice(0);
			}
			Date onlineTime = mallItemInfo.getOnlineTime();
			if (onlineTime != null) {
				java.sql.Date sqlDate = new java.sql.Date(onlineTime.getTime());
				mallItemInfo.setOnlineDate(sqlDate);
			}
			Date offlineTime = mallItemInfo.getOfflineTime();
			if (offlineTime != null) {
				java.sql.Date sqlDate = new java.sql.Date(offlineTime.getTime());
				mallItemInfo.setOnlineDate(sqlDate);
			}
		}
		Page<MallItemInfo> page = itemService.find(new Page<MallItemInfo>(
				request, response), mallItemInfo);
		for (MallItemInfo mallitemInfo : page.getList()) {
			mallitemInfo.setItemName(StringUtils.toHtml(mallitemInfo.getItemName()));
			mallitemInfo.setItemDesc(StringUtils.toHtml(mallitemInfo.getItemDesc()));
			mallitemInfo.setSellPoint(StringUtils.toHtml(mallitemInfo.getSellPoint()));
		}
		model.addAttribute("page", page);
		return "modules/item/itemList";
	}

	@RequestMapping(value = { "form", "" })
	public String form(MallItemInfo mallItemInfo, Model model,HttpServletRequest request, HttpServletResponse response) {
		boolean readFlag = false; 
		if (StringUtils.isNotBlank(mallItemInfo.getItemId())) {
			String itemId = mallItemInfo.getItemId();
			if(StringUtils.isBlank(itemId)){
				itemId = request.getParameter("itemId");
			}
			User user = UserUtils.getUser();
			if(!"系统管理员".equals(user.getName())){
				readFlag = true;
			}
			//获取商品主数据
			mallItemInfo = itemService.get(itemId);
			mallItemInfo.setMemberPriceFloat((float)mallItemInfo.getMemberPrice()/100);
			mallItemInfo.setMarketPriceFloat((float)mallItemInfo.getMarketPrice()/100);
			//获取商品图片信息（FTP获取）
			List<MallItemImage> listImage = itemImageService.findByItemId(itemId);
			mallItemInfo = itemService.getImage(listImage, mallItemInfo);
			//获取产品信息
			List<MallItemDetailVo> mallItemDetailVo = itemService.findGoodsByItemId(itemId);
			for (MallItemDetailVo detail : mallItemDetailVo) {
				detail.setPrice(detail.getPrice()/100);
				detail.setMarketPrice(detail.getMarketPrice()/100);
			}
			mallItemInfo.setMallItemDetailVo(mallItemDetailVo);
			JSONArray json = JSONArray.fromObject(mallItemDetailVo);
			model.addAttribute("mallItemDetailVos", json);
		}else{
			mallItemInfo.setStatus(null);
			mallItemInfo.setHasPresale(null);
		}
		model.addAttribute("mallItemInfo", mallItemInfo);
		model.addAttribute("readFlag", readFlag);
		return "modules/item/itemForm";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@RequestMapping(value = { "save", "" })
	@ResponseBody
	public String save(MallItemInfo mallItemInfo, Model model,HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, mallItemInfo)) {
			return form(mallItemInfo, model, request, response);
		}
		try {
			String jsonStr = request.getParameter("goodDetail");
			List<MallItemDetail> MallItemDetails = null;
			if(StringUtils.isNotBlank(jsonStr)){
				JSONArray json = JSONArray.fromObject(jsonStr);
				MallItemDetails = (List<MallItemDetail>)JSONArray.toCollection(json, MallItemDetail.class);
			}
			mallItemInfo.setHasPresale(1);
			if(StringUtils.isBlank(mallItemInfo.getItemId())){
				//保存商品主数据
				String itemId = "i"+SerialNo.getUNID();
				mallItemInfo.setItemId(itemId);
				mallItemInfo.setMemberPrice((int)Math.round(mallItemInfo.getMemberPriceFloat()*100));
				mallItemInfo.setMarketPrice((int)Math.round(mallItemInfo.getMarketPriceFloat()*100));
				mallItemInfo.setStatus(mallItemInfo.getStatus()==1?1:0);;
				mallItemInfo.setIsValid(1);
				mallItemInfo.setPicUrl(mallItemInfo.getMainPicUrl());
				mallItemInfo.setIsNewRecord(true);
				//TODO全部排序自动后延
				itemService.updatePosition(mallItemInfo);
				itemService.save(mallItemInfo);
				//保存商品分类信息
				String categoryId =  mallItemInfo.getCategoryId();
				MallCategoryItem mallCategoryItem = new MallCategoryItem();
				mallCategoryItem.setCiId(SerialNo.getUNID());
				mallCategoryItem.setCid(categoryId);
				mallCategoryItem.setItemId(itemId);
				mallCategoryItem.setCategoryPosition(mallItemInfo.getCategoryPosition());
				//2016.2.23 排序自动后延
				categoryService.updateCategoryPosition(mallCategoryItem);
				mallCategoryItem.setStatus(1);
				mallCategoryItem.setIsNewRecord(true);
				categoryService.save(mallCategoryItem);
				//保存商品子产品数据
				for (MallItemDetail mallItemDetail : MallItemDetails) {
					mallItemDetail.setDetailId(SerialNo.getUNID());
					mallItemDetail.setItemId(itemId);
					mallItemDetail.setPrice((int)Math.round(mallItemDetail.getPriceFloat()*100));
					if(mallItemDetail.getGoodType().equals("套装")){
						mallItemDetail.setGoodType("suite");
					}else if(mallItemDetail.getGoodType().equals("单品")){
						mallItemDetail.setGoodType("single");
					}else if(mallItemDetail.getGoodType().equals("虚拟")){
						mallItemDetail.setGoodType("virtual");
					}
					mallItemDetail.setIsNewRecord(true);
					itemDetailService.save(mallItemDetail);
				}
				//保存商品图片信息（上传FTP）
				String main = mallItemInfo.getMainPicUrl();
				String intro = mallItemInfo.getIntroPicUrl();
				String artMain = mallItemInfo.getArtMainPicUrl();
				//上传图片
				if(StringUtils.isNotBlank(main)){
					//主图main
					uploadImage(mallItemInfo, main, request, 0,MallItemImage.IMAGE_TYPE_MAIN);
				}
				if(StringUtils.isNotBlank(intro)){
					//细节图intro 
					String arr[] = intro.split("\\|");
					if(arr.length>0){
						for(int i=1;i<arr.length;i++){
							uploadImage(mallItemInfo, arr[i], request, i,MallItemImage.IMAGE_TYPE_INTRO);
						}
					}
				}
				if(StringUtils.isNotBlank(artMain)){
					//详情图artMain
					String arr[] = artMain.split("\\|");
					if(arr.length>0){
						for(int i=1;i<arr.length;i++){
							uploadImage(mallItemInfo, arr[i], request, i,MallItemImage.IMAGE_TYPE_ARTMAIN);
						}
					}
				}
				return "saveSuccess";
			}else{
				//更新商品
				String itemId = mallItemInfo.getItemId();
				mallItemInfo.setId(itemId);
				mallItemInfo.setMemberPrice((int)Math.round(mallItemInfo.getMemberPriceFloat()*100));
				mallItemInfo.setMarketPrice((int)Math.round(mallItemInfo.getMarketPriceFloat()*100));
				boolean skuFlag = checkItemSku(mallItemInfo, request, response);
				if(!skuFlag){
					throw new Exception();
				}
				itemService.updatePosition(mallItemInfo);
				itemService.save(mallItemInfo);
				// 删除缓存
				JedisUtils.del("item"+itemId);
				List<ItemRecommend> list = itemRecommendService.findByItemId(itemId);
				if(list.size()>0){
					//如果该商品是推荐商品，删除推荐商品缓存
					JedisUtils.del("itemall");
				}
				//更新商品分类信息
				String categoryId =  mallItemInfo.getCategoryId();
				MallCategoryItem mallCategoryItem = new MallCategoryItem();
				mallCategoryItem.setCid(categoryId);
				mallCategoryItem.setItemId(itemId);
				mallCategoryItem.setCategoryPosition(mallItemInfo.getCategoryPosition());
				mallCategoryItem.setStatus(1);
				categoryService.updateCategoryPosition(mallCategoryItem);
				categoryService.update(mallCategoryItem);
				//更新商品子产品数据
				MallItemDetail item = new MallItemDetail();
				item.setItemId(itemId);
				itemDetailService.delete(item);
				for (MallItemDetail mallItemDetail : MallItemDetails) {
					mallItemDetail.setDetailId(SerialNo.getUNID());
					mallItemDetail.setItemId(itemId);
					mallItemDetail.setPrice((int)Math.round(mallItemDetail.getPriceFloat()*100));
					if(mallItemDetail.getGoodType().equals("套装")){
						mallItemDetail.setGoodType("suite");
					}else if(mallItemDetail.getGoodType().equals("单品")){
						mallItemDetail.setGoodType("single");
					}else if(mallItemDetail.getGoodType().equals("虚拟")){
						mallItemDetail.setGoodType("virtual");
					}
					mallItemDetail.setIsNewRecord(true);
					itemDetailService.save(mallItemDetail);
				}
				//更新图片信息
				String ftpOutUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
				String mainNew = mallItemInfo.getMainPicUrl();
				String introNew = mallItemInfo.getIntroPicUrl();
				String artMainNew = mallItemInfo.getArtMainPicUrl();
				
				String main = "";
				List<String> introList = new ArrayList<String>();
				List<String> artMainList = new ArrayList<String>();
				
				List<MallItemImage> listImage = itemImageService.findByItemId(itemId);
				List<MallItemImage> mainImages = new ArrayList<MallItemImage>();
				List<MallItemImage> introImages = new ArrayList<MallItemImage>();
				List<MallItemImage> artMainImages = new ArrayList<MallItemImage>();
				if(listImage.size()>0){
					for (MallItemImage image : listImage) {
						if(StringUtils.equals("main", image.getImgType())){
							mainImages.add(image);
							main = ftpOutUrl+image.getItmeImgUrl();
						}else if(StringUtils.equals("intro", image.getImgType())){
							introImages.add(image);
							introList.add(ftpOutUrl+image.getItmeImgUrl());
						}else if(StringUtils.equals("artMain", image.getImgType())){
							artMainImages.add(image);
							artMainList.add(ftpOutUrl+image.getItmeImgUrl());
						}else{
							continue;
						}
					}
				}
				MallItemImage mallItemImage = null;
				//更新主图
				if(StringUtils.isBlank(main)){
					if(StringUtils.isNotBlank(mainNew)){
						uploadImage(mallItemInfo, mainNew, request,0,MallItemImage.IMAGE_TYPE_MAIN);
					}
				}else{
					if(!mainNew.equals(main)){
						//删除之前的图片（更改图片状态）
						mallItemImage = mainImages.get(0);
						mallItemImage.setStatus(0);//更新需要设置ID值
						mallItemImage.setIsNewRecord(false);
						itemImageService.save(mallItemImage);
						//添加新的图片
						uploadImage(mallItemInfo, mainNew, request,0,MallItemImage.IMAGE_TYPE_MAIN);
					}
				}
				//细节图
				if(introList.size()==0){
					if(StringUtils.isNotBlank(introNew)){
						String arr[] = introNew.split("\\|");
						if(arr.length>0){
							for(int i=1;i<arr.length;i++){
								uploadImage(mallItemInfo, arr[i], request, i,MallItemImage.IMAGE_TYPE_INTRO);
							}
						}
					}
				}else{
					if(StringUtils.isNotBlank(introNew)){
						String arr[] = introNew.split("\\|");
						List<String> newImageList = new ArrayList<String>();
						for(int i =1;i<arr.length;i++){
							newImageList.add(arr[i]);
						}
						//先删除需要删除的图片
						for (MallItemImage image : introImages) {
							//数据库中图片地址 不在 当前图片地址中
							if(!newImageList.contains(ftpOutUrl+image.getItmeImgUrl())){
								image.setStatus(0);//更新需要设置ID值
								image.setIsNewRecord(false);
								itemImageService.save(image);
							}
						}
						//保存新增的图片
						for(int i=0;i<newImageList.size();i++){
							String imageUrl = newImageList.get(i);
							if(!introList.contains(imageUrl)){
								uploadImage(mallItemInfo, imageUrl, request, i,MallItemImage.IMAGE_TYPE_INTRO);
							}
						}
					}
				}
				//详情图
				if(artMainList.size()==0){
					if(StringUtils.isNotBlank(artMainNew)){
						String arr[] = artMainNew.split("\\|");
						if(arr.length>0){
							for(int i=1;i<arr.length;i++){
								uploadImage(mallItemInfo, arr[i], request, i,MallItemImage.IMAGE_TYPE_ARTMAIN);
							}
						}
					}
				}else{
					if(StringUtils.isNotBlank(artMainNew)){
						String arr[] = artMainNew.split("\\|");
						List<String> newImageList = new ArrayList<String>();
						for(int i=1;i<arr.length;i++){
							newImageList.add(arr[i]);
						}
						//先删除需要删除的图片
						for (MallItemImage image : artMainImages) {
							//数据库中图片地址 不在 当前图片地址中
							if(!newImageList.contains(ftpOutUrl+image.getItmeImgUrl())){
								image.setStatus(0);//更新需要设置ID值
								image.setIsNewRecord(false);
								itemImageService.save(image);
							}
						}
						//保存新增的图片
						for(int i=0;i<newImageList.size();i++){
							String imageUrl = newImageList.get(i);
							if(!artMainList.contains(imageUrl)){
								uploadImage(mallItemInfo, imageUrl, request, i,MallItemImage.IMAGE_TYPE_ARTMAIN);
							}
						}
					}
				}
				return "updateSuccess";
			}
		}catch(Exception e){
			addMessage(redirectAttributes, "保存失败");
			return "error";
		}
	}

	@Transactional
	@RequestMapping(value = { "delItem", "" })
	@ResponseBody
	public String delItem(MallItemInfo MallItemInfo,RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		String itemIds = request.getParameter("itemIds");
		if(StringUtils.isNotBlank(itemIds)){
			String[] arr = itemIds.split(",");
			if(arr.length>0){
				boolean flag = false;
				for (String str : arr) {
					itemService.delItem(str);
					// 删除推荐商品的缓存
					JedisUtils.del("item"+str);
					if(!flag){
						List<ItemRecommend> list = itemRecommendService.findByItemId(str);
						if(list.size()>0){
							//如果该商品是推荐商品，删除推荐商品缓存
							flag = true;
						}
					}
				}
				if(flag){
					JedisUtils.del("itemall");
				}
				return "删除商品成功";
			}
		}else{
			return "删除商品失败";
		}
		return "获取数据出错";
	}
	
	@Transactional
	@RequestMapping(value = { "offItem", "" })
	@ResponseBody
	public String offItem(MallItemInfo MallItemInfo,RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		String itemIds = request.getParameter("itemIds");
		if(StringUtils.isNotBlank(itemIds)){
			String[] arr = itemIds.split(",");
			if(arr.length>0){
				boolean flag = false;
				for (String str : arr) {
					itemService.offItem(str);
					// 更新下架商品缓存信息
					JedisUtils.del("item"+str);
					if(!flag){
						List<ItemRecommend> list = itemRecommendService.findByItemId(str);
						if(list.size()>0){
							//如果该商品是推荐商品，删除推荐商品缓存
							flag = true;
						}
					}
				}
				if(flag){
					JedisUtils.del("itemall");
				}
				return "下架商品成功";
			}
		}else{
			return "下架商品失败";
		}
		return "获取数据出错";
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
	 * 套装产品列表
	 */
	@RequestMapping(value = "suite")
	public String getSuiteList(MallItemSuite mallItemSuite,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<MallItemSuite> page = suiteService.find(new Page<MallItemSuite>(
				request, response), mallItemSuite);
		model.addAttribute("page", page);
		return "modules/item/suiteList";
	}
	
	/**
	 * 虚拟产品列表
	 */
	@RequestMapping(value = "virtual")
	public String getVirtualList(MallVirtualProduct mallVirtualProduct,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<MallVirtualProduct> page = mallVirtualProductService.findPage(new Page<MallVirtualProduct>(
				request, response), mallVirtualProduct);
		model.addAttribute("page", page);
		return "modules/item/virtualList";
	}

	/**
	 * 通过产品Sku找到产品信息
	 */
	@RequestMapping(value = "getGoods")
	@ResponseBody
	public String getGoods(HttpServletRequest request,
			HttpServletResponse response) {
		String goodSkus = request.getParameter("goodSkus");
		if (goodSkus.endsWith(",")) {
			goodSkus = goodSkus.substring(0, goodSkus.length() - 1);
		}
		List<MallItemDetail> list = itemService.findItemDetailByGoodSkus(goodSkus);
		for (MallItemDetail mallItemDetail : list) {
			mallItemDetail.setMarketPrice(mallItemDetail.getMarketPrice()/100);
		}
		String jsonStr = JsonMapper.toJsonString(list);
		return jsonStr;
	}
	
	/**
	 * 校验商品SKU
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkItemSku")
	@ResponseBody
	public boolean checkItemSku(MallItemInfo mallItemInfo,HttpServletRequest request,
			HttpServletResponse response){
		boolean flag = false;
		List<MallItemInfo> list = itemService.findItemBySku(mallItemInfo.getItemSku());
		if(StringUtils.isNotBlank(mallItemInfo.getItemId())&&list.size()==1){
			if(mallItemInfo.getItemId().equals(list.get(0).getItemId())){
				return true;
			}
		}
		if(list.size()>0){
			if(mallItemInfo.getOnlineTime()!=null&&mallItemInfo.getOfflineTime()!=null){
				for (MallItemInfo item : list) {
					if(mallItemInfo.getOnlineTime().after(item.getOfflineTime())
							||mallItemInfo.getOfflineTime().before(item.getOnlineTime())){
						flag = true;
					}else{
						flag = false;
						break;
					}
				}
			}
		}else{
			flag = true;
		}
		return flag;
	}  
	
	
	public void uploadImage(MallItemInfo mallItemInfo,String fileName,HttpServletRequest request,int position,String imgType){
		String ftp = "FTP";
		String rootPath = request.getSession().getServletContext().getRealPath("/userfiles");
		if(fileName.contains("userfiles")){
			fileName = fileName.substring(fileName.indexOf("userfiles")+"userfiles".length());
		}
		String filePath =  rootPath+fileName;
//		String FTP_OUT_URL = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_OUT_URL");
		String FTP_USERNAME = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_USERNAME");
		String FTP_PASSWORD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PASSWORD");
		String FTP_URL = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_URL");
		int FTP_PORT = Integer.valueOf(mallSysParameterService.getSysParameterByTypeAndCode(ftp, "FTP_PORT"));
		String TEMP_UPLOAD = mallSysParameterService.getSysParameterByTypeAndCode(ftp, "TEMP_UPLOAD");
		String mainPath = FtpUploadUtil.getNewFilePath(mallItemInfo.getItemSku(),filePath, 
				FTP_URL, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, TEMP_UPLOAD);
		MallItemImage mallItemImage = new MallItemImage();
		mallItemImage.setId(SerialNo.getUNID());
		mallItemImage.setItemId(mallItemInfo.getItemId());
		mallItemImage.setImgType(imgType);
		mallItemImage.setItmeImgUrl(mainPath);
		mallItemImage.setStatus(1);
		mallItemImage.setPosition(position);
		mallItemImage.setIsNewRecord(true);
		itemImageService.save(mallItemImage);
	}

}