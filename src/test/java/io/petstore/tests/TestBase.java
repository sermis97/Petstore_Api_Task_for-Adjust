package io.petstore.tests;

import com.github.javafaker.Faker;
import io.petstore.utility.ConfigurationReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

import java.util.Random;

public class TestBase {

    protected Faker faker = new Faker();
    protected Random random = new Random();

    @BeforeSuite
    public void setup(){
        RestAssured.baseURI = ConfigurationReader.get("baseUrl");
    }
}
