package com.example.lab8.Models;

public class Fruit {
    private String _id, name;
    private int price;

    public Fruit(String _id, String name, int price) {
        this._id = _id;
        this.name = name;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
