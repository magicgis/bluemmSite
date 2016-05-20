package cn.com.bluemoon.jeesite.modules.item.dao;

import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallCategoryItem;

@MyBatisDao
public interface ItemCategoryDao extends CrudDao<MallCategoryItem>{
	
	public int update(MallCategoryItem mallCategoryItem);
	
	public List<MallCategoryItem> findByPosition(MallCategoryItem mallCategoryItem);
	
	public int updatePosition(MallCategoryItem mallCategoryItem);
}
