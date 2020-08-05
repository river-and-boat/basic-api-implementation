package com.thoughtworks.rslist.handler;

import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.CommonErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 21:59
 * @Description ***
 **/
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler({BadIndexParamException.class, IndexOutOfBoundsException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        CommonErrorMessage commonErrorMessage = new CommonErrorMessage();
        if (ex instanceof BadIndexParamException) {
            commonErrorMessage.setError("invalid request param");
        }
        return ResponseEntity.badRequest().body(commonErrorMessage);
    }
}
