package api.endpoints;
// This class will have all the API endpoints of the application

public class Routes {

    public static final String BASE_URL ="https://petstore.swagger.io/v2";

    //User Module

    public static String post_url = BASE_URL + "/user";
    public static String get_url = BASE_URL + "/user/{username}";
    public static String put_url = BASE_URL + "/user/{username}";
    public static String delete_url = BASE_URL + "/user/{username}";

    //Order Module
    public static String get_all_inventory_url =BASE_URL + "/store/inventory";
    public static String post_order_url = BASE_URL + "/store/order";
    public static String get_order_url = BASE_URL + "/store/order/{orderId}";
    public static String delete_order_url = BASE_URL + "/store/order/{orderId}";

    //pet Module

}
