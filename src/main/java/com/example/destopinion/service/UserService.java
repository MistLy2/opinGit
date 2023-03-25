package com.example.destopinion.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.destopinion.entity.User;

public interface UserService extends IService<User> {
    public String trusted(int type);


    public User finded(Long id);
}
