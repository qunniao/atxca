package com.atxca.data.PO;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: DaochuPO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/14 14:15
 */
@Data
public class DaochuPO implements Serializable {

    private String vName;

    private String name;

    private int peoplenum;

    private double price;

}
