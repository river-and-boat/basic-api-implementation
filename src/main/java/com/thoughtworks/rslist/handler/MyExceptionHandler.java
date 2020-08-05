package com.thoughtworks.rslist.handler;

import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.CommonErrorMessage;
import com.thoughtworks.rslist.exception.IndexOutException;
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
    @ExceptionHandler({BadIndexParamException.class, IndexOutException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        CommonErrorMessage commonErrorMessage = new CommonErrorMessage();
        commonErrorMessage.setError(ex.getMessage());
        return ResponseEntity.badRequest().body(commonErrorMessage);
    }
}
