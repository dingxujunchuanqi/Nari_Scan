package com.nari.rt21dms.scan.utils;
import android.content.Context;
import android.os.Environment;
import com.nari.rt21dms.scan.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yinl01 on 2017/6/22.
 */

public class DataBaseUtils {

    /**
     * 读取assets 数据库文件保存dao
     *
     * @param context
     * @param dbName
     */
    public static void copyDBtoDataBase(Context context, String dbName) {
        String packageName = AppInfoUtil.getPackageName(context);
        String DB_PATH = "/data/data/" + packageName + "/databases/";
        if (!(new File(DB_PATH + dbName).exists())) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }
            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                InputStream is = context.getResources().openRawResource(R.raw.rt21dmsscan);
                // 输出流,在指定路径下生成db文件
                OutputStream os = new FileOutputStream(DB_PATH + dbName);

                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                // 关闭文件流
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝文件到sd卡
     *
     * @param context
     * @param strOutFileName
     * @throws IOException
     */
    public static void copyBigDataToSD(Context context, String strInFileName, String strOutFileName) throws
            IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open(strInFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }

    /**
     * 拷贝数据库文件到sd卡
     *
     * @param context
     * @param dbName
     */
    public static void copyDBtoSD(Context context, String dbName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //获取数据库路径
            String pathDatabase = context.getDatabasePath(dbName).getPath();
            String packageName = AppInfoUtil.getPackageName(context);
            //目标路径
            String SDPath = Environment.getExternalStorageDirectory().getPath() + File.separator + packageName + File.separator;
            File mfile = new File(SDPath);
            if (!mfile.exists()) {
                mfile.mkdirs();       //如果不存在则创建新的file
            }
            try {
                File file_sd = new File(mfile, "/old_data.db");
                File file_database = new File(pathDatabase);
                copyFileUsingFileStreams(file_database, file_sd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝sd临时目录数据库文件到数据库运行目录
     *
     * @param context
     * @param dbName
     */
    public static void copySDtoDB(Context context, String dbName) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //获取数据库路径
            String pathDatabase = context.getDatabasePath(dbName).getPath();
            String packageName = AppInfoUtil.getPackageName(context);
            //目标路径
            String SDPath = Environment.getExternalStorageDirectory().getPath() + File.separator + packageName + File.separator;
            File mfile = new File(SDPath);
            if (!mfile.exists()) {
                mfile.mkdirs();       //如果不存在则创建新的file
            }
            try {
                File file_sd = new File(mfile, "/old_data.db");
                File file_database = new File(pathDatabase);
                if (!file_database.exists()) {
                    mfile.mkdirs();       //如果不存在则创建新的file
                }
                copyFileUsingFileStreams(file_sd, file_database);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            if (source.exists()) {
                source.delete();
            }
            if (input == null || output == null) {
                return;
            }
            input.close();
            output.close();
        }
    }
}
