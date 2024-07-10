package in.reqres.users;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import in.reqres.extentConfig.ExtentTestNGITestListener;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UsersTest {
    @Test(priority = 1, description = "Get all Users")
    public void getUsers() {
        ExtentTestNGITestListener.getTest().info("Get all users");

        String response =
        given()
                .baseUri("https://reqres.in/api/")
            .when()
                .get("users")
            .then()
                .log().body()
                .assertThat().statusCode(200)
                .body("data[0].id", equalTo(1))
                .body("data.first_name", hasItem("Eve"))
                .body("data.email", everyItem(endsWith(".in")))
                .extract().response().asString();

        ExtentTestNGITestListener.getTest().info("Response: " + response);
    }

    @Test(priority = 2, description = "Get single user")
    public void getUser() {
        ExtentTestNGITestListener.getTest().info("Get 3rd user");

        String response =
        given()
                .baseUri("https://reqres.in/api/")
            .when()
                .get("users/3")
            .then()
                .log().body()
                .assertThat().statusCode(200)
                .body("data.id", equalTo(3))
                .body("data.first_name", equalTo("Emma"))
                .body("data.last_name", equalTo("Wong"))
                .body("data.email", equalTo("emma.wong@reqres.in"))
                .extract().response().asString();

        ExtentTestNGITestListener.getTest().info("Response: " + response);
    }

    @Test(priority = 3, description = "update user")
    public void updateUser() {
        ExtentTestNGITestListener.getTest().info("update user");

        String response =
        given()
                .baseUri("https://reqres.in/api/")
                .param("first_name", "test")
            .when()
                .put("users/6")
            .then()
                .log().body()
                .assertThat().statusCode(200)
                .body("updatedAt", containsString("2024"))
                .extract().response().asString();

        ExtentTestNGITestListener.getTest().info("Response: " + response);
    }

    @Test(priority = 4, description = "Add user")
    public void addUser() {
        ExtentTestNGITestListener.getTest().info("Get 3rd user");

        String request;

        try {
            request = Files.readString(Path.of("src/test/resources/addUser.json"));

            String response =
                    given()
                            .baseUri("https://reqres.in/api/")
                            .contentType(ContentType.JSON)
                            .body(request)
                            .log().body()
                        .when()
                            .post("users")
                        .then()
                            .log().body()
                            .assertThat().statusCode(201)
                            .body("id", equalTo(7))
                            .body("first_name", equalTo("john"))
                            .body("last_name", equalTo("test"))
                            .body("email", equalTo("test@reqres.in"))
                            .extract().response().asString();

            ExtentTestNGITestListener.getTest().info("Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
            ExtentTestNGITestListener.getTest().info("Read file failed - " + e);
        }
    }

    @Test(priority = 5, description = "Delete user")
    public void deleteUser() {
        ExtentTestNGITestListener.getTest().info("Delete User");

        String response =
        given()
                .baseUri("https://reqres.in/api/")
            .when()
                .delete("users/7")
            .then()
                .log().body()
                .assertThat().statusCode(204)
                .extract().response().asString();

        ExtentTestNGITestListener.getTest().info("Response: " + response);
    }

}
