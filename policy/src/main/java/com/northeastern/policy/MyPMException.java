package com.northeastern.policy;

public class MyPMException extends Exception {
    public MyPMException(Throwable e) {
        super(e);
    }

    public MyPMException(String msg) {
        super(msg);
    }
}
