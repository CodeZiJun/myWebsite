package com.example.mapper;

import com.example.entity.recruit.RecruitApplication;
import com.example.entity.recruit.RecruitMessage;
import com.example.entity.recruit.RequestMsg;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface RecruitMapper {
    @Select("SELECT * FROM recruitmsg")
    List<RecruitMessage> findAll();

    @Insert("INSERT INTO recruitmsg (title, content,department,publisher_id,phone,date) VALUES (#{title}, #{content},#{department},#{publisher_id},#{phone},#{date})")
    int insertMsg(RecruitMessage recruitMsg);

    @Select("SELECT * FROM recruitmsg where id=#{id}")
    RequestMsg findOneById(int id);

    @Delete("DELETE FROM recruitmsg where id=#{id}")
    int deleteOneById(int id);

    @Update("UPDATE recruitmsg " +
            "SET title = #{title}, " +
            "content = #{content}, " +
            "department = #{department}, " +
            "publisher_id = #{publisher_id}, " +
            "phone = #{phone}, " +
            "date = #{date} " +
            "WHERE id = #{id}")
    int updateMsg(RecruitMessage recruitMsg);


    @Select("SELECT * FROM recruitmsg WHERE content LIKE CONCAT('%', #{content}, '%')")
    List<RecruitMessage> searchByContent(@Param("content") String content);


}
