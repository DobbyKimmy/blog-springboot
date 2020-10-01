package com.github.springbootdemo.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

@Service
public class OrderService {
    // OrderService -> UserService
    // @Resource
    // @Autowired
    // Resource & Autowired都是历史上Spring能够完成DI装配的注解方式
    // 不过现在这两种方式都是不推荐使用的

}
