/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommendinfo.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.dao.MallRecommendInfoDao;
import cn.com.bluemoon.jeesite.modules.recommendinfo.dao.MallRecommendOperInfoDao;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallOrderInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendInfo;
import cn.com.bluemoon.jeesite.modules.recommendinfo.entity.MallRecommendOperInfo;

/**
 * 推荐码申请审核Service
 * @author xgb
 * @version 2016-05-03
 */
@Service
@Transactional(readOnly = true)
public class MallRecommendInfoService extends CrudService<MallRecommendInfoDao, MallRecommendInfo> {

	@Autowired
	private MallRecommendOperInfoDao operDao;
	
	public MallRecommendInfo get(String id) {
		return super.get(id);
	}
	
	public List<MallRecommendInfo> findList(MallRecommendInfo mallRecommendInfo) {
		return super.findList(mallRecommendInfo);
	}
	
	public Page<MallRecommendInfo> findPage(Page<MallRecommendInfo> page, MallRecommendInfo mallRecommendInfo) {
		return super.findPage(page, mallRecommendInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(MallRecommendInfo mallRecommendInfo) {
		MallRecommendOperInfo mro = new MallRecommendOperInfo();
		BigInteger id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		mallRecommendInfo.setId(id+"");
		dao.insert(mallRecommendInfo);
		mro.setOldRecommend(mallRecommendInfo.getOldRecommend());
		mro.setRecommendInfoId(mallRecommendInfo.getId());
		mro.setNewRecommend(mallRecommendInfo.getNewRecommend());
		mro.setUpdatBy(mallRecommendInfo.getUpdatBy());
		mro.setUpdateTime(new Date());
		mro.setPictureFirst(mallRecommendInfo.getPictureFirst());
		mro.setPictureSecond(mallRecommendInfo.getPictureSecond());
		mro.setAduitStatus("0");
		id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		mro.setId(id+"");
		operDao.insert(mro);
	}
	
	@Transactional(readOnly = false)
	public void update(MallRecommendInfo mallRecommendInfo) {
		MallRecommendOperInfo mro = new MallRecommendOperInfo();
		BigInteger id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		mro.setOldRecommend(mallRecommendInfo.getOldRecommend());
		mro.setRecommendInfoId(mallRecommendInfo.getId());
		mro.setNewRecommend(mallRecommendInfo.getNewRecommend());
		mro.setUpdatBy(mallRecommendInfo.getUpdatBy());
		mro.setUpdateTime(new Date());
		mro.setAduitStatus("0");
		mro.setPictureFirst(mallRecommendInfo.getPictureFirst());
		mro.setPictureSecond(mallRecommendInfo.getPictureSecond());
		mro.setId(id+"");
		operDao.insert(mro);
	}
	
	@Transactional(readOnly = false)
	public void checkUpdate(MallRecommendInfo mallRecommendInfo) {
		MallRecommendOperInfo mro = new MallRecommendOperInfo();
		BigInteger id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		mro.setOldRecommend(mallRecommendInfo.getOldRecommend());
		mro.setRecommendInfoId(mallRecommendInfo.getId());
		mro.setNewRecommend(mallRecommendInfo.getNewRecommend());
		mro.setUpdatBy(mallRecommendInfo.getUpdatBy());
		mro.setUpdateTime(mallRecommendInfo.getUpdateTime());
		mro.setPictureFirst(mallRecommendInfo.getPictureFirst());
		mro.setPictureSecond(mallRecommendInfo.getPictureSecond());
		mro.setAduitStatus(mallRecommendInfo.getAduitStatus());
		mro.setAduitBy(mallRecommendInfo.getAduitBy());
		mro.setAduitReason(mallRecommendInfo.getAduitReason());
		mro.setAduitTime(new Date());
		mro.setId(id+"");
		operDao.insert(mro);
		try {
			if("1".equals(mro.getAduitStatus())){
				MallOrderInfo order = this.getOrderByOrderCode(mallRecommendInfo.getOrderCode());
				dao.updateOrderRecomment(mallRecommendInfo.getOrderCode(),mallRecommendInfo.getNewRecommend());
				if(order.getParentCode() != null && !"".equals(order.getParentCode())){
					order = this.getOrderByOrderCode(order.getParentCode());
					dao.updateOrderRecomment(order.getOuterCode(),mallRecommendInfo.getNewRecommend());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(MallRecommendInfo mallRecommendInfo) {
		super.delete(mallRecommendInfo);
	}
	
	public MallOrderInfo getOrderByOrderCode(String orderCode) throws Exception{
		MallOrderInfo order = dao.getOrderByOrderCode(orderCode);
		return order;
	}
	
	public MallRecommendOperInfo getOperInfoByReId(String reId,String haveRecommend,String oldRecommend,String status) throws Exception{
		MallRecommendOperInfo oper = dao.getOperInfoByReId(reId,haveRecommend,oldRecommend,status);
		return oper;
	}
	
	public List<MallRecommendOperInfo> getOperInfoById(String id)throws Exception{
		List<MallRecommendOperInfo> list = dao.getOperInfoById(id);
		return list;
	}
	
	public MallRecommendInfo getRecommendByOrderCode(String orderCode) throws Exception{
		if(orderCode.indexOf("S") != -1){
			orderCode = orderCode.substring(0,orderCode.indexOf("S"));
		}
		MallRecommendInfo info = dao.getRecommendByOrderCode(orderCode);
		return info;
	}
}