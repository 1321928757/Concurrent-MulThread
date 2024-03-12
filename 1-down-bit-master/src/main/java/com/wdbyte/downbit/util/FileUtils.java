package com.wdbyte.downbit.util;

import java.io.File;

/**
 * 文件操作工具类
 * @author 牵着猫散步的鼠鼠-LiuShiJie
 */
public class FileUtils {

    /**
     * 获取文件内容长度
     *
     * @param name
     * @return
     */
    public static long getFileContentLength(String name) {
        File file = new File(name);
        return file.exists() && file.isFile() ? file.length() : 0;
    }

}
