package com.thinkdifferent.MongoDBReciver.dubbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.thinkdifferent.MongoDBReciver.service.MongoDBService;

import net.sf.json.JSONObject;

@Service(version = "1.0.0", timeout = 10000, interfaceName = "com.thinkdifferent.MongoDBReciver.dubbo.service.MongoDBService")
public class MongoDBServiceImpl implements MongoDBService {
 
    @Autowired
    private com.thinkdifferent.MongoDBReciver.service.MongoDBService mongoService;
	
	
	/**
	 * 发送数据接口。将接收到的数据发送到Kafka MQ的队列中
	 * 
	 * @param jo 数据信息，以JSONObject形式传送
	 * @return 是否发送成功
	 */
    public boolean sendData(JSONObject jo){
    	return mongoService.sendData(jo);
    }

}
