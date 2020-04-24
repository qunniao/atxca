package com.atxca.data.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: TypePO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/14 9:20
 */
@Data
@Entity
@Table(name = "typepo", catalog = "atxca")
@ApiModel(value = "TypePO", description = "场馆类型")
public class TypePO implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键:类型", name = "id", example = "1")
    private int id;

    @Column(name = "name")
    @ApiModelProperty(value = "类型名称", name = "name", example = "1")
    private String name;

    @Column(name = "url")
    @ApiModelProperty(value = "类型图片路径", name = "url", example = "1")
    private String url;

    @Column(name = "sort")
    @ApiModelProperty(value = "排序", name = "sort", example = "0")
    private int sort;

}
