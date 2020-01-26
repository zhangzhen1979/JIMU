package com.thinkdifferent.MongoDBManager.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MongoUtil {

	/**
	 * 根据传入的数据，生成对应的查询对象
	 * 
	 * @param jaSearchData 查询条件对象
	 * @return
	 */
	public Criteria getCriteria(JSONArray jaSearchData) {
		// 查询的默认值：where 1=1
		Criteria criteria = Criteria.where("_id").ne("").ne(null);

		// 查询的数据对象如果不为空，则对查询对象进行处理
		if(jaSearchData!=null){
			JSONObject joSearch=new JSONObject();
			String strField="";
			String strCondition="";
			Object objValue=null;
			// 因为传入的查询条件时JSON数组，所以需要逐个取出每个JSON对象，拼装为查询对象
			for(int i=0;i<jaSearchData.size();i++){
				// 取出一个JSONObject
				joSearch=jaSearchData.getJSONObject(i);
				// 需要查询的字段名
				strField=joSearch.containsKey("field")?joSearch.getString("field"):"";
				// 查询条件
				strCondition=joSearch.containsKey("condition")?joSearch.getString("condition"):"";
				// 查询值
				objValue=joSearch.containsKey("value")?joSearch.get("value"):null;
				
				// 根据JSON中的值，拼装查询条件对象
				if (StringUtils.isNotBlank(strField) && StringUtils.isNotBlank(strCondition) && objValue != null) {

					if ("=".equals(strCondition)) {
						criteria.and(strField).is(objValue);
					}
					if ("<>".equals(strCondition)) {
						criteria.and(strField).ne(objValue);
					}
					if (">".equals(strCondition)) {
						criteria.and(strField).gt(objValue);
					}
					if (">=".equals(strCondition)) {
						criteria.and(strField).gte(objValue);
					}
					if ("<".equals(strCondition)) {
						criteria.and(strField).lt(objValue);
					}
					if ("<=".equals(strCondition)) {
						criteria.and(strField).lte(objValue);
					}
					// 如果操作符为in，则objValue应为数组。
					if ("in".equals(strCondition)) {
						criteria.and(strField).in(objValue);
					}

				}
			}
		}
		
		return criteria;
	}
}
