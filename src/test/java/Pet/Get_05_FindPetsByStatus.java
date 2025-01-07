package Pet;

import Base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class Get_05_FindPetsByStatus extends BaseTest {

    @Test(priority = 1)
    public void findPetsByStatus() {
        // tek status gönderirsem
        String[] statusesToTest = {"available", "pending", "sold"};

        for (String status : statusesToTest) {
            System.out.println("Testing for status: " + status);

            Response response = given()
                    .spec(spec)
                    .queryParam("status", status)
                    .when()
                    .get("/pet/findByStatus");


            response.then()
                    .statusCode(200)
                    .body("status", everyItem(equalTo(status)));


            List<String> statusesFromResponse = response.jsonPath().getList("status");
            for (String s : statusesFromResponse) {
                assertEquals(s, status, "Status of the pet is incorrect.");
            }
        }
    }

    @Test(priority = 2)
    public void findPetsByMultipleStatus() {
    // tüm statusler aynı anda send ediyorum

        String status = "available,pending,sold";

        Response response = given()
                .spec(spec)
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");

        response.then()
                .statusCode(200);

        List<String> statuses = response.jsonPath().getList("status");
        for (String s : statuses) {
            assertTrue(s.equals("available") || s.equals("pending") || s.equals("sold"),
                    "Unexpected status: " + s);
        }

        assertEquals(response.getStatusCode(), 200, "Expected 200 status code.");
    }


    // todo : Aşağıdaki  manuel postmande 200 gönderdi !!
    @Test(priority =3)
    public void findPetsByInvalidStatus() {

        String status = "invalid_status";

        Response response = given()
                .spec(spec)
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");

        // 400 Bad Request bekleniyor
        response.then()
                .statusCode(400)
                .body("message", equalTo("Invalid status value"));

        assertEquals(response.getStatusCode(), 400, "Expected 400 status code for invalid status.");
        assertEquals(response.jsonPath().getString("message"), "Invalid status value", "Expected error message for invalid status.");
    }



    @Test(priority = 4)
    public void findPetsByStatusEmptyList() {
      //  aradığımız statüde hiç pet yoksa boş list döndürdü mü kontrolünü ssağladım
        String[] statusesToTest = {"available", "pending", "sold"};

        for (String status : statusesToTest) {
            System.out.println("Testing for status: " + status);

                     Response response = given()
                    .spec(spec)
                    .queryParam("status", status)
                    .when()
                    .get("/pet/findByStatus");


            response.then()
                    .statusCode(200);


            List<String> statuses = response.jsonPath().getList("status");

            // boş olan olmadığı ama ihtimal içinde olduğu içinde if içinde kontrol etmem gerekti
            if (statuses.isEmpty()) {
                System.out.println("No pets found with '" + status + "' status.");
                assertTrue(statuses.isEmpty(), "Expected empty result list for status: " + status);

            } else {
                for (String s : statuses) {
                    assertEquals(s, status, "Expected '" + status + "' status, but found: " + s);
                }
            }
        }
    }


    // todo : 5. testi manuel de deneedim  ama boş döndü
    //  200 alıyorum  400 dönmeliydi
    @Test(priority = 5)
    public void findPetsByStatusWithoutStatusParam() {

        Response response = given()
                .spec(spec)
                .when()
                .get("/pet/findByStatus");

        // 400 Bad Request bekleniyor
        response.then()
                .statusCode(400)
                .body("message", equalTo("Missing required parameter: status"));

        // mesajdan emin değildim hata dönmedi bu kısmı  yapay zekaya sordum
        assertEquals(response.getStatusCode(), 400, "Expected 400 status code for missing status parameter.");
        assertEquals(response.jsonPath().getString("message"), "Missing required parameter: status", "Expected error message for missing status parameter.");
    }
}
