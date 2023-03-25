package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.entity.Good;
import com.example.destopinion.mapper.GoodMapper;
import com.example.destopinion.service.GoodService;
import org.springframework.stereotype.Service;

@Service
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements GoodService {
}
