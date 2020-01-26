package com.thinkdifferent.MongoDBManager.dubbo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.thinkdifferent.MongoDBManager.service.MongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service(version = "1.0.0", timeout = 10000, interfaceName = "com.thinkdifferent.MongoDBManager.dubbo.service.MongoDBService")
public class MongoDBServiceImpl implements MongoDBService {
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
 
    @Autowired
    private com.thinkdifferent.MongoDBManager.service.MongoDBService mongoService;
	
	
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
			String strOrderField, String strOrderType){
		return mongoService.listDatas(strTableName,
				jaSearchData, intPageNum,
				strOrderField, strOrderType);
	}

	/**
	 * 根据数据的id查询MongoDB中存储的对象
	 * 
	 * @param strTableName 表名
	 * @param dataID 数据id
	 * @return JSONObject
	 */
	public JSONObject findByDataId(String strTableName, String dataID){
		return mongoService.findByDataId(strTableName, dataID);
	}

	/**
	 * 将传入的JSONObject对象，存入到指定的集合（表）中
	 * 
	 * @param table 表名
	 * @param data 需要存入的JSON数据
	 * @return 保存后的JSON数据对象
	 */
	public JSONObject insertData(String table, JSONObject data){
		return mongoService.insertData(table, data);
	}
	
	/**
	 * 根据查询条件，更新符合条件的任意多条数据。
	 * 
	 * @param table 表名
	 * @param searchData 查询对象，JSON格式
	 * @param updateData 更新内容对象，JSON格式
	 */
	public void updateData(String table, JSONArray searchData, JSONObject updateData){
		mongoService.updateData(table, searchData, updateData);
	}

	/**
	 * 删除指定集合（表）中指定条件的数据
	 * 
	 * @param table 表名
	 * @param searchData 删除时的查询条件
	 */
	public void deleteData(String table, JSONArray searchData){
		mongoService.deleteData(table, searchData);
	}


}
