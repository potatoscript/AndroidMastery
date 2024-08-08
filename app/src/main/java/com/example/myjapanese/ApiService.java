package com.example.myjapanese;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("potato_posts/")
    Call<List<PotatoPost>> getPotatoPosts();

    @GET("locations/")
    Call<List<Location>> getLocations();

    @GET("item/")
    Call<List<Item>> getItems();
}
