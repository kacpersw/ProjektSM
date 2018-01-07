package com.example.ibra18plus.projekt_sm2.reservations;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public interface ReservationService {

    @POST("api/reservations")
    @FormUrlEncoded
    Call<Reservation> createReservation(@Field("ReservationStartDate") String start,
                                        @Field("ReservationEndDate") String end,
                                        @Field("RoomId") Integer roomId,
                                        @Field("UserId") Integer userId);

    @GET("api/reservations")
    Call<List<Reservation>> getAllReservations();

    @GET("api/reservations/{id}")
    Call<List<Reservation>> getAllReservationsByUserId(@Query("id") int id);

    @GET("api/room/{id}")
    Call<List<Reservation>> getAllReservationsByRoomId(@Query("id") int id);

    @DELETE("api/reservations/{id}")
    Call<Reservation> deleteReservation(@Path("id") Integer id);
}
