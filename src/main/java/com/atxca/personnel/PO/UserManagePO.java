package com.atxca.personnel.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: UserManage
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/13 17:33
 */
@Data
@Entity
@Table(name = "user_manage", catalog = "atxca")
@ApiModel(value = "UserManagePO对象", description = "管理员")
public class UserManagePO implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "user主键用户", name = "id", example = "1")
    private int id;

    @Column(name = "userName")
    @ApiModelProperty(value = "名称", name = "id", example = "小王")
    private String userName;

    @Column(name = "userPassWord")
    @ApiModelProperty(value = "md5加密后的密码", name = "userPassWord", example = "sadf65749asdf6sa48f9")
    private String userPassWord;
}
