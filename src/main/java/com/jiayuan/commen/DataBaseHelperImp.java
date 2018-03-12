package com.jiayuan.commen;

import com.jiayuan.client.DataBaseHelper;
import com.jiayuan.commen.response.ConnectionResponse;
import com.jiayuan.commen.response.DataResponse;
import com.jiayuan.commen.response.DatabaseResponse;
import com.jiayuan.commen.response.TableResponse;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.*;

/**
 * Created by LuoYJ on 2018/3/6.
 * 注意SQL语句之间的空格问题
 * 参数健壮性省略
 */
public class DataBaseHelperImp implements DataBaseHelper {
    static Logger logger = LoggerFactory.getLogger(DataBaseHelperImp.class);
    public Map<String,Connection> map = new HashMap<String, Connection>();  //共享map，存放connectionID,connection

    public ConnectionResponse connectionCreate(String serviceID, String user, String password, String extPareams) {

        ConnectionResponse response = new ConnectionResponse();
        ComboPooledDataSource cpds = new ComboPooledDataSource();  //创建数据连接池
        String id = UUID.randomUUID().toString().replace("-","");

        try {
            logger.info("------connectionCreate---------start");
            String driverName="com.mysql.jdbc.Driver";
            Class.forName(driverName); //加载驱动

            //设置连接池参数
            cpds.setDriverClass(driverName);
            cpds.setJdbcUrl(serviceID);
            cpds.setUser(user);
            cpds.setPassword(password);

            Connection connection = cpds.getConnection();
            map.put(id,connection);

        } catch (Exception e) {
            logger.error("---------有异常-----------");
            response.setErrorCode(001);
            response.setTaskStatus(0);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        response.setTaskStatus(1);
        response.setConnectionID(id);
        return response;
    }

    public ConnectionResponse connectionClose(String connectionID) {
        ConnectionResponse response = new ConnectionResponse();
        Connection connect = map.get(connectionID);

        try {
            logger.info("---------connectionClose-----start");
            connect.close();
            response.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            response.setTaskStatus(0);
            response.setErrorMsg(e.getMessage());
            response.setErrorCode(001);
            e.printStackTrace();
        }

        response.setConnectionID(connectionID);
        return response;
    }

    public DatabaseResponse createDataBase(String connectionID, String database, String expendParams) {
        DatabaseResponse response = new DatabaseResponse();
        Connection connect = map.get(connectionID);
        String sql ="CREATE DATABASE IF NOT EXISTS "+database;
        try {
            logger.info("---------create database start-----------");
            //主要
            Statement statement = connect.createStatement();
            //DDL、DML 都是.executeUpdate()
            // 查询.executeQuery(sql);
            statement.executeUpdate(sql);
            statement.close();
            response.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            response.setTaskStatus(0);
            response.setErrorCode(001);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        response.setDatabase(database);
        return response;
    }

    public DatabaseResponse deleteDataBase(String connectionID, String database) {
        DatabaseResponse response = new DatabaseResponse();
        Connection connect = map.get(connectionID);
        String sql ="DROP DATABASE "+database;

        try {
            logger.info("-----deleteDataBase----start");
            Statement statement = connect.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            response.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            response.setTaskStatus(0);
            response.setErrorCode(001);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        response.setDatabase(database);
        return response;
    }

    /**
     * @param connectionID
     * @param database
     * @param expendParams 扩展参数，此处为  字符集
     * @return
     */
    public DatabaseResponse modifyDatabase(String connectionID, String database, String expendParams) {
        DatabaseResponse response = new DatabaseResponse();
        Connection connect = map.get(connectionID);
        String sql ="ALTER DATABASE "+database+" CHARACTER SET "+expendParams;

        try {
            logger.info("-----modifyDatabase----start");
            Statement statement = connect.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            response.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            response.setTaskStatus(0);
            response.setErrorCode(001);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        response.setDatabase(database);
        return response;
    }

    public DatabaseResponse dataBaseList(String connectionID) {
        DatabaseResponse response = new DatabaseResponse();
        Connection connect = map.get(connectionID);
        String sql ="SHOW DATABASES ";

        StringBuilder databaseList = new StringBuilder();

        try {
            logger.info("-----dataBaseList----start");
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
//                System.out.println(resultSet.getString(1));
                databaseList.append(resultSet.getString(1)+",");
            }

            statement.close();
            response.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            response.setTaskStatus(0);
            response.setErrorCode(001);
            response.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        response.setDatabaseList(databaseList.toString());
        return response;
    }

    public TableResponse createTable(String connectionID, String database, String table, String filedList, String expendParams) {
        TableResponse tableResponse = new TableResponse();
        Connection connect = map.get(connectionID);

        try {
            logger.info("-----createTable----start");
            //.addBatch  添加批量SQL
            //.executeBath 执行所添加的SQL
            String sql1 = "USE "+database;
            String sql2 = "CREATE TABLE IF NOT EXISTS "+table+"("+ filedList +")";

            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            tableResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            tableResponse.setTaskStatus(0);
            tableResponse.setErrorCode(001);
            tableResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        tableResponse.setTable(table);
        return tableResponse;
    }

    /**
     * 此处是删除表drop  不是清空表数据delete
     */
    public TableResponse deleteTable(String connectionID, String database, String table) {
        TableResponse tableResponse = new TableResponse();
        Connection connect = map.get(connectionID);

        try {
            logger.info("-----deleteTable----start");
            String sql1 = "USE "+database;
            String sql2 = "DROP TABLE "+table;

            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            tableResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            tableResponse.setTaskStatus(0);
            tableResponse.setErrorCode(001);
            tableResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        tableResponse.setTable(table);
        return tableResponse;
    }

    public TableResponse alterTable(String connectionID, String database, String sqlFile) {
        TableResponse tableResponse = new TableResponse();
        Connection connect = map.get(connectionID);

        try {
            logger.info("-----alterTable----start");
            String sql1 = "USE "+database;
            String sql2 = sqlFile;

            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            tableResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            tableResponse.setTaskStatus(0);
            tableResponse.setErrorCode(001);
            tableResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        return tableResponse;
    }

    /**
     * @param connectionID
     * @param database
     * @param table 支持表名模糊查询，table为null时，查询所有
     * @return
     */
    public TableResponse listTable(String connectionID, String database, String table) {
        TableResponse tableResponse = new TableResponse();
        Connection connect = map.get(connectionID);

        StringBuilder tableList = new StringBuilder();
        try {
            logger.info("-----listTable----start");
            String sql1 = "USE "+database;
            String sql2 = "";
            if(table==null||table==""){
                sql2="SHOW TABLES";
            }else{
                sql2="SHOW TABLES LIKE %"+table+"%";
            }

            Statement statement = connect.createStatement();
            statement.executeUpdate(sql1);

            ResultSet resultSet = statement.executeQuery(sql2);
            while(resultSet.next()){
                tableList.append(resultSet.getString(1)+",");
            }

            statement.close();
            tableResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            tableResponse.setTaskStatus(0);
            tableResponse.setErrorCode(001);
            tableResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        tableResponse.setTableList(tableList.toString());
        return tableResponse;
    }

    public TableResponse queryTable(String connectionID, String database, String table) {
        TableResponse tableResponse = new TableResponse();
        Connection connect = map.get(connectionID);

        StringBuilder fileList = new StringBuilder();
        try {
            logger.info("-----queryTable----start");
            String sql1 = "USE "+database;
            String sql2 = "DESC "+table;

            Statement statement = connect.createStatement();
            statement.executeUpdate(sql1);

            ResultSet resultSet = statement.executeQuery(sql2);
            while(resultSet.next()){
                fileList.append(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getString(4)+" "+resultSet.getString(5)+" "+resultSet.getString(6)+",");
            }

            statement.close();
            tableResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.error("---------有异常-----------");
            tableResponse.setTaskStatus(0);
            tableResponse.setErrorCode(001);
            tableResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        tableResponse.setTableInfo(fileList.toString());
        return tableResponse;
    }

    /**
     * @param connectionID
     * @param database
     * @param table
     * @param fieldList 字段列表 （a,b,c）
     * @param dataList  数值列表  （1,2,3）
     * @return
     */
    public DataResponse insert(String connectionID, String database, String table, String fieldList, String dataList) {
        DataResponse dataResponse = new DataResponse();
        Connection connect = map.get(connectionID);
        try {
            logger.info("----------insert-------start");
            String sql1 = "USE "+database;
            String sql2 = "INSERT INTO "+table+fieldList+" VALUES"+dataList;
            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            dataResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.info("------有异常---------");
            dataResponse.setTaskStatus(0);
            dataResponse.setErrorCode(001);
            dataResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }

        return dataResponse;
    }

    public DataResponse delete(String connectionID, String database, String sqlFile) {
        DataResponse dataResponse = new DataResponse();
        Connection connect = map.get(connectionID);
        try {
            logger.info("----------delete-------start");
            String sql1 = "USE "+database;
            String sql2 = sqlFile;
            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            dataResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.info("------有异常---------");
            dataResponse.setTaskStatus(0);
            dataResponse.setErrorCode(001);
            dataResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return dataResponse;
    }

    public DataResponse update(String connectionID, String database, String sqlFile) {
        DataResponse dataResponse = new DataResponse();
        Connection connect = map.get(connectionID);
        try {
            logger.info("----------update-------start");
            String sql1 = "USE "+database;
            String sql2 = sqlFile;
            Statement statement = connect.createStatement();
            statement.addBatch(sql1);
            statement.addBatch(sql2);

            statement.executeBatch();
            statement.close();
            dataResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.info("------有异常---------");
            dataResponse.setTaskStatus(0);
            dataResponse.setErrorCode(001);
            dataResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        return dataResponse;
    }

    public DataResponse query(String connectionID, String database, String sqlFile) {
        DataResponse dataResponse = new DataResponse();
        Connection connect = map.get(connectionID);
        List<String> list = new ArrayList<String>();    //存储查询结果的每一行数据

        try {
            logger.info("----------query-------start");
            String sql1 = "USE "+database;
            String sql2 = sqlFile;

            if (sql2.contains("SHOW") || sql2.contains("show") || sql2.contains("DESC") || sql2.contains("desc") || sql2.contains("explain") || sql2.contains("EXPLAIN")){
                Statement statement = connect.createStatement();        //注意Statement与PreparedStatement 的关系与优劣势
                statement.executeUpdate(sql1);
                ResultSet resultSet = statement.executeQuery(sql2);
                ResultSetMetaData metaData = resultSet.getMetaData();       //获取查询的元数据
                int count = metaData.getColumnCount();                      //从元数据中获取 字段列数

                while (resultSet.next()){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1 ;i <= count; i++){
                        stringBuilder.append(resultSet.getString(i)+",");   //获取查询结果 按行拼接 然后再放入list中
                    }
                    list.add(stringBuilder.toString().substring(0,stringBuilder.length()-1));
                }
                statement.close();
            }else if (sql2.contains("SELECT") || sql2.contains("select")){
                PreparedStatement ps = connect.prepareStatement(sql2);      //.prepareStatement() 支持批处理，占位符语句
                ps.executeUpdate(sql1);
                ResultSet resultSet = ps.executeQuery();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();

                while (resultSet.next()){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1 ;i <= count; i++){
                        stringBuilder.append(resultSet.getString(i)+",");
                    }
                    list.add(stringBuilder.toString().substring(0,stringBuilder.length()-1));
                }
                ps.close();
            }else{
                dataResponse.setTaskStatus(0);
                dataResponse.setErrorMsg("非查询语句");
                dataResponse.setErrorCode(001);
            }

            dataResponse.setTaskStatus(1);
        } catch (SQLException e) {
            logger.info("------有异常---------");
            dataResponse.setTaskStatus(0);
            dataResponse.setErrorCode(001);
            dataResponse.setErrorMsg(e.getMessage());
            e.printStackTrace();
        }
        dataResponse.setResultList(list);
        return dataResponse;
    }
}
