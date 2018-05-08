package com.nari.rt21dms.scan.daoutils;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.nari.rt21dms.scan.R;
import com.nari.rt21dms.scan.utils.AppInfoUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yinl01 on 2017/6/22.
 */

public class GreenDaoContextWrapper extends ContextWrapper {
    private Context mContext;
    private static final String DB_NAME = "rt21dmsscan.db";//数据库名称

    public GreenDaoContextWrapper(Context base) {
        super(base);
        this.mContext = base;
    }

    @Override
    public File getDatabasePath(String name) {
        Log.d("GreenDao", "getDatabasePath");
        Log.d("GreenDao", mContext.getDatabasePath(name).getAbsolutePath());
        String filePath = mContext.getDatabasePath(name).getAbsolutePath();
        File file = new File(filePath);
        if (!file.exists()) {
            buildDatabase(filePath);
        }
        return file;
    }

    private void buildDatabase(String filePath) {
        String packageName = AppInfoUtil.getPackageName(mContext);
        String DB_PATH = "/data/data/" + packageName + "/databases/";
        if (!(new File(DB_PATH + DB_NAME).exists())) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                InputStream is = mContext.getResources().openRawResource(R.raw.rt21dmsscan);
                // 输出流,在指定路径下生成db文件
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

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

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        Log.d("GreenDao", "openOrCreateDatabase");
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        Log.d("GreenDao", "openOrCreateDatabase");
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
        return result;
    }
}
