package cn.com.bluemoon.jeesite.modules.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.item.dao.SuiteDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemSuite;

@Service
@Transactional(readOnly = true)
public class SuiteService extends CrudService<SuiteDao, MallItemSuite> {

	@Autowired
	private SuiteDao suiteDao;
	
	public Page<MallItemSuite> find(Page<MallItemSuite> page, MallItemSuite mallItemSuite) {
		mallItemSuite.setPage(page);
		page.setList(suiteDao.findList(mallItemSuite));
		return page;
	}
	
}
