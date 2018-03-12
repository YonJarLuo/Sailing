package mytest;

import com.jiayuan.client.DataBaseHelper;
import com.jiayuan.commen.DataBaseHelperImp;
import com.jiayuan.commen.response.ConnectionResponse;
import com.jiayuan.commen.response.DataResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by LuoYJ on 2018/3/7.
 */
public class dataTest {
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
    public void testInsert(){
        DataResponse iresponse = dbh.insert(response.getConnectionID(), database, "student", "(id,username,age,sex)", "(null,'php',17,'wm')");
        System.out.println(iresponse.getTaskStatus());
    }

    @Test
    public void testDelete(){
        DataResponse delete = dbh.delete(response.getConnectionID(), database, "delete from student where id=7");
        System.out.println(delete.getTaskStatus());
    }

    @Test
    public void testUpdate(){
        DataResponse update = dbh.update(response.getConnectionID(), database, "update student set age=18 where username='jia' ");
        System.out.println(update.getTaskStatus());

    }

    @Test
    public void testQuery(){
//        DataResponse query = dbh.query(response.getConnectionID(), database, "select * from student");
        DataResponse query = dbh.query(response.getConnectionID(), database, "show tables");
        System.out.println(query.getResultList());
    }

}
