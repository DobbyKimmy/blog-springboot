package com.github.springbootdemo.demo.entity;

public class LoginResult extends Result<User>{

    private boolean isLogin;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    protected LoginResult(ResultStatus status, String msg, User user,boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg,boolean isLogin){
        return new LoginResult(ResultStatus.OK,msg,null,isLogin);
    }

    public static LoginResult success(User user){
        return new LoginResult(ResultStatus.OK,null,user,true);
    }

    public static LoginResult success(String msg,User user){
        return new LoginResult(ResultStatus.OK,msg,user,true);
    }

    public static LoginResult failure(String msg){
        return new LoginResult(ResultStatus.FAIL,msg,null,false);
    }
}
