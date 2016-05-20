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
public class MallPromotionActivityAuthAngelService extends CrudService<MallPromotionActivityAuthAngelDao, MallPromotionActivityAuthAngel> {
	@Autowired
	private MallPromotionActivityAuthAngelDao angelDao;
	
	public List<MallPromotionActivityAuthAngel> findList(MallPromotionActivityAuthAngel mallPromotionActivityAuthAngel) {
		return super.findList(mallPromotionActivityAuthAngel);
	}
	
	public List<MallPromotionActivityAuthAngel> findListByAuthId(String authId,Integer pageNO) {
		return angelDao.findListByAuthId(authId,pageNO);
	}
	
	public MallPromotionActivityAuthAngel getAngelByAuthIdAndAngelId(String authId,String angelId){
		return angelDao.getAngelByAuthIdAndAngelId(authId,angelId);
	}
	
	@Transactional(readOnly = false)
	public int deleteAngel(String authId,String angelId) {
		User user = UserUtils.getUser();
		MallPromotionActivityAuthAngel mallPromotionActivityAuthAngel = new MallPromotionActivityAuthAngel();
		mallPromotionActivityAuthAngel.setStatus(2);
		mallPromotionActivityAuthAngel.setOpCode(user.getLoginName());
		mallPromotionActivityAuthAngel.setOpTime(new Date());
		mallPromotionActivityAuthAngel.setAngelId(angelId);
		mallPromotionActivityAuthAngel.setAuthId(authId);
		return angelDao.updateAngelStatus(mallPromotionActivityAuthAngel);
	}
	
	public void cancel(String authId,String angelId,String opCode){
		angelDao.cancel(authId,angelId,opCode);
	}
	
	@Transactional(readOnly = false)
	public void saveOrUpdate(List<MallPromotionActivityAuthAngel> list){
		for(int i=0;i<list.size();i++){
			MallPromotionActivityAuthAngel ma = list.get(i);
			if(2 == ma.getStatus()){
				this.cancel(ma.getAuthId(), ma.getAngelId(),ma.getOpCode());
			}else if(1 == ma.getStatus()){
				dao.insert(ma);
			}
		}
		
	}
	public int insert(MallPromotionActivityAuthAngel entity){
		return angelDao.insert(entity);
	}
	
}