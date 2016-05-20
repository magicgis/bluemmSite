package cn.com.bluemoon.jeesite.modules.item.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetailVo;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemInfo;

@MyBatisDao
public interface ItemDao extends CrudDao<MallItemInfo>{
	
	/**
	 * 商品主数据列表（分页）
	 * @param mallItemInfo
	 * @return
	 */
	public List<MallItemInfo> findItemPage(MallItemInfo mallItemInfo);
	
	/**
	 * 通过SKU查找单品和套餐数据
	 * @param goodSkus
	 * @return
	 */
	public List<MallItemDetail> findGoodBySku(@Param(value="goodSkus")String goodSkus);
	
	/**
	 * 删除商品(把商品状态改为0)
	 * @param itemIds
	 */
	public void delItem(@Param(value="itemId")String itemId);
	
	/**
	 * 下架商品(把商品状态改为0)
	 * @param itemIds
	 */
	public void offItem(@Param(value="itemId")String itemId);
	
	/**
	 * 通过商品ID查找子产品数据
	 * @param goodSkus
	 * @return
	 */
	public List<MallItemDetailVo> findGoodsByItemId(@Param(value="itemId")String itemId);
	
	/**
	 * 通过SKU获取商品
	 * @param itemSku
	 * @return
	 */
	public List<MallItemInfo> findItemBySku(@Param(value="itemSku")String itemSku);
	
	/**
	 * 通过ID获取商品
	 */
	public MallItemInfo getById(@Param(value="itemId")String itemId);
	
	/**
	 * 查找可选推荐商品列表
	 * @param mallItemInfo
	 * @return
	 */
	public List<MallItemInfo> findRecommendItem(MallItemInfo mallItemInfo);
	
	/**
	 * 查找重复排序
	 * @param mallItemInfo
	 * @return
	 */
	public List<MallItemInfo> findByPosition(MallItemInfo mallItemInfo);
	/**
	 * 自动排序
	 * @param mallItemInfo
	 */
	public void updatePosition(MallItemInfo mallItemInfo);
	
	/**
	 * 查找券可选范围商品列表
	 * @param mallItemInfo
	 * @return
	 */
	public List<MallItemInfo> findCouponItem(MallItemInfo mallItemInfo);
}
