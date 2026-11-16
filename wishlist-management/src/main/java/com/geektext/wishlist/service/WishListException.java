package com.geektext.wishlist.service;

import org.springframework.http.HttpStatus;

/** Business-rule error that carries the HTTP status the API should return. */
public class WishListException extends RuntimeException {
    private final HttpStatus status;

    public WishListException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
