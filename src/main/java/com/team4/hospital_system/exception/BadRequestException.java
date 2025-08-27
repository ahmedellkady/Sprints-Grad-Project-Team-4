package com.team4.hospital_system.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
