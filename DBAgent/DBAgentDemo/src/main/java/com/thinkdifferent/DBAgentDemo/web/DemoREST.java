package com.thinkdifferent.DBAgentDemo.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thinkdifferent.DBAgentDemo.service.DemoServiceImpl;

import net.sf.json.JSONObject;

/**
 * SQL语句测试专用，REST接口
 */

@CrossOrigin
@org.springframework.web.bind.annotation.RestController

@RequestMapping("/demo")
public class DemoREST {
	private static final Logger log = LoggerFactory.getLogger(DemoREST.class);

	@Autowired
	private ApplicationContext applicationContext;

	private DemoServiceImpl demoService;
	
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public Map<String,Object> test(){
		// 返回参数
		Map<String,Object> mapReturn=new HashMap<String,Object>();
		mapReturn.put("flag","FAILED");
		
		try {
			String strIP="127.0.0.1";
			int intPort=20880;
			String strVersion="1.0.0";
			String strPackage="com.thinkdifferent.DBAgent.service.DBService";
			demoService=new DemoServiceImpl(strIP, intPort, 
					strVersion, strPackage,
					applicationContext);
			// 调用Service方法，，根据SQL在xml中配置的name，从配置文件中获取需要运行的SQL
			JSONObject joResult=demoService.test();
			
			if(joResult.getBoolean("flag")){
				mapReturn.put("flag","SUCCESS");
				mapReturn.put("message","SQL执行成功");
				mapReturn.put("data", joResult);
			}else{
				mapReturn.put("flag","FAILED");
				mapReturn.put("message","SQL执行失败");
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return mapReturn;	
	}

}
