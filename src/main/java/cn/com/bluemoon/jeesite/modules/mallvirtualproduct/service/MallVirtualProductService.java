/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.mallvirtualproduct.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.entity.MallVirtualProduct;
import cn.com.bluemoon.jeesite.modules.mallvirtualproduct.dao.MallVirtualProductDao;

/**
 * 虚拟产品信息管理Service
 * @author linyihao
 * @version 2016-02-29
 */
@Service
@Transactional(readOnly = true)
public class MallVirtualProductService extends CrudService<MallVirtualProductDao, MallVirtualProduct> {

	public MallVirtualProduct get(String id) {
		return super.get(id);
	}
	
	public List<MallVirtualProduct> findList(MallVirtualProduct mallVirtualProduct) {
		return super.findList(mallVirtualProduct);
	}
	
	public Page<MallVirtualProduct> findPage(Page<MallVirtualProduct> page, MallVirtualProduct mallVirtualProduct) {
		return super.findPage(page, mallVirtualProduct);
	}
	
	@Transactional(readOnly = false)
	public void save(MallVirtualProduct mallVirtualProduct) {
		super.save(mallVirtualProduct);
	}
	
	@Transactional(readOnly = false)
	public void delete(MallVirtualProduct mallVirtualProduct) {
		super.delete(mallVirtualProduct);
	}
	
}