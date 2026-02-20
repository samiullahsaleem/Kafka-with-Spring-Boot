package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private String city;
    private String location;
    private String stripePaymentId;
    private LocalDateTime paymentDate;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
    }

    public Order(String customerName, String email, String phoneNumber, String city, String location) {
        this();
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.location = location;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", city='" + city + '\'' +
                ", location='" + location + '\'' +
                ", stripePaymentId='" + stripePaymentId + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}