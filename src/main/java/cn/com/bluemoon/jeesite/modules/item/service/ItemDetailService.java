package cn.com.bluemoon.jeesite.modules.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.item.dao.ItemDetailDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;

@Service
@Transactional(readOnly = true)
public class ItemDetailService extends CrudService<ItemDetailDao, MallItemDetail> {

	@Autowired
	private ItemDetailDao itemDetailDao;
	
	public void save(MallItemDetail mallItemDetail) {
		super.save(mallItemDetail);
	}
	
	public void delete(MallItemDetail mallItemDetail){
		itemDetailDao.delete(mallItemDetail);
	}
	
}
