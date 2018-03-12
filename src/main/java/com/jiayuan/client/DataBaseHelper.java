package com.jiayuan.client;

import com.jiayuan.commen.response.ConnectionResponse;
import com.jiayuan.commen.response.DataResponse;
import com.jiayuan.commen.response.DatabaseResponse;
import com.jiayuan.commen.response.TableResponse;

/**
 * Created by LuoYJ on 2018/3/6.
 */
public interface DataBaseHelper {
    /**创建数据库连接
     * @param serviceID  数据库连接地址
     * @param user       数据库用户名
     * @param password   数据库连接密码
     * @param extPareams 扩展参数
     * @return
     */
    ConnectionResponse connectionCreate(String serviceID, String user, String password, String extPareams);

    ConnectionResponse connectionClose(String connectionID);
    /**
     * 创建数据库
     */
    DatabaseResponse createDataBase(String connectionID, String database, String expendParams);

    /**
     * 删除数据库
     */
    DatabaseResponse deleteDataBase(String connectionID, String database);

    /**
     *修改数据库 字符集
     */
    DatabaseResponse modifyDatabase(String connectionID, String database, String expendParams);

    /**
     * 查询数据库
     */
    DatabaseResponse dataBaseList(String connectionID);

    /**
     * 创建表
     */
    TableResponse createTable(String connectionID,String database,String table,String filedList,String expendParams);

    /**
     * 删除表
     */
    TableResponse deleteTable(String connectionID,String database,String table);

    /**
     * 修改表
     */
    TableResponse alterTable(String connectionID,String database,String sqlFile);

    /**
     * 获取所有列表
     */
    TableResponse listTable(String connectionID,String database,String table);

    /**
     * 获取所有列表
     */
    TableResponse queryTable(String connectionID,String database,String table);

    /**
     * 数据插入
     * fieldList:(字段列表)
     * dataList:(与之对应的数据列表)
     */
    DataResponse insert(String connectionID,String database,String table,String fieldList,String dataList);

    /**
     * 数据删除
     */
    DataResponse delete(String connectionID,String database,String sqlFile);

    /**
     * 数据修改，更新
     */
    DataResponse update(String connectionID,String database,String sqlFile);

    /**
     * 数据查询
     */
    DataResponse query(String connectionID,String database,String sqlFile);


}
