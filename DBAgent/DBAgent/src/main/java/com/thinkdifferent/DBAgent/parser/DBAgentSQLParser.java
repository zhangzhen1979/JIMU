package com.thinkdifferent.DBAgent.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

import java.io.StringReader;
import java.util.List;

public class DBAgentSQLParser extends SelectDeParser {
    private String strSchemeName;

    public DBAgentSQLParser(String schemeName, ExpressionVisitor expressionVisitor, StringBuilder buffer) {
        super(expressionVisitor, buffer);
        this.strSchemeName = schemeName;
    }

    @Override
    public void visit(Table tableName) {
        super.visit(tableName);
        tableName.setSchemaName(strSchemeName);
    }

    /**
     * 处理 SELECT 语句
     *
     * @param strInputSQL   输入的需要处理的SQL语句
     * @param strSchemaName 数据库Schema名。MySQL为数据库名，Oracle为用户名
     * @return 处理后的SQL语句，已经将输入的Schema加入到语句中的所有表名之前
     */
    public static String sqlParserForSelect(String strInputSQL, String strSchemaName) throws JSQLParserException {
        Select select;
        StringBuilder buffer = new StringBuilder();
        select = (Select) CCJSqlParserUtil.parse(strInputSQL);
        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        DBAgentSQLParser sqlParser = new DBAgentSQLParser(strSchemaName, expressionDeParser, buffer);
        expressionDeParser.setSelectVisitor(sqlParser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(sqlParser);

        return buffer.toString();
    }

    /**
     * 处理 INSERT 语句
     *
     * @param strInputSQL   输入的需要处理的SQL语句
     * @param strSchemaName 数据库Schema名。MySQL为数据库名，Oracle为用户名
     * @return 处理后的SQL语句，已经将输入的Schema加入到语句中的所有表名之前
     */
    public static String sqlParserForInsert(String strInputSQL, String strSchemaName) throws JSQLParserException {
        StringBuilder buffer = new StringBuilder();
        Insert statement = null;
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(strInputSQL));
        statement = (Insert) stmt;

        Table t = statement.getTable();
        t.setSchemaName(strSchemaName);
        statement.setTable(t);

        Select select = statement.getSelect();
        if (select != null) {
            ExpressionDeParser expressionDeParser = new ExpressionDeParser();
            DBAgentSQLParser deparser = new DBAgentSQLParser(strSchemaName, expressionDeParser,
                    buffer);
            expressionDeParser.setSelectVisitor(deparser);
            expressionDeParser.setBuffer(buffer);
            select.getSelectBody().accept(deparser);
        }
        return statement.toString();
    }


    /**
     * 处理 UPDATE 语句
     *
     * @param strInputSQL   输入的需要处理的SQL语句
     * @param strSchemaName 数据库Schema名。MySQL为数据库名，Oracle为用户名
     * @return 处理后的SQL语句，已经将输入的Schema加入到语句中的所有表名之前
     */
    public static String sqlParserForUpdate(String strInputSQL, String strSchemaName) throws JSQLParserException {
        StringBuilder buffer = new StringBuilder();
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(strInputSQL));
        Update statement = (Update) stmt;

        List<Table> list = statement.getTables();
        for (Object object : list) {
            Table t = (Table) object;
            t.setSchemaName(strSchemaName);
        }
        statement.setTables(list);

        // 处理from
        FromItem fromItem = statement.getFromItem();
        if (fromItem != null) {
            Table t = (Table) fromItem;
            t.setSchemaName(strSchemaName);
        }
        // 处理join
        List<Join> joins = statement.getJoins();
        if (joins != null && joins.size() > 0) {
            for (Object object : joins) {
                Join t = (Join) object;
                Table rightItem = (Table) t.getRightItem();
                rightItem.setSchemaName(strSchemaName);
            }
        }

        ExpressionDeParser expressionDeParser = new ExpressionDeParser();
        UpdateDeParser p = new UpdateDeParser(expressionDeParser, null, buffer);
        expressionDeParser.setBuffer(buffer);
        p.deParse(statement);
        return statement.toString();

    }

    /**
     * 处理DELETE语句
     *
     * @param strInputSQL   输入的需要处理的SQL语句
     * @param strSchemaName 数据库Schema名。MySQL为数据库名，Oracle为用户名
     * @return 处理后的SQL语句，已经将输入的Schema加入到语句中的所有表名之前
     */
    public static String sqlParserForDelete(String strInputSQL, String strSchemaName) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(strInputSQL));
        Delete statement = (Delete) stmt;

        Table t = statement.getTable();
        t.setSchemaName(strSchemaName);
        statement.setTable(t);

        Expression where = statement.getWhere();
        if (where != null) {
            InExpression getRightItems = (InExpression) where;
            ItemsList rightItemsList = getRightItems.getRightItemsList();
            if (rightItemsList instanceof SubSelect) {
                SubSelect s = (SubSelect) rightItemsList;
                SelectBody selectBody = s.getSelectBody();
                Select se = (Select) CCJSqlParserUtil.parse(selectBody.toString());

                StringBuilder buffer = new StringBuilder();
                ExpressionDeParser expressionDeParser = new ExpressionDeParser();
                DBAgentSQLParser sqlParser = new DBAgentSQLParser(strSchemaName, expressionDeParser, buffer);
                expressionDeParser.setSelectVisitor(sqlParser);
                expressionDeParser.setBuffer(buffer);
                se.getSelectBody().accept(sqlParser);

                s.setSelectBody(se.getSelectBody());
            }
        }
        return statement.toString();
    }
}
