package com.atxca.files;

import com.atxca.Util.PO.Result;
import com.atxca.Util.ResultUtil;
import com.atxca.sportshall.Dao.VenueDao;
import com.atxca.sportshall.Dao.VhouseDao;
import com.atxca.sportshall.PO.Vhouse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.atxca.core.file.FastDFSClient.uploadUtil;

/**
 * @author 王志鹏
 * @title: FileController
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/11 17:19
 */

@Api(tags = "文件")
@RestController
@RequestMapping(value = "/files")
public class FileController {
    private static final String URL_HEAD = "http://39.106.78.98/";

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private VenueDao venueDao;

    @Autowired
    private VhouseDao vhouseDao;

    @ApiOperation(value = "查询轮播图", notes = "无")
    @RequestMapping(value = "qeryImages", method = RequestMethod.POST)
    public Result<List<ImagePO>> qeryImages() throws Exception {

        return ResultUtil.success(imageDao.findAll());
    }


    @ApiOperation(value = "轮播上传", notes = "无")
    @ApiImplicitParam(name = "files", value = "文件", required = true)
    @RequestMapping(value = "uploadScrollimg", method = RequestMethod.POST)
    public Result<Boolean> uploadScrollimg(@RequestParam("files") MultipartFile file) throws Exception {

        String url = uploadUtil(file);
        ImagePO imagePO = new ImagePO();
        imagePO.setUrl(URL_HEAD + url);
        imageDao.save(imagePO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "上传场馆图标", notes = "无")
    @RequestMapping(value = "uploadBuildingImage", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> uploadBuildingImage(@RequestParam("files") MultipartFile file,@RequestParam int id) throws Exception {

        Vhouse vhouse = vhouseDao.queryById(id);
        String url = uploadUtil(file);
        vhouse.setUrl(URL_HEAD + url);
        vhouseDao.save(vhouse);
        return ResultUtil.success(true);
    }

}
