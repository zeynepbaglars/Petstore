package Base;

import Base.models.PetObject.Category;
import Base.models.PetObject.Pet;
import Base.models.PetObject.Tag;
import Base.models.StoreObject.Store;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static io.restassured.RestAssured.given;

public class TestDataGenerator {

    private static final Faker faker = new Faker();
    private static RequestSpecification spec;

    static {
        spec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2")
                .setContentType(ContentType.JSON)
                .build();
    }


    public static Pet createRandomPet() {
        Pet pet = new Pet();

        Category category = new Category();
        category.setName(faker.animal().name());
        pet.setCategory(category);

        pet.setName(faker.funnyName().name());

        Tag tag = new Tag();
        tag.setName(faker.book().title());
        pet.setTags(Collections.singletonList(tag));
        pet.setStatus(faker.options().option("available", "pending", "sold"));

        return pet;
    }


    public static long savePetAndGetId() {
        Pet pet=createRandomPet();
        Response response = given()
                .spec(spec)
                .body(pet)
                .when()
                .post("/pet");

        response.then().statusCode(200);
        return response.jsonPath().getLong("id");
    }


    public static Store createRandomStore() {
        long petId=savePetAndGetId();
        Store store = new Store();
        store.setPetId(petId);
        store.setQuantity(faker.number().numberBetween(1, 10));
        store.setShipDate(ZonedDateTime.now().plusDays(faker.number().numberBetween(1, 10))
                .format(DateTimeFormatter.ISO_INSTANT));
        store.setStatus(faker.options().option("placed", "approved", "delivered"));
        store.setComplete(faker.bool().bool());

        return store;
    }

    public static long saveOrderAndGetId() {
        Store store=createRandomStore();
        Response response = given()
                .spec(spec)
                .body(store)
                .when()
                .post("/store/order");

        response.then().statusCode(200);
        return response.jsonPath().getLong("id");
    }


}
