package com.example.destopinion.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.destopinion.entity.Opinion;
import com.example.destopinion.mapper.OpinionMapper;
import com.example.destopinion.service.OpinionService;
import org.springframework.stereotype.Service;

@Service
public class OpinionServiceImpl extends ServiceImpl<OpinionMapper, Opinion> implements OpinionService {
}
