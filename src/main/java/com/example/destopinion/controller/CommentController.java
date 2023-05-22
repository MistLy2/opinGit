package com.example.destopinion.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.destopinion.config.R;
import com.example.destopinion.entity.Comment;
import com.example.destopinion.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/comment")
@RestController
@CrossOrigin//处理服务跳转
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisTemplate redisTemplate;
    //这里的评论肯定要加缓存层，但是我这里就不加了，节省内存
    //评论数据来的时候，先加入redis，然后通过kafka异步刷新到数据库，并发情况下也可以使用分布式锁锁定，但是会有性能开销
    //也可以使用乐观并发的理念


    //添加评论
    @PostMapping("/add")
    public R<String> add(@RequestBody Comment comment){
        //添加功能，需要判断是不是子评论
        if(comment.getParent_id() == null){
            //说明是最顶层评论，直接添加即可
            commentService.saveOrUpdate(comment);
            return R.success("添加成功");
        }
        //剩下的说明是子评论,找到对应的父评论然后判断即可
        Long pid = comment.getParent_id();
        Comment pc = commentService.getById(pid);

        if(pc.getOrigin_id() == null){
            //说明父评论已经是顶级了
            comment.setOrigin_id(pc.getOrigin_id());
            commentService.saveOrUpdate(comment);

            return R.success("添加回复成功");
        }

        comment.setOrigin_id(pc.getOrigin_id());
        commentService.saveOrUpdate(comment);
        return R.success("添加回复成功");
    }



    //评论查询功能
    @GetMapping("/list")
    public R<List<Comment>> list(){
        List<Comment> allComments = commentService.list();
        List<Comment> comments = allComments.stream().filter(comment -> comment.getOrigin_id()==null).collect(Collectors.toList());

        //设置子评论
        for(Comment origin : comments){
            List<Comment> children = allComments.stream().filter(comment -> comment.getOrigin_id()== origin.getId()).collect(Collectors.toList());

            origin.setChildren(children);
        }

        return R.success(comments);
    }
    //删除评论
    @DeleteMapping("/delete/{commentId}")
    public R<String> delete(@PathVariable Long commentId){
        commentService.removeById(commentId);

        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getOrigin_id,commentId);

        commentService.remove(wrapper);//删除所有评论

        return R.success("删除成功");
    }
}
