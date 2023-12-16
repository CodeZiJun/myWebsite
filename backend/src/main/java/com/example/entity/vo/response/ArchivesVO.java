package com.example.entity.vo.response;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivesVO {
    Integer id;
    String realName;
    String gender;
    String telephone;
    String address;
    String personalId;
    Date entryDate;
    String departmentName;
    String positionName;
    DateTime renewTime;
    String personalIntro;
    String education;
}
