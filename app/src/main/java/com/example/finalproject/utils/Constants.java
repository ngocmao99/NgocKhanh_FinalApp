package com.example.finalproject.utils;

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

    public static final int RC_SING_IN = 100;
    public static final String TAG = "GOOGLE_SIGN_IN_TAG";
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

    //Declare a constant variable for Users path on Firebase Database
    public static final String PATH_USER = "Users";

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
    public static final String POSTAL_CODE ="postCode";

    public static final String DEFAULT_VALUE ="";


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





}
