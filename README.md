# JIMU
JIMU Platform 积木框架，解决微服务支撑问题，为“单体架构”向“微服务架构”演进提供支持。


目前包含如下组件

- DBAgent  解决数据库连接、访问，执行SQL问题。各个微服务可以通过Dubbo接口调用此服务，以此服务为“数据库代理”，执行SQL。此服务支持多实例管理。
- MongoService  解决MongoDB数据库的存储、查询问题。基于Kafka MQ做队列缓冲，减轻MongoDB的写入连接压力。
- LogService   基于MongoDB数据库存储，使用Kafka MQ缓冲，解决平台中各类日志的存储、查询支持。解决集群环境下日志的统一管理问题。

