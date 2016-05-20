package cn.com.bluemoon.jeesite.modules.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.StringUtils;
import cn.com.bluemoon.jeesite.modules.item.dao.ItemDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetailVo;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemImage;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;
import cn.com.bluemoon.jeesite.modules.parameter.service.MallSysParameterService;

@Service
@Transactional(readOnly = true)
public class ItemService extends CrudService<ItemDao, MallItemInfo> {

	@Autowired
	private ItemDao itemDao;
	@Autowired
	private MallSysParameterService mallSysParameterService;
	
	public Page<MallItemInfo> find(Page<MallItemInfo> page, MallItemInfo mallItemInfo) {
		mallItemInfo.setPage(page);
		page.setList(dao.findList(mallItemInfo));
		return page;
	}
	
	
	public Page<MallItemInfo> findRecommendItem(Page<MallItemInfo> page, MallItemInfo mallItemInfo) {
		mallItemInfo.setPage(page);
		page.setList(itemDao.findRecommendItem(mallItemInfo));
		return page;
	}
	
	public Page<MallItemInfo> findCouponItem(Page<MallItemInfo> page, MallItemInfo mallItemInfo) {
		mallItemInfo.setPage(page);
		page.setList(itemDao.findCouponItem(mallItemInfo));
		return page;
	}
	
	/**
	 * 通过SKU字符串查找对应产品
	 * @param goodSkus
	 * @return
	 */
	public List<MallItemDetail> findItemDetailByGoodSkus(String goodSkus) {
		return itemDao.findGoodBySku(goodSkus);
	}
	
	/**
	 * 通过商品ID查找对应产品
	 * @param goodSkus
	 * @return
	 */
	public List<MallItemDetailVo> findGoodsByItemId(String itemId){
		return itemDao.findGoodsByItemId(itemId);
	}
	
	/**
	 * 保存商品
	 */
	public void save(MallItemInfo mallItemInfo){
		//特殊字符转义
		mallItemInfo.setItemName(StringUtils.changeHtmlStr(mallItemInfo.getItemName()));
		mallItemInfo.setItemDesc(StringUtils.changeHtmlStr(mallItemInfo.getItemDesc()));
		mallItemInfo.setSellPoint(StringUtils.changeHtmlStr(mallItemInfo.getSellPoint()));
		if(mallItemInfo.getIsNewRecord()){
			mallItemInfo.preInsert();
			itemDao.insert(mallItemInfo);
		}else{
			mallItemInfo.preUpdate();
			itemDao.update(mallItemInfo);		
		}
	};
	
	/**
	 * 删除商品(把商品状态改为0)
	 * @param itemIds
	 */
	public void delItem(String itemId){
		itemDao.delItem(itemId);
	}
	
	/**
	 * 下架商品(把商品状态改为0)
	 * @param itemIds
	 */
	public void offItem(String itemId){
		itemDao.offItem(itemId);
	}
	
	public List<MallItemInfo> findItemBySku(String itemSku){
		if(StringUtils.isNotBlank(itemSku)){
			return itemDao.findItemBySku(itemSku);
		}
		return null;
	}
	public List<MallItemDetail> findGoodBySku(String itemSku){
		if(StringUtils.isNotBlank(itemSku)){
			return itemDao.findGoodBySku(itemSku);
		}
		return null;
	}
	
	public MallItemInfo getImage(List<MallItemImage> listImage,MallItemInfo mallItemInfo){
		String ftpUrl = mallSysParameterService.getSysParameterByTypeAndCode("FTP", "FTP_OUT_URL");
		if(listImage.size()>0){
			String main = "";
			String intro = "";
			String artMain = "";
			for (MallItemImage image : listImage) {
				if(StringUtils.equals("main", image.getImgType())){
					main=ftpUrl+image.getItmeImgUrl();
				}else if(StringUtils.equals("intro", image.getImgType())){
					intro+="|"+ftpUrl+image.getItmeImgUrl();
				}else if(StringUtils.equals("artMain", image.getImgType())){
					artMain+="|"+ftpUrl+image.getItmeImgUrl();
				}else{
					continue;
				}
			}
			mallItemInfo.setMainPicUrl(main);
			mallItemInfo.setIntroPicUrl(intro);
			mallItemInfo.setArtMainPicUrl(artMain);
		}
		return mallItemInfo;
	}
	
	public MallItemInfo getById(String itemId){
		if(StringUtils.isNotBlank(itemId)){
			return itemDao.getById(itemId);
		}
		return null;
	}
	
	public void updatePosition(MallItemInfo mallItemInfo){
		List<MallItemInfo> list = itemDao.findByPosition(mallItemInfo);
		if(list.size()>0){
			itemDao.updatePosition(mallItemInfo);
		}
	}
}
