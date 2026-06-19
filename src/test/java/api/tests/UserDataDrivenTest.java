package api.tests;

import api.endpoints.UserEndPoints;
import api.payloads.User;
import api.utilities.ExcelUtil;
import api.utilities.SheetUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserDataDrivenTest {
    User payload;

    @DataProvider
    public Object[][] getUserdata(){
        return ExcelUtil.getTestData(SheetUtil.USERDATA_EXCEL_SHEET_NAME);
    }

    @DataProvider
    public Object[][] getUsernameData(){
        return ExcelUtil.getColumnData(SheetUtil.USERDATA_EXCEL_SHEET_NAME, SheetUtil.USERNAME_COLUMN);
    }

    @Test(priority = 1, dataProvider = "getUserdata")
    public void createUserTest(String id, String username, String firstName,
                               String lastName, String email, String password, String phone, String userStatus ){
        payload = new User();
        payload.setId(Integer.parseInt(id));
        payload.setUsername(username);
        payload.setFirstName(firstName);
        payload.setLastName(lastName);
        payload.setEmail(email);
        payload.setPassword(password);
        payload.setPhone(phone);
        payload.setUserStatus(Integer.parseInt(userStatus));

       Response response =  UserEndPoints.createUser(payload);
       response.then().log().all();
       Assert.assertEquals(response.statusCode(),200);

    }

    @Test(priority = 2, dataProvider = "getUsernameData")
    public void readUserDetailsTest(String username){
        Response response = UserEndPoints.getUser(username);
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
    }

    @Test(priority = 3, dataProvider ="getUsernameData")
    public void deleteUserDetailsTest(String username){
        Response response = UserEndPoints.deleteUser(username);
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
    }




}
