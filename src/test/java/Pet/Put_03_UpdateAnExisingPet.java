package Pet;

import Base.BaseTest;
import io.restassured.response.Response;
import Base.models.PetObject.Pet;
import net.datafaker.Faker;
import org.testng.annotations.*;
import Base.TestDataGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;

public class Put_03_UpdateAnExisingPet extends BaseTest {

    private static Pet newPet;
    private static final Faker faker = new Faker();

    @BeforeMethod
    public void beforeMethod() {
        newPet = TestDataGenerator.createRandomPet();
        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .post("/pet");

        long createdPetId = response.jsonPath().getLong("id");
        newPet.setId(createdPetId);


    }

    @Test(priority = 1)
    public void updateAllFields() {
        String updatedName = faker.funnyName().name();
        String updatedStatus = faker.options().option("available", "pending", "sold");
        String updatedCategoryName = faker.animal().name();
        String updatedTagName = faker.book().title();


        newPet.setName(updatedName);
        newPet.setStatus(updatedStatus);
        newPet.getCategory().setName(updatedCategoryName);
        newPet.getTags().get(0).setName(updatedTagName);

        Response response  = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        response .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("status", equalTo(updatedStatus))
                .body("category.name", equalTo(updatedCategoryName))
                .body("tags[0].name", equalTo(updatedTagName));
        assertEquals(response .jsonPath().getString("name"), updatedName, "Name doesn't match");
        assertEquals(response .jsonPath().getString("status"), updatedStatus, "Status doesn't match");
        assertEquals(response .jsonPath().getString("category.name"), updatedCategoryName, "Category name doesn't match");
        assertEquals(response .jsonPath().getString("tags[0].name"), updatedTagName, "Tag name doesn't match");
    }

    @Test(priority = 2)
    public void updatePetName() {
        String updatedName = faker.funnyName().name();
        newPet.setName(updatedName);

        Response response  = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        response .then()
                .statusCode(200)
                .body("name", equalTo(updatedName));
        assertEquals(response .jsonPath().getString("name"), updatedName, "Pet name doesn't match the updated name.");
    }



    @Test(priority = 3)
    public void updatePetId() {
        long updatedId = faker.number().randomNumber();
        newPet.setId(updatedId);

        Response response  = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");
        response .then()
                .statusCode(200)
                .body("id", equalTo(updatedId));
        assertEquals((long) response .jsonPath().getLong("id"), updatedId, "ID doesn't match");

    }
    @Test(priority = 4)
    public void updateTagName() {
        String updatedTagName = faker.book().title();
        newPet.getTags().get(0).setName(updatedTagName);

        Response response  = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        response .then()
                .statusCode(200)
                .body("tags[0].name", equalTo(updatedTagName));
        assertEquals(response .jsonPath().getString("tags[0].name"), updatedTagName, "Tag name doesn't match");
    }

    @Test(priority = 5)
    public void updatePetStatus() {
        String updatedStatus = faker.options().option("available", "pending", "sold");
        newPet.setStatus(updatedStatus);

        Response response  = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        response .then()
                .statusCode(200)
                .body("status", equalTo(updatedStatus));
        assertEquals(response .jsonPath().getString("status"), updatedStatus, "Status doesn't match");
    }

    /*
           Todo: Aşağıdaki tüm testleri Postman de manuel test ettim.
            Status code : 200 döndü ama
            400	-Invalid ID supplied
            404	-Pet not found
            405 -Validation exception dönmeliydi.
            */



    @Test(priority = 6)
    public void updateWithInvalidId() {
        long invalidId = -1;
        newPet.setId(invalidId);

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");

        assertEquals(response.statusCode(), 404);
        assertEquals(response.jsonPath().getString("message"), "Pet not found");
    }


    @Test(priority = 7)
    public void updateWithInvalidFieldTypes() {
        newPet.setName("12345");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        assertEquals(response.statusCode(), 400);
        assertEquals(response.jsonPath().getString("message"), "Invalid input");
    }

    @Test(priority = 8)
    public void updateWithEmptyName() {
        newPet.setName("");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");

        assertEquals(response.statusCode(), 400);
        assertEquals(response.jsonPath().getString("message"), "Invalid input");
    }

    @Test(priority = 9)
    public void updateWithInvalidStatus() {
        newPet.setStatus("unknown");

        Response response = given()
                .spec(spec)
                .body(newPet)
                .when()
                .put("/pet");


        assertEquals(response.statusCode(), 400);
        assertEquals(response.jsonPath().getString("message"), "Invalid status value");
    }

    @Test(priority = 10)
    public void updateWithMissingBody() {
        Response response = given()
                .spec(spec)
                .body("")
                .when()
                .put("/pet");

        assertEquals(response.statusCode(), 405);
        assertEquals(response.jsonPath().getString("message"), "Pet not found");
    }

}