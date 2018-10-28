package com.jacknic.util;

import com.jacknic.model.bean.FileBean;

import java.io.File;
import java.util.ArrayList;

/**
 * 文件操作工具类
 */
public class FileUtils {

    /**
     * 文件描述信息列表
     */
    public static ArrayList<FileBean> listDir(String dirPath) {
        File dir = new File(dirPath);
        ArrayList<FileBean> fileBeans = null;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            fileBeans = toFileBeans(files);
        }
        return fileBeans;
    }

    /**
     * 文件描述列表
     */
    public static ArrayList<FileBean> toFileBeans(File[] files) {
        ArrayList<FileBean> fileBeans = new ArrayList<>();
        for (File file : files) {
            if (file != null && file.exists()) {
                fileBeans.add(new FileBean(file));
            }
        }
        return fileBeans;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFile(f);
                }
            }
            return file.delete();
        } else {
            return file.delete();
        }
    }
}
