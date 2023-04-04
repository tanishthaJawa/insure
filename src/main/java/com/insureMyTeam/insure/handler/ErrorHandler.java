package com.insureMyTeam.insure.handler;

import com.insureMyTeam.insure.exceptions.ClaimNotFoundException;
import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import com.insureMyTeam.insure.exceptions.InsuranceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = ClaimNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public  ErrorResponse  claimNotFoundExceptionHandler(){
        return new ErrorResponse("Claim not found");
    }

    @ExceptionHandler(value = InsuranceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public  ErrorResponse  insuranceNotFoundExceptionHandler(){
        return new ErrorResponse("Insurance not found");
    }

    @ExceptionHandler(value = ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public  ErrorResponse  clientNotFoundExceptionHandler(){
        return new ErrorResponse("Client not found");
    }
}
