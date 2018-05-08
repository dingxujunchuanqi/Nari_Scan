package com.nari.rt21dms.scan.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by dingxujun on 2018/4/16.
 *
 * @project Nari_Scan
 */

public class FileUtils {

    /**
     * 获取sd卡文件目录
     *
     * @param context
     * @param folderPath
     * @return
     */
    public static String getSDPath(Context context, String folderPath) {
        String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在
        // 检查SD卡是否可用
        if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡未准备好！", Toast.LENGTH_SHORT).show();
        }
        String SDPath = Environment.getExternalStorageDirectory().getPath() +
                File.separator + folderPath + File.separator;
        File filee = new File(SDPath);
        if (!filee.exists()) {
            filee.mkdirs();
        }
        return SDPath;
    }


    /**
     * 获取sd卡文件目录
     *
     * @param context
     * @return
     */
    public static String getAbsolutePath(Context context) {
        String sdState = Environment.getExternalStorageState(); // 判断sd卡是否存在
        // 检查SD卡是否可用
        if (!sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "SD卡未准备好！", Toast.LENGTH_SHORT).show();

        } else {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }
}
