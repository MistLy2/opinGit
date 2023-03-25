package com.example.destopinion.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.destopinion.entity.Person;

public interface PersonService extends IService<Person> {
    public String looked(Long userId);
}
