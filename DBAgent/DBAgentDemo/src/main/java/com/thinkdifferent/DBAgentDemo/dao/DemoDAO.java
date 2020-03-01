package com.thinkdifferent.DBAgentDemo.dao;

import java.util.UUID;

import org.springframework.context.ApplicationContext;

import com.thinkdifferent.DBAgent.service.DBService;
import com.thinkdifferent.DBAgent.util.DBAgentUtil;

/**
 * SQL语句测试专用，DAO层方法
 */
public class DemoDAO {

	// 调用DBConnecter 查询接口
	private DBService dbaService;
	
	public DemoDAO(String strIP, int intPort,
			String strVersion, String strPackage,
			ApplicationContext applicationContext) {
		DBAgentUtil dbaUtil=new DBAgentUtil();
		dbaService=dbaUtil.getService(strIP, intPort, strVersion, strPackage, applicationContext);
	}
	
	public boolean test() {
		try{
			String strID=UUID.randomUUID().toString().replaceAll("-","");

			String strSQL="insert into dat_son1_test (id, pid) values ('"+strID+"','456')";
			
	        return dbaService.insertBySQL(strSQL, null, "udmc");
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	

}

