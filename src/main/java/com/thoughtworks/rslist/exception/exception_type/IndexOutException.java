package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 21:57
 * @Description ***
 **/
public class IndexOutException extends CommonException {
    public IndexOutException(String message) {
        setErrorMessage(message);
    }
}
