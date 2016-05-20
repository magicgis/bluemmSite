package cn.com.bluemoon.jeesite.modules.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.item.dao.ItemRecommendDao;
import cn.com.bluemoon.jeesite.modules.item.entity.ItemRecommend;


@Service
@Transactional(readOnly = true)
public class ItemRecommendService extends CrudService<ItemRecommendDao, ItemRecommend> {

	@Autowired
	private ItemRecommendDao itemRecommendDao;
	
	public List<ItemRecommend> findByItemId(String itemId){
		if(StringUtils.isBlank(itemId)){
			return null;
		}
		return itemRecommendDao.findByItemId(itemId);
	}
	
	public void updatePosition(ItemRecommend itemRecommend){
		List<ItemRecommend> list = itemRecommendDao.findByPosition(itemRecommend);
		if(list.size()>0){
			itemRecommendDao.updatePosition(itemRecommend);
		}
	}
}
