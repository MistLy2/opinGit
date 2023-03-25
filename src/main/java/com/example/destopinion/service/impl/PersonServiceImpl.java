package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.entity.Person;
import com.example.destopinion.mapper.PersonMapper;
import com.example.destopinion.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

    @Autowired
    private  PersonService personService;
    public String looked(Long userId){
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Person::getUserId,userId);

        Person one = personService.getOne(wrapper);
        return one.getNumber();
    }
}
