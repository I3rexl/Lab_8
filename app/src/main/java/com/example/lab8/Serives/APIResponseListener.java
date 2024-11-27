package com.example.lab8.Serives;

public interface APIResponseListener {
    void onSuccess(String response);
    void onFailure(String error);
}
