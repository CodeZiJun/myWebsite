package com.example.service;


import com.example.entity.recruit.RecruitApplication;
import com.example.entity.recruit.RequestApplication;
import com.example.mapper.ApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    public List<RecruitApplication> findAll(){
        return applicationMapper.findAll();
    }

    public int deleteApplyByUser(String uname){
        return applicationMapper.deleteApplyByUser(uname);
    }

    public int deleteRequestApplyByUser(String uname){
        return applicationMapper.deleteRequestApplyByUser(uname);
    }

    public int insertApply(RecruitApplication recruitApplication){
        return applicationMapper.insertApply(recruitApplication);
    }

    public int updateApply(RecruitApplication recruitApplication){
        return applicationMapper.updateApply(recruitApplication);
    }

    public int insertRequestApplication(RequestApplication requestApplication){
        return applicationMapper.insertRequestApplication(requestApplication);
    }

    public RequestApplication findApplyByUsername(String uname){
        return applicationMapper.findApplyByUsername(uname);
    }

    public int updateRequestApplication(RequestApplication requestApplication){
        return applicationMapper.updateRequestApplication(requestApplication);
    }

    public int insertReply(String reply,String uname){
        return applicationMapper.insertReply(reply,uname);
    }

    public String viewReply(String uname){
        return applicationMapper.viewReply(uname);
    }

    public int updateStatus(String uname){return applicationMapper.updateStatus(uname);}
}
