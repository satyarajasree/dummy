package com.rajasreeit.backend.dto;

import com.rajasreeit.backend.customer.entities.Customer;

public class ApiResponse {

    private String message;
    private Customer customer;

    public ApiResponse( String message, Customer customer) {
        this.customer = customer;
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
