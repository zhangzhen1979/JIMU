package com.thinkdifferent.MongoDBReciver.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.thinkdifferent.MongoDBReciver.service.MongoDBService;

import net.sf.json.JSONObject;

@Service
public class MongoDBServiceImpl implements MongoDBService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MongoDBServiceImpl.class);
 
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
	
	
	/**
	 * 发送数据接口。将接收到的数据发送到Kafka MQ的队列中
	 * 
	 * @param jo 数据信息，以JSONObject形式传送
	 * @return 是否发送成功
	 */
    public boolean sendData(JSONObject jo){
		// 对传入的数据格式进行检查
    	// 是否有“表名”
		if(!jo.containsKey("table")){
			return false;
		}
		// 是否有“操作”
		if(!jo.containsKey("operation")){
			return false;
		}
		
		// 根据传入的operation的值，判断进行何种处理，进而取出必要的变量值
		String strOperation=jo.getString("operation");
		if("insert".equalsIgnoreCase(strOperation)){
			// 如果是插入，则取dataID、data
			if(!jo.containsKey("dataID")){
				return false;
			}
			if(!jo.containsKey("data")){
				return false;
			}
		}else if("update".equalsIgnoreCase(strOperation)){
			// 如果是更新，则取query、data
			if(!jo.containsKey("query")){
				return false;
			}
			if(!jo.containsKey("data")){
				return false;
			}
		}else if("delete".equalsIgnoreCase(strOperation)){
			// 如果是删除，则取dataID
			if(!jo.containsKey("dataID")){
				return false;
			}
		}
		
		// 将检查合格的数据发送到队列中
        kafkaTemplate.send("data_topic", jo.toString());
        logger.info("发送消息完成,内容为:" + jo);
    	return true;
    }

}
