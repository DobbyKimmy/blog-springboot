package com.github.springbootdemo.demo.controller;

import com.github.springbootdemo.demo.entity.Result;
import com.github.springbootdemo.demo.entity.User;
import com.github.springbootdemo.demo.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager) {

        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Result auth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ? null : authentication.getName());
        if (loggedInUser == null) {
            return Result.failure("用户没有登录");
        }
        return Result.success("登录成功",true,loggedInUser);
    }

    // logout
    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);
        if (loggedInUser == null) {
            return Result.failure("用户尚未登录");
        } else {
            SecurityContextHolder.clearContext();
            return Result.success("注销成功",false,null);
        }
    }

    // register
    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");

        if (username.length() < 1 || username.length() > 15) {
            return Result.failure("invalid  username");
        }
        if (password.length() < 6 || password.length() > 16) {
            return Result.failure("invalid password");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return Result.failure("user already exists");
        }
        return Result.success("success",false,userService.getUserByUsername(username));
    }

    //login
    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        // 鉴权：你要登陆论坛，输入用户名张三，密码1234，密码正确，证明你张三确实是张三，这就是 authentication
        // 验权：再一check用户张三是个版主，所以有权限加精删别人帖，这就是 authorization
        try {
            authenticationManager.authenticate(token);
            // 将用户信息保存在一个地方
            // Cookie
            SecurityContextHolder.getContext().setAuthentication(token);
            User loggedInUser = userService.getUserByUsername(username);
            return Result.success("登录成功",true,loggedInUser);
        } catch (BadCredentialsException e) {
            return Result.failure("密码不正确");
        }
    }
}
