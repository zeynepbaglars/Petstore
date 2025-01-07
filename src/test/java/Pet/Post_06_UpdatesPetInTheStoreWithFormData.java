package Pet;

import Base.BaseTest;
import io.restassured.response.Response;
import Base.models.PetObject.Pet;
import net.datafaker.Faker;
import org.testng.annotations.*;
import Base.TestDataGenerator;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.*;

public class Post_06_UpdatesPetInTheStoreWithFormData  extends BaseTest{

        private static Pet newPet;
        private static final Faker faker = new Faker();

        @BeforeMethod
        public void beforeMethod() {
            newPet = TestDataGenerator.createRandomPet();
            Response createResponse = given()
                    .spec(spec)
                    .body(newPet)
                    .when()
                    .post("/pet");

            long createdPetId = createResponse.jsonPath().getLong("id");
            newPet.setId(createdPetId);
        }

        @Test(priority = 1)
        public void updatePetNameAndStatusWithForm() {
            String updatedName = faker.funnyName().name();
            String updatedStatus = "available";  // Geçerli bir durum

            // Pet'i form-data olarak güncelle
            Response updateResponse = given()
                    .spec(spec)
                    .formParam("name", updatedName)  // Adı güncelliyoruz
                    .formParam("status", updatedStatus)  // Durumu güncelliyoruz
                    .when()
                    .post("/pet/" + newPet.getId() + "/update");

            // Doğrulama
            updateResponse.then()
                    .statusCode(200)
                    .body("name", equalTo(updatedName))
                    .body("status", equalTo(updatedStatus));

            // Ek olarak Test
            String responseName = updateResponse.jsonPath().getString("name");
            String responseStatus = updateResponse.jsonPath().getString("status");
            assertEquals(responseName, updatedName, "Pet name doesn't match the updated name.");
            assertEquals(responseStatus, updatedStatus, "Pet status doesn't match the updated status.");
        }

        @Test(priority = 2)
        public void updatePetStatusWithInvalidData() {
            String updatedName = faker.funnyName().name();
            String invalidStatus = "unknown";  // Geçersiz durum

            // Geçersiz durumla güncellemeye çalışalım
            Response updateResponse = given()
                    .spec(spec)
                    .formParam("name", updatedName)  // Adı güncelliyoruz
                    .formParam("status", invalidStatus)  // Geçersiz durumu gönderiyoruz
                    .when()
                    .post("/pet/" + newPet.getId() + "/update");

            // Doğrulama
            updateResponse.then()
                    .statusCode(400)  // Geçersiz durumda hata bekliyoruz
                    .body("message", equalTo("Invalid status value"));
        }

        @Test(priority = 3)
        public void updatePetWithMissingName() {
            String updatedStatus = "sold";  // Geçerli bir durum

            // Adı eksik gönderme
            Response updateResponse = given()
                    .spec(spec)
                    .formParam("status", updatedStatus)  // Sadece durumu güncelliyoruz
                    .when()
                    .post("/pet/" + newPet.getId() + "/update");

            // Doğrulama
            updateResponse.then()
                    .statusCode(400)  // Hata bekliyoruz çünkü 'name' eksik
                    .body("message", equalTo("Invalid input"));
        }

        @Test(priority = 4)
        public void updatePetWithEmptyFormData() {
            // Boş form verisi gönderme
            Response updateResponse = given()
                    .spec(spec)
                    .formParam("name", "")
                    .formParam("status", "")
                    .when()
                    .post("/pet/" + newPet.getId() + "/update");

            // Doğrulama
            updateResponse.then()
                    .statusCode(400)  // Hata bekliyoruz
                    .body("message", equalTo("Invalid input"));
        }
    }


