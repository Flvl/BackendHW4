import Pojo.PostImageResponse;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static Pojo.PostImageResponse.*;

import java.io.*;

import static endpoints.Endpoints.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class ImageTest extends BaseTest {

    String ImageId_Jpg;
    String ImageDeleteHash_Jpg;
    String ImageId_GIF;
    String VideoId_mp4;

    static RequestSpecification requestSpecificationWithAuthAndMultiPartImageJPG;
    static RequestSpecification requestSpecificationWithAuthAndMultiPartImageGIF;
    static RequestSpecification requestSpecificationWithAuthAndMultiPartTXT;
    static RequestSpecification requestSpecificationWithAuthAndMultiPartVideoMP4;
    static RequestSpecification requestSpecificationWithAuthAndMultiPartImagePix;
    MultiPartSpecification MultiPartSpecWithFileJPG;
    MultiPartSpecification MultiPartSpecWithFilePixJPG;
    MultiPartSpecification MultiPartSpecWithFileGIF;
    MultiPartSpecification MultiPartSpecWithFileTXT;
    MultiPartSpecification MultiPartSpecWithFileMP4;

    @BeforeEach
    void beforeTest (){

        MultiPartSpecWithFileJPG = new MultiPartSpecBuilder(new File(PATH_FILE+"test_JPG.JPG"))
        .controlName("image")
        .build();
        requestSpecificationWithAuthAndMultiPartImageJPG = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "Tombs")
                .addFormParam("type", "jpg")
                .addMultiPart(MultiPartSpecWithFileJPG)
                .build();

        MultiPartSpecWithFileGIF = new MultiPartSpecBuilder(new File(PATH_FILE+"test_GIF.gif"))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPartImageGIF = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "Tombs")
                .addFormParam("type", "gif")
                .addMultiPart(MultiPartSpecWithFileGIF)
                .build();

        MultiPartSpecWithFileTXT = new MultiPartSpecBuilder(new File(PATH_FILE+"test_txt.txt"))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPartTXT = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "my TXT")
                .addMultiPart(MultiPartSpecWithFileTXT)
                .build();

        MultiPartSpecWithFileMP4 = new MultiPartSpecBuilder(new File(PATH_FILE+"Test_mp4.mp4"))
                .controlName("video")
                .build();
        requestSpecificationWithAuthAndMultiPartVideoMP4 = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "10 seconds")
                .addMultiPart(MultiPartSpecWithFileMP4)
                .build();

        MultiPartSpecWithFilePixJPG = new MultiPartSpecBuilder(new File(PATH_FILE+"1pix.JPG"))
                .controlName("image")
                .build();
        requestSpecificationWithAuthAndMultiPartImagePix = new RequestSpecBuilder()
                .addRequestSpecification(requestSpecificationWithAuth)
                .addFormParam("title", "One_pix")
                .addFormParam("type", "jpg")
                .addFormParam("Description","Min size")
                .addMultiPart(MultiPartSpecWithFileJPG)
                .build();

    }
    @Test
    void UploadFileJPGImageTest(){
        Response response = given (requestSpecificationWithAuthAndMultiPartImageJPG)
        .post(UPLOAD_IMAGE)
        .prettyPeek();

        ImageId_Jpg=response.body().as(PostImageResponse.class).getData().getId();
        ImageDeleteHash_Jpg=response.body().as(PostImageResponse.class).getData().getDeletehash();
        assertThat(ImageId_Jpg,notNullValue());
        assertThat(ImageDeleteHash_Jpg,notNullValue());
    }

    @Test
    void UploadFileGIFImageTest(){
        Response response = given (requestSpecificationWithAuthAndMultiPartImageGIF)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

        ImageId_GIF=response.jsonPath().get("data.id");
        assertThat(ImageId_GIF,notNullValue());
    }

    @Test
    void UploadFileTxtTest(){

        Response response = given (requestSpecificationWithAuthAndMultiPartTXT, negativeResponseSpecificationBR)
                .post(UPLOAD_IMAGE)
                .prettyPeek();
    }

    @Test
    void UploadFileVideoTest(){
        Response response = given (requestSpecificationWithAuthAndMultiPartVideoMP4)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

        assertThat(response.body().as(PostImageResponse.class).getData().getId(),notNullValue());
    }

    @Test
    void FavouriteAnImageTest () {
        Response response = given (requestSpecificationWithAuthAndMultiPartImageJPG)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

        ImageId_Jpg=response.body().as(PostImageResponse.class).getData().getId();
        assertThat(ImageId_Jpg,notNullValue());
        System.out.println(ImageId_Jpg);

         given(requestSpecificationWithAuth, positiveResponseSpecificationFavorite)
                 .post(ADD_FAVORITE,ImageId_Jpg)
                .prettyPeek();

    }

    @Test
    void UnFavouriteAnImageTest () {
        Response response = given (requestSpecificationWithAuthAndMultiPartImageJPG)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

        ImageId_Jpg=response.body().as(PostImageResponse.class).getData().getId();
        assertThat(ImageId_Jpg,notNullValue());
        System.out.println(ImageId_Jpg);

        given(requestSpecificationWithAuth)
                .post(ADD_FAVORITE,ImageId_Jpg)
                .prettyPeek();
        given(requestSpecificationWithAuth,positiveResponseSpecificationUnfavorite)
                .post(ADD_FAVORITE,ImageId_Jpg)
                .prettyPeek();

    }

    @Test
    void DeleteImageTest () {
        Response response = given (requestSpecificationWithAuthAndMultiPartImageJPG)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

                    given(requestSpecificationWithAuth)
                .delete(DELETE_IMAGE,username,response.body().as(PostImageResponse.class).getData().getDeletehash())
                .prettyPeek();

    }

    @Test
   void FavoriteDeletedImageTest () {
        Response response = given (requestSpecificationWithAuthAndMultiPartImageJPG)
                .post(UPLOAD_IMAGE)
                .prettyPeek();

        ImageDeleteHash_Jpg=response.jsonPath().get("data.deletehash");
        ImageId_Jpg=response.jsonPath().get("data.id");

        given(requestSpecificationWithAuth)
                .delete(DELETE_IMAGE,username,response.body().as(PostImageResponse.class).getData().getDeletehash())
                .prettyPeek();

        given(requestSpecificationWithAuth,negativeResponseSpecificationPNF)
                .post(ADD_FAVORITE,response.body().as(PostImageResponse.class).getData().getId())
                .prettyPeek();

    }

    @Test
    void UploadFile1pixImageTest(){
        Response response = given (requestSpecificationWithAuthAndMultiPartImagePix)
                .post(UPLOAD_IMAGE)
                .prettyPeek();
        assertThat(response.body().as(PostImageResponse.class).getData().getId(),notNullValue());


    }

    @Test
    void UploadFileVideoWithoutSoundTest(){
        Response response = given (requestSpecificationWithAuthAndMultiPartVideoMP4)
                .expect()
                .body("data.has_sound",equalTo(false))
                .when()
                .post(UPLOAD_IMAGE)
                .prettyPeek();


        assertThat(response.body().as(PostImageResponse.class).getData().getId(),notNullValue());

    }
}
