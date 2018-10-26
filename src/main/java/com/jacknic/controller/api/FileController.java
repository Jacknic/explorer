package com.jacknic.controller.api;

import com.jacknic.util.JSON;
import com.jacknic.util.Response;
import com.jacknic.util.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Api(tags = "file", description = "文件操作")
@RestController
@RequestMapping("/api/file")
public class FileController {

    @ApiOperation("获取文件列表")
    @GetMapping("/list")
    public String list(
            @RequestParam(value = "path", defaultValue = "/") String path,
            HttpServletRequest request,
            HttpServletResponse response

    ) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        StringBuffer url = request.getRequestURL();
        System.out.println("当前的URL：" + url);
        File dir = new File(path);
        ResultBody resultBody;
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            resultBody = Response.data(files);
        } else {
            resultBody = Response.fail(404, "文件夹路径有误");
        }
        JSON.write(outputStream, resultBody);
        return "";
    }

    @ApiOperation("创建新文件")
    @PostMapping("/create")
    public String create() {
        return null;
    }

    @ApiOperation("上传新文件")
    @PostMapping("/upload")
    public String upload() {
        return null;
    }

    @ApiOperation("读取文件")
    @GetMapping("/read")
    public String read() {
        return null;
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public String delete() {
        return null;
    }

    @ApiOperation("移动文件")
    @PostMapping("/move")
    public String move() {
        return null;
    }
}
