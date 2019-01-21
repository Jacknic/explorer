package com.jacknic.model.bean;

import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 文件描述信息
 *
 * @author Jacknic
 */
public class FileBean {

    private File mFile;

    public FileBean(File file) {
        this.mFile = file;
    }


    /**
     * 文件名
     */
    public String getName() {
        String absolutePath = getAbsolutePath();
        String dirName = absolutePath.substring(absolutePath.lastIndexOf('/') + 1);
        if (StringUtils.isEmpty(dirName)) {
            dirName = absolutePath;
        }
        return mFile.isFile() ? mFile.getName() : dirName;
    }

    /**
     * 是否为目录
     */
    public boolean isDir() {
        return mFile.isDirectory();
    }

    /**
     * 文件绝对路径
     */
    public String getAbsolutePath() {
        return mFile.getAbsolutePath().replace('\\', '/');
    }

    /**
     * 文件大小
     */
    public long getLength() {
        return mFile.length();
    }

    /**
     * 文件上次更新时间
     */
    public long getLastModified() {
        return mFile.lastModified();
    }

}
