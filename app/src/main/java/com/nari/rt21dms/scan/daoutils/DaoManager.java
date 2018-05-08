package com.nari.rt21dms.scan.daoutils;

import android.content.Context;
import com.nari.rt21dms.scan.gen.DaoMaster;
import com.nari.rt21dms.scan.gen.DaoSession;
import com.nari.rt21dms.scan.gen.MySQLiteOpenHelper;

import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by yinl01 on 2017/6/15.
 */

public class DaoManager {

    private Context context;
    private static final String DB_NAME = "rt21dmsscan.db";//数据库名称
    private volatile static DaoManager daoManager;//多线程访问
    private static MySQLiteOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    public static DaoManager getInstance() {
        DaoManager instance = null;
        if (daoManager == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                    daoManager = instance;
                }
            }
        }
        return daoManager;
    }

    public void init(Context context) {
        this.context = context;
        getDaoMaster();
        getDaoSession();
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (mDaoMaster == null) {
            mHelper = new MySQLiteOpenHelper(new GreenDaoContextWrapper(context), DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的增删查找
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            if (null == mDaoMaster) {
                mDaoMaster = getDaoMaster();
            }
            mDaoSession = mDaoMaster.newSession(IdentityScopeType.None);
        }
        return mDaoSession;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }

    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }
}
