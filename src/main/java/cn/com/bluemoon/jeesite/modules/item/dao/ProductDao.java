package cn.com.bluemoon.jeesite.modules.item.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallProductInfo;

@MyBatisDao
public interface ProductDao extends CrudDao<MallProductInfo>{
	
	public List<MallProductInfo> getById(@Param(value="productId")String productId);
}
