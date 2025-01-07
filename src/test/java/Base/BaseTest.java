package Base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

public  class BaseTest {

    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        spec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setContentType("application/json")
                .build();
    }

}

