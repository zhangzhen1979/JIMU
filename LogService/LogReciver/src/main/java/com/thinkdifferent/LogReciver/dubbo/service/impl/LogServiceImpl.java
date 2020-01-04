package com.thinkdifferent.LogReciver.dubbo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.thinkdifferent.LogReciver.service.LogService;

import net.sf.json.JSONObject;

@Service(version = "1.0.0", timeout = 10000, interfaceName = "com.thinkdifferent.LogReciver.dubbo.service.LogService")
public class LogServiceImpl implements LogService {
 
    @Autowired
    private com.thinkdifferent.LogReciver.service.LogService logService;
	
	
	/**
	 * 记录错误日志
	 * @param joLog 日志信息，以JSONObject形式传送
	 * @return
	 */
    public boolean sendLog(JSONObject joLog){
    	
    	return logService.sendLog(joLog);
    	
    }

}
