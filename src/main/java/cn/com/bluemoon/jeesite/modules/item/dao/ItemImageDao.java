package cn.com.bluemoon.jeesite.modules.item.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemImage;

@MyBatisDao
public interface ItemImageDao extends CrudDao<MallItemImage>{
	
	public List<MallItemImage> findByItemId(@Param(value="itemId")String itemId);
	
	public List<MallItemImage> findByWhere(@Param(value="itemId")String itemId,@Param(value="imgType")String imgType);
}
