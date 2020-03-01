package com.thinkdifferent.DBAgent.entity;

/**
 * SQL查询实体类
 * Created by 张镇 on 2017/12/22.
 */
public class SQLObject implements java.io.Serializable {

    private static final long serialVersionUID = 4854868830385504412L;

    /** sql语句 */
    private String sql;
    /** 参数类型：object或者map */
    private String paramType;
    /** Object类型参数 */
    private Object[] objParams;
    /** 操作类型。此值只在进行“批量处理”时有效，原子操作时无效 */
    private String modifyType;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getModifyType() {
        return modifyType;
    }

    public void setModifyType(String modifyType) {
        this.modifyType = modifyType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getObjParams() {
        return objParams;
    }

    public void setObjParams(Object[] objParams) {
        this.objParams = objParams;
    }

}