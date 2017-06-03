package com.example.android.firebaseauthdemo;

/**
 * Created by Admin on 25/5/2017.
 */

public class Product {

    String productId;
    String productBuyer;
    String productCourier;
    String productName;
    String productType;
    String productCoords;
    String length;
    String width;
    String height;
    String weight;
    String price;
    String date;
    String imgurl;
    String country;

    public Product(){

    }

    public Product(String productId, String productBuyer, String productCourier, String productName, String productType, String productCoords, String length, String width, String height, String weight, String price, String date, String imgurl, String country) {
        this.productId = productId;
        this.productBuyer = productBuyer;
        this.productCourier = productCourier;
        this.productName = productName;
        this.productType = productType;
        this.productCoords = productCoords;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.price = price;
        this.date = date;
        this.imgurl = imgurl;
        this.country = country;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductBuyerEmail() {
        return productBuyer;
    }

    public String getProductCourier() {
        return productCourier;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductType() {
        return productType;
    }

    public String getProductCoords() {
        return productCoords;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getImgurl() { return imgurl; }

    public String getCountry() { return country; }
}
