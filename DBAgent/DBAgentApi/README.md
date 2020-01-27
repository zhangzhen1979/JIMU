 

**DBAgentApi  数据连接代理服务接口  V1.0.0  说明** 



# 代码简介

本微服务代码采用Eclipse开发，是一个标准的Eclipse Maven工程。该工程为独立pom文件的maven项目，提供公共接口。消费者、提供者都需依赖该pom文件，由提供者实现接口，消费者调用使用。

请用Eclipse导入此工程即可。



# 功能实现逻辑

## 核心逻辑

本工程提供两类资源：

1、数据对象：SQL对象，用于对数据进行增删改查等处理时作为参数使用。

2、Service提供者接口：作为接口定义，供消费者调用；提供者（DBAgent）需要实现这些接口。

## SQL查询实体类

本实体类专为SQL批量操作设计，包含如下内容：

```java
  // sql语句
  private String sql;

  // 参数类型：object或者map
  private String paramType;

  // Object类型参数
  private Object[] objParams;

  // map类型参数
  private Map<String, Object> mapParams;

  // 操作类型。此值只在进行“批量处理”时有效，原子操作时无效
  private String modifyType;
```

 

## 接口类

### SQL-插入：insertBySQL

```java
  /**
   * 通过 SQL语句插入数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param objParams
   *      拼装参数对象数组，可以替换SQL中的“？”对应的值
   * @param strDBName
   *      数据库名
   * @return 是否插入成功 boolean
   */
  public boolean insertBySQL(String strSQL, Object[] objParams, String strDBName) ;
```

 

### SQL-修改：updateBySQL

```java
  /**
   * 通过SQL语句修改数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param objParams
   *      拼装参数对象数组，可以替换SQL中的“？”对应的值
   * @param strDBName
   *      数据库名
   * @return 是否修改成功 boolean
   */
  public boolean updateBySQL(String strSQL, Object[] objParams, String strDBName);
```

 

### SQL-删除：deleteBySQL

```java
  /**
   * 通过SQL语句删除数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param objParams
   *      拼装参数对象数组，可以替换SQL中的“？”对应的值
   * @param strDBName
   *      数据库名
   * @return 是否删除成功 boolean
   */
  public boolean deleteBySQL( String strSQL, Object[] objParams, String strDBName) ;
```

 

### SQL-查询：listBySQL（Object[]）

```java
  /**
   * 通过SQL语句查询数据，返回List<Object>对象。一条数据也通过此方法查询。
   * SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param objParams
   *      拼装参数对象数组，可以替换SQL中的“？”对应的值
   * @param strDBName
   *      数据库名
   * @return 是否删除成功 boolean
   */
  public List<Map<String, Object>> listBySQL(String strSQL, Object[] objParams, String strDBName);
```

 

### SQL-分页查询：listPageDataBySQL（Object[]）

```java
  /**
   * 通过SQL语句查询数据，返回List<Map>对象。一条数据也通过此方法查询。
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param intPageSize
   *      页面数据条数
   * @param intPageNum
   *      页面
   * @param strDbType
   *      数据库类型：oracle/mysql
   * @param strDBName
   *      数据库名
   * @return 以List<Map>方式组装的查询结果集 List<Map>
   */
  public List<Map<String, Object>> listPageDataBySQL( String strSQL, Object[] objParams,
     int intPageSize, int intPageNum, String strDbType, String strDBName);
```

 

### SQL-查询：listBySQL（Map）

```java
  /**

   \* 通过SQL语句查询数据，返回List<Map>对象。 该方法可传递in动态参数，具体操作方法如下： 示例：Sql ："select * from

   \* table where a =:name and b in(:names)" 参数由Map mapParams传递 , 其中Map中参数的key与

   \* SQL中"："匹配的参数名相对应,以下为组成方法

   \* 

   \* List list = new ArrayList(); list.add("name1"); list.add("name2");

   \* ........ mapParams.put("name","条件1") // 与：name对应

   \* mapParams.put("names",list) // 与：names对应 然后调用 ，

   \* listBySQL（strSQ,mapParamsL）

   \* 

   \* **@param** strSQL

   \*      输入的SQL语句

   \* **@param** mapParams

   \*      拼装参数对象数组，可以替换SQL中的“:name”对应的值

   \* **@param** strDBName

   \*      数据库名

   \* **@return** 是否删除成功 boolean

   */

  **public** List<Map<String, Object>> listBySQL(String strSQL, Map<String,Object> mapParams, String strDBName) ;
```

 

### SQL-分页查询：listPageDataBySQL（Map）

```java
  /**
   * 通过SQL语句查询数据，返回List<Map>对象。 该方法可传递in动态参数，具体操作方法如下： 示例：Sql ："select * from
   * table where a =:name and b in(:names)" 参数由Map mapParams传递 , 其中Map中参数的key与
   * SQL中"："匹配的参数名相对应,以下为组成方法
   * 
   * List list = new ArrayList(); list.add("name1"); list.add("name2");
   * ........ mapParams.put("name","条件1") // 与：name对应
   * mapParams.put("names",list) // 与：names对应 然后调用 ，
   * listBySQL（strSQ,mapParamsL）
   * 
   * @param strSQL
   *      输入的SQL语句
   * @param mapParams
   *      拼装参数对象数组，可以替换SQL中的“:name”对应的值
   * @param intPageSize
   *      页面数据条数
   * @param intPageNum
   *      页面
   * @param strDbType
   *      数据库类型：oracle/mysql
   * @param strDBName
   *      数据库名
   * @return 是否删除成功 boolean
   */

  public List<Map<String, Object>> listPageDataBySQL(String strSQL, Map<String,Object> mapParams, int intPageSize, int intPageNum, String strDbType, String strDBName) ;
```

 

### SQL-批量处理：batchModifyBySQL

```java
  /**
   * 通过封装的SQL语句对象，批量处理SQL操作请求。全部处理作为一个事务，失败时全部回滚。
   * 系统根据SQLObject中的参数类型（paramType）为map还是object，调用对应的参数值。
   * 
   * @param listSQLObject
   *      需要操作的SQL对象，其中包含了需要传入的参数对象，以及操作类型modifyType。
   * @param strDBName
   *      数据库名
   * @return 是否删除成功 boolean
   */
  public boolean batchModifyBySQL(List<SQLObject> listSQLObject, String strDBName) ;
```

