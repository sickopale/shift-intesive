package by.koronatech.office.core.handler;

import by.koronatech.office.core.exception.AppException;
import by.koronatech.office.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppException> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new AppException(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppException> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(new AppException(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}