package com.thinkdifferent.LogReciver.service;

import net.sf.json.JSONObject;

public interface LogService {

	/**
	 * 记录错误日志
	 * @param joLog 日志信息，以JSONObject形式传送
	 * @return
	 */
	public boolean sendLog(JSONObject joLog);
	
	
}
