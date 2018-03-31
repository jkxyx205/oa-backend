package com.yodean.oa.common.plugin.ztree.service;


import com.yodean.oa.common.plugin.ztree.dto.TreeNode;
import com.yodean.oa.common.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ZtreeService {
	
	@Resource
	private BaseService baseService;

	public List<TreeNode> getTreeNode(String sql, Map<String, Object> param) {
		return baseService.query(sql, param, TreeNode.class);
	}
	
	private String treeSql(String sql,String id) {
		//case when count(1)=0 then 'false' else 'true' end as parent 注是mysql的写法
		String rsql = "select _temp_tree.id,_temp_tree.name, " +
				" case when max(p.id) is null then 'false' else 'true' end as parent" + 
				" from ("+sql+")  _temp_tree  LEFT JOIN ("+sql+") p on _temp_tree.id = p.pId where _temp_tree.pid = ? GROUP BY _temp_tree.id,_temp_tree.name";
		return rsql;
	}

}
