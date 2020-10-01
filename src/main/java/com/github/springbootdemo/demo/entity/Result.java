package com.github.springbootdemo.demo.entity;

public class Result {
    // 遵循Java Bean的约定 我们要使用get set
    private String status;
    private String msg;
    private boolean isLogin;
    private Object data;

    public static Result failure(String message){
        return new Result("fail",message,false);
    }

    public static Result success (String message,boolean isLogin,Object data){
        return new Result("ok",message,isLogin,data);
    }

    private Result(String status, String msg) {
        this(status, msg, false, null);
    }

    private Result(String status, String msg, boolean isLogin) {
        this(status, msg, isLogin, null);
    }

    private Result(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
