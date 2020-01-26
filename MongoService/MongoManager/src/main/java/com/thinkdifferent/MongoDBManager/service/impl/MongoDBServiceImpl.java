package com.thinkdifferent.MongoDBManager.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.thinkdifferent.MongoDBManager.dao.MongoBaseDao;
import com.thinkdifferent.MongoDBManager.dao.MongoDBDao;
import com.thinkdifferent.MongoDBManager.dao.Pagination;
import com.thinkdifferent.MongoDBManager.service.MongoDBService;
import com.thinkdifferent.MongoDBManager.util.MongoUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements MongoDBService {
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
 
	@Autowired
	private MongoDBDao mongoDao;
	@Autowired
	private MongoBaseDao<?> mongoBaseDao;
	
	
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
		Map<Object, Object> mapResult = new HashMap<Object, Object>();
		Pagination<JSONObject> pagination = mongoDao.getPage(strTableName, jaSearchData, intPageNum, strOrderField, strOrderType);

		mapResult.put("count", pagination.getTotalCount());
		mapResult.put("data", pagination.getDatas());
		return mapResult;
	}

	/**
	 * 根据数据的id查询MongoDB中存储的对象
	 * 
	 * @param strTableName 表名
	 * @param dataID 数据id
	 * @return JSONObject
	 */
	public JSONObject findByDataId(String strTableName, String dataID){
		JSONObject joReturn=mongoBaseDao.findByDataId(strTableName, dataID);
		return joReturn;
	}

	/**
	 * 将传入的JSONObject对象，存入到指定的集合（表）中
	 * 
	 * @param strTableName 表名
	 * @param joData 需要存入的JSON数据
	 * @return 保存后的JSON数据对象
	 */
	public JSONObject insertData(String strTableName, JSONObject joData){
		JSONObject joReturn=mongoBaseDao.save(strTableName, joData);
		return joReturn;
	}
	
	/**
	 * 根据查询条件，更新符合条件的任意多条数据。
	 * 
	 * @param strTableName 表名
	 * @param jaSearchData 查询对象，JSONArray格式
	 * @param joUpdateData 更新内容对象，JSON格式
	 */
	public void updateData(String strTableName, JSONArray jaSearchData, JSONObject joUpdateData){
		
		MongoUtil mu=new MongoUtil();
		Criteria criteria = mu.getCriteria(jaSearchData);

		Query query = new Query(criteria);

		Update update = new Update();
		if(joUpdateData!=null){
			Iterator<?> it = joUpdateData.keys(); 
			while(it.hasNext()){
				// 获得key
				String strKey = (String)it.next(); 
				String strValue = joUpdateData.getString(strKey);    
				
				update.set(strKey, strValue);
			}	
		}
		
		mongoBaseDao.findAndModify(strTableName, query, update);
//		mongoBaseDao.updateMulti(strTableName, query, update);
	}
	
	/**
	 * 删除指定集合（表）中指定条件的数据
	 * 
	 * @param strTableName 表名
	 * @param jaSearchData 删除时的查询条件
	 */
	public void deleteData(String strTableName, JSONArray jaSearchData){
		MongoUtil mu=new MongoUtil();
		Criteria criteria = mu.getCriteria(jaSearchData);

		Query query = new Query(criteria);

		mongoBaseDao.findAndRemove(strTableName, query);
	}



}
