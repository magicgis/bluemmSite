/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.DateUtils;
import cn.com.bluemoon.jeesite.modules.sys.dao.MallSysLogInfoDao;
import cn.com.bluemoon.jeesite.modules.sys.entity.MallSysLogInfo;

/**
 * 日志Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class MallSysLogInfoService extends CrudService<MallSysLogInfoDao, MallSysLogInfo> {

	
	
	public Page<MallSysLogInfo> findGatewayPage(Page<MallSysLogInfo> page, MallSysLogInfo mallSysLogInfo) {
		
		// 设置默认时间范围，默认当前月
		if (mallSysLogInfo.getBeginOperTime() == null){
			mallSysLogInfo.setBeginOperTime(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (mallSysLogInfo.getEndOperTime() == null){
			mallSysLogInfo.setEndOperTime(DateUtils.addMonths(mallSysLogInfo.getBeginOperTime(), 1));
		}
		
		Page<MallSysLogInfo> ss=super.findPage(page, mallSysLogInfo);
		
		return ss;
		
	}
	
}
