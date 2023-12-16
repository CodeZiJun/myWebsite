package com.example.entity.dto;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_archives")
public class Archives {
    @TableId(type = IdType.AUTO)
    Integer id;
    String realName;
    String gender;
    String telephone;
    String address;
    String personalId;
    Date entryDate;
    Integer departmentId;
    Integer positionId;
    DateTime renewTime;
    String personalIntro;
    String education;
}
