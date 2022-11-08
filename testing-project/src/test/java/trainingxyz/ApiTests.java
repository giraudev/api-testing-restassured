package trainingxyz;

import models.Product;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void getCategories(){
        String endpoint = "http://localhost:8888/api_testing/category/read.php";
        var response = given().when().get(endpoint)
                .then().assertThat().statusCode(200);
        response.log().body();
    }

    @Test
    public void getProducts(){
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        given().when().get(endpoint)
                .then().assertThat().statusCode(200).log().body();
    }

    @Test
    public void getProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response =
                given().queryParam("id",1002).
                        when().get(endpoint).then().assertThat()
                        .statusCode(200)
                        .body("id",equalTo("1002"))
                        .body("name", equalTo("Water Bottlle"))
                        .body("description", equalTo("Blue water"))
                        .body("price", equalTo("12.00")).body("category_id", equalTo("3"))
                        .body("category_name", equalTo("Active Wear - Unisex"));
        response.log().body();
    }

    @Test
    public void getProductWithAssert(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response =
                given().queryParam("id",2).
                        when().get(endpoint).then();
        response.log().body();
    }

    @Test
    public void getProductWithGreaterThan(){
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        var response =
                given().when().get(endpoint).then()
                        .log().body().assertThat().statusCode(200)
                        .body("records.size()", greaterThan(4))
                        .body("records.id", everyItem(notNullValue()))
                        .body("records.name", everyItem(notNullValue()))
                        .body("records.category_id", everyItem(notNullValue()))
                        .body("records.category_name", everyItem(notNullValue()))
                        .body("records.description", everyItem(notNullValue()))
                        .body("records.price", everyItem(notNullValue()));
                        //.body("records.id[0]",equalTo("1002"));
    }

    @Test
    public void createProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        String body = """
               {"name": "Water Bottle",
                "description": "Blue water bottle. Holds 64 ounces",
                "price": 12,
                "category_id": 3
               }""";
        var response = given().body(body).when().post(endpoint).then();
        response.log().body();
    }

    @Test
    public void updateProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        String body = """
               {
               "id": 20,
               "name": "Water Bottle",
               "description": "Blue water bottle. Holds 64 ounces",
               "price": 78,
               "category_id": 3
               }""";
        var response = given().body(body).when().put(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteProductWithId(){
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        var response =
                given().queryParam("id",20).
                        when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void deleteProductWithRequestBody(){
        String endpoint = "http://localhost:8888/api_testing/product/delete.php";
        String body = """
               {
               "id": 19
               }""";

        var response =
                given().body(body).when().delete(endpoint).then();
        response.log().body();
    }

    @Test
    public void createSerializeProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/create.php";
        Product product = new Product("Water Bottlle", "Blue water", 12, 3);

        var response = given().body(product).post(endpoint).then();
        response.log().body();

    }
}
