package com.github.springbootdemo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springbootdemo.demo.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
// 因为AuthController涉及到Mock数据以及处理Http请求，所以要引入Mockito和Spring的Extension
class AuthControllerTest {
    private MockMvc mvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUP(){
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(userService,authenticationManager)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(mvcResult -> {
                    System.out.println(mvcResult.getResponse().getContentAsString());
                    Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录"));
                });

    }


    @Test
    void login() throws Exception {
        // 登录操作第一步检验/auth接口应该为没有登录的状态
        mvc.perform(get("/auth").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(mvcResult -> {
            System.out.println(mvcResult.getResponse().getContentAsString());
            Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("用户没有登录"));
        });

        // mock登录数据
        Map<String,String> map = new HashMap<>();
        map.put("username","MyUser");
        map.put("password",bCryptPasswordEncoder.encode("MyPassword"));
        // 将传入的对象序列化为json，并返回给调用者
        String usernameAndPasswordJson = new ObjectMapper().writeValueAsString(map);

        // 需要mock数据
        Mockito.when(userService.loadUserByUsername("MyUser")).thenReturn(new User("MyUser",bCryptPasswordEncoder.encode("MyPassword"), Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("MyUser")).thenReturn(new com.github.springbootdemo.demo.entity.User(1,"MyUser",bCryptPasswordEncoder.encode("MyPassword")));

        MvcResult response = mvc.perform(post("/auth/login")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(usernameAndPasswordJson)
        )
                .andExpect(status().isOk())
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        System.out.println(mvcResult.getResponse().getContentAsString());
                        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("登录成功"));
                    }
                }).andReturn();

        HttpSession session = response.getRequest().getSession();

        // 登录之后，登录信息保存到Cookie中，然后再去/auth接口下查看登录信息
        mvc.perform(get("/auth").session((MockHttpSession)session).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andExpect(mvcResult -> {
            System.out.println(mvcResult.getResponse().getContentAsString());
            Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("MyUser"));
        });

    }
}