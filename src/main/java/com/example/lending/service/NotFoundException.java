package com.example.lending.service;

/** 対象が存在しない（HTTP 404 に対応づける） */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
