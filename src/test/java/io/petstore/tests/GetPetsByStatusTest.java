package io.petstore.tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetPetsByStatusTest extends TestBase {

    @DataProvider(name = "dataAbove")
    public Object[][] statuses(){
        return new Object[][]{
                {"available"},
                {"sold"},
                {"pending"}
        };
    }

    @Test(dataProvider = "dataAbove")
    public void getPetsByStatus(String status) {
        Response response = given()
                .accept(ContentType.JSON)
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");

        Assert.assertEquals(response.statusCode(), 200);
        response.prettyPrint();
        List<Map<String,Object>> pets = response.jsonPath().get();

        pets.forEach(pet -> {
            Assert.assertEquals(pet.get("status"), status);
        });

    }
}
