package com.bezkoder.springjwt.payload.response;

public class NotFoundException extends Exception {

public NotFoundException(long book_id) {
        super(String.format("Item is not found with id : '%s'", book_id));
        }
}