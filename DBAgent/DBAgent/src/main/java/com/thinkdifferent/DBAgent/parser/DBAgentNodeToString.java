package com.thinkdifferent.DBAgent.parser;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.DropIndexNode;
import com.foundationdb.sql.parser.ParameterNode;
import com.foundationdb.sql.parser.TableName;
import com.foundationdb.sql.unparser.NodeToString;
import com.thinkdifferent.DBAgent.execption.DBAgentException;
import com.thinkdifferent.DBAgent.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重写 NodeToString 部分方法, 强制指定库
 */
public class DBAgentNodeToString extends NodeToString {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBAgentNodeToString.class);

    private String strDBName;

    public DBAgentNodeToString(String strDBName) {
        if (StringUtils.isBlank(strDBName)) {
            throw new DBAgentException("DBName is blank");
        }
        this.strDBName = strDBName;
    }

    @Override
    protected String parameterNode(ParameterNode node) throws StandardException {
        return "?";
    }

    /**
     * 表名强制添加库
     *
     * @param node 表名节点
     * @return 库名.表名
     * @throws StandardException
     */
    @Override
    protected String tableName(TableName node) throws StandardException {
        // TODO 验证表名是否可以包含【.】 示例：abc.user
        LOGGER.info("DBAgentNodeToString.tableName-->" + node.getTableName());
        return maybeQuote(strDBName) + "." + maybeQuote(node.getTableName());
    }

    /**
     * TODO 是否必须
     *
     * @param node
     * @return
     * @throws StandardException
     */
    @Override
    protected String dropIndexNode(DropIndexNode node) throws StandardException {
        StringBuilder sbSQL = new StringBuilder(node.statementToString());
        sbSQL.append(" ")
                .append(existenceCheck(node.getExistenceCheck()))
                .append(strDBName)
                .append(".")
                .append(maybeQuote(node.getIndexName()));
        return sbSQL.toString();
    }
}
