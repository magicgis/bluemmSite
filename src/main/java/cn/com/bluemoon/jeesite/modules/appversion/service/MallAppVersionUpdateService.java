/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.appversion.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.appversion.entity.MallAppVersionUpdate;
import cn.com.bluemoon.jeesite.modules.appversion.dao.MallAppVersionUpdateDao;

/**
 * APP版本信息管理Service
 * @author linyihao
 * @version 2016-01-20
 */
@Service
@Transactional(readOnly = true)
public class MallAppVersionUpdateService extends CrudService<MallAppVersionUpdateDao, MallAppVersionUpdate> {

	public MallAppVersionUpdate get(String id) {
		return super.get(id);
	}
	
	public List<MallAppVersionUpdate> findList(MallAppVersionUpdate mallAppVersionUpdate) {
		return super.findList(mallAppVersionUpdate);
	}
	
	public Page<MallAppVersionUpdate> findPage(Page<MallAppVersionUpdate> page, MallAppVersionUpdate mallAppVersionUpdate) {
		return super.findPage(page, mallAppVersionUpdate);
	}
	
	@Transactional(readOnly = false)
	public void save(MallAppVersionUpdate mallAppVersionUpdate) {
		mallAppVersionUpdate.setAppType("moonMall");
		super.save(mallAppVersionUpdate);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallAppVersionUpdate mallAppVersionUpdate) {
		super.delete(mallAppVersionUpdate);
	}
	
}