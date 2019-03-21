package com.jacknic.util;

import com.jacknic.model.bean.FileBean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 *
 * @author Jacknic
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

    /**
     * 压缩文件
     */
    public static void compress(ZipOutputStream zos, File file, String baseDir) throws Exception {
        if (file.isDirectory()) {
            compressDir(zos, file, baseDir);
        } else if (file.isFile()) {
            compressFile(zos, file, baseDir);
        }
    }

    /**
     * 压缩文件夹
     */
    private static void compressDir(ZipOutputStream zos, File file, String baseDir) throws Exception {
        File[] files = file.listFiles();
        String dir = baseDir + "/" + file.getName();
        if (files == null || files.length == 0) {
            zos.putNextEntry(new ZipEntry(dir + "/"));
            return;
        }
        for (File fileItem : files) {
            compress(zos, fileItem, dir);
        }
    }

    /**
     * 压缩普通文件
     */
    private static void compressFile(ZipOutputStream zos, File file, String baseDir) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        zos.putNextEntry(new ZipEntry(baseDir + "/" + file.getName()));
        int len;
        byte[] buffer = new byte[8192];
        while ((len = bis.read(buffer)) != -1) {
            zos.write(buffer, 0, len);
        }
        zos.closeEntry();
        bis.close();
    }

}
