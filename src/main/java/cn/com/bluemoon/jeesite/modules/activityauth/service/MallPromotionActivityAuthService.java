/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.modules.activityauth.dao.MallPromotionActivityAuthAngelDao;
import cn.com.bluemoon.jeesite.modules.activityauth.dao.MallPromotionActivityAuthDao;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuth;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuthAngel;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 活动权限表Service
 * @author linyihao
 * @version 2016-04-19
 */
@Service
@Transactional(readOnly = true)
public class MallPromotionActivityAuthService extends CrudService<MallPromotionActivityAuthDao, MallPromotionActivityAuth> {
	@Autowired
	private MallPromotionActivityAuthAngelDao angelDao;
	
	@Autowired
	private MallPromotionActivityAuthDao dao;
	public MallPromotionActivityAuth get(String id) {
		return super.get(id);
	}
	
	public List<MallPromotionActivityAuth> findList(MallPromotionActivityAuth mallPromotionActivityAuth) {
		return super.findList(mallPromotionActivityAuth);
	}
	
	public Page<MallPromotionActivityAuth> findPage(Page<MallPromotionActivityAuth> page, MallPromotionActivityAuth mallPromotionActivityAuth) {
		return super.findPage(page, mallPromotionActivityAuth);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPromotionActivityAuth mallPromotionActivityAuth) {
		User user = UserUtils.getUser();
		BigInteger id = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		Date date = new Date();
		mallPromotionActivityAuth.setAuthId(id+"");
		mallPromotionActivityAuth.setStatus(1);
		mallPromotionActivityAuth.setCreatTime(date);
		mallPromotionActivityAuth.setCreatCode(mallPromotionActivityAuth.getCreateCode());
		mallPromotionActivityAuth.setCreatBy(mallPromotionActivityAuth.getCreateName());
		mallPromotionActivityAuth.setUpdateCode(mallPromotionActivityAuth.getCreateCode());
		mallPromotionActivityAuth.setUpdatBy(mallPromotionActivityAuth.getCreateName());
		mallPromotionActivityAuth.setUpdateTime(date);
		List<MallPromotionActivityAuthAngel> au = mallPromotionActivityAuth.getAngelUserList();
		if(au != null && au.size() > 0){
			for(MallPromotionActivityAuthAngel aUser : au){
				BigInteger ids = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
				aUser.setMapId(ids+"");
				aUser.setAuthId(mallPromotionActivityAuth.getAuthId());
				aUser.setStatus(1);
				aUser.setOpCode(mallPromotionActivityAuth.getCreateCode());
				aUser.setOpTime(new Date());
				angelDao.insert(aUser);
			}
		}
		super.save(mallPromotionActivityAuth);
	}
	
	@Transactional(readOnly = false)
	public void saveAngel(MallPromotionActivityAuthAngel mallPromotionActivityAuthAngel) {
		User user = UserUtils.getUser();
		BigInteger ids = BigInteger.valueOf(Long.valueOf(SerialNo.getSerialforDB()));
		mallPromotionActivityAuthAngel.setMapId(ids+"");
		mallPromotionActivityAuthAngel.setStatus(1);
		mallPromotionActivityAuthAngel.setOpCode(user.getLoginName());
		mallPromotionActivityAuthAngel.setOpTime(new Date());
		angelDao.insert(mallPromotionActivityAuthAngel);
	}
	

	
	@Transactional(readOnly = false)
	public void delete(MallPromotionActivityAuth mallPromotionActivityAuth) {
		super.delete(mallPromotionActivityAuth);
	}
	
	@Transactional(readOnly = false)
	public void cancel(MallPromotionActivityAuth mallPromotionActivityAuth) {
		User user = UserUtils.getUser();
		mallPromotionActivityAuth.setUpdateTime(new Date());
		mallPromotionActivityAuth.setUpdatBy(user.getName());
		mallPromotionActivityAuth.setUpdateCode(user.getLoginName());
		dao.cancel(mallPromotionActivityAuth);
	}
	
	@Transactional(readOnly = false)
	public void updateUser(MallPromotionActivityAuth mallPromotionActivityAuth){
		User user = UserUtils.getUser();
		mallPromotionActivityAuth.setUpdateTime(new Date());
		mallPromotionActivityAuth.setUpdatBy(user.getName());
		mallPromotionActivityAuth.setUpdateCode(user.getLoginName());
		dao.updateUser(mallPromotionActivityAuth);
	}
	
}