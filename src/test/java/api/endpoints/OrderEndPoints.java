package api.endpoints;

import api.payloads.Order;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderEndPoints {
    //Create order
    public static Response createOrder(Order payload){
        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .log().all()
                .when()
                .post(Routes.post_order_url);

        return response;
    }

    //Retrieve all inventory details
    public static Response getAllInventory(){
        Response response = given()
                .accept("application/json")
                .when()
                .get(Routes.get_all_inventory_url);
        return response;
    }

    //Retrieve an order details
    public static Response getOrder(int orderId){
        Response response = given()
                .accept("application/json")
                .pathParams("orderId", orderId)
                .when()
                .get(Routes.get_order_url);
        return response;
    }

    //Delete an order details
    public static Response deleteOrder(int orderId){
        Response response = given()
                .accept("application/json")
                .pathParams("orderId", orderId)
                .when()
                .delete(Routes.delete_order_url);
        return response;
    }
}
