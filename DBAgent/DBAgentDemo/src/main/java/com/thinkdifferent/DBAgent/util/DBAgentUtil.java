package com.thinkdifferent.DBAgent.util;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.dubbo.config.spring.ReferenceBean;
import org.springframework.context.ApplicationContext;

import com.thinkdifferent.DBAgent.service.DBService;

/**
 * 动态连接指定URL的Service
 * 
 * @author zz 2020-1-27
 */
public class DBAgentUtil {

	private ConcurrentHashMap<String, ReferenceBean<DBService>> map = new ConcurrentHashMap<String, ReferenceBean<DBService>>();

	public DBService getService(String strIP, int intPort, String strVersion, String strPackage,
			ApplicationContext applicationContext) {
		try {
			// 地址示例：  dubbo://127.0.0.1:20881/com.thinkdifferent.DBAgent.service.DBService:1.0.0
			String strIpPort=strIP+":"+intPort;
			String strURL="dubbo://"+strIpPort+"/"+strPackage+":"+strVersion;
			
			ReferenceBean<DBService> referenceBean = map.get(strIpPort);
			if (referenceBean == null) {
				referenceBean = new ReferenceBean<DBService>();
				referenceBean.setApplicationContext(applicationContext);
				referenceBean.setInterface(DBService.class);
				referenceBean.setUrl(strURL);
				referenceBean.afterPropertiesSet();
				map.put(strIpPort, referenceBean);
			}
	        DBService dbConnecter = referenceBean.get();

			System.out.println("获取service："+dbConnecter);
			return dbConnecter;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
