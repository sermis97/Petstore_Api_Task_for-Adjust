package io.petstore.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UserTests extends TestBase {


    /**
     * @description This map holds newly-created user data
     */
    Map<String, Object> expectedUserInfo = new HashMap<>();


    /**
     * @method post
     * @endpoint /user
     * @body expectedUserInfo
     * @description This method creates new random user and stores user info in the expectedUserInfo map.
    * */
    @Test(priority = 1)
    public void createNewUser() {

        String username = faker.name().username();

        String firstname = faker.name().firstName();
        String lastname = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = "123456";
        String phone = "+123456";
        double userStatus = 0d;

        expectedUserInfo.put("id", 0);
        expectedUserInfo.put("username", username);
        expectedUserInfo.put("firstName", firstname);
        expectedUserInfo.put("lastName", lastname);
        expectedUserInfo.put("email", email);
        expectedUserInfo.put("password", password);
        expectedUserInfo.put("phone", phone);
        expectedUserInfo.put("userStatus", userStatus);

        System.out.println("expectedUserInfo = " + expectedUserInfo);

        Response response = given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(expectedUserInfo)
                .when().post("/user");
        assertEquals(response.statusCode(), 200);

        expectedUserInfo.put("id",Double.parseDouble(response.jsonPath().get("message")));

        response.prettyPrint();

    }

    /**
     * @method GET
     * @endpoint /user/login
     * @description This test method runs after creating new user and verify new user can log in via api
     */
    @Test(priority = 2, dependsOnMethods = "createNewUser")
    public void loginWithNewUser() {
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("username", expectedUserInfo.get("username"));
        loginData.put("password", expectedUserInfo.get("password"));

        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON)
                .body(loginData)
                .when().get("/user/login");
        assertEquals(response.statusCode(),200);
        response.prettyPrint();
    }

    /**
     * @method GET
     * @endpoint /user/{username}
     * @description This test method runs after creating new user and verify that new user exists in the response
     */
    @Test(dependsOnMethods = "createNewUser")
    public void getUserByUsername(){

        String username = expectedUserInfo.get("username").toString();

        Response response = given().accept(ContentType.JSON).
                when().get("/user/"+ username);
        assertEquals(response.statusCode(), 200);
        response.prettyPrint();
        Map<String,Object> bodyAsMap = response.body().as(Map.class);
        assertEquals(bodyAsMap,expectedUserInfo);
    }


    /**
     * @method DELETE
     * @endpoint /user/{username}
     * @description This test method deletes the new user and verifies that the user is deleted by
     * sending get request to '/user/login' endpoint and checking response
     */
    @Test(priority = 3, dependsOnMethods = "loginWithNewUser")
    public void deleteUser() {

        Response response = given().accept(ContentType.JSON).when().delete("/user/" + expectedUserInfo.get("username"));
        assertEquals(response.statusCode(),200);

        Response verificationResponse = given().accept(ContentType.JSON).when().get("/user/"+ expectedUserInfo.get("username"));
        assertEquals(verificationResponse.statusCode(), 404);
    }
}
