package com.fu.lhm.exception;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Generated
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class BadRequestException extends Exception {
    private int status;
    private String title;

    public BadRequestException(String msg) {
        super(msg, null, false, false);
        status = HttpStatus.BAD_REQUEST.value();
        title = HttpStatus.BAD_REQUEST.name();
    }
}
