package com.github.springbootdemo.demo.service;

import com.github.springbootdemo.demo.entity.User;
import com.github.springbootdemo.demo.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    // UserService依赖UserMapper和BCryptPasswordEncoder两个Bean
    // 但是我们要做的是单元测试，不能引入其他的相关依赖
    // mockito为我们提供了mock注解，其原理为继承

    @Mock
    BCryptPasswordEncoder mockEncoder;
    @Mock
    UserMapper mockMapper;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave(){
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");

        userService.save("myUser","myPassword");

        Mockito.verify(mockMapper).save("myUser","myEncodedPassword");
    }

    @Test
    public void testGetUserByUsername(){
        userService.getUserByUsername("myUser");
        Mockito.verify(mockMapper).findUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound(){
        Assertions.assertThrows(UsernameNotFoundException.class,()->userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound(){
        Mockito.when(mockMapper.findUserByUsername("myUser")).thenReturn(new User(11,"myUser","myPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUser");
        Assertions.assertEquals("myUser",userDetails.getUsername());
        Assertions.assertEquals("myPassword",userDetails.getPassword());
    }
}