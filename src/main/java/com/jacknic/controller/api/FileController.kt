package com.jacknic.controller.api

import com.jacknic.util.FileUtils
import com.jacknic.util.JSON
import com.jacknic.util.Result
import com.jacknic.util.ResultBody
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.apache.tomcat.util.http.fileupload.util.Streams
import org.springframework.util.FileSystemUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URLEncoder
import java.util.*
import java.util.zip.ZipOutputStream
import javax.servlet.http.HttpServletResponse

/**
 * 文件管理基本操作
 * @author Jacknic
 */
@CrossOrigin(origins = ["*"])
@Api(tags = ["file"], description = "文件操作")
@RestController
@RequestMapping("/api/file")
class FileController {

    @ApiOperation("显示根目录信息")
    @GetMapping("/root")
    fun root(): ResultBody {
        val files = File.listRoots()
        val fileBeans = FileUtils.toFileBeans(files)
        val resultMap = HashMap<String, Any>()
        resultMap["list"] = fileBeans
        val spaceList = HashMap<String, HashMap<String, Any>>()
        for (dir in files) {
            val spaceInfo = HashMap<String, Any>()
            spaceInfo["total"] = dir.totalSpace
            spaceInfo["free"] = dir.freeSpace
            spaceInfo["usable"] = dir.usableSpace
            val name = dir.absolutePath.replace('\\', '/')
            spaceList[name] = spaceInfo
        }
        resultMap["space"] = spaceList
        return Result.data(resultMap)
    }

    @ApiOperation("上传新文件")
    @PostMapping("/upload")
    @ApiImplicitParams(
            ApiImplicitParam(name = "dirPath", value = "文件夹路径", paramType = "query", required = true),
            ApiImplicitParam(name = "file", value = "文件", paramType = "form", required = true, dataType = "file")
    )
    fun upload(
            @RequestParam("file") uploadFile: MultipartFile,
            @RequestParam("dirPath") dirPath: String
    ): ResultBody {
        if (uploadFile.isEmpty) {
            return Result.fail(504, "上传文件不能为空")
        }
        val dir = File(dirPath)
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return Result.fail(404, "上传文件所在文件夹有误")
            }
        }
        val fileName = uploadFile.originalFilename
        var destFile = File(dir, fileName ?: "newFile")
        if (destFile.exists()) {
            destFile = File(dir, "new_" + fileName!!)
        }
        try {
            uploadFile.transferTo(destFile)
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.fail(504, "上传文件失败")
        }

        return Result.data("上传新文件成功")
    }

    @ApiOperation("获取文件列表")
    @ApiImplicitParam(name = "path", value = "文件夹路径", paramType = "query", required = true)
    @GetMapping("/list")
    fun list(
            @RequestParam("path") path: String
    ): ResultBody {
        val fileBeans = FileUtils.listDir(path)
        val resultBody: ResultBody
        if (fileBeans != null) {
            val resultMap = HashMap<String, Any>()
            resultMap["list"] = fileBeans
            resultBody = Result.data(resultMap)
        } else {
            resultBody = Result.fail(404, "文件夹路径有误")
        }
        return resultBody
    }


    @ApiOperation("下载文件")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "query", required = true)
    @GetMapping("/download")
    @Throws(Exception::class)
    fun download(@RequestParam("path") path: String, response: HttpServletResponse) {
        val downloadFile = File(path)
        val outputStream = response.outputStream
        val fileName = URLEncoder.encode(downloadFile.name, "UTF-8")
        println("下载的文件名为：$fileName")
        if (downloadFile.exists()) {
            response.contentType = "application/octet-stream;charset=UTF-8"
            if (downloadFile.isFile) {
                response.setHeader("Content-Disposition", "attachment; filename=\"$fileName\"")
                response.addHeader("Content-Length", "" + downloadFile.length())
                val fileInputStream = FileInputStream(downloadFile)
                Streams.copy(fileInputStream, outputStream, true)
            } else {
                response.setHeader("Content-Disposition", "attachment; filename=\"$fileName.zip\"")
                val zipOutputStream = ZipOutputStream(outputStream)
                FileUtils.compress(zipOutputStream, downloadFile, "")
                zipOutputStream.close()
                outputStream.close()
            }
        } else {
            val resultBody = Result.fail(503, "文件不存在")
            JSON.write(outputStream, resultBody)
        }

    }

    @ApiOperation("删除文件")
    @ApiImplicitParam(name = "path", value = "文件夹路径", paramType = "query", required = true)
    @DeleteMapping("/delete")
    fun delete(@RequestParam("path") path: String): ResultBody {
        val file = File(path)
        val success = FileSystemUtils.deleteRecursively(file)
        return if (success) {
            Result.data("删除文件成功")
        } else {
            Result.fail(404, "目标文件不存在")
        }

    }

    @ApiOperation("移动文件")
    @ApiImplicitParams(ApiImplicitParam(name = "from", value = "所移动文件路径", paramType = "query", required = true),
            ApiImplicitParam(name = "to", value = "目标文件路径", paramType = "query", required = true))
    @GetMapping("/move")
    fun move(@RequestParam("from") from: String, @RequestParam("to") to: String): ResultBody {
        val fromFile = File(from)
        val toFile = File(to)
        if (fromFile.exists()) {
            if (toFile.exists()) {
                return Result.fail(502, "目标地址已存在该文件")
            } else {
                if (!toFile.parentFile.exists()) {
                    return Result.fail(502, "目标文件夹不存在")
                }
                val success = fromFile.renameTo(toFile)
                if (!success) {
                    return Result.fail(502, "移动文件失败")
                }
            }
        } else {
            return Result.fail(502, "移动文件不存在")
        }
        return Result.data("移动文件成功")
    }
}
