package com.atxca.files;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: ImagePO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 17:23
 */

@Data
@Entity
@Table(name = "user", catalog = "atxca")
@ApiModel(value = "UserPO对象", description = "用户信息")
public class ImagePO implements Serializable {

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", name = "uid", example = "1")
    private int id;

    @Column(name = "url")
    @ApiModelProperty(value = "文件路径", name = "url", example = "1")
    private String url;

}
