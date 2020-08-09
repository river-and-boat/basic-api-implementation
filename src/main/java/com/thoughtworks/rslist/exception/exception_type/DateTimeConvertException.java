package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 14:49
 * @Description ***
 **/
public class DateTimeConvertException extends CommonException {
    public DateTimeConvertException(String message) {
        setErrorMessage(message);
    }
}
