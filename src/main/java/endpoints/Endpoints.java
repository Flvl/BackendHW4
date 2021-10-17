package endpoints;

public class Endpoints {
    public static final String UPLOAD_IMAGE ="/upload";
    public static final String GET_ACCOUNT="/account/{username}";
    public static final String PATH_FILE ="src/test/resources/";
    public static final String ADD_FAVORITE ="/image/{UploadImageId}/favorite";
    public static final String DELETE_IMAGE ="account/{username}/image/{DeleteHashImage}";
    public static final String CREATE_ALBUM ="/album";
    public static final String ADD_FAVORITE_ALBUM ="/album/{albumHash}/favorite";
    public static final String DELETE_ALBUM ="/album/{albumHash}";

}
