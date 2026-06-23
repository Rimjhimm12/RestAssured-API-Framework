package api.tests;

import api.endpoints.OrderEndPoints;
import api.payloads.Order;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class OrderTest {
    Faker faker;
    Order payload;
    public static final Logger log = LogManager.getLogger(OrderTest.class);


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
        log.info("Test data initialised — UserID: {}", payload.getId());
    }

    @Test(priority = 1)
    public void orderTest() {
        log.info("=== orderTest started ===");
        Response  response = OrderEndPoints.createOrder(payload);
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
        log.info("createOrder PASSED — status code: {}", response.getStatusCode());
    }

    @Test(priority = 2)
    public void getALLOrderInventoryTest(){
        log.info("=== getALLOrderInventoryTest started ===");
        Response response = OrderEndPoints.getAllInventory();
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
        log.info("getAllInventory PASSED — status code: {}", response.body().asString());
    }

    @Test(priority = 3)
    public void getOrderByIdTest(){
        log.info("=== getOrderByIdTest started ===");
        Response response = OrderEndPoints.getOrder(payload.getId());
        log.info("Order Id is: {}", this.payload.getId());
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
        log.info("getOrderByIdTest PASSED — status code: {}", response.body().asString());

    }

    @Test(priority = 4)
    public void deleteOrderByIdTest(){
        log.info("=== deleteOrderByIdTest started ===");
        Response response = OrderEndPoints.deleteOrder(this.payload.getId());
        response.then().log().all();
        Assert.assertEquals(response.statusCode(),200);
        log.info("deleteOrderByIdTest PASSED — status code: {}", response.body().asString());

    }
}
