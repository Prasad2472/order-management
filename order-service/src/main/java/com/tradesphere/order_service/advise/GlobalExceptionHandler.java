package com.tradesphere.order_service.advise;

import com.tradesphere.order_service.model.ErrorDTO;
import com.tradesphere.order_service.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<ErrorDTO> handleOrderNotFoundException(WebRequest request, Exception exception) {
        ErrorDTO errorDTO = new ErrorDTO(request.getDescription(false),
                exception.getMessage(), 500, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
}
