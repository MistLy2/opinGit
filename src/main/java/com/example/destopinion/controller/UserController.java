package com.example.destopinion.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.destopinion.config.BaseContext;
import com.example.destopinion.config.R;
import com.example.destopinion.config.SaltGenerator;
import com.example.destopinion.dto.UpdateDto;
import com.example.destopinion.dto.UserDto;
import com.example.destopinion.entity.User;
import com.example.destopinion.service.PersonService;
import com.example.destopinion.service.UserService;
import com.example.destopinion.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RequestMapping("/user")
@RestController
@Configuration
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonService personService;


    @PostMapping("/login")
    public R<UserDto> login(HttpSession session, @RequestBody Map map){
        //这里用session实现登录功能
        //log.info(map.toString());//通过map也可以进行json数据接收
        //System.out.println("进来了");
        String account = map.get("account").toString();

        String password = map.get("password").toString();

        String admin = map.get("admin").toString();

        System.out.println(map);

        //如果redis没有命中，就需要去数据库中查找
        //如果存在就登录，并存到redis中，如果不存在就返回登录失败
        LambdaQueryWrapper<User> wrap = new LambdaQueryWrapper<User>();
        wrap.eq(User::getAccount,account);

        User uu=userService.getOne(wrap);//判断密码相同即可
        if(uu != null){
            //暂时不设置过期时间
            //判断密码是否相等
            String password1 = uu.getPassword();
            String salt = password1.split(":")[1];//拿到盐值
            if(!password1.equals(salt+password) && uu.getAdmin()!=Integer.valueOf(admin)){
                return R.error("密码错误 或 您不是管理员");
            }

            session.setAttribute("user",uu.getId());
            BaseContext.setId(uu.getId());

            String token = JwtUtils.getJwtToken(BaseContext.getId(),account);//生成令牌

            UserDto userDto1 = new UserDto();
            userDto1.setUser(uu);
            userDto1.setToken(token);

            return R.success(userDto1);

        }
        return R.error("账号或密码错误，请重试");
    }
    //注册功能
    @PostMapping("/register")
    public R<String> register(@RequestBody Map map){
        String account = map.get("account").toString();

        String password = map.get("password").toString();

        String admin = map.get("admin").toString();

        User user = new User();
        user.setAccount(account);//这里密码用加密加盐进行

        String p = DigestUtils.md5DigestAsHex(password.getBytes())+":"+SaltGenerator.getSalt();
        user.setPassword(p);//加密加盐存储
        user.setAdmin(Integer.parseInt(admin));

        userService.save(user);//mp的根据自动雪花算法会生成用户id

        return R.success("注册成功，请返回登录");
    }

    //根据id查找指定用户
    @GetMapping("/find/{id}")
    public R find(@PathVariable Long id){
        return R.success(userService.finded(id));
    }

    //用户修改功能
    @PostMapping
    public R<String>  update(@RequestBody User user){
        userService.updateById(user);
        return R.success("修改成功");
    }

    //修改用户信任值
    @PostMapping("/trust")
    public String trust(int type,Long userId){
        return userService.trusted(type);
    }

//    //token验证,每一次页面跳转的时候进行token验证
//    @GetMapping("/checkToken")
//    public Boolean checkToken(HttpServletRequest request){
//        String token = request.getHeader("token");
//
//        return JwtUtils.checkToken(token);
//    }

    //修改密码  也是忘记密码
    @PostMapping("/up")
    public R<String> updatePassword(@RequestBody UpdateDto userDto){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount,userDto.getAccount());

        User one = userService.getOne(wrapper);
        if(one == null){
            return R.error("对不起，没有此账号");
        }
        String number = personService.looked(one.getId());

        if(number.equals("null")){
            return R.error("对不起 您的电话号为空");
        }
        if(number.equals(userDto.getNumber())){
            //如果电话号相同，修改密码即可
            String p = one.getPassword();
            String salt = p.split(":")[1];
            one.setPassword(DigestUtils.md5DigestAsHex(userDto.getPassword().getBytes())+":"+salt);
            userService.updateById(one);
            return R.success("修改成功");
        }
        //电话号不相同返回错误
        return R.error("您的电话号不正确");
    }
}
