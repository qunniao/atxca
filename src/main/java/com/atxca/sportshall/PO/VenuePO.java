package com.atxca.sportshall.PO;

import com.atxca.data.PO.TypePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author 王志鹏
 * @title: VenuePO
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 10:16
 */

@Data
@Entity
@Table(name = "venues", catalog = "atxca")
@ApiModel(value = "VenuePO", description = "场地")
public class VenuePO implements Serializable {

    @Id
    @Column(name = "vid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", name = "vid", example = "1")
    private int vid;

    @Column(name = "vNumber")
    @ApiModelProperty(value = "编号", name = "vNumber", example = "1s2gfa131")
    private String vNumber;

    @Column(name = "vName")
    @ApiModelProperty(value = "场地名称", name = "vName", example = "篮球场地01")
    private String vName;

    @Column(name = "type")
    @ApiModelProperty(value = "场地类型1篮球馆、2羽毛球馆、3网球馆、4足球馆、5笼室足球馆", name = "type", example = "1")
    private int type;

    @Column(name = "groups")
    @ApiModelProperty(value = "组别,1篮球组,2羽毛球组,3网球组,4足球组,5笼室足球组", name = "groups", example = "1")
    private int groups;

    @Column(name = "status")
    @ApiModelProperty(value = "0禁用,1启用", name = "status", example = "1")
    private int status;

    @Column(name = "sort")
    @ApiModelProperty(value = "0禁用,1启用", name = "sort", example = "0")
    private int sort;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "vid", referencedColumnName = "vid")
    @ApiModelProperty(value = "详细", name = "periodTimePOList", example = "1")
    private List<PeriodTimePO> periodTimePOList;

    @Transient
    @ApiModelProperty(value = "子对象参数", name = "periodTimePO", example = "periodTimePO.xxx")
    private PeriodTimePO periodTimePO;


    @OneToOne(targetEntity = TypePO.class)
    @JoinColumn(name = "type", referencedColumnName = "id", insertable = false, updatable = false)
    private TypePO typePO;

    private int yuyue_state;
}
