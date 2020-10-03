package com.github.springbootdemo.demo.dao;

import com.github.springbootdemo.demo.entity.Blog;
import com.github.springbootdemo.demo.entity.BlogResult;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;
    @Inject
    public BlogDao(SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("user_id",userId);
        parameters.put("offset",(page - 1) * pageSize);
        parameters.put("limit",pageSize);
        return sqlSession.selectList("selectBlog",parameters);
    }
    public int count(Integer userId){
        return sqlSession.selectOne("countBlog",userId);
    }

    public Blog getBlogByBlogId(Integer blogId) {
        Map<String,Integer> parameters = new HashMap<>();
        parameters.put("blog_id",blogId);
        return sqlSession.selectOne("selectBlogById",parameters);
    }

    public Blog insertBlog(Blog newBlog) {
        sqlSession.insert("insertBlog",newBlog);
        return getBlogByBlogId(newBlog.getId());
    }

    public Blog updateBlog(Blog targetBlog) {
        sqlSession.update("updateBlog",targetBlog);
        return getBlogByBlogId(targetBlog.getId());
    }

    public void deleteBlog(Integer blogId) {
        sqlSession.delete("deleteBlog",blogId);
    }
}
