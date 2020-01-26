package com.thinkdifferent.MongoDBReciver.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thinkdifferent.MongoDBReciver.service.MongoDBService;

import net.sf.json.JSONObject;


@RestController
@RequestMapping("/api")
public class MongoDBController {

	private static final Logger log = LoggerFactory.getLogger(MongoDBController.class);
	
	@Autowired
	private MongoDBService mongoService;
	
	/**
	 * 接收日志信息，JSON格式
	 * 步骤说明：
	 * 1、应用（异步）调用LogService的/api/writeLog接口，写入日志数据。
	 * 2、LogService（接收服务）将日志数据（JSON）传入KafkaMQ的log_topic队列。（完成）
	 * 
	 * @param mapData
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
	 * 
	 * @return
	 */
	@RequestMapping(value="/setData",method=RequestMethod.POST)
	public Map<String,String> setData(@RequestBody Map<String, Object> mapData) {
		// 返回参数
		Map<String,String> mapReturn=new HashMap<String,String>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "Send data message to KafkaMQ failed.");
		
		try {
			// 将接收到的数据，格式化为JSONObject
			JSONObject jo=JSONObject.fromObject(mapData);
			
			boolean blnFlag=mongoService.sendData(jo);
			
			if(blnFlag){
				// 如果发送成功，则返回调用者“成功”标志。
				mapReturn.put("flag", "SUCCESS");
				mapReturn.put("message", "Send data message to KafkaMQ success.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

}
