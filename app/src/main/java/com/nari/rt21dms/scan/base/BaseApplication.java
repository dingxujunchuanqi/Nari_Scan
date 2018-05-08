package com.nari.rt21dms.scan.base;

import android.app.Application;
import com.nari.rt21dms.scan.daoutils.DaoManager;
import com.nari.rt21dms.scan.daoutils.DaoUtils;

/**
 * Created by Administrator on 2017/5/22.
 */

public class BaseApplication extends Application {
    private static BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initDataBase();
        instance = this;
    }

    private void initDataBase() {
        DaoManager.getInstance().init(this);
        DaoUtils.init(this);
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
