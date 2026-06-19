package api.tests;

import api.endpoints.UserEndPoints;
import api.payloads.User;
import api.utilities.ExcelUtil;
import api.utilities.SheetUtil;
import com.beust.ah.A;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UserTest {
    Faker faker;
    User payload;

    @BeforeClass
    public void setUp(){
        faker = new Faker();
        payload = new User();
        payload.setId(faker.idNumber().hashCode());
        payload.setUsername(faker.name().username());
        payload.setFirstName(faker.name().firstName());
        payload.setLastName(faker.name().lastName());
        payload.setEmail(faker.internet().emailAddress());
        payload.setPassword(faker.internet().password(5,10));
        payload.setPhone(faker.phoneNumber().cellPhone());
        payload.setUserStatus(1);
    }



    @Test(priority = 1)
    public void postUserTest(){
        Response response = UserEndPoints.createUser(payload);
        response.then().log().all();
        String ResponseBody = response.asString();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void getUserByNameTest(){
        Response response = UserEndPoints.getUser(this.payload.getUsername());
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("username"), this.payload.getUsername());

    }

    @Test(priority = 3)
    public void updateUserTest(){
        //Updating user data using payload
         payload.setFirstName(faker.name().firstName());
         payload.setLastName(faker.name().lastName());
         payload.setEmail(faker.internet().emailAddress());


         Response responseAfterUpdate = UserEndPoints.updateUser(payload, this.payload.getUsername());
         //Checking data after update
         UserEndPoints.getUser(this.payload.getUsername()).then().log().all();
         Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
     }

     @Test(priority = 4)
    public void deleteUserTest(){
        Response response = UserEndPoints.deleteUser(this.payload.getUsername());
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
        //After deleting the user, we will try to get the same user and check the response
        Response afterDeleteResponse = UserEndPoints.getUser(this.payload.getUsername());
        afterDeleteResponse.then().log().all();
        Assert.assertEquals(afterDeleteResponse.getStatusCode(), 404);
        Assert.assertTrue(afterDeleteResponse.getBody().asString().contains("User not found"));
     }


}




