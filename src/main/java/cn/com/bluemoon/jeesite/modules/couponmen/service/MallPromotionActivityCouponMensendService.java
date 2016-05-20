/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.couponmen.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.couponmen.dao.MallPromotionActivityCouponMensendDao;
import cn.com.bluemoon.jeesite.modules.couponmen.dao.MallPromotionActivityCouponMensendDetailDao;
import cn.com.bluemoon.jeesite.modules.couponmen.entity.MallPromotionActivityCouponMensend;
import cn.com.bluemoon.jeesite.modules.couponmen.entity.MallPromotionActivityCouponMensendDetail;

/**
 * 人工派券Service
 * @author linyihao
 * @version 2016-04-19
 */
@Service
@Transactional(readOnly = true)
public class MallPromotionActivityCouponMensendService extends CrudService<MallPromotionActivityCouponMensendDao, MallPromotionActivityCouponMensend> {

	@Autowired
	private MallPromotionActivityCouponMensendDetailDao detailDao;
	
	public MallPromotionActivityCouponMensend get(String id) {
		return super.get(id);
	}
	
	public List<MallPromotionActivityCouponMensend> findList(MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend) {
		return super.findList(mallPromotionActivityCouponMensend);
	}
	
	public Page<MallPromotionActivityCouponMensend> findPage(Page<MallPromotionActivityCouponMensend> page, MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend) {
		return super.findPage(page, mallPromotionActivityCouponMensend);
	}
	
	public Page<MallPromotionActivityCouponMensend> findExcelPage(Page<MallPromotionActivityCouponMensend> page, MallPromotionActivityCouponMensend entity) {
		entity.setPage(page);
		page.setList(dao.findExcelList(entity));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(MallPromotionActivityCouponMensend entity) {
		String userMobile = entity.getUserMobile();
		List<MallPromotionActivityCouponMensendDetail> list = entity.getListDetail();
		String[] arr = userMobile.split(";");
		if(arr.length>0&&list.size()>0){
			for(int i=0;i<arr.length;i++){
				entity.setMensendId(SerialNo.getSerialforDB());
				entity.setUserMobile(arr[i]);
				entity.setIsSend("2");
				entity.setCreatTime(new Date());
				dao.insert(entity);
				for (MallPromotionActivityCouponMensendDetail detail : list) {
					detail.setDetailId(SerialNo.getSerialforDB());
					detail.setMensendId(entity.getMensendId());
					detailDao.insert(detail);
				}
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPromotionActivityCouponMensend mallPromotionActivityCouponMensend) {
		super.delete(mallPromotionActivityCouponMensend);
	}
	
//	public List<MallPromotionActivityCouponMensendDetail> getDetail(MallPromotionActivityCouponMensendDetail entity){
//		return detailDao.findList(entity);
//	}
}