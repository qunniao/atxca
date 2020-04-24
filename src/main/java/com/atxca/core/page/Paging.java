package com.atxca.core.page;


import lombok.Data;

/**
 * @author 王志鹏
 * @title: Pageing
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/1916:42
 */
@Data
public class Paging {

    private int pageNum = Integer.valueOf(1);

    private int pageSize = Integer.valueOf(15);

}
