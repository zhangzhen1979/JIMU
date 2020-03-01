 

**DBAgentDemo  调用DBAgent数据库连接代理服务示例** 



# 代码简介

本微服务代码采用Eclipse开发，是一个标准的Eclipse Maven工程。

本微服务基于Spring Boot 2.1.9开发。

请用Eclipse导入此工程即可。




# 功能说明

本程序演示如何通过DBAgentUtil，调用已部署好的DBAgent服务。

## 配置文件（客户端配置）

本实例为application.properties，具体内容如下：

```properties
#=====Port=====
server.port=8088

# ===============dubbo配置细信息（开始）
# dubbo提供者的别名，只是个标识
dubbo.application.name=demo

# 注册模式：dubbo注册中心地址（ZooKeeper）
dubbo.registry.address=N/A
dubbo.scan.base-packages=com.thinkdifferent.dbagent.service
# dubbo配置细信息（结束）
```

 

# 上线运行前置条件

- DBAgent已经连接数据库正常启动。



# 客户端调用示例

REST层

```java
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

```



Service层

```java
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
```

Dao层

```java
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
```



