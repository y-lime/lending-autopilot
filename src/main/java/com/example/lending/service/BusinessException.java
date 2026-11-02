package com.example.lending.service;

/** 業務ルール違反（HTTP 409 に対応づける） */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
