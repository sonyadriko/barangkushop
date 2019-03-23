package com.example.xdreamer.barangkushop.Object;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String idgame;
    private String total;
    private String status;
    private String catatan;
    private String contact;
    //private String paymentState;  when using PAYPAL USING THIS STRING
    private List<Order> products;

    public Request() {
    }

    public Request(String phone, String name, String idgame, String total, String status, String catatan, String contact, List<Order> products) {
        this.phone = phone;
        this.name = name;
        this.idgame = idgame;
        this.total = total;
        this.status = status;
        this.catatan = catatan;
        this.contact = contact;
        this.products = products;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getIdgame() {
        return idgame;
    }

    public void setIdgame(String idgame) {
        this.idgame = idgame;
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

    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }
}

