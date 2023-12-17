package com.example.entity.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivesAddVO {
    String realName;
    String telephone;
    String gender;
    String personalId;
    String address;
    String education;
    String personalIntro;
    String email;
}
