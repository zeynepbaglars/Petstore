package Store;

import Base.BaseTest;
import Base.TestDataGenerator;
import io.restassured.response.*;
import net.datafaker.Faker;
import org.testng.annotations.*;
import Base.models.StoreObject.Store;
import java.time.*;
import java.time.temporal.ChronoUnit;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class Post_01_PlaceAnOrderForPet extends BaseTest {
    private static final Faker faker = new Faker();

    // TODO : Manuelini yaptım.Düzgün çalışıyor.
    @Test(priority = 1)
    public void placeOrderTest() {
        Store store = TestDataGenerator.createRandomStore();

        Response response = given()
                .spec(spec)
                .body(store)
                .when()
                .post("/store/order");

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("petId", equalTo(store.getPetId()))
                .body("quantity", equalTo(store.getQuantity()))
                .body("status", equalTo(store.getStatus()))
                .body("complete", equalTo(store.isComplete()));


        String actualShipDate = response.path("shipDate");
        String normalizedShipDate = actualShipDate.replace("+0000", "Z");
        Instant expectedInstant = ZonedDateTime.parse(store.getShipDate()).toInstant();
        Instant actualInstant = ZonedDateTime.parse(normalizedShipDate).toInstant();
        expectedInstant = expectedInstant.truncatedTo(ChronoUnit.SECONDS);
        actualInstant = actualInstant.truncatedTo(ChronoUnit.SECONDS);

        assertEquals(actualInstant, expectedInstant, "Ship Date does not match.");
        assertEquals((Integer) response.path("quantity"), store.getQuantity(), "Quantity does not match.");
        assertEquals((Long) response.path("petId"), store.getPetId(), "Pet ID does not match.");
        assertEquals(response.path("status"), store.getStatus(), "Status does not match.");
        assertEquals(response.path("complete"), store.isComplete(), "Complete flag does not match.");


    }


    /* todo:Manuelde yaptım - değer de kabul ediyor.
        Aslına bakarsanız aşağıdaki tüm testler için yaptım ne yapsamda hata vermedi.
        o yüzden dönecek mesajlardan emin olamadım.
     */

    @Test(priority = 2)
    public void placeOrderWithInvalidPetId() {

        Store invalidPetIdStore = TestDataGenerator.createRandomStore();
        invalidPetIdStore.setPetId(faker.number().negative());

        Response responseInvalidPetId = given()
                .spec(spec)
                .body(invalidPetIdStore)
                .when()
                .post("/store/order");

        responseInvalidPetId.then()
                .statusCode(400)
                .body("message", equalTo("Invalid Pet ID"));

             assertEquals(responseInvalidPetId.path("message"), "Invalid Pet ID", "Error message mismatch");
    }

    @Test(priority = 3)
    public void placeOrderWithInvalidQuantity() {

        Store invalidQuantityStore = TestDataGenerator.createRandomStore();
        invalidQuantityStore.setQuantity(faker.number().negative());

        Response responseInvalidQuantity = given()
                .spec(spec)
                .body(invalidQuantityStore)
                .when()
                .post("/store/order");

        responseInvalidQuantity.then()
                .statusCode(400)
                .body("message", equalTo("Invalid Quantity"));

          assertEquals(responseInvalidQuantity.path("message"), "Invalid Quantity", "Error message mismatch");
    }

    @Test(priority = 4)
    public void placeOrderWithInvalidStatus() {

        Store invalidStatusStore = TestDataGenerator.createRandomStore();
        invalidStatusStore.setStatus(faker.science().element());

        Response responseInvalidStatus = given()
                .spec(spec)
                .body(invalidStatusStore)
                .when()
                .post("/store/order");

        responseInvalidStatus.then()
                .statusCode(400)
                .body("message", equalTo("Invalid Status"));

        assertEquals(responseInvalidStatus.path("message"), "Invalid Status", "Error message mismatch");
    }

    @Test(priority = 5)
    public void placeOrderWithInvalidShipDate() {

        Store invalidShipDateStore = TestDataGenerator.createRandomStore();
        invalidShipDateStore.setShipDate(faker.zelda().game());

        Response responseInvalidShipDate = given()
                .spec(spec)
                .body(invalidShipDateStore)
                .when()
                .post("/store/order");

        responseInvalidShipDate.then()
                .statusCode(400)
                .body("message", equalTo("Invalid Ship Date"));


        assertEquals(responseInvalidShipDate.path("message"), "Invalid Ship Date", "Error message mismatch");
    }

    @Test(priority = 6)
    public void placeOrderWithEmptyBody() {

        Store emptyStore = new Store();
        Response responseEmptyBody = given()
                .spec(spec)
                .body(emptyStore)
                .when()
                .post("/store/order");

        responseEmptyBody.then()
                .statusCode(400)
                .body("message", containsString("not found"));

        String errorMessage = responseEmptyBody.path("message");
        assertTrue(errorMessage.contains("not found"), "Error message mismatch");
    }

}
