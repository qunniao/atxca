package com.atxca.sportshall.VO;

import com.atxca.sportshall.PO.PeriodTimePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 王志鹏
 * @title: VenueVO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/22 11:29
 */
@Data
@ApiModel(value = "VenueVO", description = "后台场地数据")
public class VenueVO implements Serializable {

    @ApiModelProperty(value = "主键", name = "vid", example = "1")
    private int vid;

    @ApiModelProperty(value = "编号", name = "vNumber", example = "1s2gfa131")
    private String vNumber;

    @ApiModelProperty(value = "场地名称", name = "vName", example = "篮球场地01")
    private String vName;

    @ApiModelProperty(value = "场地类型1篮球馆、2羽毛球馆、3网球馆、4足球馆、5笼室足球馆", name = "type", example = "1")
    private int type;

    @ApiModelProperty(value = "组别,1篮球组,2羽毛球组,3网球组,4足球组,5笼室足球组", name = "groups", example = "1")
    private int groups;

    @ApiModelProperty(value = "0禁用,1启用", name = "status", example = "1")
    private int status;

    @ApiModelProperty(value = "详细", name = "periodTimePOList", example = "1")
    private List<PeriodTimePO> periodTimePOList;

    @ApiModelProperty(value = "日期", name = "查询日期", example = "2019-05-01")
    private Timestamp times;

    @ApiModelProperty(value = "时间段", name = "betTime", example = "10:00-11:00")
    private String betTime;

    @ApiModelProperty(value = "最小价格", name = "price", example = "110")
    private int startPrice;

    @ApiModelProperty(value = "最大价格", name = "price", example = "120")
    private int endPrice;

    @ApiModelProperty(value = "子对象参数", name = "periodTimePO", example = "periodTimePO.xxx")
    private PeriodTimePO periodTimePO;

}
