package com.example.destopinion.entity;

import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
@Data
public class Reson implements Serializable {
    private static final long serialVersionUID = 1L;

    ResponseEntity  respon;

    List<Opinion>   list;

    public  Reson(ResponseEntity repon,List<Opinion> list){
        this.respon=repon;
        this.list=list;
    }
}
