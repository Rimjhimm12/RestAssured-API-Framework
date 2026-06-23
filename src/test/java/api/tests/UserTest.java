package api.tests;

import api.endpoints.UserEndPoints;
import api.endpoints.UserEndPointsWithProperties;
import api.payloads.User;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class UserTest {

    private static final Logger log = LogManager.getLogger(UserTest.class);

    Faker faker;
    User payload;

    @BeforeClass
    public void setUp() {
        faker = new Faker();
        payload = new User();
        payload.setId(faker.idNumber().hashCode());
        payload.setUsername(faker.name().username());
        payload.setFirstName(faker.name().firstName());
        payload.setLastName(faker.name().lastName());
        payload.setEmail(faker.internet().emailAddress());
        payload.setPassword(faker.internet().password(5, 10));
        payload.setPhone(faker.phoneNumber().cellPhone());
        payload.setUserStatus(1);
        log.info("Test data initialised — username: {}", payload.getUsername());
    }

    @Test(priority = 1)
    public void postUserTest(){
        log.info("=== postUserTest started ===");
        //Response response = UserEndPoints.createUser(payload);
        Response response = UserEndPointsWithProperties.createUserWithProperties(payload);
        Assert.assertEquals(response.getStatusCode(), 200);
        log.info("postUserTest PASSED — status code: {}", response.getStatusCode());
    }

    @Test(priority = 2)
    public void getUserByNameTest(){
        log.info("=== getUserByNameTest started ===");
        //Response response = UserEndPoints.getUser(this.payload.getUsername());
        Response response = UserEndPointsWithProperties.getUserWithProperties(this.payload.getUsername());
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("username"), this.payload.getUsername());
        log.info("getUserByNameTest PASSED — username verified: {}", this.payload.getUsername());
    }

    @Test(priority = 3)
    public void updateUserTest(){
        log.info("=== updateUserTest started ===");
        payload.setFirstName(faker.name().firstName());
        payload.setLastName(faker.name().lastName());
        payload.setEmail(faker.internet().emailAddress());
        log.info("Updating user: {}", this.payload.getUsername());
        //Response responseAfterUpdate = UserEndPoints.updateUser(payload, this.payload.getUsername());
        Response responseAfterUpdate = UserEndPointsWithProperties.updateUserWithProperties(payload,this.payload.getUsername());
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
        log.info("updateUserTest PASSED — user updated successfully");
    }

    @Test(priority = 4)
    public void deleteUserTest(){
        log.info("=== deleteUserTest started ===");
        //Response response = UserEndPoints.deleteUser(this.payload.getUsername());
        Response response = UserEndPointsWithProperties.deleteUserWithProperties(this.payload.getUsername());
        Assert.assertEquals(response.getStatusCode(), 200);
        log.info("User deleted — verifying 404 on subsequent GET");
        //Response afterDeleteResponse = UserEndPoints.getUser(this.payload.getUsername());
        Response afterDeleteResponse = UserEndPointsWithProperties.getUserWithProperties(this.payload.getUsername());

        Assert.assertEquals(afterDeleteResponse.getStatusCode(), 404);
        Assert.assertTrue(afterDeleteResponse.getBody().asString().contains("User not found"));
        log.info("deleteUserTest PASSED — user no longer exists");
    }
}




