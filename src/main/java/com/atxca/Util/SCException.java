package com.atxca.Util;

import com.atxca.Util.Enums.ResultEnum;
import lombok.Data;


/**
 * @author 王志鹏
 * @title: ErrorCode
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/21 10:17 RuntimeException
 */

@Data
public class SCException extends RuntimeException {

    private Integer code;

    public SCException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public SCException(Integer code,String msg) {
        super(msg);
        this.code = code;
    }

    public SCException(String msg) {
        super(msg);
        this.code = this.getCode();
    }


}
