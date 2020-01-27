package com.thinkdifferent.DBAgent.parser;

import com.foundationdb.sql.unparser.NodeToString;

public class SQLNode2String extends NodeToString {

    @SuppressWarnings("unused")
	private String dbName;

    public SQLNode2String(String dbName) {
        this.dbName = dbName;
    }


}
