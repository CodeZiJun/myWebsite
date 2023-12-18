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
@TableName("recruitment_applications")
@AllArgsConstructor
@NoArgsConstructor
public class RecruitApplication {
    @TableId(type = IdType.AUTO)
    Integer id;
    String position;
    String status;
    String phone;
    String salary;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date applicationDate;

    String resumePath;
    String applicantUsername;
}
