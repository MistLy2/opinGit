package com.example.destopinion.dto;

import com.example.destopinion.entity.Opinion;
import lombok.Data;

@Data
public class OpinionDto extends Opinion{
    private static final long serialVersionUID = 1L;

    private String nickName;
}
