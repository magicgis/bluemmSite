package cn.com.bluemoon.jeesite.modules.item.dao;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;

@MyBatisDao
public interface ItemDetailDao extends CrudDao<MallItemDetail>{
	
	public int delete(MallItemDetail mallItemDetail);
}
