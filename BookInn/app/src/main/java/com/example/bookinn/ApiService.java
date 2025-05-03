package com.example.bookinn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/v_Hotels_with_Photos/{id}")
    Call<Hotels> getHotel(@Path("id") int postId);

    @GET("api/v_Hotels_with_Photos")
    Call<List<Hotels>> getAllHotels();
}
