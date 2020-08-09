package com.thoughtworks.rslist.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    protected CommonExceptionMessage commonExceptionMessage;

    protected void setErrorMessage(String errorMessage) {
        commonExceptionMessage.setError(errorMessage);
    }
}
