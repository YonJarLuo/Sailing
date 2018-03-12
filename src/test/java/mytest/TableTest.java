package mytest;

import com.jiayuan.client.DataBaseHelper;
import com.jiayuan.commen.DataBaseHelperImp;
import com.jiayuan.commen.response.ConnectionResponse;
import com.jiayuan.commen.response.TableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LuoYJ on 2018/3/6.
 */
public class TableTest {
    private ConnectionResponse response;
    private DataBaseHelper dbh;

    private String database = "java_test01";
    private String serviceID = "jdbc:mysql://localhost:3306";
    private String user = "root";
    private String password = "root";


    @Before
    public void beforetest(){
        dbh = new DataBaseHelperImp();
        response = dbh.connectionCreate(serviceID, user, password, "");
    }

    @After
    public void aftertest(){
        dbh.connectionClose(response.getConnectionID());
    }

    @Test
    public void testCreateTable(){
        String filedList = "id int(12) not null auto_increment primary key,username varchar(20) not null,age int(2) not null ";
        String table = "student";
        TableResponse tresponse = dbh.createTable(response.getConnectionID(), database, table, filedList, "");
        System.out.println(tresponse.getTaskStatus());
    }

    @Test
    public void testDeleteTable(){
        TableResponse tresponse = dbh.deleteTable(response.getConnectionID(), database, "student");
        System.out.println(tresponse.getTaskStatus());
    }

    @Test
    public void testAlterTable(){
        String sqlFile = "alter table student add sex varchar(4) not null";
        TableResponse tresponse = dbh.alterTable(response.getConnectionID(), database, sqlFile);
        System.out.println(tresponse.getTaskStatus());
    }

    @Test
    public void testTableList(){
        TableResponse tableResponse = dbh.listTable(response.getConnectionID(), database, "");
        System.out.println(tableResponse.getTableList());
    }

    @Test
    public void testQueryTable(){
        TableResponse student = dbh.queryTable(response.getConnectionID(), database, "student");
        System.out.println(student.getTableInfo());
    }
}
