package com.example.myjapanese;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("potato_posts/")
    Call<List<PotatoPost>> getPotatoPosts();

    @GET("location/")
    Call<List<Location>> getLocations();

    @GET("item/")
    Call<List<Item>> getItems();

    @POST("item/")
    Call<Item> createItem(@Body Item item);

    @PUT("item/{id}/")
    Call<Item> updateItem(@Path("id") int id, @Body Item item);

    @DELETE("item/{id}/")
    Call<Void> deleteItem(@Path("id") int id);


}
