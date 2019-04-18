package com.jacknic.model.bean

import org.springframework.util.StringUtils

import java.io.File

/**
 * 文件描述信息
 *
 * @author Jacknic
 */
class FileBean(private val mFile: File) {


    /**
     * 文件名
     */
    val name: String
        get() {
            val absolutePath = absolutePath
            var dirName = absolutePath.substring(absolutePath.lastIndexOf('/') + 1)
            if (StringUtils.isEmpty(dirName)) {
                dirName = absolutePath
            }
            return if (mFile.isFile) mFile.name else dirName
        }

    /**
     * 是否为目录
     */
    val isDir: Boolean
        get() = mFile.isDirectory

    /**
     * 文件绝对路径
     */
    val absolutePath: String
        get() = mFile.absolutePath.replace('\\', '/')

    /**
     * 文件大小
     */
    val length: Long
        get() = mFile.length()

    /**
     * 文件上次更新时间
     */
    val lastModified: Long
        get() = mFile.lastModified()

}
