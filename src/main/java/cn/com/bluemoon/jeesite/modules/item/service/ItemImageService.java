package cn.com.bluemoon.jeesite.modules.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.item.dao.ItemImageDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemImage;


@Service
@Transactional(readOnly = true)
public class ItemImageService extends CrudService<ItemImageDao, MallItemImage> {

	@Autowired
	private ItemImageDao itemImageDao;
	
	public void save(MallItemImage mallItemImage) {
		super.save(mallItemImage);
	}
	
	public List<MallItemImage> findByItemId(String itemId){
		if(StringUtils.isBlank(itemId)){
			return null;
		}
		return itemImageDao.findByItemId(itemId);
	}
	
	public List<MallItemImage> findByWhere(String itemId,String imgType){
		if(StringUtils.isBlank(itemId)){
			return null;
		}
		return itemImageDao.findByWhere(itemId,imgType);
	}
}
