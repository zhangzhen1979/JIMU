package com.thinkdifferent.DBAgentDemo.service;

import org.springframework.context.ApplicationContext;

import com.thinkdifferent.DBAgentDemo.dao.DemoDAO;

import net.sf.json.JSONObject;

/**
 * SQL语句测试专用，Service层方法
 */
public class DemoServiceImpl {

	private DemoDAO demoDAO;
	
	public DemoServiceImpl(String strIP, int intPort, 
			String strVersion, String strPackage,
			ApplicationContext applicationContext){
		demoDAO=new DemoDAO(strIP, intPort, 
				strVersion, strPackage,
				applicationContext);
	}

	public JSONObject test() {
		JSONObject joReturn=new JSONObject();
		try{
			boolean blnFlag=false;
				blnFlag=demoDAO.test();
				joReturn.put("flag", blnFlag);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return joReturn;
	}


}

