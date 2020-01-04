package com.thinkdifferent.LogManager.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkdifferent.LogManager.dao.LogDao;
import com.thinkdifferent.LogManager.dao.MongoBaseDao;
import com.thinkdifferent.LogManager.dao.Pagination;
import com.thinkdifferent.LogManager.pojo.LogObject;
import com.thinkdifferent.LogManager.service.LogService;

@Service
public class LogServiceImpl implements LogService {
//    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);
 
	@Autowired
	private LogDao logDao;
	@Autowired
	private MongoBaseDao<?> mongoDao;
	
	
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
		Map<Object, Object> mapResult = new HashMap<Object, Object>();
		Pagination<LogObject> pagination = logDao.getPage(pageNum, logID, logType, appID, appAddress, startTime, endTime);

		mapResult.put("count", pagination.getTotalCount());
		mapResult.put("data", pagination.getDatas());
		return mapResult;
	}

	/**
	 * 根据数据的id查询MongoDB中存储的对象
	 * @param id 数据id
	 * @param logType 日志类型  sys/ope
	 * @return
	 */
	public LogObject findById(String id, String logType){
		LogObject lo=mongoDao.findByLogId(id, logType);
		return lo;
	}



}
