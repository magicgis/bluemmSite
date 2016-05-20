package cn.com.bluemoon.jeesite.modules.suite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetailVo;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemSuite;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetail;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetailGoodVo;
@MyBatisDao
public interface MallSuiteInfoDao extends CrudDao<MallItemSuite> {
	public List<MallSuiteDetailGoodVo> suiteProductList(String suiteId);
	
	public void saveDetails(MallSuiteDetail mallSuiteDetail);
	
	public MallItemSuite querySuiteInfoBySuiteId(String suiteId);
	
	public List<MallItemDetailVo> querySuiteDetailBySuiteId(String suiteId);
	
	public List<MallItemSuite> findSameSkuList(MallItemSuite mallItemSuite);
	
	public void deleteSuiteDetail(@Param(value="suiteId")String suiteId);
	
	public List<MallItemSuite> getItemSuiteById(@Param(value="suiteId")String suiteId);
}
