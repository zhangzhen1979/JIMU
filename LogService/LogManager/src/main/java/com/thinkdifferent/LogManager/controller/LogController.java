package com.thinkdifferent.LogManager.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thinkdifferent.LogManager.pojo.LogObject;
import com.thinkdifferent.LogManager.service.LogService;

import net.sf.json.JSONObject;


@RestController
@RequestMapping("/api")
public class LogController {

	private static final Logger log = LoggerFactory.getLogger(LogController.class);
	
	@Autowired
	private LogService logService;
	
	/**
	 * 根据查询条件，返回日志数据列表
	 * @param mapData
	 * @return
	 */
	@RequestMapping(value="/listLog",method=RequestMethod.POST)
	public Map<String,Object> listLog(@RequestBody Map<String, String> mapData) {
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "List log message failed.");
		
		JSONObject joLog=JSONObject.fromObject(mapData);
		
		try {
			String strPageNum=joLog.containsKey("pageNum")?joLog.getString("pageNum"):"1";
			String strLogID=joLog.containsKey("logID")?joLog.getString("logID"):null;
			String strLogType=joLog.containsKey("logType")?joLog.getString("logType"):null;
			String strAppID=joLog.containsKey("appID")?joLog.getString("appID"):null;
			String strAppAddress=joLog.containsKey("appAddress")?joLog.getString("appAddress"):null;
			String strStartTime=joLog.containsKey("startTime")?joLog.getString("startTime"):null;
			String strEndTime=joLog.containsKey("endTime")?joLog.getString("endTime"):null;
			
			if(strLogType!=null){
				
				Map<Object, Object> mapLogs=logService.listLogs(strPageNum, 
						strLogID, strLogType, 
						strAppID, strAppAddress, 
						strStartTime, strEndTime);
				
				if(mapLogs!=null){
					mapReturn.put("flag", "SUCCESS");
					mapReturn.put("message", "List log message success.");
					mapReturn.put("count", mapLogs.get("count"));
					mapReturn.put("data", mapLogs.get("data"));
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

	/**
	 * 根据LogID，获取日志的详细信息
	 * @param mapData
	 * @return
	 */
	@RequestMapping(value="/getLog",method=RequestMethod.POST)
	public Map<String,Object> getLog(@RequestBody Map<String, String> mapData) {
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag", "FAIL");
		mapReturn.put("message", "Get log message failed.");
		
		JSONObject joLog=JSONObject.fromObject(mapData);
		
		try {
			String strLogID=joLog.containsKey("logID")?joLog.getString("logID"):null;
			String strLogType=joLog.containsKey("logType")?joLog.getString("logType"):null;
			
			if(strLogID!=null && strLogType!=null){
				LogObject logObject=logService.findById(strLogID, strLogType);
				
				if(logObject!=null){
					mapReturn.put("flag", "SUCCESS");
					mapReturn.put("message", "Get log message success.");

					Map<String,Object> mapLog=new HashMap<String,Object>();
					mapLog.put("logID", logObject.getLogID());
					mapLog.put("logType", logObject.getLogType());
					mapLog.put("appID", logObject.getAppID());
					mapLog.put("appAddress", logObject.getAppAddress());
					mapLog.put("operationTime", logObject.getOperationTime());
					mapLog.put("logData", logObject.getLogData());
					
					mapReturn.put("data", mapLog);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return mapReturn;
	}

	
}
