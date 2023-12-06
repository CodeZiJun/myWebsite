package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("db_uploadFile")
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** * 文件实际名称 */
    private String realName;
    /** * 文件名 */
    private String fileName;
    /** * 文件主名称 */
    private String primaryName;
    /** * 文件扩展名 */
    private String extension;
    /** * 存放路径 */
    private String path;
    /** * 文件类型 */
    private String type;
    /** * 文件大小 */
    private Long size;
    /** * 上传人 */
    private String uploader;
    private Timestamp createTime;
    @Override
    public String toString() {
        return "UploadFile{" +
                "fileName='" + fileName + '\'' +
                ", uploader='" + uploader + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
