package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 19:56
 * @Description ***
 **/
public class MysqlOperatingException extends CommonException {
    public MysqlOperatingException(String message) {
        setErrorMessage(message);
    }
}
