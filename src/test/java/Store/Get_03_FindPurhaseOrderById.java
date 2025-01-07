package Store;

import Base.BaseTest;
import io.restassured.response.*;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class Get_03_FindPurhaseOrderById extends BaseTest {

    // TODO : Manuel testini yaptım.Düzgün çalışıyor.
    @Test(priority = 1)
    public void getOrderByIdTest() {


        int orderId = (int) (Math.random() * 8) + 2;


        Response response = given()
                .spec(spec)
                .pathParam("orderId", orderId)
                .when()
                .get("/store/order/{orderId}");


        response.then()
                .statusCode(200);


        int actualOrderId = response.path("id");
        assertEquals(actualOrderId, orderId, "Order ID does not match");

        String status = response.path("status");
        assertNotNull(status, "Status should not be null");

//        System.out.println("Response Body: " + response.asString());
    }


    @Test(priority = 2)
    public void getOrderByIdWithDynamicInvalidIdTest() {

        int invalidOrderId = (int) (Math.random() * 1000000);

        Response response = given()
                .spec(spec)
                .pathParam("orderId", invalidOrderId)
                .when()
                .get("/store/order/{orderId}");

        response.then()
                .statusCode(404);

        String errorMessage = response.path("message");
        assertEquals(errorMessage, "Order not found", "Error message doesn't match");
    }

    @Test(priority = 3)
    public void getOrderByIdWithDynamicNegativeIdTest() {

        int negativeOrderId = (int) (Math.random() * -1000000);

        Response response = given()
                .spec(spec)
                .pathParam("orderId", negativeOrderId)
                .when()
                .get("/store/order/{orderId}");

        response.then()
                .statusCode(404);

        String errorMessage = response.path("message");
        assertEquals(errorMessage, "Order not found", "Error message doesn't match");
    }





}
