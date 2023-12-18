package com.example.mapper;

import com.example.entity.recruit.RecruitApplication;
import com.example.entity.recruit.RecruitMessage;
import com.example.entity.recruit.RequestApplication;
import com.example.entity.recruit.RequestMsg;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ApplicationMapper {
    @Select("SELECT * FROM recruitment_applications")
    List<RecruitApplication> findAll();

    @Delete("DELETE FROM recruitment_applications where applicant_username=#{uname}")
    int deleteApplyByUser(String uname);

    @Delete("DELETE FROM request_applications where username=#{uname}")
    int deleteRequestApplyByUser(String uname);


    @Insert("INSERT INTO recruitment_applications " +
            "(position, status, phone, application_date, resume_path, applicant_username,salary) " +
            "VALUES (#{position}, #{status}, #{phone}, #{applicationDate}, #{resumePath}, #{applicantUsername},#{salary})")
    int insertApply(RecruitApplication recruitApplication);

    @Update("UPDATE recruitment_applications " +
            "SET position = #{position}, " +
            "status = #{status}, " +
            "phone = #{phone}, " +
            "application_date = #{applicationDate}, " +
            "resume_path = #{resumePath}, " +
            "applicant_username = #{applicantUsername} " +
            "salary = #{salary} " +
            "WHERE id = #{id}")
    int updateApply(RecruitApplication recruitApplication);

    @Insert("INSERT INTO request_applications " +
            "(content, position, salary, phone, date, filename, username) " +
            "VALUES (#{content}, #{position}, #{salary}, #{phone}, #{date}, #{filename}, #{username})")
    int insertRequestApplication(RequestApplication requestApplication);

    @Select("SELECT * FROM request_applications where username=#{uname}")
    RequestApplication findApplyByUsername(String uname);

    @Update("UPDATE request_applications " +
            "SET content = #{content}, " +
            "position = #{position}, " +
            "salary = #{salary}, " +
            "phone = #{phone}, " +
            "date = #{date}, " +
            "filename = #{filename} "+
            "WHERE username = #{username}")
    int updateRequestApplication(RequestApplication requestApplication);

    @Insert("INSERT INTO request_applications (reply) values (#{reply}) WHERE username=#{uname}")
    int insertReply(String reply,String uname);

    @Select("SELECT reply FROM request_applications where username=#{uname}")
    String viewReply(String uname);

}
