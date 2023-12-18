package com.example.entity.recruit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("recruitmsg")
@AllArgsConstructor
@NoArgsConstructor
public class RecruitMessage {
    @TableId(type = IdType.AUTO)
    Integer id;
    String title;
    String content;
    String department;
    Integer publisher_id;
    String phone;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date date;
}
