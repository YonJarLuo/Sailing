package mytest;

import com.jiayuan.client.DataBaseHelper;
import com.jiayuan.commen.DataBaseHelperImp;
import com.jiayuan.commen.response.ConnectionResponse;
import com.jiayuan.commen.response.DatabaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LuoYJ on 2018/3/6.
 */
public class DataBaseTest {

    private ConnectionResponse response;
    private DataBaseHelper dbh;
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
    public void testCreateDatabase(){
        String id = response.getConnectionID();
        DatabaseResponse dresponse = dbh.createDataBase(id,"java_test02", "");
        System.out.println(dresponse.getTaskStatus()+"--创建数据库--"+dresponse.getDatabase());

    }

    @Test
    public void testDeleteDataBase(){
        String id = response.getConnectionID();
        dbh.deleteDataBase(id,"java_test02");
    }

    @Test
    public void testmodifyDatabase(){
        DatabaseResponse databaseResponse = dbh.modifyDatabase(response.getConnectionID(), "java_test01", "gbk");
        System.out.println(databaseResponse.getTaskStatus());
    }

    @Test
    public void testDatabaseList(){
        DatabaseResponse databaseResponse = dbh.dataBaseList(response.getConnectionID());
        System.out.println(databaseResponse.getDatabaseList());
    }

}
