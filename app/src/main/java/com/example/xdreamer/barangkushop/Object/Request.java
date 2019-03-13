package com.example.xdreamer.barangkushop.Object;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private String comment;
    //private String paymentState;  when using PAYPAL USING THIS STRING
    private List<Order> products;

    public Request() {

    }

    public Request(String phone, String name, String address, String total, String status, String comment, List<Order> products) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.products = products;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }
}

