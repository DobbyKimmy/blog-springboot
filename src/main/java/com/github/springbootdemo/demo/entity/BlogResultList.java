package com.github.springbootdemo.demo.entity;

import java.util.List;

public class BlogResultList extends Result<List<Blog>>{
    private int total;
    private int page;
    private int totalPage;

    private BlogResultList(ResultStatus status, String msg, List<Blog> data,int total,int page,int totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public static BlogResultList success(List<Blog> data,int total,int page,int totalPage){
        return new BlogResultList(ResultStatus.OK,"获取成功",data, total,page,totalPage);
    }

    public static BlogResultList failure(String msg){
        return new BlogResultList(ResultStatus.FAIL,msg,null,0,0,0);
    }
}
