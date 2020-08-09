package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 22:12
 * @Description ***
 **/
public class BadIndexParamException extends CommonException {
    public BadIndexParamException(String message) {
        setErrorMessage(message);
    }
}
