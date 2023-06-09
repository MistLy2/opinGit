package com.example.destopinion.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    //用户实体类
    private static final long serialVersionUID = 1L;

    private Long id;//用户id

    private String account;//账号

    private String password;//密码

    private Long goodId;//商品id

    private int score;//积分

    private int trustValue;//信任值

    private int admin;//默认为0，不是管理员
}
