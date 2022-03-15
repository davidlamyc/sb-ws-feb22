package com.springbootfeb.ws.exceptions;

import com.springbootfeb.ws.service.UserService;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}
