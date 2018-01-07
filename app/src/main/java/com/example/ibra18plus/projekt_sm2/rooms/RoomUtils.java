package com.example.ibra18plus.projekt_sm2.rooms;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class RoomUtils {
    private RoomUtils() {}

    public static final String BASE_URL = "http://mobileapiks.azurewebsites.net/";

    public static RoomService getAPIService() {

        return RoomClient.getClient(BASE_URL).create(RoomService.class);
    }
}
