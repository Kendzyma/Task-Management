package com.taskmanagement.dto;

public class DefaultResponse {
    private boolean success;
    private int code;

    public DefaultResponse() {
        this.success = true;
        this.code = 200;
    }
}
