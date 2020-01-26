package com.thinkdifferent.MongoDBManager.service;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface MongoDBService {

    /**
     * 根据条件，查询数据
     * 
     * @param strTableName 表名
     * @param jaSearchData JSON格式的查询条件
     * @param intPageNum 页码
	 * @param strOrderField 排序字段名
	 * @param strOrderType 排序类型：asc 升序；desc 降序
     * @return 结果Map对象
     */
	public Map<Object, Object> listDatas(String strTableName, 
			JSONArray jaSearchData, int intPageNum,
			String strOrderField, String strOrderType);

	/**
	 * 根据数据的id查询MongoDB中存储的对象
	 * 
	 * @param strTableName 表名
	 * @param dataID 数据id
	 * @return JSONObject
	 */
	public JSONObject findByDataId(String strTableName, String dataID);

	/**
	 * 将传入的JSONObject对象，存入到指定的集合（表）中
	 * 
	 * @param strTableName 表名
	 * @param joData 需要存入的JSON数据
	 * @return 保存后的JSON数据对象
	 */
	public JSONObject insertData(String strTableName, JSONObject joData);

	/**
	 * 根据查询条件，更新符合条件的任意多条数据。
	 * 
	 * @param strTableName 表名
	 * @param jaSearchData 查询对象，JSONArray格式
	 * @param joUpdateData 更新内容对象，JSON格式
	 */
	public void updateData(String strTableName, JSONArray jaSearchData, JSONObject joUpdateData);

	/**
	 * 删除指定集合（表）中指定条件的数据
	 * 
	 * @param strTableName 表名
	 * @param jaSearchData 删除时的查询条件
	 */
	public void deleteData(String strTableName, JSONArray jaSearchData);

	
}
