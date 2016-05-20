package cn.com.bluemoon.jeesite.modules.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.item.dao.ProductDao;
import cn.com.bluemoon.jeesite.modules.item.entity.MallProductInfo;

@Service
@Transactional(readOnly = true)
public class ProductService extends CrudService<ProductDao, MallProductInfo> {

	@Autowired
	private ProductDao productDao;
	
	public Page<MallProductInfo> find(Page<MallProductInfo> page, MallProductInfo mallProductInfo) {
		mallProductInfo.setPage(page);
		page.setList(productDao.findList(mallProductInfo));
		return page;
	}
	
	public List<MallProductInfo> findAllList(MallProductInfo mallProductInfo) {
		return productDao.findAllList(mallProductInfo);
	}
	
	public MallProductInfo get(String id) {
		return super.get(id);
	}
	
	public MallProductInfo getById(String productId) {
		List<MallProductInfo> list = productDao.getById(productId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<MallProductInfo> findList(MallProductInfo mallProductInfo) {
		return super.findList(mallProductInfo);
	}
	
	public Page<MallProductInfo> findPage(Page<MallProductInfo> page, MallProductInfo mallProductInfo) {
		return super.findPage(page, mallProductInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(MallProductInfo mallProductInfo) {
		boolean flag = mallProductInfo.getIsNewRecord();
		if (flag){
			mallProductInfo.preInsert();
			dao.insert(mallProductInfo);
		}else{
			mallProductInfo.preUpdate();
			dao.update(mallProductInfo);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(MallProductInfo mallProductInfo) {
		mallProductInfo.setStatus(0);
		super.delete(mallProductInfo);
	}
	
}
