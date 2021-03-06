 

**LogReceiver  日志管理服务  V1.0.0  说明** 



# 代码简介

本微服务代码采用Eclipse开发，是一个标准的Eclipse Maven工程。

本微服务基于Spring Boot 1.5.10开发。

请用Eclipse导入此工程即可。



# 服务架构说明

一般情况下，系统的“日志”分为两种：

- 系统日志。即通过系统的Log4j输出到日志文件中内容。


- 操作日志。记录用户各种操作的信息，记录到系统数据库或指定文件中。




以上两种记录日志的方式基于现有技术实现，使用简便。但是缺点也很明显：

- 系统日志。
  - 在集群环境排查问题时，需要从多台应用服务器中下载日志文件；
  - 日志文件体积很大，排查问题效率低。

- 操作日志。
  - 由于将大量的操作信息写入到数据库表中，导致日志表非常大。
  - 由于在数据操作的同时需要在日志表中同步记录信息，导致数据库操作事务时间长，容易引起异常锁表等问题。

  

为了解决以上的问题，开发了本微服务。本系列服务的调用模式如下图：

![img](clip_image002.jpg)

**调用过程：**

- 调用方需要自行开发实现日志工具类（本例中命名为logUtil.jar），将需要记录的日志推送到“LogReceiver”服务的接口中。

- “LogReceiver”服务：接收日志服务，需要连接Kafka MQ服务（Kafka MQ需要先行启动）。“LogReceiver”提供REST接口和Dubbo接口可供日志发送方调用；通过接口接收到日志数据后，将日志信息推送到“Kafka MQ”的“log_topic”队列中，等待“消费端”处理。

- “LogManager”服务：
  - “Kafka MQ”的“消费端”，接收MQ推送过来的日志数据，逐条写入到MongoDB数据库中。
  - 日志查询管理后端服务，为“LogManagerUI”前端程序提供后端接口，专用于数据查询。
- “LogManagerUI”：日志查询前端页面。




**优点：**

- 调用方可以将日志以异步方式推送给“LogReceiver”即可，不会影响当前操作。

- 日志数据存储在外部的MongoDB中，减轻文件系统和数据库的压力。

- MongoDB可以很方便的设置集群、分片，可以快速提升存储能力和运行效率。




# 微服务部署说明

工程通过Eclipse的”Maven Install“功能，会在Target目录中生成jar包。

将生成的“LogManager-1.0.0.jar”文件与工程根目录的两个properties文件放在同一个文件夹中，即可启动此微服务。

完整的部署文件夹应该包含以下文件：

- LogManager-1.0.0.jar：微服务文件，可执行。

- application.properties：微服务配置文件,可根据实际需求修改。各配置项说明如下（其他配置不建议修改）：
  - server.port=8899：本微服务端口。
  - spring.kafka.producer.bootstrap-servers=127.0.0.1:9092：kafka服务的地址和端口
  - spring.data.mongodb.uri=mongodb://127.0.0.1:27017/logdata：MongoDB的连接字符串。目前的连接方式是无密码；如果MongoDB是有用户名和密码的，此参数应该按照如下格式配置：mongodb://username:password@127.0.0.1:27017/logdata
- spring.dubbo.registry.address=N/A：dubbo注册中心地址。如果配置成“N/A”，则为“不注册模式”（单机模式）；正常应配置为如下格式内容zookeeper://ip:2181
  
- dubbo.properties：dubbo连接数配置文件

- startup.bat：Windows版启动脚本。内容如下：
  
`java -jar -Xms1g -Xmx1g ./LogManager-1.0.0.jar`
  
- startup.sh：Linux版启动脚本。内容如下：
  
  `nohup  java -jar -Xms1g -Xmx1g ./LogManager-1.0.0.jar  2>&1 &`



# 微服务运行说明

本微服务依赖 Kafka MQ，需要先将需要连接的Kafka MQ服务先行启动。

启动本微服务时，只需执行“startup.bat”或“startup.sh”即可。

或可以在命令行窗口执行：

`java -jar -Xms1g -Xmx1g ./LogManager-1.0.0.jar`

其中Xms是设置最小内存；Xmx是设置最大内存。可以根据实际情况修改内存大小。

**注意：**在Windows中，执行微服务后，命令行窗口不能关闭！否则此java进程会随之关闭！！！

 

# 接口调用说明

## REST接口

### 查询日志信息列表

接口地址：http://127.0.0.1:8899/api/listLog

输入参数：

```json
{
    "pageNum": "1",
    "logID": "3abad323dfad32",
    "logType": "sys",
    "appID": "testAppServer001",
    "appAddress": "http://abc.def.com/api/test",
    "startTime": "2019-12-26 08:30:00",
    "endTime": "2019-12-26 17:30:00"
}
```

 

说明：（以下查询参数，输入一个值即可；不输入值，则查询全部数据）

- pageNum：页码。
- logID：日志ID，可供返回给前端，供精确查询用。
- logType：日志类型。
- appID：服务的ID，用以区分服务或应用
- appAddress：服务地址
- startTime：查询记录的时间范围-开始时间。
- endTime：查询记录的时间范围-结束时间。

 

### 根据LogID获取日志详细信息

接口地址：http://127.0.0.1:8899/api/getLog

输入参数：

```json
{
    "logID": "20191216-1704",
    "logType": "sys"
}
```

说明：

- logID：日志ID，可供返回给前端，供精确查询用。
- logType：日志类型。

 

## Dubbo接口

```java
@Service(version = "1.0.0") 


```



### 查询日志信息列表

```java
  /**
   * 根据条件，查询日志
   *
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
     String startTime, String endTime);
```

 

### 根据LogID获取日志详细信息

```java
  /**
   * 根据数据的id查询MongoDB中存储的对象
   *
   * @param id 数据id
   * @param logType 日志类型 sys/ope
   * @return
   */
  public LogObject findById(String id, String logType);
```


