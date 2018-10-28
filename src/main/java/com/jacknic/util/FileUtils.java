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
    public static ArrayList<FileBean> toFileBeans(File... files) {
        ArrayList<FileBean> fileBeans = new ArrayList<>();
        for (File file : files) {
            if (file != null && file.exists()) {
                fileBeans.add(new FileBean(file));
            }
        }
        return fileBeans;
    }
}
