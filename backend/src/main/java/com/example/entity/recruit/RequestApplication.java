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
@TableName("request_applications")
@AllArgsConstructor
@NoArgsConstructor
public class RequestApplication {
    @TableId(type = IdType.AUTO)
    Integer id;
    String content;
    String position;
    String salary;
    String phone;

    @JsonFormat(pattern="yyyy-MM-dd")
    Date date;

    String filename;
    String username;
    String reply;
}
