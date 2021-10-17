import Pojo_Account.PostAccountResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.*;
import static endpoints.Endpoints.GET_ACCOUNT;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import static Pojo_Account.PostAccountResponse.*;


public class AccountTest extends BaseTest{

    @Test
    void getAccountInfoTest (){
        given().spec(requestSpecificationWithAuth)
                .when()
                .get(GET_ACCOUNT,username);

    }

    @Test
    void getAccountInfoWithLoggingTest (){
        given().spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(GET_ACCOUNT,username)
                .prettyPeek();
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest (){
        given().spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url",equalTo(username))
                .body("success",equalTo(true))
                .body("status",equalTo(200))
                .contentType("application/json")
                .when()
                .get(GET_ACCOUNT,username)
                .prettyPeek();

    }

    @Test
    void getAccountInfoWithAssertionsAfterTest (){
       Response response= given().spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get(GET_ACCOUNT,username)
                .prettyPeek();

        assertThat(response.body().as(PostAccountResponse.class).getData().getUrl(),equalTo(username));
    }
}
