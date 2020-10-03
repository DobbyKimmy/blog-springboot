package com.github.springbootdemo.demo.controller;

import com.github.springbootdemo.demo.entity.*;
import com.github.springbootdemo.demo.service.AuthService;
import com.github.springbootdemo.demo.service.BlogService;
import com.github.springbootdemo.demo.service.UserService;
import com.github.springbootdemo.demo.utils.AssertUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class BlogController {
    private BlogService blogService;
    private AuthService authService;

    @Inject
    public BlogController(BlogService blogService,AuthService authService){
        this.blogService = blogService;
        this.authService = authService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public Result getBlogs(@RequestParam Integer page,@RequestParam(value = "userId",required = false) Integer userId) {
        // Integer page, Integer pageSize, Integer userId
        if(page == null || page < 0){
            page = 1;
        }
        return blogService.getBlogs(page,10,userId);
    }

    @GetMapping(value = "/blog/{blogId}")
    @ResponseBody
    public Result getBlogById(@PathVariable("blogId") Integer blogId){
        return blogService.getBlogById(blogId);
    }

    // 提交一篇新的博客
    @PostMapping("/blog")
    @ResponseBody
    public BlogResult createBlog(@RequestBody Map<String, String> param){
        try{
            return authService.getCurrentUser()
                    .map(user -> blogService.insertBlog(createBlogFromParam(param,user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        }catch (IllegalArgumentException e){
            return BlogResult.failure(e);
        }
    }

    // 修改博客
    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult updateBlog(@PathVariable("blogId") Integer blogId,@RequestBody Map<String, String> param){
        try{
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlog(blogId,createBlogFromParam(param,user)))
                    .orElse(BlogResult.failure("登录后才能操作"));
        }catch (IllegalArgumentException e){
            return BlogResult.failure(e);
        }
    }

    // 删除博客
    @DeleteMapping(value = "/blog/{blogId}")
    @ResponseBody
    public BlogResult deleteBlog(@PathVariable("blogId") Integer blogId){
        try{
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlog(blogId,user))
                    .orElse(BlogResult.failure("登录后才能操作"));
        }catch (IllegalArgumentException e){
            return BlogResult.failure(e);
        }
    }
    private Blog createBlogFromParam(Map<String,String> param,User user){
        Blog blog = new Blog();
        String title = param.get("title");
        String description = param.get("description");
        String content = param.get("content");
        AssertUtils.assertTrue(title != null && title.length() <= 100,"title is invalid");
        AssertUtils.assertTrue(content != null && content.length() <= 10000,"content is invalid");
        if(description == null){
            description = content.substring(0,Math.min(content.length(),10)) + "...";
        }
        blog.setTitle(title);
        blog.setDescription(description);
        blog.setContent(content);
        blog.setUser(user);
        return blog;
    }


}
