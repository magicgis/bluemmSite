package cn.com.bluemoon.jeesite.modules.suite.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetail;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemDetailVo;
import cn.com.bluemoon.jeesite.modules.item.entity.MallItemSuite;
import cn.com.bluemoon.jeesite.modules.suite.dao.MallSuiteInfoDao;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetail;
import cn.com.bluemoon.jeesite.modules.suite.entity.MallSuiteDetailGoodVo;
@Service
public class SuiteInfoService extends CrudService<MallSuiteInfoDao, MallItemSuite> {
	@Autowired MallSuiteInfoDao mallSuiteInfoDao;
	
	public List<MallSuiteDetailGoodVo> searchSuiteProductList(String suiteId){
		List<MallSuiteDetailGoodVo> list= new ArrayList<MallSuiteDetailGoodVo>();
		list=mallSuiteInfoDao.suiteProductList(suiteId);
		return list;
	}
	public void saveDetails(MallSuiteDetail mallSuiteDetail){
		mallSuiteInfoDao.saveDetails(mallSuiteDetail);
	}
	
	public MallItemSuite querySuiteInfoBySuiteId(String suiteId){
		return  mallSuiteInfoDao.querySuiteInfoBySuiteId(suiteId);
	}

	public List<MallItemDetailVo> querySuiteDetailBySuiteId(String suiteId){
		return  mallSuiteInfoDao.querySuiteDetailBySuiteId(suiteId);
	}
	
	public List<MallItemSuite> findSameSkuList(MallItemSuite mallItemSuite){
		return mallSuiteInfoDao.findSameSkuList(mallItemSuite);
	}
	
	public MallItemSuite findById(MallItemSuite mallItemSuite){
		List<MallItemSuite> list = mallSuiteInfoDao.getItemSuiteById(mallItemSuite.getSuiteId());
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void deleteSuiteDetail(String suiteId){
		mallSuiteInfoDao.deleteSuiteDetail(suiteId);
	}
}
