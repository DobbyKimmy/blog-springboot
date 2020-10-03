package com.github.springbootdemo.demo.dao;


import com.github.springbootdemo.demo.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class BlogDaoTest {
    private SqlSession mockSession = Mockito.mock(SqlSession.class);
    private BlogDao blogDao = new BlogDao(mockSession);


    @Test
    public void testGetBlogs(){
        // page = 2,pageSize = 10,userId = 1
        blogDao.getBlogs(2,10,1);
        Map<String,Object> expectedParam = new HashMap<>();
        expectedParam.put("user_id",1);
        expectedParam.put("offset",10);
        expectedParam.put("limit",10);

        Mockito.verify(mockSession).selectList("selectBlog",expectedParam);
    }

    @Test
    public void testGetBlogByBlogId(){
        blogDao.getBlogByBlogId(1);
        Map<String,Integer> expectedParam = new HashMap<>();
        expectedParam.put("blog_id",1);
        Mockito.verify(mockSession).selectOne("selectBlogById",expectedParam);
    }

    @Test
    public void testInsertBlog(){
        Blog newBlog = new Blog();
        newBlog.setId(1);
        blogDao.insertBlog(newBlog);
        Mockito.verify(mockSession).insert("insertBlog",newBlog);
    }

    @Test
    public void testUpdateBlog(){
        Blog targetBlog = new Blog();
        targetBlog.setId(1);
        blogDao.updateBlog(targetBlog);
        Mockito.verify(mockSession).update("updateBlog",targetBlog);
    }

    @Test
    public void testDeleteBlog(){
        blogDao.deleteBlog(1);
        Mockito.verify(mockSession).delete("deleteBlog",1);
    }
}