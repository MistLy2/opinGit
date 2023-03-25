package com.example.destopinion.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.destopinion.entity.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
