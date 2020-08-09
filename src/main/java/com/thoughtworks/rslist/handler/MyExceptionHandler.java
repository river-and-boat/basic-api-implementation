package com.thoughtworks.rslist.handler;

import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.DateTimeConvertException;
import com.thoughtworks.rslist.exception.exception_type.IndexOutException;
import com.thoughtworks.rslist.exception.exception_type.VotingEventException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 21:59
 * @Description ***
 **/
@ControllerAdvice
public class MyExceptionHandler {

    Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity exceptionHandler(Exception ex) {
        CommonExceptionMessage commonExceptionMessage = new CommonExceptionMessage();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            commonExceptionMessage.setError("The datetime is bad format");
        } else {
            commonExceptionMessage.setError(ex.getMessage());
        }
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(commonExceptionMessage);
    }
}
