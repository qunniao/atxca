package com.atxca.sportshall.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: Vhouse
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/13 9:32
 */
@Data
@Entity
@Table(name = "vhouse", catalog = "atxca")
@ApiModel(value = "Vhouse", description = "场馆")
public class Vhouse implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", name = "id", example = "1")
    private int  id;

    @Column(name = "name")
    @ApiModelProperty(value = "场馆名称", name = "name", example = "篮球馆")
    private String name;


    @Column(name = "type")
    @ApiModelProperty(value = "类型:1篮球,2羽毛球,3网球,4足球,5笼式足球,6,乒乓球", name = "type", example = "1")
    private int  type;

    @Column(name = "url")
    @ApiModelProperty(value = "url", name = "url", example = "url")
    private String url;
}
