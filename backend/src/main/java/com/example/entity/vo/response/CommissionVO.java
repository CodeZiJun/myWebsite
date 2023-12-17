package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionVO {
    String username;
    String realName;
    String email;
    String departmentName;
    String positionName;
    Date entryDate;
}
