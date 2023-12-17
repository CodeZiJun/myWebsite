package com.example.entity.vo.response;

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
    Date renewTime;
    String personalIntro;
    String education;
}
