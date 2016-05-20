/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.com.bluemoon.jeesite.common.config.Global;
import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.common.utils.excel.ExportExcel;
import cn.com.bluemoon.jeesite.common.utils.excel.ImportExcel;
import cn.com.bluemoon.jeesite.common.web.BaseController;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApply;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetails;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetailsVo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketExcel;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketFileDetails;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketFileDetailsVo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketMerchantInfo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketMerchantInfoVo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketSendinfo;
import cn.com.bluemoon.jeesite.modules.hb.service.MallWxRedpacketApplyService;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;
import cn.com.bluemoon.redpacket.cocurrent.SendRedPackedUtil;

import com.bluemoon.proxy.service.SpringContextUtils;
import com.bluemoon.proxy.service.packet.WxRedPacketService;
import com.google.common.collect.Lists;


/**
 * 红包申请Controller
 * @author linyihao
 * @version 2016-05-03
 */
@Controller
@RequestMapping(value = "${adminPath}/hb/mallWxRedpacketApply")
public class MallWxRedpacketApplyController extends BaseController {
	
	private static final int PAGE_SIZE = 20;
	private static final int MIN_AMOUNT = 100;
	private static final int MAX_AMOUNT = 499900;
	
	@Autowired
	private MallWxRedpacketApplyService mallWxRedpacketApplyService;
	
	@ModelAttribute
	public MallWxRedpacketApply get(@RequestParam(required=false) String id) {
		MallWxRedpacketApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = mallWxRedpacketApplyService.get(id);
		}
		if (entity == null){
			entity = new MallWxRedpacketApply();
		}
		return entity;
	}
	
	@RequiresPermissions("hb:mallWxRedpacketApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(MallWxRedpacketApply mallWxRedpacketApply, Model model
			, HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String url = "modules/hb/mallWxRedpacketApplyList";
		if(StringUtils.equals("aduit", type)){
			url = "modules/hb/mallWxRedpacketApplyAduitList";
		}else if(StringUtils.equals("send", type)){
			mallWxRedpacketApply.setApplystatus("send");
			url = "modules/hb/mallWxRedpacketApplySendList";
		}
		Page<MallWxRedpacketApply> page = mallWxRedpacketApplyService.findPage(
				new Page<MallWxRedpacketApply>(request, response), mallWxRedpacketApply); 
		if(StringUtils.equals("send", type)){
			List<MallWxRedpacketApply> list = page.getList();
			List<MallWxRedpacketApply> listNew = new ArrayList<MallWxRedpacketApply>();
			for (MallWxRedpacketApply temp : list) {
				if("2".equals(temp.getSendStatus())){
					MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
					detail.setApplyid(temp.getApplyid());
					detail.setRedpacketStatus("1");
					List<MallWxRedpacketApplyDetails> listDetails = mallWxRedpacketApplyService.findList(detail);
					if(listDetails.size()==0){
						temp.setSendStatus("3");
						mallWxRedpacketApplyService.updateEntity(temp);
					}else{
						listNew.add(temp);
					}
				}else{
					listNew.add(temp);
				}
			}
			page.setList(listNew);
		}
		model.addAttribute("page", page);
		return url;
	}

	@RequiresPermissions("hb:mallWxRedpacketApply:view")
	@RequestMapping(value = "form")
	public String form(MallWxRedpacketApply mallWxRedpacketApply, Model model ,HttpServletRequest request) {
		User user = UserUtils.getUser();
		String type = request.getParameter("type");
		model.addAttribute("type", type);
		String fromType = request.getParameter("fromType");
		model.addAttribute("fromType", fromType);
		if(mallWxRedpacketApply.getApplyid()==null){
			mallWxRedpacketApply.setApplyercode(user.getLoginName());
			mallWxRedpacketApply.setApplyername(user.getName());
			mallWxRedpacketApply.setApplydate(new Date());
		}
		//获取红包发放商户信息
		List<MallWxRedpacketMerchantInfo> merchantList = 
				mallWxRedpacketApplyService.findList(new MallWxRedpacketMerchantInfo());
		List<MallWxRedpacketMerchantInfoVo> merchantVoList = new ArrayList<MallWxRedpacketMerchantInfoVo>();
		for(MallWxRedpacketMerchantInfo po : merchantList){
			merchantVoList.add(new MallWxRedpacketMerchantInfoVo(po));
		}
		JSONArray jsonMerchant = JSONArray.fromObject(merchantVoList);
		mallWxRedpacketApply.setMerchantJson(jsonMerchant);
		//获取初始化红包信息数据
		if(!StringUtils.isEmpty(mallWxRedpacketApply.getFileid())){
			if(!StringUtils.equals("send", type)){
				mallWxRedpacketApply.setCheckValue("1");
				mallWxRedpacketApply.setSendCode(user.getLoginName());
				mallWxRedpacketApply.setSendName(user.getName());
				MallWxRedpacketFileDetails detail = new MallWxRedpacketFileDetails();
				detail.setFileid(mallWxRedpacketApply.getFileid());
				Page<MallWxRedpacketFileDetails> page = mallWxRedpacketApplyService.findPage(
						new Page<MallWxRedpacketFileDetails>(1,PAGE_SIZE), detail); 
				if(page.getList().size()>0){
					model.addAttribute("pageNo", 1);
				}
				model.addAttribute("page", page);
			}else{
				//发放红包明细
				if(StringUtils.isNotEmpty(mallWxRedpacketApply.getOperator())){
					User u = UserUtils.getByLoginName(mallWxRedpacketApply.getOperator());
					if(u!=null){
						mallWxRedpacketApply.setSendCode(u.getLoginName());
						mallWxRedpacketApply.setSendName(u.getName());
					}
				}
				MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
				detail.setApplyid(mallWxRedpacketApply.getApplyid());
				Page<MallWxRedpacketApplyDetails> page = mallWxRedpacketApplyService.findPage(
						new Page<MallWxRedpacketApplyDetails>(1,PAGE_SIZE), detail);
				if(page.getList().size()>0){
					model.addAttribute("pageNo", 1);
				}
				model.addAttribute("page", page);
			}
		}
		model.addAttribute("merchantList", merchantVoList);
		model.addAttribute("mallWxRedpacketApply", mallWxRedpacketApply);
		if(StringUtils.equals("query", type)){
			if(StringUtils.equals("3", mallWxRedpacketApply.getApplystatus())
					||StringUtils.equals("5", mallWxRedpacketApply.getApplystatus())){
				mallWxRedpacketApply.setCheckValue("0");
			}else{
				mallWxRedpacketApply.setCheckValue("1");
			}
			return "modules/hb/mallWxRedpacketApplyQuery";
		}else if(StringUtils.equals("aduit", type)){
			return "modules/hb/mallWxRedpacketApplyAduit";
		}else if(StringUtils.equals("send", type)){
			model.addAttribute("sendType", request.getParameter("sendType"));
			return "modules/hb/mallWxRedpacketApplySend";
		}
		return "modules/hb/mallWxRedpacketApplyForm";
	}
	
	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
	@RequestMapping(value = "edit")
	public String edit(MallWxRedpacketApply mallWxRedpacketApply, Model model) {
		//获取红包发放商户信息
		List<MallWxRedpacketMerchantInfo> merchantList = 
				mallWxRedpacketApplyService.findList(new MallWxRedpacketMerchantInfo());
		List<MallWxRedpacketMerchantInfoVo> merchantVoList = new ArrayList<MallWxRedpacketMerchantInfoVo>();
		for(MallWxRedpacketMerchantInfo po : merchantList){
			merchantVoList.add(new MallWxRedpacketMerchantInfoVo(po));
		}
		JSONArray jsonMerchant = JSONArray.fromObject(merchantVoList);
		mallWxRedpacketApply.setMerchantJson(jsonMerchant);
		model.addAttribute("merchantList", merchantVoList);
		//获取初始化红包信息数据
		MallWxRedpacketFileDetails detail = new MallWxRedpacketFileDetails();
		detail.setFileid(mallWxRedpacketApply.getFileid());
		Page<MallWxRedpacketFileDetails> page = mallWxRedpacketApplyService.findPage(
				new Page<MallWxRedpacketFileDetails>(1,PAGE_SIZE), detail); 
		if(page.getList().size()>0){
			model.addAttribute("pageNo", 1);
		}
		model.addAttribute("page", page);
		model.addAttribute("mallWxRedpacketApply", mallWxRedpacketApply);
		return "modules/hb/mallWxRedpacketApplyEdit";
	}
	
	@RequiresPermissions("hb:mallWxRedpacketApply:aduit")
	@RequestMapping(value = "aduit")
	public String aduit(MallWxRedpacketApply mallWxRedpacketApply, Model model) {
		if(mallWxRedpacketApply.getApplyid()==null){
			User user = UserUtils.getUser();
			mallWxRedpacketApply.setApplyercode(user.getLoginName());
			mallWxRedpacketApply.setApplyername(user.getName());
			mallWxRedpacketApply.setApplydate(new Date());
		}
		//获取红包发放商户信息
		List<MallWxRedpacketMerchantInfo> merchantList = mallWxRedpacketApplyService.findList(new MallWxRedpacketMerchantInfo());
		List<MallWxRedpacketMerchantInfoVo> merchantVoList = new ArrayList<MallWxRedpacketMerchantInfoVo>();
		for(MallWxRedpacketMerchantInfo po : merchantList){
			merchantVoList.add(new MallWxRedpacketMerchantInfoVo(po));
		}
		JSONArray jsonMerchant = JSONArray.fromObject(merchantVoList);
		mallWxRedpacketApply.setMerchantJson(jsonMerchant);
		//获取初始化红包信息数据
		if(!StringUtils.isEmpty(mallWxRedpacketApply.getFileid())){
			MallWxRedpacketFileDetails detail = new MallWxRedpacketFileDetails();
			detail.setFileid(mallWxRedpacketApply.getFileid());
			Page<MallWxRedpacketFileDetails> page = mallWxRedpacketApplyService.findPage(
					new Page<MallWxRedpacketFileDetails>(1,PAGE_SIZE), detail); 
			if(page.getList().size()>0){
				model.addAttribute("pageNo", 1);
			}
			model.addAttribute("page", page);
		}
		model.addAttribute("merchantList", merchantVoList);
		model.addAttribute("mallWxRedpacketApply", mallWxRedpacketApply);
		return "modules/hb/mallWxRedpacketApplyAduit";
	}
	
	@RequestMapping(value = "send")
	@ResponseBody
	public Map<String,Object> send(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User user = UserUtils.getUser();
		Map<String,Object> map = new HashMap<String, Object>();
		String checkValue = request.getParameter("checkValue");		//红包编号
		String applyid = request.getParameter("applyid");			//申请单号
		String mchid = request.getParameter("mchid");				//商户号
		
		MallWxRedpacketApplyDetails details = new MallWxRedpacketApplyDetails();
		details.setApplyid(applyid);
		details.setRedpacketNoStr(checkValue);
		List<MallWxRedpacketApplyDetails> detailList = mallWxRedpacketApplyService.findByRedpackno(details);
		if(detailList.size()>0){
			WxRedPacketService demoService = (WxRedPacketService) SpringContextUtils.getBean("wxRedPacketService");
			int i = 0;
			while(null==demoService){
				demoService = (WxRedPacketService) SpringContextUtils.getBean("wxRedPacketService");
				logger.info("红包发放结果 demoService:"+demoService);
				if(i++>4)break;
			}
			SendRedPackedUtil sendRed = new SendRedPackedUtil();
			JSONObject jsonObj = sendRed.sendObj(detailList, mchid);
			String sendPacketResult = demoService.send(jsonObj);
			logger.info(sendPacketResult.toString());
			JSONArray arr = JSONArray.fromObject(sendPacketResult);
			if(arr.size()>0){
				boolean flag = true;
				StringBuffer failMsg = new StringBuffer();
				for (Object obj : arr) {
					JSONObject para = (JSONObject)obj;
					String returncode= para.containsKey("returncode")?para.getString("returncode"):"FAIL";
					String result_code = para.containsKey("result_code")?para.getString("result_code"):"";
					String send_time= para.containsKey("send_time")?para.getString("send_time"):"";
					String send_listid= para.containsKey("send_listid")?para.getString("send_listid"):"";
					String mch_billno= para.containsKey("mch_billno")?para.getString("mch_billno"):"";
					String appid = para.containsKey("wxappid") ? para.getString("wxappid") : "";
					String return_msg = para.containsKey("return_msg") ? para.getString("return_msg") : "";
					
					if(returncode.equalsIgnoreCase("SUCCESS")){	//发送成功(调用微信接口)
						if("SUCCESS".equalsIgnoreCase(result_code)){	//发送成功（交易成功）
							if(flag){
								//更新红包状态
								MallWxRedpacketApply mallWxRedpacketApply = new MallWxRedpacketApply();
								mallWxRedpacketApply.setApplyid(applyid);
								mallWxRedpacketApply = mallWxRedpacketApplyService.get(mallWxRedpacketApply);
								mallWxRedpacketApply.setSendCode(user.getLoginName());
								mallWxRedpacketApply.setSendName(user.getLoginName());
								mallWxRedpacketApply.setOperator(user.getLoginName());
								mallWxRedpacketApply.setOperatortime(new Date());
								mallWxRedpacketApply.setApplystatus("5");//已发放
								mallWxRedpacketApply.setSendStatus("2");//发放中
								mallWxRedpacketApplyService.updateApply(mallWxRedpacketApply);
								flag = false;
							}
							//更新红包明细状态
							MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
							detail.setMerchantOrderNo(mch_billno);
							detail = mallWxRedpacketApplyService.findEntityByMerchantOrderNo(detail);
							detail.setSendListid(send_listid);
							//
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
							detail.setSendTime(sdf.parse(send_time));
							detail.setRedpacketStatus("2");
							mallWxRedpacketApplyService.updateAppluDetail(detail);
							//更新红包发送信息明细
							MallWxRedpacketSendinfo sendInfo = new MallWxRedpacketSendinfo();
							sendInfo.setMchBillno(mch_billno);
							sendInfo = mallWxRedpacketApplyService.getSendInfo(sendInfo);
							sendInfo.setSendTime(sdf.parse(send_time));
							sendInfo.setStatus("2");
							sendInfo.setDetailId(send_listid);
							sendInfo.setAppid(appid);
							sendInfo.setOperator(user.getName());
							sendInfo.setOperatortime(new Date());
							sendInfo.setSendType("API");
							mallWxRedpacketApplyService.updateSendInfo(sendInfo);
						}else{
							if(StringUtils.isNotEmpty(mch_billno)){	//交易失败
								MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
								detail.setMerchantOrderNo(mch_billno);
								detail = mallWxRedpacketApplyService.findEntityByMerchantOrderNo(detail);
								detail.setSendListid(send_listid);
								detail.setRedpacketStatus("1");//未发放
								mallWxRedpacketApplyService.updateAppluDetail(detail);
								//更新红包发送信息明细
								MallWxRedpacketSendinfo sendInfo = new MallWxRedpacketSendinfo();
								sendInfo.setMchBillno(mch_billno);
								sendInfo = mallWxRedpacketApplyService.getSendInfo(sendInfo);
								sendInfo.setStatus("3");//发送失败
								sendInfo.setAppid(appid);
								sendInfo.setOperator(user.getName());
								sendInfo.setOperatortime(new Date());
								sendInfo.setSendType("API");
								sendInfo.setDetailId(send_listid);
								mallWxRedpacketApplyService.updateSendInfo(sendInfo);
								if(failMsg.length()>0){
									failMsg.append(mch_billno+"发放出错了！<br/>");
								}else{
									failMsg.append("商户订单号为：");
									failMsg.append(mch_billno+"发放出错了！<br/>");
								}
							}else{
								failMsg.append(return_msg);
							}
						}
					}
				}
				if(failMsg.length()>0){
					map.put("flag", "false");
					map.put("failMsg", failMsg.toString());
				}else{
					map.put("flag", "success");
				}
			}else{
				logger.error("红包发送返回数据未空");
				map.put("flag", "false");
			}
		}else{
			map.put("flag", "false");
		}
		return map;
	}
	
	
	/**
	 * 红包锁定
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "lock")
	@ResponseBody
	public Map<String,Object> lock(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User user = UserUtils.getUser();
		Map<String,Object> map = new HashMap<String, Object>();
		String applyid = request.getParameter("applyid");			//申请单号
		MallWxRedpacketApply apply = new MallWxRedpacketApply();
		apply.setApplyid(applyid);
		apply.setSendStatus("4");
		apply.setOperator(user.getName());
		mallWxRedpacketApplyService.updateStatus(apply);
		map.put("flag", "success");
		return map;
	}

	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
	@RequestMapping(value = "save")
	public String save(MallWxRedpacketApply mallWxRedpacketApply, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, mallWxRedpacketApply)){
			return form(mallWxRedpacketApply, model ,request);
		}
		mallWxRedpacketApply.setOperator(mallWxRedpacketApply.getApplyername());
		mallWxRedpacketApply.setOperatortime(new Date());
		try {
			if(StringUtils.isEmpty(mallWxRedpacketApply.getApplyid())){
				mallWxRedpacketApplyService.save(mallWxRedpacketApply);
			}else{
				mallWxRedpacketApplyService.updateMallWxRedpacketApply(mallWxRedpacketApply);
			}
		} catch (Exception e) {
			addMessage(redirectAttributes, "红包编号重复，请检验！");
			model.addAttribute("falseMsg", "保存失败，有红包编号重复，请修改红包编号后再次导入，谢谢！");
			return form(mallWxRedpacketApply, model,request);
		}
		addMessage(redirectAttributes, "保存红包申请成功");
		return "redirect:"+Global.getAdminPath()+"/hb/mallWxRedpacketApply/?repage";
	}
	
	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
	@RequestMapping(value = "aduitSave")
	public String aduitSave(MallWxRedpacketApply mallWxRedpacketApply, RedirectAttributes redirectAttributes,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		mallWxRedpacketApply.setCheckercode(user.getLoginName());
		mallWxRedpacketApply.setCheckername(user.getName());
		mallWxRedpacketApply.setCheckdate(new Date());
		mallWxRedpacketApply.setOperator("");
		mallWxRedpacketApply.setOperatortime(null);
		mallWxRedpacketApplyService.aduitSave(mallWxRedpacketApply);
		if(mallWxRedpacketApply.getApplystatus().equals("3")){
			addMessage(redirectAttributes, "红包审核通过");
		}else{
			addMessage(redirectAttributes, "红包审核未通过，已退回");
		}
		return "redirect:"+Global.getAdminPath()+"/hb/mallWxRedpacketApply/list?type=aduit";
	}
	
	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
	@RequestMapping(value = "delete")
	public String delete(MallWxRedpacketApply mallWxRedpacketApply, RedirectAttributes redirectAttributes) {
		mallWxRedpacketApplyService.delete(mallWxRedpacketApply);
		addMessage(redirectAttributes, "删除红包申请成功");
		return "redirect:"+Global.getAdminPath()+"/hb/mallWxRedpacketApply/?repage";
	}
	
	/**
	 * 获取更多红包明细
	 */
	@RequestMapping(value = "getMoreDetail")
	@ResponseBody
	public Map<String,Object> getMoreDetail(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		int pageNo = 1;
		String pageNoStr = request.getParameter("pageNo");
		String fileid = request.getParameter("fileid");
		String type = request.getParameter("type");
		if(!StringUtils.isEmpty(pageNoStr)){
			pageNo = Integer.valueOf(pageNoStr);
		}
		if(StringUtils.equals("send", type)){
			String applyid = request.getParameter("applyid");
			//查找文件数据
			MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
			detail.setApplyid(applyid);
			Page<MallWxRedpacketApplyDetails> page = mallWxRedpacketApplyService.findPage(
					new Page<MallWxRedpacketApplyDetails>(pageNo+1,PAGE_SIZE), detail); 
			List<MallWxRedpacketApplyDetails> list = page.getList();
			List<MallWxRedpacketApplyDetailsVo> listVo = new ArrayList<MallWxRedpacketApplyDetailsVo>();
			if(list.size()>0){
				for (MallWxRedpacketApplyDetails po : list) {
					if(po!=null){
						listVo.add(new MallWxRedpacketApplyDetailsVo(po));
					}
				}
				JSONArray jsonMerchant = JSONArray.fromObject(listVo);
				map.put("flag", "success");
				map.put("pageNo", pageNo+1);
				map.put("json", jsonMerchant);
			}else{
				map.put("flag", "false");
			}
		}else{
			//查找文件数据
			MallWxRedpacketFileDetails detail = new MallWxRedpacketFileDetails();
			detail.setFileid(fileid);
			Page<MallWxRedpacketFileDetails> page = mallWxRedpacketApplyService.findPage(
					new Page<MallWxRedpacketFileDetails>(pageNo+1,PAGE_SIZE), detail); 
			List<MallWxRedpacketFileDetails> list = page.getList();
			List<MallWxRedpacketFileDetailsVo> listVo = new ArrayList<MallWxRedpacketFileDetailsVo>();
			if(list.size()>0){
				for (MallWxRedpacketFileDetails po : list) {
					listVo.add(new MallWxRedpacketFileDetailsVo(po));
				}
				JSONArray jsonMerchant = JSONArray.fromObject(listVo);
				map.put("flag", "success");
				map.put("pageNo", pageNo+1);
				map.put("json", jsonMerchant);
			}else{
				map.put("flag", "false");
			}
		}
		return map;
	}
	
	/**
	 * 清空红包数据
	 */
	@RequestMapping(value = "clearFileData")
	@ResponseBody
	public Map<String,Object> clearFileData(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		String fileid = request.getParameter("fileid");
		String applyid = request.getParameter("applyid");
		//查找文件数据
		MallWxRedpacketFileDetails fileDetail = new MallWxRedpacketFileDetails();
		fileDetail.setFileid(fileid);
		mallWxRedpacketApplyService.delFileDetailByFileid(fileDetail);
		MallWxRedpacketApplyDetails applyDetail = new MallWxRedpacketApplyDetails();
		applyDetail.setApplyid(applyid);
		mallWxRedpacketApplyService.delFileDetailByApplyid(applyDetail);
		MallWxRedpacketApply apply = new MallWxRedpacketApply();
		apply.setApplyid(applyid);
		apply = mallWxRedpacketApplyService.get(apply);
		apply.setFileid("");
		apply.setRedpacketTotalAmount("0");
		apply.setRedpacketTotalCount("0");
		mallWxRedpacketApplyService.updateApply(apply);
//		apply.setFileid(fileid);
		map.put("flag", "success");
		return map;
	}
	
	
	/**
	 * 导入用户数据
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(Model model,MultipartFile file, RedirectAttributes redirectAttributes, 
    		HttpServletRequest request, HttpServletResponse response) {
		MallWxRedpacketApply mallWxRedpacketApply = new MallWxRedpacketApply();
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:"+Global.getAdminPath()+"/mallticketsendtemp/mallTicketSendTemp/?repage";
		}
		String applyId = request.getParameter("applyidCode");
		String merchantNo = request.getParameter("merchantNoCode");
		String merchantName = request.getParameter("merchantNameCode");
		String provider = request.getParameter("providerCode");
		String webchatPublicNo = request.getParameter("webchatPublicNoCode");
		String fileid = request.getParameter("fileidCode");
		mallWxRedpacketApply.setApplyid(applyId);
		mallWxRedpacketApply.setMerchantNo(merchantNo);
		mallWxRedpacketApply.setMerchantName(merchantName);
		mallWxRedpacketApply.setProvider(provider);
		mallWxRedpacketApply.setWebchatPublicNo(webchatPublicNo);
		mallWxRedpacketApply.setFileid(fileid);
		User user = UserUtils.getUser();
		mallWxRedpacketApply.setApplyercode(user.getLoginName());
		mallWxRedpacketApply.setApplyername(user.getName());
		mallWxRedpacketApply.setApplydate(new Date());
		if(file.getSize()==0){
			model.addAttribute("failureMsg", "导入失败，文件为空！");
			addMessage(redirectAttributes, "导入用户失败！失败信息：上传文件为空！");
			return form(mallWxRedpacketApply, model,request);
		}
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 0, 0);
			//保存导入文件信息
			MallWxRedpacketExcel excelFile = new MallWxRedpacketExcel();
			excelFile.setClientfilename(file.getOriginalFilename());
			excelFile.setId(SerialNo.getUNID());
			List<MallWxRedpacketFileDetails> list = ei.getDataList(MallWxRedpacketFileDetails.class);
			mallWxRedpacketApply.setFileid(excelFile.getId());
			int orderNum = mallWxRedpacketApplyService.getMerchantOrderNo("2");
			List<MallWxRedpacketFileDetails> listNew = new ArrayList<MallWxRedpacketFileDetails>();
			int totalAmout = 0;
			String redpacketNo = "";
			for (MallWxRedpacketFileDetails detail : list){
				redpacketNo += detail.getRedpacketNo()+",";
				if(StringUtils.isEmpty(detail.getRedpacketNo())){
					continue;
				}
				String amount = detail.getRedpacketAmountFen();
				if(Integer.valueOf(amount)>=MIN_AMOUNT&&Integer.valueOf(amount)<=MAX_AMOUNT){
					if(StringUtils.equals("1", detail.getSendTotalPeople())){
						totalAmout = totalAmout+ Integer.valueOf(detail.getRedpacketAmountFen());
						String orderNo = getOrderNo(orderNum, merchantNo);
						detail.setId(SerialNo.getUNID());
						detail.setFileid(excelFile.getId());
						detail.setMerchantOrderNo(orderNo);
						detail.setRedpacketAmountYuan(
								BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString());
						detail.setRedpacketMinAmount("100");
						detail.setRedpacketMaxAmount("499900");
						orderNum++;
						listNew.add(detail);
						successNum++;
					}else{
						failureNum++;
						failureMsg.append("导入失败！<br/>红包号 "+detail.getRedpacketNo()+" 的发放人数不正确; 发放人数必须为1");
						model.addAttribute("failureMsg", failureMsg);
						throw new Exception();
					}
				}else{
					failureNum++;
					failureMsg.append("导入失败！<br/>红包号 "+detail.getRedpacketNo()+" 的红包金额不正确; 发放金额范围为"
							+ BigDecimal.valueOf(MIN_AMOUNT).divide(new BigDecimal(100)).toString()+"元~"
							+ BigDecimal.valueOf(MAX_AMOUNT).divide(new BigDecimal(100)).toString()+"元！");
					model.addAttribute("failureMsg", failureMsg);
					throw new Exception();
				}
			}
			if(redpacketNo.length()>0){
				redpacketNo = redpacketNo.substring(0, redpacketNo.length()-1);
			}
			//判断红包编号是否有重复
			boolean flag = mallWxRedpacketApplyService.isExist(redpacketNo,fileid);
			if(flag){
				failureMsg.append("导入失败！<br/>红包编号已存在，请重新填写红包编号后再次导入");
				model.addAttribute("failureMsg", failureMsg);
				throw new Exception();
			}
			excelFile = mallWxRedpacketApplyService.save(excelFile);
			mallWxRedpacketApplyService.save(listNew);
			mallWxRedpacketApply.setRedpacketTotalCount(Integer.toString(listNew.size()));
			mallWxRedpacketApply.setRedpacketTotalAmount(
					BigDecimal.valueOf(Long.valueOf(totalAmout)).divide(new BigDecimal(100)).toString());
			mallWxRedpacketApplyService.saveOrderNum(merchantNo,orderNum+1,2);
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条用户，导入信息如下：");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条用户"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入用户失败！失败信息："+e.getMessage());
			return form(mallWxRedpacketApply, model,request);
		}
		return form(mallWxRedpacketApply, model,request);
    }
	
	/**
	 * 导出模板
	 */
	@RequiresPermissions("hb:mallWxRedpacketApply:edit")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes, 
    		HttpServletRequest request) {
		try {
            String fileName = "红包明细模板.xlsx";
    		List<MallWxRedpacketFileDetails> list = Lists.newArrayList(); 
    		String agent = request.getHeader("USER-AGENT").toLowerCase();
    		new ExportExcel(null, 
    				MallWxRedpacketFileDetails.class,2).setDataList(list).write(agent,response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/hb/mallWxRedpacketApply/?repage";
    }

	//获取序列号
	private String getOrderNo(int orderNum,String merchantNo){
		String orderNo = "";
		String orderNumStr = Integer.toString(orderNum);
		while(orderNumStr.length()<10){
			orderNumStr = "0"+orderNumStr;
		}
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
		String strDate=df.format(new Date());
		orderNo = merchantNo + strDate + orderNumStr;
		return orderNo;
	}
	
	
	/**
	 * 返回时清空红包数据
	 */
	@RequestMapping(value = "rebackClearData")
	@ResponseBody
	public String rebackClearData(HttpServletRequest request, HttpServletResponse response) {
		String fileid = request.getParameter("fileid");
		String applyid = request.getParameter("applyid");
		//查找文件数据
		MallWxRedpacketApply apply = new MallWxRedpacketApply();
		apply.setApplyid(applyid);
		apply = mallWxRedpacketApplyService.get(apply);
		if(!StringUtils.equals(apply.getFileid(), fileid)){
			MallWxRedpacketFileDetails detail = new MallWxRedpacketFileDetails();
			detail.setFileid(fileid);
			mallWxRedpacketApplyService.delFileDetailByFileid(detail);
			apply.setFileid("");
			mallWxRedpacketApplyService.updateApply(apply);
			return "success";
		}else{
			return "donot";
		}
	}
	
}