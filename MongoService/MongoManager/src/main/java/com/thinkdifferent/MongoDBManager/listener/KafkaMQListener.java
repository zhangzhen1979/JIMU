package com.thinkdifferent.MongoDBManager.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.thinkdifferent.MongoDBManager.service.MongoDBService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class KafkaMQListener {
	
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KafkaMQListener.class);
    
    @Autowired
    private MongoDBService mongoDBService;

    @KafkaListener(topics = "data_topic")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        logger.info("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
        
        // 写入MongoDB
        try {
            
        	String strMessage=record.value().toString();
        	
        	JSONObject joInput=JSONObject.fromObject(strMessage);
    		// 对传入的数据格式进行检查，合法才允许插入
    		if(joInput.containsKey("table") &&
    				joInput.containsKey("operation") ){
    			
    			String strTableName=joInput.getString("table");
    			
            	JSONArray jaQuery=new JSONArray();
            	JSONObject joData=new JSONObject();
    			
    			// 根据传入的operation的值，判断进行何种处理，进而取出必要的变量值
    			String strOperation=joInput.getString("operation");
    			if("insert".equalsIgnoreCase(strOperation)){
       				// 如果是插入，则取dataID、data
     				joData.put("dataID", joInput.getString("dataID"));
                	joData.putAll(joInput.getJSONObject("data"));

    				
    			}else if("update".equalsIgnoreCase(strOperation)){
    				// 如果是更新，则取query、data
     				jaQuery=joInput.getJSONArray("query");
    				
                	joData.putAll(joInput.getJSONObject("data"));

    			}else if("delete".equalsIgnoreCase(strOperation)){
    				// 如果是删除，则取dataID
    				JSONObject joDataID=new JSONObject();
    				joDataID.put("dataID", joInput.getString("dataID"));
    				jaQuery.add(joDataID);
    			}
    			
    			
                if("insert".equalsIgnoreCase(joInput.getString("operation"))){
                	mongoDBService.insertData(strTableName, joData);
                }else if("update".equalsIgnoreCase(joInput.getString("operation"))){
                	mongoDBService.updateData(strTableName, jaQuery, joData);
                }else if("delete".equalsIgnoreCase(joInput.getString("operation"))){
                	mongoDBService.deleteData(strTableName, jaQuery);
                }
                
                joData.clear();
                joData=null;
    		}else{
                logger.error("接收到的日志消息格式错误：", strMessage);
    		}
    		
    		joInput.clear();
    		joInput=null;
    		
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
