package com.example.destopinion.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChildComment {
    private static final long serialVersionUID = 1L;

    private Long id;//评论id

    private Long userId;//用户id

    private String commented;//评论内容

    private Long parentId;//记录父评论id

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;//更新时间

}
