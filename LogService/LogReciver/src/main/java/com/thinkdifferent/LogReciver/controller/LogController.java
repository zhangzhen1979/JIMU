package com.thinkdifferent.LogReciver.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thinkdifferent.LogReciver.service.LogService;

import net.sf.json.JSONObject;


@RestController
@RequestMapping("/api")
public class LogController {

	private static final Logger log = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private LogService logService;
	
	/**
	 * 接收日志信息，JSON格式
	 * 步骤说明：
	 * 1、应用（异步）调用LogService的/api/writeLog接口，写入日志数据。
	 * 2、LogService（接收服务）将日志数据（JSON）传入KafkaMQ的log_topic队列。（完成）
	 * 
	 * @param mapData
	 * 	{
	 * 		"logID":"A00000000007dfadfadfafd",
	 * 		"logType":"sys",
	 * 		"appID":"adadskfj23ipruqadf",
	 *		"appAddress":"http://127.0.0.1/demo",
	 * 		"operationTime":"2019-3-10 12:00:00",
	 * 		"logData":{
	 * 			"creator":"wld",
	 * 			"pcurl":"/showtask.aspx?id=A00000000007"
	 * 		}
	 * 	}
	 * @return
	 */
	@RequestMapping(value="/setLog",method=RequestMethod.POST)
	public Map<String,String> setLog(@RequestBody Map<String, Object> mapData) {
		// 返回参数
		Map<String,String> mapReturn=new HashMap<String,String>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "Send log message to KafkaMQ failed.");
		
		try {
			JSONObject joLog=JSONObject.fromObject(mapData);
			
			boolean blnFlag=logService.sendLog(joLog);
			
			if(blnFlag){
				mapReturn.put("flag", "SUCCESS");
				mapReturn.put("message", "Send log message to KafkaMQ success.");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

}
