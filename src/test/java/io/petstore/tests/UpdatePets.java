package io.petstore.tests;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class UpdatePets extends TestBase{




    @Test(invocationCount = 5)
    public void updatePetNameAndStatus(){
        int petId = 601;
        String expectedNewPetName = faker.animal().name();
        List<String> statuses = Arrays.asList("available","pending","sold");
        String expectedNewStatus = statuses.get(random.nextInt(statuses.size()));

        System.out.println("petId = " + petId);
        System.out.println("expectedNewPetName = " + expectedNewPetName);
        System.out.println("expectedNewStatus = " + expectedNewStatus);


        Map<String, String> info = new HashMap<>();
        info.put("name", expectedNewPetName);
        info.put("status", expectedNewStatus);

        Response response = given().accept(ContentType.JSON)
                .contentType(ContentType.URLENC)
                .formParams(info)
                .when()
                .post("/pet/"+petId);

        assertEquals(response.statusCode(), 200);

        Response getThatPet = given().accept(ContentType.JSON)
                .when().get("/pet/" + petId);


        assertEquals(getThatPet.statusCode(), 200);
        String actualName = getThatPet.jsonPath().getString("name");
        String actualStatus = getThatPet.jsonPath().getString("status");
        System.out.println("actualName = " + actualName);
        System.out.println("actualStatus = " + actualStatus);

        assertEquals(expectedNewPetName, actualName);
        assertEquals(expectedNewStatus, actualStatus);
    }
}
