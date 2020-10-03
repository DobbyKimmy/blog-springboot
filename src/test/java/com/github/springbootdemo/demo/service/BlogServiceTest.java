package com.github.springbootdemo.demo.service;

import com.github.springbootdemo.demo.dao.BlogDao;
import com.github.springbootdemo.demo.entity.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    BlogDao blogDao;

    @InjectMocks
    BlogService blogService;

    @Test
    public void getBlogsFromDB(){
        blogService.getBlogs(1,10,null);

        Mockito.verify(blogDao).getBlogs(1,10,null);
    }

    // 当dao丢出一个异常，我们希望返回数据：
    // {"status": "fail", "msg": "系统异常"}
    @Test
    public void returnFailureWhenExceptionThrown(){
        // 当blogDao的查询数据库操作返回一个异常的时候
        when(blogDao.getBlogs(anyInt(), anyInt(), any())).thenThrow(new RuntimeException());
        Result result = blogService.getBlogs(1,10,null);

        Assertions.assertEquals("fail",result.getStatus());
        Assertions.assertEquals("系统异常",result.getMsg());
    }

}
