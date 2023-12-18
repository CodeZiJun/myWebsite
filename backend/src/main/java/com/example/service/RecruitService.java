package com.example.service;


import com.example.entity.recruit.RecruitMessage;
import com.example.entity.recruit.RequestMsg;
import com.example.mapper.RecruitMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    public List<RecruitMessage> findAll() {
        return recruitMapper.findAll();
    }

    public int insertMsg(RecruitMessage recruitMsg){
        return recruitMapper.insertMsg(recruitMsg);
    }

    public RequestMsg findOneById(int id){ return recruitMapper.findOneById(id); }

    public int deleteOneById(int id){return recruitMapper.deleteOneById(id);}

    public int updateMsg(RecruitMessage recruitMsg){return recruitMapper.updateMsg(recruitMsg);}

    public List<RecruitMessage> searchByContent(@Param("content") String content){
        return recruitMapper.searchByContent(content);
    }
}
