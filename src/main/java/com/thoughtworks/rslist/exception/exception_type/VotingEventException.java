package com.thoughtworks.rslist.exception.exception_type;

import com.thoughtworks.rslist.exception.CommonException;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/7 20:59
 * @Description ***
 **/
public class VotingEventException extends CommonException {
    public VotingEventException(String message) {
        setErrorMessage(message);
    }
}
