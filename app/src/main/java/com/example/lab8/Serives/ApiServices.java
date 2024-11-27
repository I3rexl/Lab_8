package com.example.lab8.Serives;

import com.example.lab8.Models.ResponseGHN;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiServices {
    String BASE_URL= "http://10.0.2.2:3000/api/";

    @POST("order")
    Call<Response<Order>> order(@Body Order order);


    @POST("v2/order/create")
    Call<ResponseGHN<GHNOrderRespone>> createGHNOrder(@Body GHNOrderRequest request);

}
