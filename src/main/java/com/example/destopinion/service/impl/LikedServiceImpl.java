package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.entity.Liked;
import com.example.destopinion.mapper.LikedMapper;
import com.example.destopinion.service.LikedService;
import org.springframework.stereotype.Service;

@Service
public class LikedServiceImpl extends ServiceImpl<LikedMapper, Liked> implements LikedService {
}
