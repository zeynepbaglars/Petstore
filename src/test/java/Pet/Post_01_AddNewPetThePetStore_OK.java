package Pet;

import Base.BaseTest;
import Base.TestDataGenerator;
import org.testng.annotations.Test;
import io.restassured.response.Response;

import Base.models.PetObject.Pet;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class Post_01_AddNewPetThePetStore_OK extends BaseTest {


    //todo: aşağıdaki testi manuel postmande yaptım ama  body değiştirsem bile aynı id dönüyor  .

    @Test(priority = 1)
    public void createPet() {
        Pet newPet = TestDataGenerator.createRandomPet();
        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo(newPet.getName()))
                .body("status", equalTo(newPet.getStatus()))
                .body("photoUrls", is(nullValue()))
                .body("category.name", equalTo(newPet.getCategory().getName()))
                .body("tags[0].name", equalTo(newPet.getTags().get(0).getName()))
                .body("status", isOneOf("available", "pending", "sold"));

        assertNotNull(response.path("id"), "Pet ID must not be null.");
        assertEquals(response.path("name"), newPet.getName(), "Your pet's name doesn't match.");
        assertEquals(response.path("status"), newPet.getStatus(), "Pet's status doesn't match.");
        System.out.println(response.jsonPath().getString("id"));


    }

    //todo: aşağıdaki testleri  manuel postmande yaptım ama hepsinde 200 dönüyor.
    @Test(priority = 2)
    public void createPetWithoutName() {
        Pet newPet = TestDataGenerator.createRandomPet();
        newPet.setName(null);
        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(405)
                .body("message", equalTo("Method Not Allowed"));

        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Method Not Allowed");
    }

    @Test(priority = 3)
    public void createPetWithInvalidStatus() {
        Pet newPet = TestDataGenerator.createRandomPet();
        newPet.setStatus("invalid_status");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(405)
                .body("message", equalTo("Method Not Allowed"));

        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Method Not Allowed");
    }

    @Test(priority = 4)
    public void createPetWithInvalidCategory() {
        Pet newPet = TestDataGenerator.createRandomPet();
        newPet.getCategory().setName("");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(405)
                .body("message", equalTo("Method Not Allowed"));

        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Method Not Allowed");
    }

    @Test(priority = 5)
    public void createPetWithEmptyTagName() {
        Pet newPet = TestDataGenerator.createRandomPet();
        newPet.getTags().get(0).setName("");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(405)
                .body("message", equalTo("Method Not Allowed"));

        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Method Not Allowed");
    }

    @Test(priority = 6)
    public void createPetWithoutTags() {
        Pet newPet = TestDataGenerator.createRandomPet();
        newPet.setTags(null);

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        response.then()
                .statusCode(405)
                .body("message", equalTo("Method Not Allowed"));

        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Method Not Allowed");
    }
}
