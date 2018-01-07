package com.example.ibra18plus.projekt_sm2.reservations;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public class ReservationUtils {
    private ReservationUtils() {}

    public static final String BASE_URL = "http://mobileapiks.azurewebsites.net/";

    public static ReservationService getAPIService() {

        return ReservationClient.getClient(BASE_URL).create(ReservationService.class);
    }
}
