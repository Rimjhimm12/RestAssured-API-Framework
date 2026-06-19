package api.endpoints;

//Created for CURD operation
//In this class we will have all the API endpoints' logic of the framework

import api.payloads.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserEndPoints {
    //Create
    public static Response createUser(User payload){
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.post_url);
        return response;
    }

    //Retrieve User
    public static Response getUser(String username){
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                .get(Routes.get_url);
        return response;
    }

    //Update User

    public static Response updateUser(User payload, String username){
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .body(payload)
                .when()
                .put(Routes.put_url);
        return response;
    }

    //Delete User

    public static Response deleteUser(String username){
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                .delete(Routes.delete_url);
        return response;
    }



}
