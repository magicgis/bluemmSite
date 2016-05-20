package cn.com.bluemoon.jeesite.modules.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.item.dao.ItemCategoryDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallCategoryItem;

@Service
@Transactional(readOnly = true)
public class ItemCategoryService extends CrudService<ItemCategoryDao, MallCategoryItem> {

	@Autowired
	private ItemCategoryDao categoryDao;
	
	public void save(MallCategoryItem mallCategoryItem) {
		super.save(mallCategoryItem);
	}
	
	public int update(MallCategoryItem mallCategoryItem){
		return categoryDao.update(mallCategoryItem);
	}
	
	public void updateCategoryPosition(MallCategoryItem mallCategoryItem){
		List<MallCategoryItem> list = categoryDao.findByPosition(mallCategoryItem);
		if(list.size()>0){
			categoryDao.updatePosition(mallCategoryItem);
		}
	}
}
