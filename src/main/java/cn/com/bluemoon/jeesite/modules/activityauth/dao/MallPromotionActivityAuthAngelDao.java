/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.activityauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.activityauth.entity.MallPromotionActivityAuthAngel;

/**
 * 授权天使DAO接口
 * @author linyihao
 * @version 2016-04-19
 */
@MyBatisDao
public interface MallPromotionActivityAuthAngelDao extends CrudDao<MallPromotionActivityAuthAngel> {
	public List<MallPromotionActivityAuthAngel> findListByAuthId(@Param(value="authId")String authId,@Param(value="pageNo")Integer pageNo);
	
	public void cancel(@Param(value="authId")String authId,@Param(value="angelId")String angelId,@Param(value="opCode")String opCode);
	
	public MallPromotionActivityAuthAngel getAngelByAuthIdAndAngelId(@Param(value="authId")String authId,@Param(value="angelId")String angelId);
	
	public int updateAngelStatus(MallPromotionActivityAuthAngel allPromotionActivityAuthAngel);
}