package com.example.destopinion.dto;

import com.example.destopinion.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private User user;

    private String token;
}
