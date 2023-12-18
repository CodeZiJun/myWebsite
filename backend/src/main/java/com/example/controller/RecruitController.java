package com.example.controller;


import com.example.entity.recruit.RecruitApplication;
import com.example.entity.recruit.RecruitMessage;
import com.example.entity.recruit.RequestApplication;
import com.example.entity.recruit.RequestMsg;
import com.example.service.ApplicationService;
import com.example.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitService recruitService;

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/msg")
    public ResponseEntity<Map<String, Object>> handle_msg(){
        Map<String, Object> response = new HashMap<>();
        response.put("code",200);
        response.put("data",recruitService.findAll());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/addmsg")
    public ResponseEntity<Map<String, Object>> handle_addMsg(@RequestBody RequestMsg requestMsg){
        Map<String, Object> response = new HashMap<>();
        RecruitMessage recruitMessage=new RecruitMessage();
        recruitMessage.setTitle(requestMsg.getTitle());
        recruitMessage.setContent(requestMsg.getContent());
        recruitMessage.setDepartment(requestMsg.getDepartment());
        recruitMessage.setPublisher_id(11);
        recruitMessage.setPhone(requestMsg.getPhone());
        recruitMessage.setDate(requestMsg.getDate());
        int ret=recruitService.insertMsg(recruitMessage);
        response.put("code",200);
        if(ret==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/findMsgById")
    public ResponseEntity<Map<String, Object>> handle_findOneMsgById(@RequestBody RequestMsg requestMsg){
        Map<String, Object> response = new HashMap<>();
        int tid=requestMsg.getId();
        RequestMsg retmsg=recruitService.findOneById(tid);
        response.put("code",200);
        if(retmsg!=null){
            response.put("data",retmsg);
        }else {
            retmsg=new RequestMsg();
            response.put("data",retmsg);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteMsgById")
    public ResponseEntity<Map<String, Object>> handle_deleteOneMsgById(@RequestBody RequestMsg requestMsg){
        Map<String, Object> response = new HashMap<>();
        int tid=requestMsg.getId();
        int ret=recruitService.deleteOneById(tid);
        response.put("code",200);
        if(ret==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateMsg")
    public ResponseEntity<Map<String, Object>> handle_updateMsgById(@RequestBody RequestMsg requestMsg){
        Map<String, Object> response = new HashMap<>();
        RecruitMessage recruitMessage=new RecruitMessage();
        recruitMessage.setId(requestMsg.getId());
        recruitMessage.setTitle(requestMsg.getTitle());
        recruitMessage.setContent(requestMsg.getContent());
        recruitMessage.setDepartment(requestMsg.getDepartment());
        recruitMessage.setPublisher_id(11);
        recruitMessage.setPhone(requestMsg.getPhone());
        recruitMessage.setDate(requestMsg.getDate());
        int ret=recruitService.updateMsg(recruitMessage);
        response.put("code",200);
        if(ret==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/matchMsgContent")
    public ResponseEntity<Map<String, Object>> handle_matchMsgContent(@RequestBody String content){
        Map<String, Object> response = new HashMap<>();
        content=content.substring(0,content.length()-1);
        response.put("code",200);
        response.put("data",recruitService.searchByContent(content));
        return ResponseEntity.ok(response);
    }

    // 下面是关于招聘申请的接口

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> handle_application(){
        Map<String, Object> response = new HashMap<>();
        response.put("code",200);
        response.put("data",applicationService.findAll());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteApplyByUser")
    public ResponseEntity<Map<String, Object>> handle_delApplication(@RequestBody RequestApplication requestApplication){
        Map<String, Object> response = new HashMap<>();
        response.put("code",200);
        String uname=requestApplication.getUsername();
        int ret=applicationService.deleteApplyByUser(uname);
        int res=applicationService.deleteRequestApplyByUser(uname);
        response.put("code",200);
        if(ret==1&&res==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addApply")
    public ResponseEntity<Map<String, Object>> handle_addApplication(@RequestBody RequestApplication requestApplication){
        Map<String, Object> response = new HashMap<>();
        response.put("code",200);
        RecruitApplication recruitApplication=new RecruitApplication();
        recruitApplication.setPosition(requestApplication.getPosition());
        recruitApplication.setStatus("待处理");
        recruitApplication.setResumePath("/resources/"+requestApplication.getUsername()+"/"+requestApplication.getFilename());
        recruitApplication.setPhone(requestApplication.getPhone());
        recruitApplication.setApplicantUsername(requestApplication.getUsername());
        recruitApplication.setApplicationDate(requestApplication.getDate());
        recruitApplication.setSalary(requestApplication.getSalary());
        int ret=applicationService.insertApply(recruitApplication);
        int res=applicationService.insertRequestApplication(requestApplication);
        if(ret==1&&res==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/findApplyByUser")
    public ResponseEntity<Map<String, Object>> handle_findApplyByUser(@RequestBody RequestApplication requestApplication){
        Map<String, Object> response = new HashMap<>();
        String tname=requestApplication.getUsername();
        RequestApplication retapply=applicationService.findApplyByUsername(tname);
        response.put("code",200);
        if(retapply!=null){
            response.put("data",retapply);
        }else {
            retapply=new RequestApplication();
            response.put("data",retapply);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateApply")
    public ResponseEntity<Map<String, Object>> handle_updateApply(@RequestBody RequestApplication requestApplication){
        Map<String, Object> response = new HashMap<>();
        response.put("code",200);
        int res=applicationService.updateRequestApplication(requestApplication);
        if(res==1){
            response.put("data","success");
        }else {
            response.put("data","error");
        }
        return ResponseEntity.ok(response);
    }

}
