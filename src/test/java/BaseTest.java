import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import static org.hamcrest.core.IsEqual.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public abstract class BaseTest {
static ResponseSpecification positiveResponseSpecification;
static ResponseSpecification positiveResponseSpecificationFavorite;
static ResponseSpecification positiveResponseSpecificationUnfavorite;
static ResponseSpecification negativeResponseSpecificationBR;
    static ResponseSpecification negativeResponseSpecificationPNF;
static RequestSpecification requestSpecificationWithAuth;

    static Properties properties= new Properties();
    static String token;
    static String username;

    @BeforeAll
    static void beforeAll()
    {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI="https://api.imgur.com/3";
        getProperties();
        token= properties.getProperty("token");
        username= properties.getProperty("username");

        positiveResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveResponseSpecificationFavorite = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .expectBody("data", equalTo("favorited"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveResponseSpecificationUnfavorite = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .expectBody("data", equalTo("unfavorited"))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        negativeResponseSpecificationBR = new ResponseSpecBuilder()
                .expectBody("status", equalTo(400))
                .expectBody("success", equalTo(false))
                .expectContentType(ContentType.JSON)
                .expectStatusCode(400)
                .build();

        negativeResponseSpecificationPNF = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();

        RestAssured.responseSpecification= positiveResponseSpecification;
    }
    private static void getProperties(){
        try (InputStream output= new FileInputStream("src/test/resources/application.properties")){
            properties.load(output);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}
