package com.example.ibra18plus.projekt_sm2.users;

/**
 * Created by Ibra18plus on 2018-01-07.
 */

public class UserUtils {
    public UserUtils(){}

    public static final String BASE_URL = "http://mobileapiks.azurewebsites.net/";

    public static UserService getAPIService() {

        return UserClient.getClient(BASE_URL).create(UserService.class);
    }
}


