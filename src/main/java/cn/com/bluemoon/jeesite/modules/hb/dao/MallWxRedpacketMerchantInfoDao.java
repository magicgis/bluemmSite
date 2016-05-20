/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketMerchantInfo;

/**
 * 红包发放商户名称DAO接口
 * @author linyihao
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallWxRedpacketMerchantInfoDao extends CrudDao<MallWxRedpacketMerchantInfo> {
	
}