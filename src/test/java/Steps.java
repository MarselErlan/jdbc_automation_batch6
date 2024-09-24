
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.json.JSONObject;
import org.junit.Assert;

import java.sql.*;


public class Steps {

    Logger logger = LogManager.getLogger(Steps.class);
    RequestSpecification request;
    JSONObject requestBody = new JSONObject();
    Response response;
    String id;

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;





    @Given("base url {string}")
    public void base_url(String baseURL) {
        request = RestAssured.given()
                .baseUri(baseURL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
        logger.info("BaseUrl " + baseURL);

    }


    @Given("user has endpoint {string}")
    public void user_has_endpoint(String endpoint) {
        request = request.basePath(endpoint);

    }
    @When("user provides valid token")
    public void user_provides_valid_token() {
        request = request.auth().oauth2(APIUtils.getToken());
        logger.info("User provide valid token");

    }
    @When("user provides request body with {string} and {string}")
    public void user_provides_request_body_with_and(String key, String value) {
        requestBody.put(key, value);
        logger.info("added" + key + " with " + value + " in the request body");
        request = request.body(requestBody.toString());

    }
    @When("user hits POST request")
    public void user_hits_post_request() {
        response = request.post();
        logger.info("Post request sent");

        id = response.jsonPath().getString("id");
        logger.info("retrieved id: " + id);

    }
    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer statusCode) {
        logger.info(response.prettyPrint());
        Assert.assertEquals((int) statusCode, response.statusCode());
    }


    @Given("user set up connection to database")
    public void user_set_up_connection_to_database() throws SQLException {
        String url = "jdbc:postgresql://18.159.52.24:5434/postgres";
        String username = "cashwiseuser";
        String password = "cashwisepass";
        connection = DriverManager.getConnection(url, username, password);
        logger.info("successfully ");

    }

    @When("user sends the query {string}")
    public void user_sends_the_query(String query) {
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));
            resultSet = preparedStatement.executeQuery();
        }catch (SQLException e) {
        logger.error(e.getStackTrace());
        }


    }

    @Then("verify result set contains {string} with {string}")
    public void verify_result_set_contains_with(String columnName, String value) {

        try {
            boolean hasValue = false;
            logger.info("checking if the " + columnName + " contains " +  value);

            while (resultSet.next()) {
                if (resultSet.getString(columnName).equalsIgnoreCase(value)) {
                    logger.info("verified the " + columnName + " contains " + value);

                    hasValue = true;
                    break;
                }
                Assert.assertTrue(hasValue);
            }
        }catch (SQLException e){
            logger.error(e.getStackTrace());
            Assert.fail("SQL query issues, exiting the tests");
//            System.exit(0);
        }

    }


    @Then("verify result set contains id {string}")
    public void verify_result_set_contains_id(String columnName) {
        try {
            boolean hasValue = false;
            logger.info("verified the " + columnName + " contains " + id);

            while (resultSet.next()) {
                if (resultSet.getString(columnName).equalsIgnoreCase(id)) {
                    logger.info("verified the " + columnName + " contains " + id);
                    hasValue = true;
                    break;
                }
                Assert.assertTrue(hasValue);
            }
        }catch (SQLException e){
            logger.error(e.getStackTrace());

        }




    }


}
