package com.atxca.module.Controller;

import com.atxca.module.Dao.ModuleDao;
import com.atxca.module.PO.ModulePO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 王志鹏
 * @title: MoudelController
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/22 14:42
 */
@Api(tags = "后台管理页面列表渲染用户API")
@RestController
@RequestMapping(value = "/modules")
public class MoudelController {
    @Autowired
    private ModuleDao moduleDao;

    @ApiOperation("获得页面列表数据")
    @RequestMapping(value = "/findAllModule", method = RequestMethod.POST)
    public List<ModulePO> findAllModule() {

        return moduleDao.queryAllBySrot();
    }
}
