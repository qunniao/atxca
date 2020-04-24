package com.atxca.Util.PO;

import lombok.Data;

/**
 * @author 王志鹏
 * @title: Result
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/28 9:33
 */
@Data
public class Result<T> {
    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体的内容.
     */
    private T data;
}
