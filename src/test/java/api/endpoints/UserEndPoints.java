package api.endpoints;

import api.payloads.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class UserEndPoints {

    private static final Logger log = LogManager.getLogger(UserEndPoints.class);

    public static Response createUser(User payload) {
        log.info("POST /user — username: {}", payload.getUsername());
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(payload)
                .when()
                .post(Routes.post_url);
        log.info("createUser response status: {}", response.getStatusCode());
        log.debug("createUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response getUser(String username) {
        log.info("GET /user/{}", username);
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                .get(Routes.get_url);
        log.info("getUser response status: {}", response.getStatusCode());
        log.debug("getUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response updateUser(User payload, String username) {
        log.info("PUT /user/{}", username);
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .body(payload)
                .when()
                .put(Routes.put_url);
        log.info("updateUser response status: {}", response.getStatusCode());
        log.debug("updateUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response deleteUser(String username) {
        log.info("DELETE /user/{}", username);
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                .delete(Routes.delete_url);
        log.info("deleteUser response status: {}", response.getStatusCode());
        log.debug("deleteUser response body: {}", response.getBody().asString());
        return response;
    }
}
