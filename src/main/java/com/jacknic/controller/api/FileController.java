package com.jacknic.controller.api;

import com.jacknic.model.bean.FileBean;
import com.jacknic.util.FileUtils;
import com.jacknic.util.JSON;
import com.jacknic.util.Result;
import com.jacknic.util.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipOutputStream;

/**
 * @author Jacknic
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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

    @ApiOperation("上传新文件")
    @PostMapping("/upload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirPath", value = "文件夹路径", paramType = "query", required = true),
    })
    public ResultBody upload(
            @RequestParam("file") MultipartFile uploadFile,
            @RequestParam("dirPath") String dirPath
    ) {
        if (uploadFile.isEmpty()) {
            return Result.fail(504, "上传文件不能为空");
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return Result.fail(404, "上传文件所在文件夹有误");
            }
        }
        String fileName = uploadFile.getOriginalFilename();
        File destFile = new File(dir, fileName == null ? "newFile" : fileName);
        if (destFile.exists()) {
            destFile = new File(dir, "new_" + fileName);
        }
        try {
            uploadFile.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(504, "上传文件失败");
        }

        return Result.data("上传新文件成功");
    }

    @ApiOperation("获取文件列表")
    @ApiImplicitParam(name = "path", value = "文件夹路径", paramType = "query", required = true)
    @GetMapping("/list")
    public ResultBody list(
            @RequestParam("path") String path
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


    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)
    @GetMapping("/download")
    public void download(
            @RequestParam("path") String path,
            HttpServletResponse response
    ) throws Exception {
        File downloadFile = new File(path);
        ServletOutputStream outputStream = response.getOutputStream();
        if (downloadFile.exists()) {
            response.setContentType("application/octet-stream;charset=UTF-8");
            if (downloadFile.isFile()) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + "\"");
                response.addHeader("Content-Length", "" + downloadFile.length());
                FileInputStream fileInputStream = new FileInputStream(downloadFile);
                Streams.copy(fileInputStream, outputStream, true);
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFile.getName() + ".zip\"");
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                FileUtils.compress(zipOutputStream, downloadFile, downloadFile.getPath());
                zipOutputStream.close();
                outputStream.close();
            }
        } else {
            ResultBody resultBody = Result.fail(503, "文件不存在");
            JSON.write(outputStream, resultBody);
        }

    }

    @ApiOperation("删除文件")
    @ApiImplicitParam(name = "path", value = "文件夹路径", paramType = "query", required = true)
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "from", value = "所移动文件路径", paramType = "query", required = true),
            @ApiImplicitParam(name = "to", value = "目标文件路径", paramType = "query", required = true)
    })
    @GetMapping("/move")
    public ResultBody move(@RequestParam("from") String from, @RequestParam("to") String to) {
        File fromFile = new File(from);
        File toFile = new File(to);
        if (fromFile.exists()) {
            if (toFile.exists()) {
                return Result.fail(502, "目标地址已存在该文件");
            } else {
                if (!toFile.getParentFile().exists()) {
                    return Result.fail(502, "目标文件夹不存在");
                }
                boolean success = fromFile.renameTo(toFile);
                if (!success) {
                    return Result.fail(502, "移动文件失败");
                }
            }
        } else {
            return Result.fail(502, "移动文件不存在");
        }
        return Result.data("移动文件成功");
    }
}
