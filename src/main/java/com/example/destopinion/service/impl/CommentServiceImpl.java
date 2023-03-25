package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.entity.Comment;
import com.example.destopinion.mapper.CommentMapper;
import com.example.destopinion.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
