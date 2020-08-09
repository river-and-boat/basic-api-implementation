package com.thoughtworks.rslist.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 22:02
 * @Description ***
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonExceptionMessage {

    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
