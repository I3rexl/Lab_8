package com.example.lab8.Serives;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiServices service;
    public HttpRequest(){
        service= new Retrofit.Builder().baseUrl(ApiServices.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiServices.class);
    }

    public ApiServices callAPI(){
        return service;
    }
}
