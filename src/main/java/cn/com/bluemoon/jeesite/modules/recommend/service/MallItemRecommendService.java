/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.recommend.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.SerialNo;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemImage;
import cn.com.bluemoon.jeesite.modules.item.service.ItemImageService;
import cn.com.bluemoon.jeesite.modules.recommend.entity.MallItemRecommend;
import cn.com.bluemoon.jeesite.modules.recommend.dao.MallItemRecommendDao;

/**
 * 商品推荐位Service
 * @author linyihao
 * @version 2016-01-15
 */
@Service
@Transactional(readOnly = true)
public class MallItemRecommendService extends CrudService<MallItemRecommendDao, MallItemRecommend> {

	@Autowired
	private MallItemRecommendDao recommendDao;
	@Autowired
	private ItemImageService itemImageService;
	
	public MallItemRecommend get(String id) {
		return super.get(id);
	}
	
	public List<MallItemRecommend> findList(MallItemRecommend mallItemRecommend) {
		return super.findList(mallItemRecommend);
	}
	
	public Page<MallItemRecommend> findPage(Page<MallItemRecommend> page, MallItemRecommend mallItemRecommend) {
		if(mallItemRecommend.getOffRecomDate()!=null){
			mallItemRecommend.setOffRecomTime(new Date(mallItemRecommend.getOffRecomDate().getTime()));
		}
		if(mallItemRecommend.getOnRecomDate()!=null){
			mallItemRecommend.setOnRecomTime(new Date(mallItemRecommend.getOnRecomDate().getTime()));
		}
		return super.findPage(page, mallItemRecommend);
	}
	
	@Transactional(readOnly = false)
	public void save(MallItemRecommend mallItemRecommend) {
		if(mallItemRecommend.getIsNewRecord()){
			mallItemRecommend.preInsert();
			recommendDao.insert(mallItemRecommend);
			MallItemImage mallItemImage = new MallItemImage();
			mallItemImage.setId(SerialNo.getUNID());
			mallItemImage.setItemId(mallItemRecommend.getItemId());
			mallItemImage.setImgType("reco");
			mallItemImage.setItmeImgUrl(mallItemRecommend.getRePicUrl());
			mallItemImage.setStatus(1);
			mallItemImage.setPosition(1);
			mallItemImage.setIsNewRecord(true);
			itemImageService.save(mallItemImage);
		}else{
			mallItemRecommend.preUpdate();
			recommendDao.update(mallItemRecommend);		
			List<MallItemImage> listImg = itemImageService.findByWhere(mallItemRecommend.getItemId(), "reco");
			boolean existFlag = false;
			for (MallItemImage image : listImg) {
				if(StringUtils.equals(image.getItmeImgUrl(), mallItemRecommend.getRePicUrl())){
					existFlag = true;
				}else{
					image.setStatus(0);
					image.setIsNewRecord(false);
					itemImageService.save(image);
				}
			}
			if(!existFlag){
				MallItemImage mallItemImage = new MallItemImage();
				mallItemImage.setId(SerialNo.getUNID());
				mallItemImage.setItemId(mallItemRecommend.getItemId());
				mallItemImage.setImgType("reco");
				mallItemImage.setItmeImgUrl(mallItemRecommend.getRePicUrl());
				mallItemImage.setStatus(1);
				mallItemImage.setPosition(1);
				mallItemImage.setIsNewRecord(true);
				itemImageService.save(mallItemImage);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallItemRecommend mallItemRecommend) {
		super.delete(mallItemRecommend);
	}
	
	@Transactional(readOnly = false)
	public void updatePosition(MallItemRecommend mallItemRecommend){
		List<MallItemRecommend> list = recommendDao.findByPosition(mallItemRecommend);
		if(list.size()>0){
			recommendDao.updatePosition(mallItemRecommend);
		}
	}
	
}