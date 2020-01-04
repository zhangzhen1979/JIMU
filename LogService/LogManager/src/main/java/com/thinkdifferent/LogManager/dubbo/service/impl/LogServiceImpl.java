package com.thinkdifferent.LogManager.dubbo.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.thinkdifferent.LogManager.pojo.LogObject;
import com.thinkdifferent.LogManager.service.LogService;

@Service(version = "1.0.0", timeout = 10000, interfaceName = "com.thinkdifferent.LogManager.dubbo.service.LogService")
public class LogServiceImpl implements LogService {
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
 
    @Autowired
    private com.thinkdifferent.LogManager.service.LogService logService;
	
	
	/**
     * 根据条件，查询日志
     * @param pageNum 页码
     * @param logID 日志id
     * @param logType 日志类型（sys/ope）
     * @param appID 应用服务id
     * @param appAddress 应用服务地址
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 结果Map对象
     */
	public Map<Object, Object> listLogs(String pageNum, 
			String logID, String logType,
			String appID, String appAddress,
			String startTime, String endTime) {
		
		return logService.listLogs(pageNum, logID, logType, appID, appAddress, startTime, endTime);

	}

	/**
	 * 根据数据的id查询MongoDB中存储的对象
	 * @param id 数据id
	 * @param logType 日志类型  sys/ope
	 * @return
	 */
	public LogObject findById(String id, String logType){
		
		return logService.findById(id, logType);
				
	}



}
