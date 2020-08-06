package com.thoughtworks.rslist.handler;

import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.CommonErrorMessage;
import com.thoughtworks.rslist.exception.IndexOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 21:59
 * @Description ***
 **/
@ControllerAdvice
public class MyExceptionHandler {

    Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler({BadIndexParamException.class, IndexOutException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        CommonErrorMessage commonErrorMessage = new CommonErrorMessage();
        if (ex.toString().contains("TrendingController.addNewTrending")) {
            commonErrorMessage.setError("invalid param");
        } else if (ex.toString().contains("UserController.addNewUser")) {
            commonErrorMessage.setError("invalid user");
        } else {
            commonErrorMessage.setError(ex.getMessage());
        }
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(commonErrorMessage);
    }
}
