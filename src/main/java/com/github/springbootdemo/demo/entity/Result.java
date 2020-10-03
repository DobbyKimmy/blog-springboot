package com.github.springbootdemo.demo.entity;

public abstract class Result<T> {
    // 使用枚举列出返回的状态
    public enum ResultStatus {
        OK("ok"),
        FAIL("fail");

        private String status;

        ResultStatus(String status){
            this.status = status;
        }
    }
    private ResultStatus status;
    private String msg;
    private T data;

    protected Result(ResultStatus status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getStatus() {
        return status.status;
    }

    public String getMsg() {
        return msg;
    }
}
