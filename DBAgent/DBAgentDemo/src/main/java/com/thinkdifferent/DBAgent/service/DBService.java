package com.thinkdifferent.DBAgent.service;

import com.thinkdifferent.DBAgent.entity.SQLObject;

import java.util.List;
import java.util.Map;

public interface DBService {

    /**
     * 通过 SQL语句插入数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否插入成功 boolean
     */
    boolean insertBySQL(String strSQL, Object[] objParams, String strDBName);

    /**
     * 通过SQL语句修改数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否修改成功 boolean
     */
    boolean updateBySQL(String strSQL, Object[] objParams, String strDBName);

    /**
     * 通过SQL语句删除数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否删除成功 boolean
     */
    boolean deleteBySQL(String strSQL, Object[] objParams, String strDBName);

    /**
     * 通过SQL语句查询数据，返回List<Object>对象。一条数据也通过此方法查询。
     * SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否删除成功 boolean
     */
    List<Map<String, Object>> listBySQL(String strSQL, Object[] objParams, String strDBName);

    /**
     * 通过SQL语句查询数据，返回List<Map>对象。一条数据也通过此方法查询。
     *
     * @param strSQL      输入的SQL语句
     * @param intPageSize 页面数据条数
     * @param intPageNum  页面
     * @param strDbType   数据库类型：oracle/mysql
     * @param strDBName   数据库名
     * @return 以List<Map>方式组装的查询结果集 List<Map>
     */
    List<Map<String, Object>> listPageDataBySQL(String strSQL, Object[] objParams,
                                                int intPageSize, int intPageNum, String strDbType, String strDBName);

    /**
     * 通过SQL语句查询数据，返回List<Map>对象。 该方法可传递in动态参数，具体操作方法如下： 示例：Sql ："select * from
     * table where a =:name and b in(:names)" 参数由Map mapParams传递 , 其中Map中参数的key与
     * SQL中"："匹配的参数名相对应,以下为组成方法
     * <p>
     * List list = new ArrayList(); list.add("name1"); list.add("name2");
     * ........ mapParams.put("name","条件1") // 与：name对应
     * mapParams.put("names",list) // 与：names对应 然后调用 ，
     * listBySQL（strSQ,mapParamsL）
     *
     * @param strSQL    输入的SQL语句
     * @param mapParams 拼装参数对象数组，可以替换SQL中的“:name”对应的值
     * @param strDBName 数据库名
     * @return 是否删除成功 boolean
     */
    List<Map<String, Object>> listBySQL(String strSQL, Map<String, Object> mapParams, String strDBName);

    /**
     * 通过SQL语句查询数据，返回List<Map>对象。 该方法可传递in动态参数，具体操作方法如下： 示例：Sql ："select * from
     * table where a =:name and b in(:names)" 参数由Map mapParams传递 , 其中Map中参数的key与
     * SQL中"："匹配的参数名相对应,以下为组成方法
     * <p>
     * List list = new ArrayList(); list.add("name1"); list.add("name2");
     * ........ mapParams.put("name","条件1") // 与：name对应
     * mapParams.put("names",list) // 与：names对应 然后调用 ，
     * listBySQL（strSQ,mapParamsL）
     *
     * @param strSQL      输入的SQL语句
     * @param mapParams   拼装参数对象数组，可以替换SQL中的“:name”对应的值
     * @param intPageSize 页面数据条数
     * @param intPageNum  页面
     * @param strDbType   数据库类型：oracle/mysql
     * @param strDBName   数据库名
     * @return 是否删除成功 boolean
     */
    List<Map<String, Object>> listPageDataBySQL(String strSQL, Map<String, Object> mapParams,
                                                int intPageSize, int intPageNum, String strDbType, String strDBName);

    /**
     * 通过封装的SQL语句对象，批量处理SQL操作请求。全部处理作为一个事务，失败时全部回滚。
     * 系统根据SQLObject中的参数类型（paramType）为map还是object，调用对应的参数值。
     *
     * @param listSQLObject 需要操作的SQL对象，其中包含了需要传入的参数对象，以及操作类型modifyType。
     * @param strDBName     数据库名
     * @return 是否删除成功 boolean
     */
    boolean batchModifyBySQL(List<SQLObject> listSQLObject, String strDBName);

}
