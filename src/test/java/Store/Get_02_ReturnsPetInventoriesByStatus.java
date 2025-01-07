package Store;

import Base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.*;

public class Get_02_ReturnsPetInventoriesByStatus extends BaseTest {

    // TODO : Manuel baktım.Düzgün çalışıyor.
    @Test(priority = 1)
    public void getInventoryTest() {

        Response response = given()
                .spec(spec)
                .when()
                .get("/store/inventory");


        response.then()
                .statusCode(200);

        String available = response.path("available").toString();
        String pending = response.path("pending").toString();
        String sold = response.path("sold").toString();


        assertNotNull(available, "Available count should not be null");
        assertNotNull(pending, "Pending count should not be null");
        assertNotNull(sold, "Sold count should not be null");

        assertTrue("Available count should be >= 0", Integer.parseInt(available) >= 0);
        assertTrue("Pending count should be >= 0", Integer.parseInt(pending) >= 0);
        assertTrue("Sold count should be >= 0", Integer.parseInt(sold) >= 0);
    }

    /* TODO :Aslında yukarıda yaptığım  mantıkken olması gereken fakat
        manuel kontrol sağladığım yukarıda doğeuladığım üçünden farklı
        status verilebiliyor bunun kontrolü için yazma gereği duydum
    */

    @Test(priority = 2)
    public void getInventoryTest2() {

        Response response = given()
                .spec(spec)
                .when()
                .get("/store/inventory");

        response.then()
                .statusCode(200);


        Map<String, Integer> inventory = response.as(Map.class);

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            String status = entry.getKey();
            Integer count = entry.getValue();

//            System.out.println(status + ": " + count);

            assertNotNull(count.toString(), status + " count should not be null");
            assertTrue(status + " count should be >= 0", count >= 0);
        }
    }

    //negatif: get yerine post ile gönderdim
    @Test(priority = 3)
    public void getInventoryWrongMethodTest() {
        Response response = given()
                .spec(spec)
                .when()
                .post("/store/inventory");

        response.then()
                .statusCode(405);

    }

    //negatif: geçersiz JSON formatı gönderdim
    @Test(priority = 3)
    public void getInventoryInvalidEndpointTest() {
        Response response = given()
                .spec(spec)
                .when()
                .get("/store/invalidInventory");

        response.then()
                .statusCode(404);
    }

}
