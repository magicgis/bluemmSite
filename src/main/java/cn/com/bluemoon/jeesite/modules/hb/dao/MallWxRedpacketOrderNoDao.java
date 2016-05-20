/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketOrderNo;

/**
 * 红包申请DAO接口
 * @author linyihao
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallWxRedpacketOrderNoDao extends CrudDao<MallWxRedpacketOrderNo> {
	
}