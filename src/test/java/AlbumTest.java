import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class AlbumTest extends BaseTest{

    static RequestSpecification requestSpecificationWithAuthAndBody;

    String AlbumId;

    @BeforeEach
    void beforeTest (){

        requestSpecificationWithAuthAndBody = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "My Album")
                .addFormParam("description", "This album contains test images.")
                .addFormParam("privacy", "public")
                .build();

    }
    @Test
    void createAlbumTest(){
        given (requestSpecificationWithAuth)
                .post(CREATE_ALBUM)
                .prettyPeek();
    }

    @Test
    void createAlbumWithBodyTest(){
         given (requestSpecificationWithAuthAndBody)
                .post(CREATE_ALBUM)
                .prettyPeek();
    }

    @Test
    void FavoriteAlbumTest(){
        Response response = given (requestSpecificationWithAuth)
                .post(CREATE_ALBUM)
                .prettyPeek();

        AlbumId = response.jsonPath().get("data.id");

        given(requestSpecificationWithAuth, positiveResponseSpecificationFavorite)
                .post(ADD_FAVORITE_ALBUM,AlbumId)
                .prettyPeek();
    }

    @Test
    void UnfavoriteAlbumTest(){
        Response response = given (requestSpecificationWithAuth)
                .post(CREATE_ALBUM)
                .prettyPeek();

        AlbumId = response.jsonPath().get("data.id");

        given(requestSpecificationWithAuth)
                .post(ADD_FAVORITE_ALBUM,AlbumId)
                .prettyPeek();

        given(requestSpecificationWithAuth, positiveResponseSpecificationUnfavorite)
                .post(ADD_FAVORITE_ALBUM,AlbumId)
                .prettyPeek();
    }

    @Test
    void DeleteAlbumTest () {
        Response response = given (requestSpecificationWithAuth)
                .post(CREATE_ALBUM)
                .prettyPeek();

        AlbumId = response.jsonPath().get("data.id");

        given(requestSpecificationWithAuth)
                .delete(DELETE_ALBUM,AlbumId)
                .prettyPeek();
    }

    @Test
    void DeleteDeletedAlbumTest () {
        Response response = given (requestSpecificationWithAuth)
                .post(CREATE_ALBUM)
                .prettyPeek();

        AlbumId = response.jsonPath().get("data.id");

        given(requestSpecificationWithAuth)
                .delete(DELETE_ALBUM,AlbumId)
                .prettyPeek();
        given(requestSpecificationWithAuth,negativeResponseSpecificationPNF)
                .delete(DELETE_ALBUM,AlbumId)
                .prettyPeek();
    }
}
