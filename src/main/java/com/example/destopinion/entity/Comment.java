package com.example.destopinion.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;//评论id

    private Long userId;//用户id

    private Long opinionId;//舆情id，表示当前评论在此舆情底下

    private String commented;//评论内容

    private Long parent_id;//父评论id

    private Long origin_id;//源id

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;//更新时间


    @TableField(exist = false)//表示数据库表中不存在当前字段
    private List<Comment> children;
}
