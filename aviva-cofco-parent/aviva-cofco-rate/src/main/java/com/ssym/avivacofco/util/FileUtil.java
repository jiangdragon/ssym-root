package com.ssym.avivacofco.util;


import java.io.File;

/**
 * @author WB20200724005
 * @Description
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/17
 */
public class FileUtil {
    public static File createFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }
}
