package cn.com.bluemoon.jeesite.modules.item.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.ItemRecommend;

@MyBatisDao
public interface ItemRecommendDao extends CrudDao<ItemRecommend>{
	
	public List<ItemRecommend> findByItemId(@Param(value="itemId")String itemId);
	
	/**
	 * 查找重复排序
	 * @param mallItemInfo
	 * @return
	 */
	public List<ItemRecommend> findByPosition(ItemRecommend itemRecommend);
	/**
	 * 自动排序
	 * @param mallItemInfo
	 */
	public void updatePosition(ItemRecommend itemRecommend);
}
