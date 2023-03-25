package com.example.destopinion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.destopinion.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
