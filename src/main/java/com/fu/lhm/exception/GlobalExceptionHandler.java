package com.fu.lhm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<BadRequestException> badRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    }

//    @ExceptionHandler({AuthenticationException.class})
//    public ResponseEntity<String> entityNotFoundException(AuthenticationException authException) {
//        if (authException.getMessage().contains("User is disabled")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vui lòng kích hoạt tài khoản!");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai tài khoản hoặc mật khẩu!");
//        }
//    }
}
