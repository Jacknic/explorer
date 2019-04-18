package com.jacknic.util

import com.jacknic.model.bean.FileBean
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * 文件操作工具类
 *
 * @author Jacknic
 */
object FileUtils {

    /**
     * 文件描述信息列表
     */
    fun listDir(dirPath: String): ArrayList<FileBean>? {
        val dir = File(dirPath)
        var fileBeans: ArrayList<FileBean>? = null
        if (dir.isDirectory) {
            val files = dir.listFiles()
            fileBeans = toFileBeans(files!!)
        }
        return fileBeans
    }

    /**
     * 文件描述列表
     */
    fun toFileBeans(files: Array<File>): ArrayList<FileBean> {
        val fileBeans = ArrayList<FileBean>()
        for (file in files) {
            if (file.exists()) {
                fileBeans.add(FileBean(file))
            }
        }
        return fileBeans
    }

    /**
     * 删除文件
     */
    fun deleteFile(file: File): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    deleteFile(f)
                }
            }
            return file.delete()
        } else {
            return file.delete()
        }
    }

    /**
     * 压缩文件
     */
    @Throws(Exception::class)
    fun compress(zos: ZipOutputStream, file: File, baseDir: String) {
        if (file.isDirectory) {
            compressDir(zos, file, baseDir)
        } else if (file.isFile) {
            compressFile(zos, file, baseDir)
        }
    }

    /**
     * 压缩文件夹
     */
    @Throws(Exception::class)
    private fun compressDir(zos: ZipOutputStream, file: File, baseDir: String) {
        val files = file.listFiles()
        val dir = baseDir + "/" + file.name
        if (files == null || files.size == 0) {
            zos.putNextEntry(ZipEntry("$dir/"))
            return
        }
        for (fileItem in files) {
            compress(zos, fileItem, dir)
        }
    }

    /**
     * 压缩普通文件
     */
    @Throws(Exception::class)
    private fun compressFile(zos: ZipOutputStream, file: File, baseDir: String) {
        val bis = FileInputStream(file)
        zos.putNextEntry(ZipEntry(baseDir + "/" + file.name))
        bis.copyTo(zos)
        zos.closeEntry()
        bis.close()
    }

}
