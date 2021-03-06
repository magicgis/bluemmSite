/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.com.bluemoon.jeesite.test.dao;

import cn.com.bluemoon.jeesite.common.persistence.TreeDao;
import cn.com.bluemoon.jeesite.common.persistence.annotation.MyBatisDao;
import cn.com.bluemoon.jeesite.test.entity.TestTree;

/**
 * 树结构生成DAO接口
 * @author ThinkGem
 * @version 2015-04-06
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}