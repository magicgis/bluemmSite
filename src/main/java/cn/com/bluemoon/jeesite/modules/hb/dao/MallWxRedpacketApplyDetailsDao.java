/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketApplyDetails;

/**
 * 红包明细DAO接口
 * 
 * @author linyihao
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallWxRedpacketApplyDetailsDao extends CrudDao<MallWxRedpacketApplyDetails> {
	public int findListNum(MallWxRedpacketApplyDetails entity);

	public void updateStatus(MallWxRedpacketApplyDetails entity);

	public List<MallWxRedpacketApplyDetails> findByRedpackno(@Param(value="applyid")String applyid,
			@Param(value="redpacketNoStr")String redpacketNoStr);
	
	public void delFileDetailByApplyid(MallWxRedpacketApplyDetails entity);

}