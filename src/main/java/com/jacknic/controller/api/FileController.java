package com.jacknic.controller.api;

import com.jacknic.model.bean.FileBean;
import com.jacknic.util.FileUtils;
import com.jacknic.util.Result;
import com.jacknic.util.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

@Api(tags = "file", description = "文件操作")
@RestController
@RequestMapping("/api/file")
public class FileController {


    @ApiOperation("显示根目录信息")
    @GetMapping("/root")
    public ResultBody root() {
        File[] files = File.listRoots();
        ArrayList<FileBean> fileBeans = FileUtils.toFileBeans(files);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", fileBeans);
        HashMap<String, HashMap<String, Object>> spaceList = new HashMap<>();
        for (File dir : files) {
            HashMap<String, Object> spaceInfo = new HashMap<>();
            spaceInfo.put("total", dir.getTotalSpace());
            spaceInfo.put("free", dir.getFreeSpace());
            spaceInfo.put("usable", dir.getUsableSpace());
            String name = dir.getAbsolutePath().replace('\\', '/');
            spaceList.put(name, spaceInfo);
        }
        resultMap.put("space", spaceList);
        return Result.data(resultMap);
    }

    @ApiOperation("获取文件列表")
    @ApiImplicitParam(name = "path", value = "文件夹路径", paramType = "query", required = true)
    @GetMapping("/list")
    public ResultBody list(
            @RequestParam(value = "path") String path
    ) {
        ArrayList<FileBean> fileBeans = FileUtils.listDir(path);
        ResultBody resultBody;
        if (fileBeans != null) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("list", fileBeans);
            resultBody = Result.data(resultMap);
        } else {
            resultBody = Result.fail(404, "文件夹路径有误");
        }
        return resultBody;
    }

    @ApiOperation("创建新文件")
    @PostMapping("/create")
    public ResultBody create() {
        return Result.ok();
    }

    @ApiOperation("上传新文件")
    @PostMapping("/upload")
    public ResultBody upload() {
        return Result.ok();
    }

    @ApiOperation("读取文件")
    @GetMapping("/read")
    public ResultBody read() {
        return Result.ok();
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public ResultBody delete(@RequestParam("path") String path) {
        File file = new File(path);
        boolean deleteFile = FileUtils.deleteFile(file);
        if (deleteFile) {
            return Result.data("删除文件成功");
        } else {
            return Result.fail(404, "目标文件不存在");
        }

    }

    @ApiOperation("移动文件")
    @PostMapping("/move")
    public ResultBody move(@RequestParam("path") String path) {
        return Result.ok();
    }
}
