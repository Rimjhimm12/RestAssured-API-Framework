package api.endpoints;

import api.payloads.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;


import static io.restassured.RestAssured.given;

public class UserEndPointsWithProperties {

    private static final Logger log = LogManager.getLogger(UserEndPointsWithProperties.class);

//    1. Using Properties class
//    Properties prop;
//    public  Properties initProp() throws IOException {
//       prop = new Properties();
//       FileInputStream ip = new FileInputStream("./src/test/resources/routes.properties");
//       prop.load(ip);
//       return prop;
//    }

    //2. Using ResourceBundle
    private static ResourceBundle getURLs(){
        ResourceBundle routes = ResourceBundle.getBundle("routes");
        return routes;
    }


    public static Response createUserWithProperties(User payload){
        //UserEndPointsWithProperties userRoutes = new UserEndPointsWithProperties();
        log.info("Creating user with properties: {}", payload.getUsername());
        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(payload)
                .when()
                //.post(userRoutes.initProp().getProperty("post_url"));
                        .post(getURLs().getString("post_url"));

        log.info("createUser response status: {}", response.getStatusCode());
        log.debug("createUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response getUserWithProperties(String username) {
        //UserEndPointsWithProperties userRoutes = new UserEndPointsWithProperties();
        log.info("GET /user/{}", username);
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                //.get(userRoutes.initProp().getProperty("get_url"));
                        .get(getURLs().getString("get_url"));
        log.info("getUser response status: {}", response.getStatusCode());
        log.debug("getUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response updateUserWithProperties(User payload, String username) {
        //UserEndPointsWithProperties userRoutes = new UserEndPointsWithProperties();
        log.info("PUT /user/{}", username);
        Response response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .body(payload)
                .when()
                //.put(userRoutes.initProp().getProperty("put_url"));
                        .put(getURLs().getString("put_url"));
        log.info("updateUser response status: {}", response.getStatusCode());
        log.debug("updateUser response body: {}", response.getBody().asString());
        return response;
    }

    public static Response deleteUserWithProperties(String username) {
        //UserEndPointsWithProperties userRoutes = new UserEndPointsWithProperties();
        log.info("DELETE /user/{}", username);
        Response response = given()
                .accept(ContentType.JSON)
                .pathParams("username", username)
                .when()
                //.delete(userRoutes.initProp().getProperty("delete_url"));
        .delete(getURLs().getString("delete_url"));
        log.info("deleteUser response status: {}", response.getStatusCode());
        log.debug("deleteUser response body: {}", response.getBody().asString());
        return response;
    }
}


