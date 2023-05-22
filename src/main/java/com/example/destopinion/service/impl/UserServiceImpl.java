package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.config.BaseContext;
import com.example.destopinion.entity.User;
import com.example.destopinion.mapper.UserMapper;
import com.example.destopinion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    public String trusted(int type){
        Object o = redisTemplate.opsForValue().get(BaseContext.getId()+"");
        if(o != null){
            User user = (User) o;
            if(type ==1){
                user.setTrustValue(user.getTrustValue() + 20);
                userService.updateById(user);
            }else {
                user.setTrustValue(user.getTrustValue() - 20);
                userService.updateById(user);
            }
            return "修改成功";
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,BaseContext.getId());

        User one = userService.getOne(wrapper);

        if(type ==1){
            one.setTrustValue(one.getTrustValue() + 20);
            userService.updateById(one);
        }else {
            one.setTrustValue(one.getTrustValue() - 20);
            userService.updateById(one);
        }
        redisTemplate.opsForValue().set(one.getId()+"",one);//存入redis

        return "修改成功";
    }

    public User finded(Long id){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,id);

        User uu = (User)redisTemplate.opsForValue().get(id+"");
        if(uu != null){
            //说明redis中存在此用户
            return uu;
        }
        User user = userService.getOne(wrapper);
        redisTemplate.opsForValue().set(id+"",user);
        //注意这里还有一点问题
        //当用户信息发生修改的时候需要发生改变
        return user;
    }
}
