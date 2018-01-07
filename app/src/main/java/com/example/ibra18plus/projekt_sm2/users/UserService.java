package com.example.ibra18plus.projekt_sm2.users;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ibra18plus on 2018-01-07.
 */

public interface UserService {
    @POST("api/user")
    @FormUrlEncoded
    Call<User> createUser(@Field("FirstName") String firstName,
                          @Field("Surname") String surname,
                          @Field("Email") String email,
                          @Field("Username") String username,
                          @Field("Password") String password,
                          @Field("AdminRole") Boolean adminRole);

    @GET("api/user/{Username}")
    Call<User> getUser(@Query("Username") String username);

}

