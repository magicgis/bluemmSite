/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommend.dao;

import java.util.List;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.recommend.entity.MallItemRecommend;

/**
 * 商品推荐位DAO接口
 * @author linyihao
 * @version 2016-01-15
 */
@MyBatisDao
public interface MallItemRecommendDao extends CrudDao<MallItemRecommend> {
	
	/**
	 * 查找重复排序
	 * @param mallItemInfo
	 * @return
	 */
	public List<MallItemRecommend> findByPosition(MallItemRecommend mallItemRecommend);
	/**
	 * 自动排序
	 * @param mallItemInfo
	 */
	public void updatePosition(MallItemRecommend mallItemRecommend);
	
}