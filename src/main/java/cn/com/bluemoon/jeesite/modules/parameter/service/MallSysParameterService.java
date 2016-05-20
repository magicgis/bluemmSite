/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.parameter.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.bluemoon.jeesite.common.persistence.Page;
import cn.com.bluemoon.jeesite.common.service.CrudService;
import cn.com.bluemoon.jeesite.common.utils.IdGen;
import cn.com.bluemoon.jeesite.common.utils.JedisUtils;
import cn.com.bluemoon.jeesite.modules.parameter.dao.MallSysParameterDao;
import cn.com.bluemoon.jeesite.modules.parameter.entity.MallSysParameter;
import cn.com.bluemoon.jeesite.modules.sys.entity.User;
import cn.com.bluemoon.jeesite.modules.sys.utils.UserUtils;

/**
 * 系统参数Service
 * @author mij
 * @version 2015-12-28
 */
@Service
@Transactional(readOnly = true)
public class MallSysParameterService extends CrudService<MallSysParameterDao, MallSysParameter> {
	
	@Autowired
	private MallSysParameterDao parameterDao;

	public MallSysParameter get(String id) {
		return super.get(id);
	}
	
	public List<MallSysParameter> findList(MallSysParameter mallSysParameter) {
		return super.findList(mallSysParameter);
	}
	
	
	
	
	public Page<MallSysParameter> findPage(Page<MallSysParameter> page, MallSysParameter mallSysParameter) {
		return super.findPage(page, mallSysParameter);
	}
	
	@Transactional(readOnly = false)
	public void save(MallSysParameter mallSysParameter) {
//		String redisKey=mallSysParameter.getParaType()+mallSysParameter.getParaCode();
//		if(JedisUtils.exists(redisKey)){
//			JedisUtils.del(redisKey);
//		}
		
		if (StringUtils.isBlank(mallSysParameter.getParaId())){
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				mallSysParameter.setParaId(IdGen.uuid());
				mallSysParameter.setOperDate(new Date());
				mallSysParameter.setOperUser(user.getName());
				mallSysParameter.setId(mallSysParameter.getParaId());
				mallSysParameter.setIsNewRecord(true);
				super.save(mallSysParameter);
			}
			
		}else{
			User user = UserUtils.getUser();
			if (StringUtils.isNotBlank(user.getId())){
				mallSysParameter.setId(mallSysParameter.getParaId());
				mallSysParameter.setOperDate(new Date());
				mallSysParameter.setOperUser(user.getName());
				mallSysParameter.setIsNewRecord(false);
				super.save(mallSysParameter);
//				JedisUtils.set(redisKey, mallSysParameter.getParaValue(), 0);
			}
		}
		
		
	}
	
	@Transactional(readOnly = false)
	public void delete(MallSysParameter mallSysParameter) {
		super.delete(mallSysParameter);
	}
	
	
	public String getSysParameterByTypeAndCode(String paraType,String paraCode){
		String returnParaValue="";
		String redisKey=paraType+paraCode;
		if (JedisUtils.exists(redisKey)) {
			returnParaValue=JedisUtils.get(redisKey);
		} else {
			MallSysParameter sysParameter =getSysParameter(paraType,paraCode);
			if(sysParameter!=null){
				returnParaValue=sysParameter.getParaValue();
				JedisUtils.set(redisKey,returnParaValue,0);
			}
		}
		return returnParaValue;
	}
	
	/**
	 * 根据类型与code查询数据库
	 * @param paraType
	 * @param paraCode
	 * @return
	 */
	private MallSysParameter getSysParameter(String paraType,String paraCode){
		MallSysParameter sysParameter= parameterDao.getSysParameterByCode(paraType,paraCode);
		return sysParameter;
	}
	
}