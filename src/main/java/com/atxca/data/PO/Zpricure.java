package com.atxca.data.PO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * @author 王志鹏
 * @title: Zpricure
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/14 19:51
 */
@Data
public class Zpricure {
    @JsonProperty("year")
    private String reserveTime;
    @JsonProperty("value")
    private int numers;
}
