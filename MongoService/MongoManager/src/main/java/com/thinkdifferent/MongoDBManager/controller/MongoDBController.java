package com.thinkdifferent.MongoDBManager.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thinkdifferent.MongoDBManager.service.MongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@RestController
@RequestMapping("/api")
public class MongoDBController {

	private static final Logger log = LoggerFactory.getLogger(MongoDBController.class);
	
	@Autowired
	private MongoDBService mongoService;
	
	/**
	 * 根据查询条件，返回数据列表
	 * @param mapData
	 * 
	 * {
	 * 	    "table":"union_todo_list",
	 *  	"query":[
	 *  		{
	 *  			"field":"dataID",
	 *  			"condition":"=",
	 *  			"value":"202001262054"
	 *  		}
	 *  	],
	 * 	    "pageNum": 1,
	 * 	    "orderField":"dataID",
	 * 	    "orderType":"asc"
	 * }
	 * 
	 * @return
	 */
	@RequestMapping(value="/listData",method=RequestMethod.POST)
	public Map<String,Object> listData(@RequestBody Map<String, String> mapData) {
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "List data failed.");
		
		JSONObject jo=JSONObject.fromObject(mapData);
		
		try {
			String strTable=jo.containsKey("table")?jo.getString("table"):null;
			JSONArray jaQuery=jo.containsKey("query")?jo.getJSONArray("query"):null;
			int intPageNum=jo.containsKey("pageNum")?jo.getInt("pageNum"):1;
			String strOrderField=jo.containsKey("orderField")?jo.getString("orderField"):null;
			String strOrderType=jo.containsKey("orderType")?jo.getString("orderType"):null;
			
			if(strTable!=null){
				Map<Object, Object> mapDB=mongoService.listDatas(strTable, jaQuery, intPageNum, 
						strOrderField, strOrderType);
				
				if(mapDB!=null){
					mapReturn.put("flag", "SUCCESS");
					mapReturn.put("message", "List data success.");
					mapReturn.put("count", mapDB.get("count"));
					mapReturn.put("data", mapDB.get("data"));
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

	/**
	 * 根据dataID，获取数据的详细信息
	 * @param mapData
	 * @return
	 */
	@RequestMapping(value="/getData",method=RequestMethod.POST)
	public Map<String,Object> getData(@RequestBody Map<String, String> mapData) {
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "Get data failed.");
		
		JSONObject jo=JSONObject.fromObject(mapData);
		
		try {
			String strTable=jo.containsKey("table")?jo.getString("table"):null;
			String strDataID=jo.containsKey("dataID")?jo.getString("dataID"):null;
			
			if(strTable!=null && strDataID!=null){
				JSONObject joDB=mongoService.findByDataId(strTable, strDataID);
				
				if(joDB!=null){
					mapReturn.put("flag", "SUCCESS");
					mapReturn.put("message", "Get data success.");
					mapReturn.put("data", joDB);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

	/**
	 * 维护数据。可以插入、更新、删除
	 * 
	 * @param mapData
	 * 
	 * 插入：
	 * 	{
	 * 		"table":"union_todo_list",
	 * 		"operation":"insert",
	 * 		"dataID":"202001262139",
	 * 		"data":{
	 * 			"creator":"wld",
	 * 			"pcurl":"/showtask.aspx?id=A00000000007"
	 * 		}
	 * 	}
	 * 
	 * 更新：（更新哪个字段，在data中写哪个字段和值。
	 * {
	 * 	    "table":"union_todo_list",
	 *  	"operation":"update",
	 *  	"query":[
	 *  		{
	 *  			"field":"dataID",
	 *  			"condition":"=",
	 *  			"value":"202001262054"
	 *  		}
	 *  	],
	 * 	    "data":{
	 * 		    "creator":"dadr23erwerwer"
	 * 	    }
	 * }
	 * 
	 * 删除：（为了保证安全，目前只支持通过dataID逐条删除。
	 * {
	 *  	"table":"union_todo_list",
	 *  	"operation":"delete",
	 *  	"dataID":"202001262139"
	 * }

	 * @return
	 */
	@RequestMapping(value="/modifyData",method=RequestMethod.POST)
	public Map<String,Object> modifyData(@RequestBody Map<String, String> mapData) {
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "Modify data failed.");
		
		// 对传入的数据格式进行检查
    	// 是否有“表名”
		if(!mapData.containsKey("table")){
			return mapReturn;
		}
		// 是否有“操作”
		if(!mapData.containsKey("operation")){
			return mapReturn;
		}
		
		JSONObject joInput=JSONObject.fromObject(mapData);
		String strTableName=joInput.getString("table");
		
		// 根据传入的operation的值，判断进行何种处理，进而取出必要的变量值
		String strOperation=(String)mapData.get("operation");
		if("insert".equalsIgnoreCase(strOperation)){
			// 如果是插入，则取dataID、data
			if(!joInput.containsKey("dataID")){
				return mapReturn;
			}
			if(!joInput.containsKey("data")){
				return mapReturn;
			}
			
			JSONObject joData=new JSONObject();
			joData.put("dataID", joInput.getString("dataID"));
			joData.putAll(joInput.getJSONObject("data"));
			
			JSONObject joDB=mongoService.insertData(strTableName, joData);
			
			if(joDB!=null){
				mapReturn.put("flag", "SUCCESS");
				mapReturn.put("message", "Insert data success.");
			}
			
		}else if("update".equalsIgnoreCase(strOperation)){
			// 如果是更新，则取query、data
			if(!joInput.containsKey("query")){
				return mapReturn;
			}
			if(!joInput.containsKey("data")){
				return mapReturn;
			}

			JSONArray jaQuery=joInput.getJSONArray("query");
			JSONObject joData=joInput.getJSONObject("data");

			mongoService.updateData(strTableName, jaQuery, joData);
			
			mapReturn.put("flag", "SUCCESS");
			mapReturn.put("message", "Update data success.");

		}else if("delete".equalsIgnoreCase(strOperation)){
			// 如果是删除，则取dataID
			if(!joInput.containsKey("dataID")){
				return mapReturn;
			}

			JSONObject joQuery=new JSONObject();
			joQuery.put("dataID", joInput.getString("dataID"));
			JSONArray jaQuery=new JSONArray();
			jaQuery.add(joQuery);

			mongoService.deleteData(strTableName, jaQuery);
			
			mapReturn.put("flag", "SUCCESS");
			mapReturn.put("message", "Delete data success.");
		}

		return mapReturn;
	}
	
}
