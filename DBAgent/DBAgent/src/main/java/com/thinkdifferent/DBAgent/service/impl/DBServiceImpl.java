package com.thinkdifferent.DBAgent.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.StatementNode;
import com.thinkdifferent.DBAgent.constant.DBAgentConstant;
import com.thinkdifferent.DBAgent.entity.SQLObject;
import com.thinkdifferent.DBAgent.execption.DBAgentException;
import com.thinkdifferent.DBAgent.parser.DBAgentNodeToString;
import com.thinkdifferent.DBAgent.parser.DBAgentSQLParser;
import com.thinkdifferent.DBAgent.service.DBService;
import net.sf.json.JSONObject;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(version = "1.0.0", timeout = 10000, interfaceName = "com.thinkdifferent.DBAgent.service.DBService")
public class DBServiceImpl implements DBService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DBServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 将数据库Schema名称加入到SQL语句中数据表名的前面
     *
     * @param strSchemeName 数据库Schema名。MySQL为数据库名，Oracle为用户名
     * @param strInputSQL   输入的需要处理的SQL语句
     * @return 处理后的SQL语句，已经将输入的Schema加入到语句中的所有表名之前
     */
    private String addSchema2SQL(String strSchemeName, String strInputSQL) throws JSQLParserException {
        String strOutputSQL = "";
        // 如果输入的Schema不为空，并且输入的SQL语句不为空，则处理SQL语句
        if (null != strSchemeName && !strSchemeName.trim().isEmpty() && null != strInputSQL && !strInputSQL.trim().isEmpty()) {
            // 解析SQL语句，判断SQL语句的类型。查询、更新、插入、删除，等DML语句单独调用对应的处理方法；
            Statement stmtJsql = CCJSqlParserUtil.parse(strInputSQL);
            if (stmtJsql instanceof Select) {
                strOutputSQL = DBAgentSQLParser.sqlParserForSelect(strInputSQL, strSchemeName);
            } else if (stmtJsql instanceof Update) {
                strOutputSQL = DBAgentSQLParser.sqlParserForUpdate(strInputSQL, strSchemeName);
            } else if (stmtJsql instanceof Insert) {
                strOutputSQL = DBAgentSQLParser.sqlParserForInsert(strInputSQL, strSchemeName);
            } else if (stmtJsql instanceof Delete) {
                strOutputSQL = DBAgentSQLParser.sqlParserForDelete(strInputSQL, strSchemeName);
            } else {
                // 其他DDL语句调用通用的处理方法。
                SQLParser parser = new SQLParser();
                DBAgentNodeToString nodeToString = new DBAgentNodeToString(strSchemeName);

                StatementNode stmt;
                try {
                    stmt = parser.parseStatement(strInputSQL);
                    stmt.treePrint();
                    strOutputSQL = nodeToString.toString(stmt);
                } catch (StandardException e) {
                    e.printStackTrace();
                }
            }
        }

        return strOutputSQL;
    }

    /**
     * 通过 SQL语句插入数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @return 是否插入成功 boolean
     */
    @Override
    public boolean insertBySQL(String strSQL, Object[] objParams, String strScheme) {
        try {
            Statement stmtJSQL = CCJSqlParserUtil.parse(strSQL);
            if (stmtJSQL instanceof Insert) {
                return modifyBySQL(strSQL, objParams, strScheme);
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过SQL语句修改数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否修改成功 boolean
     */
    @Override
    public boolean updateBySQL(String strSQL, Object[] objParams, String strDBName) {
        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Update语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Update) {
                return modifyBySQL(strSQL, objParams, strDBName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 通过SQL语句删除数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否删除成功 boolean
     */
    @Override
    public boolean deleteBySQL(String strSQL, Object[] objParams, String strDBName) {
        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Delete语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Delete) {
                return modifyBySQL(strSQL, objParams, strDBName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 通过SQL语句查询数据，返回List<Object>对象。一条数据也通过此方法查询。
     * SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @param strDBName 数据库名
     * @return 是否删除成功 boolean
     */
    @Override
    public List<Map<String, Object>> listBySQL(String strSQL, Object[] objParams, String strDBName) {
        List<Map<String, Object>> listData = null;
        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Select语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Select) {
                // 如果数据库连接成功（不为空）
                if (jdbcTemplate != null) {
                    strSQL = this.addSchema2SQL(strDBName, strSQL);
                    //System.out.println(strSQL);
                    // 执行查询
                    // 返回结果对象处理的回调函数
                    listData = jdbcTemplate.query(strSQL, objParams, (rs, intRowNum) -> {
                        // 单个记录对象，用Map拼装
                        Map<String, Object> mapRow = new HashMap<>();
                        // 获取当前查询表的字段列表
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int intFieldCount = rsmd.getColumnCount();
                        // 循环处理每个字段，将字段值put到Map中
                        for (int i = 0; i < intFieldCount; i++) {
                            mapRow.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i + 1)));
                        }

                        return mapRow;
                    });
                    LOGGER.info("listBySQL method, end, params-->" + Arrays.toString(objParams));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    /**
     * 通过SQL语句查询数据，返回List<Map>对象。一条数据也通过此方法查询。
     *
     * @param strSQL      输入的SQL语句
     * @param objParams
     * @param intPageSize 页面数据条数
     * @param intPageNum  页面
     * @param strDbType   数据库类型：oracle/mysql
     * @param strDBName   数据库名
     * @return 以List<Map>方式组装的查询结果集 List<Map>
     */
    @Override
    public List<Map<String, Object>> listPageDataBySQL(String strSQL, Object[] objParams, int intPageSize
            , int intPageNum, String strDbType, String strDBName) {
        List<Map<String, Object>> listData = null;

        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Select语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Select) {
                // 如果数据库连接成功（不为空）
                if (jdbcTemplate != null) {
                    Map<String, Integer> mapValue = getRowNumberInPage(intPageSize, intPageNum, strDbType);
                    // 处理SQL语句，加入分页处理
                    if (DBAgentConstant.DB_MYSQL.equalsIgnoreCase(strDbType)) {
                        strSQL = strSQL + " limit " + mapValue.get("beginRecordNumber") + ","
                                + mapValue.get("endRecordNumber");
                    } else if (DBAgentConstant.DB_ORACLE.equalsIgnoreCase(strDbType)) {
                        strSQL = "select * from " + "(select ROWNUM as rowno, t.* from (" + strSQL + ") t "
                                + "where ROWNUM <= " + mapValue.get("endRecordNumber") + ") d " + "where d.rowno >= "
                                + mapValue.get("beginRecordNumber");
                    } else {
                        return null;
                    }

                    listData = listBySQL(strSQL, objParams, strDBName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

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
    @Override
    public List<Map<String, Object>> listBySQL(String strSQL, Map<String, Object> mapParams, String strDBName) {
        List<Map<String, Object>> listData = null;

        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Select语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Select) {
                // 如果数据库连接成功（不为空）
                if (jdbcTemplate != null) {
                    NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                            jdbcTemplate);
                    strSQL = this.addSchema2SQL(strDBName, strSQL);
                    listData = namedParameterJdbcTemplate.queryForList(strSQL, mapParams);
                    LOGGER.info("listBySQL method, end, params-->" + JSONObject.fromObject(mapParams));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

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
    @Override
    public List<Map<String, Object>> listPageDataBySQL(String strSQL, Map<String, Object> mapParams, int intPageSize
            , int intPageNum, String strDbType, String strDBName) {
        List<Map<String, Object>> listData = null;

        try {
            Statement stmtJsql = CCJSqlParserUtil.parse(strSQL);
            // 如果当前的SQL语句是Select语句，则执行；否则返回失败状态
            if (stmtJsql instanceof Select) {
                strSQL = this.addSchema2SQL(strDBName, strSQL);

                // 如果数据库连接成功（不为空）
                if (jdbcTemplate != null) {
                    Map<String, Integer> mapValue = getRowNumberInPage(intPageSize, intPageNum, strDbType);
                    // 处理SQL语句，加入分页处理
                    if (DBAgentConstant.DB_MYSQL.equalsIgnoreCase(strDbType)) {
                        strSQL = strSQL + " limit " + mapValue.get("beginRecordNumber") + ","
                                + mapValue.get("endRecordNumber");
                    } else if (DBAgentConstant.DB_ORACLE.equalsIgnoreCase(strDbType)) {
                        strSQL = "select * from " + "(select ROWNUM as rowno, t.* from (" + strSQL + ") t "
                                + "where ROWNUM <= " + mapValue.get("endRecordNumber") + ") d " + "where d.rowno >= "
                                + mapValue.get("beginRecordNumber");
                    } else {
                        return null;
                    }

                    listData = listBySQL(strSQL, mapParams, strDBName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    /**
     * 通过封装的SQL语句对象，批量处理SQL操作请求。全部处理作为一个事务，失败时全部回滚。
     * 系统根据SQLObject中的参数类型（paramType）为map还是object，调用对应的参数值。
     *
     * @param listSQLObject 需要操作的SQL对象，其中包含了需要传入的参数对象，以及操作类型modifyType。
     * @param strDBName     数据库名
     * @return 是否删除成功 boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = DBAgentException.class)
    public boolean batchModifyBySQL(List<SQLObject> listSQLObject, String strDBName) {
        boolean blnFlag = false;
        // 如果数据库连接成功（不为空）
        if (jdbcTemplate != null && listSQLObject != null && listSQLObject.size() > 0) {
            for (SQLObject sqlObject : listSQLObject) {
                // 如果当前SQL对象的“操作类型”值为空，则直接终止执行，回滚操作。
                if (sqlObject.getModifyType() == null || "".equals(sqlObject.getModifyType())) {
                    throw new DBAgentException("ModifyType is NULL!");
                } else {
                    // 否则，正常执行对应的操作
                    if ("insert".equalsIgnoreCase(sqlObject.getModifyType())) {
                        blnFlag = insertBySQL(sqlObject.getSql(), sqlObject.getObjParams(), strDBName);
                        if (!blnFlag) {
                            throw new DBAgentException("Insert by SQL fail!");
                        }
                    } else if ("update".equalsIgnoreCase(sqlObject.getModifyType())) {
                        blnFlag = updateBySQL(sqlObject.getSql(), sqlObject.getObjParams(), strDBName);
                        if (!blnFlag) {
                            throw new DBAgentException("Update by SQL fail!");
                        }
                    } else if ("delete".equalsIgnoreCase(sqlObject.getModifyType())) {
                        blnFlag = deleteBySQL(sqlObject.getSql(), sqlObject.getObjParams(), strDBName);
                        if (!blnFlag) {
                            throw new DBAgentException("Delete by SQL fail!");
                        }
                    } else {
                        // 如果传入的执行类型值不对，也抛出异常，回滚执行
                        throw new DBAgentException("ModifyType is not correct!");
                    }
                }
            }
        }
        return blnFlag;
    }


    /**
     * 通用方法：通过 SQL语句插入/修改/删除数据。SQL中的参数必须使用？作为占位符，所有参数必须通过参数列表传入
     * INSERT | DELETE | UPDATE
     *
     * @param strSQL    输入的SQL语句
     * @param objParams 拼装参数对象数组，可以替换SQL中的“？”对应的值
     * @return 是否插入成功 boolean
     */
    private boolean modifyBySQL(String strSQL, Object[] objParams, String strScheme) {
        // 如果数据库连接成功（不为空）
        if (jdbcTemplate == null) {
            LOGGER.error("modifyBySQL method, get jdbcTemplate null");
            return false;
        }
        try {
            strSQL = this.addSchema2SQL(strScheme, strSQL);
            // 执行查询
            jdbcTemplate.update(strSQL, objParams);
            LOGGER.info("modifyBySQL method, end, params-->" + Arrays.toString(objParams));
            return true;
        } catch (Exception e) {
            LOGGER.error("modifyBySQL method error: ", e);
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 根据传入的分页参数，计算总页数、开始记录条数、结束记录条数
     *
     * @param intPageSize 页面大小，即页面显示条数
     * @param intPageNum  页码，即当前需要显示数据的页码
     * @param strDBType   数据库类型。mysql/oracle
     * @return 以Map拼装的结果： beginRecordNumber，开始记录条数、endRecordNumber，结束记录条数
     * Map<String,Integer>
     */
    private Map<String, Integer> getRowNumberInPage(int intPageSize, int intPageNum, String strDBType) {
        if (intPageSize < 0) {
            intPageSize = 1;
        }
        if (intPageNum < 0) {
            intPageNum = 1;
        }

        Map<String, Integer> mapValue = new HashMap<String, Integer>();
        // 开始条数
        int intBeginReocrdNumber = 1 + (intPageSize * (intPageNum - 1));
        // 结束条数
        int intEndReocrdNumber = intPageSize * intPageNum;

        if ("mysql".equalsIgnoreCase(strDBType)) {
            intBeginReocrdNumber--;
            intEndReocrdNumber--;
        }

        mapValue.put("beginRecordNumber", intBeginReocrdNumber);
        mapValue.put("endRecordNumber", intEndReocrdNumber);

        return mapValue;
    }
}
