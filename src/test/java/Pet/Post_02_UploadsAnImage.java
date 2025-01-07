package Pet;

import Base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import Base.TestDataGenerator;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class Post_02_UploadsAnImage extends BaseTest {

    // todo: Manuel denemeyi unutma!!!!!!!
    @Test(priority = 1)
    public void uploadPetImage() {
        long petId = TestDataGenerator.savePetAndGetId();
        File imageFile = new File("src/test/resources/kediko.jpg");

        Response response = given()
                .spec(spec)
                .multiPart("file", imageFile)
                .formParam("petId", petId)
                .contentType("multipart/form-data")
                .when()
                .post("/pet/" + petId + "/uploadImage");


        response.then()
                .statusCode(200)
                .body("message", containsString("File uploaded to"))
                .body("code", equalTo(200)); // Code kontrolü


        assertEquals((int) response.path("code"), 200, "Photo upload failed.");
        assertTrue(response.body().asString().contains("File uploaded to"), "Upload message not successful");
    }

    // todo : Aşağıdaki test çalışmış olsaydı peti silip tekrardan istek atıp gatifini deneyecektim.
    @Test(priority = 2)
    public void deletePet() {

        long petId = TestDataGenerator.savePetAndGetId();


        Response deleteResponse = given()
                .spec(spec)
                .when()
                .delete("/pet/" + petId)
                .then()
                .statusCode(200) // Silme işlemi başarılı olduğunda
                .extract()
                .response();


        assertEquals((int) deleteResponse.path("code"), 200, "Pet deletion failed.");
        assertTrue(deleteResponse.body().asString().contains("Pet deleted"), "Delete message not found");
    }
}
