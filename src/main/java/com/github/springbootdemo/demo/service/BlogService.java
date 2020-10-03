package com.github.springbootdemo.demo.service;

import com.github.springbootdemo.demo.dao.BlogDao;
import com.github.springbootdemo.demo.entity.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@Service
public class BlogService {

    private BlogDao blogDao;
    private UserService userService;

    @Inject
    public BlogService(BlogDao blogDao,UserService userService){
        this.blogDao = blogDao;
        this.userService = userService;
    }

    public BlogResultList getBlogs(Integer page, Integer pageSize, Integer userId){
        try {
            List<Blog> blogs = blogDao.getBlogs(page, pageSize, userId);

            int count = blogDao.count(userId);
            // id为userId的用户博客总页数
            int pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogResultList.success(blogs, count, page, pageCount);
        }catch (Exception e){
            return BlogResultList.failure("系统异常");
        }
    }

    public BlogResult getBlogById(Integer blogId){
        try {
            return BlogResult.success("获取成功",blogDao.getBlogByBlogId(blogId));
        }catch (Exception e){
            return BlogResult.failure(e);
        }
    }

    public BlogResult insertBlog(Blog newBlog) {
        try{
            return BlogResult.success("创建成功",blogDao.insertBlog(newBlog));
        }catch (Exception e){
            return BlogResult.failure(e);
        }
    }

    public BlogResult updateBlog(Integer blogId,Blog targetBlog) {
        Blog blogInDB = blogDao.getBlogByBlogId(blogId);
        if(blogId == null){
            return BlogResult.failure("博客不存在");
        }
        // Objects.equals(blogId,blogInDB.getId())
        if(!Objects.equals(blogId,blogInDB.getId())){
            return BlogResult.failure("无法修改别人的博客");
        }
        try{
            targetBlog.setId(blogId);
            return BlogResult.success("修改成功",blogDao.updateBlog(targetBlog));
        }catch (Exception e){
            return BlogResult.failure(e);
        }
    }

    public BlogResult deleteBlog(Integer blogId, User user) {
        Blog blogInDB = blogDao.getBlogByBlogId(blogId);
        if(blogId == null){
            return BlogResult.failure("博客不存在");
        }
        if(!Objects.equals(blogInDB.getUser().getId(),user.getId())){
            return BlogResult.failure("无法删除别人的博客");
        }
        try{
            blogDao.deleteBlog(blogId);
            return BlogResult.success("删除成功");
        }catch (Exception e){
            return BlogResult.failure(e);
        }
    }
}
