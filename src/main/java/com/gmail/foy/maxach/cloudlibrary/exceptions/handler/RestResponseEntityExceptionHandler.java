package com.gmail.foy.maxach.cloudlibrary.exceptions.handler;

import com.gmail.foy.maxach.cloudlibrary.exceptions.*;
import com.gmail.foy.maxach.cloudlibrary.exceptions.model.ApiExceptionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handleUserNotFoundException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handleUserAlreadyExistsException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handleRoleNotFoundException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(PasswordIsInvalidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handlePasswordIsInvalidException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(UserIsNotAuthException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ApiExceptionModel handleUserIsNotAuthException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(ForbiddenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ApiExceptionModel handleForbiddenException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(PostAlreadyExistsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handlePostAlreadyExistsException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


    @ExceptionHandler(PostNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiExceptionModel handlePostNotFoundException(RuntimeException exception, WebRequest request) {
        return new ApiExceptionModel(exception.getMessage(), LocalDateTime.now(),
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }

}

