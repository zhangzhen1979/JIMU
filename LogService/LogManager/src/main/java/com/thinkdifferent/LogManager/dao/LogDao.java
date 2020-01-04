package com.thinkdifferent.LogManager.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.thinkdifferent.LogManager.pojo.LogObject;

@Component
public class LogDao extends MongoBaseDao<LogObject> {
	
	public Pagination<LogObject> getPage(String pageNum, 
			String logID, String logType,
			String appID, String appAddress, 
			String startTime, String endTime) {

		// where 1=1
		Criteria criteria = Criteria.where("_id").ne("").ne(null);

		if (StringUtils.isNotBlank(logID)) {
			criteria.and("logID").is(logID);
		}

		if (StringUtils.isNotBlank(logType)) {
			criteria.and("logType").is(logType);
		}
		
		if (StringUtils.isNotBlank(appID)) {
			criteria.and("appID").is(appID);
		}

		if (StringUtils.isNotBlank(appAddress)) {
			criteria.and("appAddress").is(appAddress);
		}

		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			criteria.and("operationTime").gte(startTime).lte(endTime);
		} else if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
			criteria.and("operationTime").gte(startTime);
		} else if (StringUtils.isNotBlank(endTime) && StringUtils.isBlank(startTime)) {
			criteria.and("operationTime").lte(endTime);
		}

		Query query = new Query(criteria);
		query.with(new Sort(Direction.DESC, "operationTime"));
		Integer pageNo = 0;
		if (StringUtils.isNotBlank(pageNum)) {
			pageNo = Integer.parseInt(pageNum);
		}
		return this.getPage(pageNo, PAGE_SIZE, query, logType);
	}

	@Override
	protected Class<LogObject> getEntityClass() {
		return LogObject.class;
	}
}
