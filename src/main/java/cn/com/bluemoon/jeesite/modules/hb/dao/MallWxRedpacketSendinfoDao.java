/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.dao;

import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketSendinfo;

/**
 * 红包发送详情DAO接口
 * @author linyihao
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallWxRedpacketSendinfoDao extends CrudDao<MallWxRedpacketSendinfo> {
	public List<MallWxRedpacketSendinfo> findByRedpackno(MallWxRedpacketSendinfo entity);
}