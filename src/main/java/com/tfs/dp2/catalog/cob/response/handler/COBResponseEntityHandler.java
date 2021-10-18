package com.tfs.dp2.catalog.cob.response.handler;

import com.tfs.dp2.catalog.baseexceptions.COBInternalException;
import com.tfs.dp2.catalog.baseexceptions.COBMalformedRequestException;
import com.tfs.dp2.catalog.baseexceptions.COBValidationException;
import com.tfs.dp2.catalog.cob.response.entity.COBResponse;
import com.tfs.dp2.catalog.cob.response.entity.COBValidationExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@Slf4j
public class COBResponseEntityHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(COBValidationException.class)
    public final ResponseEntity<Object> handleCOBValidationException(Exception ex, WebRequest request) {
        COBValidationException exception = (COBValidationException) ex;
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        COBValidationExceptionResponse cobExceptionResponse = exception.getCobExceptionResponse();
        log.info(String.format("Response code:%s, Response body:%s", httpStatus, cobExceptionResponse.toString()));
        return new ResponseEntity<>(cobExceptionResponse, httpStatus);
    }


    @ExceptionHandler(COBMalformedRequestException.class)
    public final ResponseEntity<Object> handleCOBMalformedException(Exception ex, WebRequest request) {
        COBMalformedRequestException exception = (COBMalformedRequestException) ex;
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        COBResponse cobExceptionResponse = exception.getExceptionResponse();
        log.info(String.format("Response code:%s, Response body:%s", httpStatus, cobExceptionResponse.toString()));
        return new ResponseEntity<>(cobExceptionResponse, httpStatus);
    }

    @ExceptionHandler(COBInternalException.class)
    public final ResponseEntity<Object> handleCOBInternalException(Exception ex, WebRequest request) {
        COBInternalException exception = (COBInternalException) ex;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        COBResponse cobExceptionResponse = exception.getCobInternalExceptionResponse();
        log.info(String.format("Response code:%s, Response body:%s", httpStatus, cobExceptionResponse.toString()));
        return new ResponseEntity<>(cobExceptionResponse, httpStatus);
    }

}
