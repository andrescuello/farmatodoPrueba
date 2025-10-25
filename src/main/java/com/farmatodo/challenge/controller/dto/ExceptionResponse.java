package com.farmatodo.challenge.controller.dto;

public record ExceptionResponse(
        String message,
        int status
) {}
