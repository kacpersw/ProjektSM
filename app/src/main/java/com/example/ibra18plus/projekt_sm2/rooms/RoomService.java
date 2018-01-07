package com.example.ibra18plus.projekt_sm2.rooms;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Ibra18plus on 2017-12-09.
 */

public interface RoomService {

    @POST("api/rooms")
    @FormUrlEncoded
    Call<Room> createRoom(@Field("RoomName") String roomName,
                          @Field("Nr") Integer nr,
                          @Field("Capacity") Integer capacity);

    @GET("api/rooms")
    Call<List<Room>> getAllRooms();

    @DELETE("api/rooms/{id}")
    Call<Room> deleteRoom(@Path("id") Integer id);
}
