package com.example.destopinion.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.destopinion.config.BaseContext;
import com.example.destopinion.config.R;
import com.example.destopinion.entity.Person;
import com.example.destopinion.service.PersonService;
import com.example.destopinion.util.RSAUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

@Slf4j
@RequestMapping("/person")
@RestController
@Configuration
@CrossOrigin
public class PersonController {

    @Autowired
    private PersonService personService;


    private final KeyPair keyPair;

    public PersonController() throws NoSuchAlgorithmException, NoSuchProviderException {
        //构造函数中初始化数据
        keyPair = RSAUtils.generateKeyPair();
    }

    //用户信息展示功能,需要加密传输
    @GetMapping("/show")//给前端传输加密后的字符串
    public R<String> show() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //直接利用ThreadLocal 进行用户id的获取并且展示即可
        Long userId = BaseContext.getId();//能到当前界面肯定是已经登录了的

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Person::getUserId,userId);

        Person one = personService.getOne(wrapper);
        if(one == null){
            return R.success("");//返回数据为空
        }

        //加密后返回类型
        byte[] encrypt = RSAUtils.encrypt(JSON.toJSONString(one).getBytes(), keyPair.getPublic());//公钥加密

        return R.success(new String(encrypt));
    }

    @GetMapping("/look/{userId}")
    //查找用户电话号
    public String look(@PathVariable Long userId){
        return personService.looked(userId);
    }

    //用户信息添加功能//加密形成匿名功能
    @PutMapping("/add")
    public R<String> add(@PathVariable String person) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //rsa解密然后存入,前端需要传输过来Base64转化的rsa加密byte数组
        Long userId = BaseContext.getId();
        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Person::getUserId,userId);
        Person one = personService.getOne(wrapper);

        //解密person
        byte[] codes = Base64.getDecoder().decode(person);
        byte[] decrypt = RSAUtils.decrypt(codes, keyPair.getPrivate());//私钥解密

        String per = new String(decrypt);
        JSONObject jsonObject = JSON.parseObject(per);
        Person person1 = jsonCast(jsonObject);


        if(one != null){
            //如果不是null，就修改
            personService.updateById(person1);
            return R.success("添加成功");
        }
        //没有就进行添加
        personService.save(person1);

        return R.success("添加成功");
    }
    public Person jsonCast(JSONObject jsonObject){
        Person person1 = new Person();
        person1.setId(Long.parseLong(jsonObject.getString("Id")));
        person1.setGender(jsonObject.getString("gender"));
        person1.setNickname(jsonObject.getString("nickname"));
        person1.setNumber(jsonObject.getString("number"));
        person1.setUserId(Long.parseLong(jsonObject.getString("UserId")));

        return person1;
    }
}
