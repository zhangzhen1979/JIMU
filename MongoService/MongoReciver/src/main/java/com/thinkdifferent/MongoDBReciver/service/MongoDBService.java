package com.thinkdifferent.MongoDBReciver.service;

import net.sf.json.JSONObject;

public interface MongoDBService {

	/**
	 * 发送数据接口。将接收到的数据发送到Kafka MQ的队列中
	 * 
	 * @param jo 数据信息，以JSONObject形式传送
	 * @return 是否发送成功
	 */
	public boolean sendData(JSONObject jo);
	
	
}
