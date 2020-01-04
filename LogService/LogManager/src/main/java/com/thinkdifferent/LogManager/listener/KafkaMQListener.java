package com.thinkdifferent.LogManager.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import net.sf.json.JSONObject;

@Component
public class KafkaMQListener {
	
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaMQListener.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @KafkaListener(topics = "log_topic")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        logger.info("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
        
        // 写入MongoDB
        try {
            
        	String strMessage=record.value().toString();
        	
        	JSONObject joLog=JSONObject.fromObject(strMessage);
    		// 对传入的数据格式进行检查，合法才允许插入
    		if(joLog.containsKey("logID") &&
    				joLog.containsKey("logType") &&
    				joLog.containsKey("appID") &&
    				joLog.containsKey("appAddress") &&
    				joLog.containsKey("operationTime") &&
    				joLog.containsKey("logData")){
    			
    			DBCollection coll = mongoTemplate.getCollection(joLog.getString("logType"));
    			
                BasicDBObject obj = BasicDBObject.parse(joLog.toString());
                coll.insert(obj);
    		}else{
                logger.error("接收到的日志消息格式错误：", strMessage);
    		}

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
