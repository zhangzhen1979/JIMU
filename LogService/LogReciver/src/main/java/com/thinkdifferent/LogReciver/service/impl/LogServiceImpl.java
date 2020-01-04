package com.thinkdifferent.LogReciver.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.thinkdifferent.LogReciver.service.LogService;

import net.sf.json.JSONObject;

@Service
public class LogServiceImpl implements LogService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
 
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
	
	
	/**
	 * 记录错误日志
	 * @param joLog 日志信息，以JSONObject形式传送
	 * @return
	 */
    public boolean sendLog(JSONObject joLog){
    	
		// 对传入的数据格式进行检查
		if(!joLog.containsKey("logID")){
			return false;
		}
		if(!joLog.containsKey("logType")){
			return false;
		}
		if(!joLog.containsKey("appID")){
			return false;
		}
		if(!joLog.containsKey("appAddress")){
			return false;
		}
		if(!joLog.containsKey("operationTime")){
			return false;
		}
		if(!joLog.containsKey("logData")){
			return false;
		}
    	
        kafkaTemplate.send("log_topic", joLog.toString());
        logger.info("发送消息完成,内容为:" + joLog);
    	return true;
    }

}
