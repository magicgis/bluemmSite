/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.pay.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.pay.entity.MallPaySearchRecord;
import cn.com.bluemoon.jeesite.modules.pay.dao.MallPaySearchRecordDao;

/**
 * 第三方同步记录Service
 * @author liao
 * @version 2016-03-07
 */
@Service
@Transactional(readOnly = true)
public class MallPaySearchRecordService extends CrudService<MallPaySearchRecordDao, MallPaySearchRecord> {

	public MallPaySearchRecord get(String id) {
		return super.get(id);
	}
	
	public List<MallPaySearchRecord> findList(MallPaySearchRecord mallPaySearchRecord) {
		return super.findList(mallPaySearchRecord);
	}
	
	public Page<MallPaySearchRecord> findPage(Page<MallPaySearchRecord> page, MallPaySearchRecord mallPaySearchRecord) {
		return super.findPage(page, mallPaySearchRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(MallPaySearchRecord mallPaySearchRecord) {
		super.save(mallPaySearchRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallPaySearchRecord mallPaySearchRecord) {
		super.delete(mallPaySearchRecord);
	}
	
}