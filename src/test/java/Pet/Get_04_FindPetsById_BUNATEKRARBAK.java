package Pet;

import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;
import net.datafaker.Faker;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Get_04_FindPetsById_BUNATEKRARBAK {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test
    public void getPetById_ValidId() {

        Faker faker = new Faker();
        long randomPetId = faker.number().numberBetween(1, 10000);


        getPetById(randomPetId);
    }

    public static void getPetById(long petId) {

        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .build();


        given()
                .spec(reqSpec)
                .when()
                .get("/pet/{petId}", petId)
                .then()
                .statusCode(200)
                .log().all();
    }
    }

