package com.atxca.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 王志鹏
 * @title: UserPO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 16:31
 */

@Data
@Entity
@Table(name = "user", catalog = "atxca")
@ApiModel(value = "UserPO对象", description = "用户信息")
public class UserPO implements Serializable {

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "user主键用户", name = "uid", example = "1")
    private int uid;

    @Column(name = "openid")
    @ApiModelProperty(value = "微信openid", name = "openid;", example = "oy_LM5I3jcI--jySxskn6ooi1ZbA")
    private String openid;

    @Column(name = "name")
    @ApiModelProperty(value = "名称", name = "name", example = "小王")
    private String name;

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "2019-03-25 15:19:49")
    private Timestamp createTime;

}