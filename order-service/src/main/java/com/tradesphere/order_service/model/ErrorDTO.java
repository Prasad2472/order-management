package com.tradesphere.order_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String apiPath;
    private String exception;
    private int errorCode;
    private LocalDateTime occurrence;
}
