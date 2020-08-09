package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 0:20
 * @Description ***
 **/
public class TrendingBadParamException extends CommonException {
    public TrendingBadParamException(String message) {
        setErrorMessage(message);
    }
}
