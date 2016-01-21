package client.linguaconnect.com.linguaconnectclient.utils;

/**
 * Created by anisha on 16/12/15.
 */
public class Constants {

    public static String IS_LOGGED_IN = "isLoggedIn";

    public static String USER_PHONE_NUMBER = "userPhoneNumber";
    public static String USER_PICTURE_URL = "userPictureUrl";
    public static String USER_LOCATION = "userLocation";
    public static String USER_LAST_NAME = "userLastName";
    public static String USER_FIRST_NAME = "userFirstName";
    public static String USER_EMAIL = "userEmail";
    public static String USER_COMPANY = "userCompany";
    public static String USER_AGE = "userAge";
    public static String USER_DESIGNATION = "userDesignation";
    public static String USER_GENDER = "userGender";
    public static String USER_RATING = "userRating";

    public static String BASE_URL = "http://ec2-52-71-92-147.compute-1.amazonaws.com/lingua/";
    
    //public static String BASE_URL = "http://ec2-52-90-237-213.compute-1.amazonaws.com/lingua/";
    public static String REGISTER_URL = "register/";
    public static String LOGIN_URL = "login/";
    public static String GET_EVENT_DETAILS = "get_event_details/";
    public static String GET_INTERPRETER_LIST = "interpreter_list/";
    public static String CREATE_BOOKING = "create_booking/";
    public static String GET_HISTORY = "get_client_booking/";
    public static String UPDATE_CLIENT = "update_client/";
    public static String UPLOAD_PIC = "upload_pic";
    public static String REGISTER_GCM = "add_device";
    public static String UPDATE_PASSWORD = "update_password/";
    public static String BOOKING_REQUEST = "booking_request/";
    public static String UNREGISTER_GCM = "remove_device";
    public static String CANCEL_BOOKING = "cancel_booking";
    public static String GET_BOOKING = "get_booking";
    public static String FORGOT_PASSWORD = "send_password";
    public static String UPDATE_FEEDBACK = "update_feedback";

    public static String currentEvent = "current_event";



    public final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public static final String EVENT_ID = "eventId";

    public final static int REQUEST_CODE_NATIVE_CAMERA=1115;

}
