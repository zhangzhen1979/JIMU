package com.thinkdifferent.LogReciver.pojo;

import java.io.Serializable;
import java.util.Map;

public class LogObject implements Serializable {
	private static final long serialVersionUID = 3784727693030907054L;
	
	// 日志ID（日志产生时创建，与存储中的id不同）
	private String logID;
	// 日志类型（sys/ope）（系统日志/操作日志）
	private String logType;
	// 应用服务ID（名称）
	private String appID;
	// 服务器地址
	private String appAddress; 
	// 操作时间
	private String operationTime; 
	// 日志内容
	private Map<String, Object> logData;

//	private String userBehavior; // 用户行为
//	private String orgId; // 组织ID
//	private String operationUser; // 操作用户
//	private String accessIp; // 访问IP
//	private String accessType; // 访问类型
//	private String accessPath;// 访问路径
//	private String invokeService; // 调用服务
//	private String executeMethod;// 执行方法
//	private String executeMethodTime;// 执行方法耗时
//	private String executeResult; // 执行结果


	@Override
	public String toString() {
		return "[logID=" + logID + ", logType=" + logType + ", appID=" + appID
				+ ", appAddress=" + appAddress + ", operationTime=" + operationTime
				+ ", logData=" + logData + "]";
	}

	public String getLogID() {
		return logID;
	}

	public void setLogID(String logID) {
		this.logID = logID;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getAppAddress() {
		return appAddress;
	}

	public void setAppAddress(String appAddress) {
		this.appAddress = appAddress;
	}

	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	public Map<String, Object> getLogData() {
		return logData;
	}

	public void setLogData(Map<String, Object> logData) {
		this.logData = logData;
	}

}
