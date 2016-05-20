/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.modules.hb.dao;

import org.apache.ibatis.annotations.Param;

import cn.com.bluemoon.jeesite.common.persistence.CrudDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.modules.hb.entity.MallWxRedpacketFileDetails;

/**
 * 导入文件信息DAO接口
 * 
 * @author linyihao
 * @version 2016-05-03
 */
@MyBatisDao
public interface MallWxRedpacketFileDetailsDao extends
		CrudDao<MallWxRedpacketFileDetails> {
	public int findListNum(MallWxRedpacketFileDetails entity);

	public void updateOrderNo(MallWxRedpacketFileDetails entity);

	public int isExist(@Param(value = "redpacketNo") String redpacketNo,
			@Param(value = "fileid") String fileid);

	public void delFileDetailByFileid(MallWxRedpacketFileDetails entity);
}