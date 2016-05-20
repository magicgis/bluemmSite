/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketApplyDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketApplyDetailsDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketExcelDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketFileDetailsDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketMerchantInfoDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketOrderNoDao;
import cn.com.bluemoon.jeesite.modules.hb.dao.MallWxRedpacketSendinfoDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApply;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetails;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketExcel;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketFileDetails;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketMerchantInfo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketOrderNo;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketSendinfo;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 红包申请Service
 * @author linyihao
 * @version 2016-05-03
 */
@Service
@Transactional(readOnly = true)
public class MallWxRedpacketApplyService extends CrudService<MallWxRedpacketApplyDao, MallWxRedpacketApply> {
	
	@Autowired
	private MallWxRedpacketMerchantInfoDao merchantDao;
	@Autowired
	private MallWxRedpacketFileDetailsDao detailDao;
	@Autowired
	private MallWxRedpacketOrderNoDao orderNoDao;
	@Autowired
	private MallWxRedpacketExcelDao excelDao;
	@Autowired
	private MallWxRedpacketApplyDetailsDao applyDetailsDao;
	@Autowired
	private MallWxRedpacketSendinfoDao sendInfoDao;

	public MallWxRedpacketApply get(String id) {
		return super.get(id);
	}
	
	@Transactional(readOnly = false)
	public void updateStatus(MallWxRedpacketApply mallWxRedpacketApply){
		MallWxRedpacketApply entity = dao.get(mallWxRedpacketApply);
		entity.setSendStatus(mallWxRedpacketApply.getSendStatus());
		entity.setOperator(mallWxRedpacketApply.getOperator());
		entity.setOperatortime(new Date());
		dao.update(entity);
	}
	
	public List<MallWxRedpacketApply> findList(MallWxRedpacketApply mallWxRedpacketApply) {
		return super.findList(mallWxRedpacketApply);
	}
	
	public List<MallWxRedpacketApplyDetails> findList(MallWxRedpacketApplyDetails details) {
		return applyDetailsDao.findList(details);
	}
	
	@Transactional(readOnly = false)
	public void delFileDetailByFileid(MallWxRedpacketFileDetails entity) {
		if(!StringUtils.isEmpty(entity.getFileid())){
			detailDao.delFileDetailByFileid(entity);
		}
	}
	@Transactional(readOnly = false)
	public void delFileDetailByApplyid(MallWxRedpacketApplyDetails entity) {
		if(!StringUtils.isEmpty(entity.getApplyid())){
			applyDetailsDao.delFileDetailByApplyid(entity);
		}
	}
	
	
	public Page<MallWxRedpacketApply> findPage(Page<MallWxRedpacketApply> page, MallWxRedpacketApply mallWxRedpacketApply) {
		Date date = mallWxRedpacketApply.getEndTime();
		if(date!=null){
			long newTime = date.getTime()+1000*3600*24;
			mallWxRedpacketApply.setEndTime(new Date(newTime));
		}
		Date aduitDate = mallWxRedpacketApply.getEndAduitTime();
		if(aduitDate!=null){
			long newTime = aduitDate.getTime()+1000*3600*24;
			mallWxRedpacketApply.setEndAduitTime(new Date(newTime));
		}
		mallWxRedpacketApply.setPage(page);
		page.setList(dao.findList(mallWxRedpacketApply));
		mallWxRedpacketApply.setEndTime(date);
		mallWxRedpacketApply.setEndAduitTime(aduitDate);
		return page;
	}
	
	@Transactional(readOnly = false)
	public void updateApply(MallWxRedpacketApply mallWxRedpacketApply) {
		dao.update(mallWxRedpacketApply);
	}
	
	@Transactional(readOnly = false)
	public void save(MallWxRedpacketApply mallWxRedpacketApply) {
		int applyNo = getApplyNo();
		String applyId = getApplyId(applyNo);
		//保存主表信息
		mallWxRedpacketApply.setApplyid(applyId);
		mallWxRedpacketApply.setIsdeleted("0");
		dao.insert(mallWxRedpacketApply);
		//保存红包明细
		MallWxRedpacketFileDetails details = new MallWxRedpacketFileDetails();
		details.setFileid(mallWxRedpacketApply.getFileid());
		List<MallWxRedpacketFileDetails> detailList = detailDao.findList(details);
		for (MallWxRedpacketFileDetails detail : detailList) {
			MallWxRedpacketApplyDetails temp = new MallWxRedpacketApplyDetails();
			temp.setApplyid(mallWxRedpacketApply.getApplyid());
			temp.setMerchantOrderNo(detail.getMerchantOrderNo());
			temp.setRedpacketNo(detail.getRedpacketNo());
			temp.setReferrerNo(detail.getReferrerNo());
			temp.setReferrerName(detail.getReferrerName());
			temp.setUserOpenid(detail.getUserOpenid());
			temp.setRedpacketAmountYuan(detail.getRedpacketAmountYuan());
			temp.setRedpacketAmountFen(detail.getRedpacketAmountFen());
			temp.setRedpacketMinAmount(detail.getRedpacketMinAmount());
			temp.setRedpacketMaxAmount(detail.getRedpacketMaxAmount());
			temp.setSendTotalPeople(detail.getSendTotalPeople());
			temp.setRedpacketGreetings(detail.getRedpacketGreetings());
			temp.setRedpacketStatus("1");
			temp.setHdName(detail.getHdName());
			temp.setMarker(detail.getMarker());
			temp.setIsdeleted("0");
			temp.setOperator(mallWxRedpacketApply.getOperator());
			temp.setOperatortime(new Date());
			try {
				applyDetailsDao.insert(temp);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("可能是：红包编号为："+detail.getReferrerNo()+"重复了！");
				throw e;
			}
		}
//		detailDao.delete(detailList.get(0));
	}
	@Transactional(readOnly = false)
	public void updateEntity(MallWxRedpacketApply mallWxRedpacketApply){
		dao.update(mallWxRedpacketApply);
	}
	
	@Transactional(readOnly = false)
	public void updateMallWxRedpacketApply(MallWxRedpacketApply mallWxRedpacketApply) throws Exception{
		User user = UserUtils.getUser();
		MallWxRedpacketApply oldEntity = dao.get(mallWxRedpacketApply);
		//保存主表信息
		if("2".equals(mallWxRedpacketApply.getApplystatus())){
			mallWxRedpacketApply.setApplyercode(user.getLoginName());
			mallWxRedpacketApply.setApplyername(user.getName());
			mallWxRedpacketApply.setApplydate(new Date());
		}
//		mallWxRedpacketApply.setOperator(user.getName());
//		mallWxRedpacketApply.setOperatortime(new Date());
		dao.update(mallWxRedpacketApply);
		if(!oldEntity.getFileid().equals(mallWxRedpacketApply.getFileid())){
			//删除旧的红包明细
			MallWxRedpacketApplyDetails entity = new MallWxRedpacketApplyDetails();
			entity.setApplyid(mallWxRedpacketApply.getApplyid());
			entity.setOperator(mallWxRedpacketApply.getOperator());
			entity.setOperatortime(new Date());
			entity.setIsdeleted("1");
			applyDetailsDao.delete(entity);
			//保存新的红包明细
			MallWxRedpacketFileDetails details = new MallWxRedpacketFileDetails();
			details.setFileid(mallWxRedpacketApply.getFileid());
			List<MallWxRedpacketFileDetails> detailList = detailDao.findList(details);
			for (MallWxRedpacketFileDetails detail : detailList) {
				MallWxRedpacketApplyDetails temp = new MallWxRedpacketApplyDetails();
				temp.setApplyid(mallWxRedpacketApply.getApplyid());
				temp.setMerchantOrderNo(detail.getMerchantOrderNo());
				temp.setRedpacketNo(detail.getRedpacketNo());
				temp.setReferrerNo(detail.getReferrerNo());
				temp.setReferrerName(detail.getReferrerName());
				temp.setUserOpenid(detail.getUserOpenid());
				temp.setRedpacketAmountYuan(detail.getRedpacketAmountYuan());
				temp.setRedpacketAmountFen(detail.getRedpacketAmountFen());
				temp.setRedpacketMinAmount(detail.getRedpacketMinAmount());
				temp.setRedpacketMaxAmount(detail.getRedpacketMaxAmount());
				temp.setSendTotalPeople(detail.getSendTotalPeople());
				temp.setRedpacketGreetings(detail.getRedpacketGreetings());
				temp.setRedpacketStatus("1");
				temp.setHdName(detail.getHdName());
				temp.setMarker(detail.getMarker());
				temp.setIsdeleted("0");
				temp.setOperator(mallWxRedpacketApply.getOperator());
				temp.setOperatortime(new Date());
				try {
					applyDetailsDao.insert(temp);
				} catch (Exception e) {
					logger.error("红包编号为："+detail.getReferrerNo()+"重复了，请重新创建申请或者更改红包编号！");
					throw e;
				}
			}
//			detailDao.delete(detailList.get(0));
		}else{
			if(!oldEntity.getMerchantNo().equals(mallWxRedpacketApply.getMerchantNo())){
				//获取红包明细
				MallWxRedpacketApplyDetails entity = new MallWxRedpacketApplyDetails();
				entity.setApplyid(mallWxRedpacketApply.getApplyid());
				entity.setIsdeleted("0");
				List<MallWxRedpacketApplyDetails> applyList = applyDetailsDao.findList(entity);
				MallWxRedpacketFileDetails entityDetail = new MallWxRedpacketFileDetails();
				entityDetail.setFileid(mallWxRedpacketApply.getFileid());
				//更新红包明细
				if(applyList.size()>0){
					int orderNum = getMerchantOrderNo("2");
					for (MallWxRedpacketApplyDetails mallWxRedpacketApplyDetails : applyList) {
						String orderNo = getOrderNo(orderNum, mallWxRedpacketApply.getMerchantNo());
						mallWxRedpacketApplyDetails.setMerchantOrderNo(orderNo);
						mallWxRedpacketApplyDetails.setOperator(user.getName());
						mallWxRedpacketApplyDetails.setOperatortime(new Date());
						applyDetailsDao.update(mallWxRedpacketApplyDetails);
						//更新导入文件红包明细
						entityDetail.setMerchantOrderNo(orderNo);
						entityDetail.setRedpacketNo(mallWxRedpacketApplyDetails.getRedpacketNo());
						detailDao.updateOrderNo(entityDetail);
						orderNum ++;
					}
					saveOrderNum(mallWxRedpacketApply.getMerchantNo(),orderNum,2);
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallWxRedpacketApply mallWxRedpacketApply) {
		super.delete(mallWxRedpacketApply);
	}
	
	public List<MallWxRedpacketMerchantInfo> findList(MallWxRedpacketMerchantInfo entity) {
		return merchantDao.findList(entity);
	}
	
	public Page<MallWxRedpacketFileDetails> findPage(Page<MallWxRedpacketFileDetails> page, MallWxRedpacketFileDetails entity) {
		int allNum = detailDao.findListNum(entity);
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		if((pageNo-1)*pageSize>=allNum){
			return page;
		}
		entity.setPage(page);
		page.setList(detailDao.findList(entity));
		return page;
	}
	
	public Page<MallWxRedpacketApplyDetails> findPage(Page<MallWxRedpacketApplyDetails> page, MallWxRedpacketApplyDetails entity) {
		int allNum = applyDetailsDao.findListNum(entity);
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		if((pageNo-1)*pageSize>=allNum){
			return page;
		}
		entity.setPage(page);
		page.setList(applyDetailsDao.findList(entity));
		return page;
	}
	
	
	/**
	 * 
	 * @param merchantNo
	 * @param orderNum
	 * @param type	1代表申请单，2代表红包（商户订单号）
	 */
	@Transactional(readOnly = false)
	public void saveOrderNum(String merchantNo,int orderNum,int type){
		User user = UserUtils.getUser();
		MallWxRedpacketOrderNo orderNo = new MallWxRedpacketOrderNo();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
		String strDate=df.format(new Date());
		orderNo.setApplydate(strDate);
		orderNo.setType(Integer.toString(type));
		orderNo.setIsdeleted("0");
		orderNo.setNo(Integer.toString(orderNum));
		if(orderNoDao.findList(orderNo).size()>0){
			orderNo.setOperator(user.getName());
			orderNo.setOperatortime(new Date());
			orderNoDao.update(orderNo);
		}else{
			orderNo.setOperator(user.getName());
			orderNo.setOperatortime(new Date());
			orderNo.setNo("1");
			orderNoDao.insert(orderNo);
		}
	}
	
	
	@Transactional(readOnly = false)
	public int getApplyNo(){
		int no = 1;
		MallWxRedpacketOrderNo orderNo = new MallWxRedpacketOrderNo();
		SimpleDateFormat df=new SimpleDateFormat("yyMMdd");  
		String strDate=df.format(new Date());
		orderNo.setApplydate(strDate);
		orderNo.setType("1");
		orderNo.setIsdeleted("0");
		List<MallWxRedpacketOrderNo> list = orderNoDao.findList(orderNo);
		User user = UserUtils.getUser();
		if(list.size()>0){
			orderNo = list.get(0);
			no = Integer.valueOf(orderNo.getNo())+1;
			orderNo.setNo(Integer.toString(no));
			orderNo.setOperator(user.getName());
			orderNo.setOperatortime(new Date());
			orderNoDao.update(orderNo);
		}else{
			orderNo.setOperator(user.getName());
			orderNo.setOperatortime(new Date());
			orderNo.setNo("1");
			orderNoDao.insert(orderNo);
		}
		return no;
	}
	
	private String getApplyId(int applyNo){
		String orderNo = "";
		String orderNumStr = Integer.toString(applyNo);
		while(orderNumStr.length()<5){
			orderNumStr = "0"+orderNumStr;
		}
		SimpleDateFormat df=new SimpleDateFormat("yyMMdd");  
		String strDate=df.format(new Date());
		orderNo = "HB" + strDate + orderNumStr;
		return orderNo;
	}
	
	@Transactional(readOnly = false)
	public void save(String merchantNo,int no) {
		MallWxRedpacketOrderNo orderNo = new MallWxRedpacketOrderNo();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
		String strDate=df.format(new Date());
		orderNo.setApplydate(strDate);
		orderNo.setType("2");
		orderNo.setIsdeleted("0");
		orderNo.setNo(Integer.toString(no));
		User user = UserUtils.getUser();
		orderNo.setOperator(user.getName());
		orderNo.setOperatortime(new Date());
		orderNoDao.update(orderNo);
	}
	
	@Transactional(readOnly = false)
	public void save(List<MallWxRedpacketFileDetails> list) {
		for (MallWxRedpacketFileDetails entity : list) {
			detailDao.insert(entity);
		}
	}
	
	@Transactional(readOnly = false)
	public MallWxRedpacketExcel save(MallWxRedpacketExcel entity) {
		User user = UserUtils.getUser();
		entity.setIsdeleted("0");
		entity.setUploaduser(user.getName());
		entity.setUploadtime(new Date());
		excelDao.insert(entity);
		return entity;
	}
	
	//获取红包序列号
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
	
	
	//获取商户订单号序号
	@Transactional(readOnly = false)
	public int getMerchantOrderNo(String type){
		int no = 1;
		MallWxRedpacketOrderNo orderNo = new MallWxRedpacketOrderNo();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");  
		String strDate=df.format(new Date());
		orderNo.setApplydate(strDate);
		orderNo.setType(type);
		orderNo.setIsdeleted("0");
		List<MallWxRedpacketOrderNo> list = orderNoDao.findList(orderNo);
		if(list.size()>0){
			orderNo = list.get(0);
			no = Integer.valueOf(orderNo.getNo());
		}else{
			User user = UserUtils.getUser();
			orderNo.setOperator(user.getName());
			orderNo.setOperatortime(new Date());
			orderNo.setNo("1");
			orderNoDao.insert(orderNo);
		}
		return no;
	}
	
	
	@Transactional(readOnly = false)
	public void aduitSave(MallWxRedpacketApply mallWxRedpacketApply) {
		//保存主表信息
		if(StringUtils.equals(mallWxRedpacketApply.getApplystatus(), "3")){
			mallWxRedpacketApply.setSendStatus("1");
			MallWxRedpacketApplyDetails detail = new MallWxRedpacketApplyDetails();
			detail.setApplyid(mallWxRedpacketApply.getApplyid());
			detail.setRedpacketStatus("1");
			applyDetailsDao.updateStatus(detail);
			List<MallWxRedpacketApplyDetails> list = applyDetailsDao.findList(detail);
			for(MallWxRedpacketApplyDetails detailTemp : list){
				MallWxRedpacketSendinfo sendInfo = new MallWxRedpacketSendinfo(detailTemp,mallWxRedpacketApply);
				sendInfoDao.insert(sendInfo);
			}
		}
		dao.aduitSave(mallWxRedpacketApply);
	}
	
	public List<MallWxRedpacketSendinfo> findByRedpackno(MallWxRedpacketSendinfo entity){
		return sendInfoDao.findByRedpackno(entity);
	}
	
	public List<MallWxRedpacketApplyDetails> findByRedpackno(MallWxRedpacketApplyDetails entity){
		return applyDetailsDao.findByRedpackno(entity.getApplyid(),entity.getRedpacketNoStr());
	}
	
	public MallWxRedpacketApplyDetails findByMerchantOrderNo(MallWxRedpacketApplyDetails entity){
		List<MallWxRedpacketApplyDetails> details = applyDetailsDao.findByRedpackno(entity.getApplyid(),entity.getRedpacketNoStr());
		if(details.size()>0){
			return details.get(0);
		}else{
			return null;
		}
	}
	
	public MallWxRedpacketApplyDetails findEntityByMerchantOrderNo(MallWxRedpacketApplyDetails entity){
		List<MallWxRedpacketApplyDetails> details = applyDetailsDao.findList(entity);
		if(details.size()>0){
			return details.get(0);
		}else{
			return null;
		}
	}
	
	@Transactional(readOnly = false)
	public void updateAppluDetail(MallWxRedpacketApplyDetails entity){
		applyDetailsDao.update(entity);
	}
	
	public MallWxRedpacketSendinfo getSendInfo(MallWxRedpacketSendinfo entity){
		return sendInfoDao.get(entity);
	}
	
	//发送完成后修改红包发送信息
	@Transactional(readOnly = false)
	public void updateSendInfo(MallWxRedpacketSendinfo entity){
		sendInfoDao.update(entity);
	}
	
	public boolean isExist(String redpacketNo,String fileid){
		int num = detailDao.isExist(redpacketNo,fileid);
		if(num>0){
			return true;
		}else{
			return false;
		}
	}
	
}