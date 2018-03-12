package com.jiayuan.commen.response;

/**
 * Created by LuoYJ on 2018/3/6.
 */
public class DatabaseResponse extends Response {
    private String databaseList;

    private String database;

    public String getDatabaseList() {
        return databaseList;
    }

    public void setDatabaseList(String databaseList) {
        this.databaseList = databaseList;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }


}
