package com.jiayuan.commen.response;

/**
 * Created by LuoYJ on 2018/3/6.
 */
public class TableResponse extends Response {
    private String table;
    private String tableList;
    private String tableInfo;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTableList() {
        return tableList;
    }

    public void setTableList(String tableList) {
        this.tableList = tableList;
    }

    public String getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }
}
