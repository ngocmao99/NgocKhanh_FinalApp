package com.example.finalproject.utils;

import android.Manifest;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Constants {
    public static final String EMAIL_REGEX = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    public static final String NAME_REGEX1= "^[a-zA-Z\\s]+$";
    public static final String NAME_REGEX2="[0-9]";
    public static final String PHONE_REGEX="[^0\\d{9}$]";
    public static final String DATE_FORMAT ="dd/MM/yyyy";

    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    public static final int RC_SING_IN = 100;
    public static final String TAG = "Item";
    public static final String FB_TAG = "FACEBOOK_SIGN_IN_TAG";

    public static final int FM_HOME =1;
    public static final int FM_ABOUT_APP =2;
    public static final int FM_HELP = 3;
    public static final int FM_FEED=4;
    public static final int FM_PROFILE=5;
    public static final int FM_FAVORITE=6;

    public static final String FB_LINK = "https://www.facebook.com/ngockhanh.nguyen.944023";
    public static final String GITHUB_LINK = "https://github.com/ngocmao99/NgocKhanh_FinalApp";
    public static final int RB_V = -1;

    public static final String ZERO ="0";

    //Declare a constant variable for Users path on Firebase Database
    public static final String PATH_USER = "Users";
    public static final String PATH_PROPERTIES = "Properties";

    //Declare a constant variable for Users ID of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String USER_ID = "userId";

    //Declare a constant variable for Users name of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String FULLNAME = "fullName";

    //Declare a constant variable for Users email of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String USER_EMAIL = "email";

    //Declare a constant variable for Users avatar of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String AVATAR ="userImgId";

    //Declare a constant variable for Users birthday of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String DOB ="dob";

    //Declare a constant variable for Users phone number of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String PHONE_NUMBER = "phoneNumber";

    //Declare a constant variable for Users gender of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String GENDER ="gender";

    //Declare a constant variable for Users latitude of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String LATITUDE ="latitude";

    //Declare a constant variable for Users longitude of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String LONGITUDE ="longitude";

    //Declare a constant variable for Users province of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String PROVINCE ="province";

    //Declare a constant variable for Users district of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String DISTRICT ="district";

    //Declare a constant variable for Users gender of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String WARD ="ward";

    //Declare a constant variable for Users house number of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String HOUSE_NUMBER ="houseNumber";

    //Declare a constant variable for Users house number of Users tree on Firebase Database, the value depends on the user Model on Model package.
    public static final String POSTAL_CODE ="postalCode";

    public final static String LOCAL = "Local";
    public final static String PROVINCE_NAME = "name";
    public static final String DEFAULT_VALUE ="";
    public static final String TAG_DISTRICT = "DISTRICT";
    public static final String TYPE_PATH ="PropertyTypes";
    public static final String NUMBER_VALID_FORMAT = "[0-9]*?[1-9][0-9]*";


    //Declare a request code to open gallery of device
    public static final int RC_IMAGE = 100;

    public static final String FEMALE ="Female";
    public static final String MALE ="Male";
    public static final String OTHER = "Other";

    //Google Location
    public static final int RC_REPERMISSION = 44;
    public static final int MAX_RESULT =1;
    public static final int POSITION_ADDRESS = 0;
    public static final double DEFAULT_RED_CODE = 0.0d;

    //Places Autocomplete
    public static final int AUTOCOMPLETE_REQUEST_CODE =100;

    //Logcat
    public static final String DEBUG_LOG = "debug";
    public static final String TAG_TYPE ="PROPERTY_TYPE";
    public static final String TAG_FACILITY ="FACILITIES";
    public static final String INPUT = "INPUT";
    public static final String PUSH_DATA = "PUSH DATA";

    //Map
    public static final String TAG_MAP = "MapActivity";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final float DEFAULT_ZOOM = 15f;
    public static final String TAG_PLACE = "PlaceAutoCompleteAd";
    public static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71,136));
    public static final int PAUSE_TIMEOUT = 500;
    public static final String MAP_API_KEY = "AIzaSyAGOXlpwmCSSAlmUxL2wY_0TZKBrRHef10";
    public static final int REQUEST_LOCATION_CODE = 44;
    public static final String TAG_LOCATION = "LocationService";
    public static final String TAG_PROVINCE = "Province";
    public static final String MAIL_TO = "mailto:";
    public static final String SUBJECT = "?subject=";
    public static final String BODY_MAIL ="&body=";
    public static final int RESULT_CODE = 1;
    public static final int GALLERY_CODE = 1;
    public static final int SELECTED_CODE = -1;


    //Property model
    public static final String P_ID ="propertyId";
    public static final String P_CREATOR = "userId";
    public static final String TITLE = "propertyName";
    public static final String LOCATION = "propertyLocation";
    public static final String P_LAT = "lat";
    public static final String P_LNG = "lng";
    public static final String P_PROVINCE = "province";
    public static final String P_POSTALCODE ="postalCode";
    public static final String P_DISTRICT = "district";
    public static final String P_WARD = "ward";
    public static final String P_HN = "houseNumber";
    public static final String P_IMG = "propertyImage";
    public static final String P_TYPE= "propertyType";
    public static final String FLOOR= "propertyFloor";
    public static final String P_DESCRIPTION= "propertyDescription";
    public static final String P_FACILITIES= "propertyFacilities";
    public static final String BED= "bedroom";
    public static final String BATH = "bathroom";
    public static final String AREA = "area";
    public static final String PRICE = "price";
    public static final String TIME = "timestamp";

    public static final String DETAIL_KEY = "item property";
    public static final String UPDATE_DB = "Update Property";




}
