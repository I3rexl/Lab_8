package com.example.lab8.Serives;

public class GHNOrderRespone {
    private String order_code;

    public GHNOrderRespone(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_code(){
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}