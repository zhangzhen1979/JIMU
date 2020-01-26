package com.thinkdifferent.MongoDBManager.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.thinkdifferent.MongoDBManager.util.MongoUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class MongoDBDao extends MongoBaseDao<JSONObject> {
	
	/**
	 * 根据条件，分页查询所需数据
	 * 
	 * @param table 表名
	 * @param jaSearchData 需要查询的自定义数据内容，JSONArray格式。
	 * @param pageNum 页码
	 * @param orderField 排序字段名
	 * @param orderType 排序类型：asc 升序；desc 降序
	 * @return 分页后的查询结果
	 */
	public Pagination<JSONObject> getPage(String table,
			JSONArray jaSearchData, int pageNum, 
			String orderField, String orderType) {

		MongoUtil mu=new MongoUtil();
		Criteria criteria = mu.getCriteria(jaSearchData);
		Query query = new Query(criteria);
		
		Direction sortDirection=Direction.DESC;
		if("asc".equalsIgnoreCase(orderType)){
			sortDirection=Direction.ASC;
		}
		query.with(new Sort(sortDirection, orderField));
		
		return this.getPage(pageNum, PAGE_SIZE, query, table);
	}

	@Override
	protected Class<JSONObject> getEntityClass() {
		return JSONObject.class;
	}
}
