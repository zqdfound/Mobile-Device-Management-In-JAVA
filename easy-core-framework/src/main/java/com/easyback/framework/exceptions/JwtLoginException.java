package com.easyback.framework.exceptions;

import java.io.IOException;

/**
 *
 * @author fancy
 * @date 2018/10/19
 */
public class JwtLoginException extends IOException {
    public JwtLoginException(String message){
        super(message);
    }
}
