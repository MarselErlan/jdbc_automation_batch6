
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public  class APIUtils {
    public static String getToken(){
        String endPoint = "https://backend.cashwise.us/api/myaccount/auth/login";

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "erxmen.97@gmail.com");
        requestBody.put("password", "Erlan1824");
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .body(requestBody).post(endPoint);
        System.out.println(response.jsonPath().getString("jwt_token"));

        return response.jsonPath().getString("jwt_token");
    }
}