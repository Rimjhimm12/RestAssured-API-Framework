package api.tests;

import api.endpoints.StoreEndPoints;
import api.payloads.Order;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OrderTest {
    Faker faker;
    Order payload;



    @BeforeTest
    public void setUp() {
        faker = new Faker();
        payload = new Order();
        payload.setId(faker.number().numberBetween(1, 10));
        payload.setPetId(faker.number().numberBetween(1, 10));
        payload.setQuantity(faker.number().numberBetween(1, 10));
        payload.setShipDate(java.time.OffsetDateTime.now().plusDays(1).toString());
        payload.setStatus("placed");
        payload.setComplete(false);
    }

    @Test
    public void orderTest() {
        Response  response = StoreEndPoints.createOrder(payload);
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
    }
}
