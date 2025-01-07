package Store;

import Base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Delete_04_DeletePurchaseOrderById_BUNATEKRARBAK extends BaseTest {


    @Test(priority = 1)
    public void deleteOrder_ValidId_ShouldReturn200() {
       int orderId = 6;  // Geçerli bir order ID

        // Order'ı silme işlemi
        given()
                .pathParam("orderId", orderId)
                .when()
                .delete("/store/order/{orderId}")
                .then()
                .statusCode(200)  // Başarılı silme, 200 OK bekleniyor
                .body("message", equalTo("Order deleted successfully"));
    }

    //negatif orderId ile test yaz
}
