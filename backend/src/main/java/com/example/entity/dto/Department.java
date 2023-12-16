package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("db_department")
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @TableId(type = IdType.AUTO)
    Integer id;
    String departmentName;
    Integer ministerId;
}
