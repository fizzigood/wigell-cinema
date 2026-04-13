package com.wigell.cinema.config;

// Eget exception som kastas när en resurs inte hittas i databasen.
// Fångas av GlobalExceptionHandler och returnerar HTTP 404.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}