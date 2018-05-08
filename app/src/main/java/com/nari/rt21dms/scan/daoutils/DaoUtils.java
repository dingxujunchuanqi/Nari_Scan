package com.nari.rt21dms.scan.daoutils;

import android.content.Context;

import com.nari.rt21dms.scan.dbmanager.MaterialsManager;

/**
 * Created by dingxujun on 2018/4/8.
 *
 * @project Nari_Scan
 */

public class DaoUtils {
    private static Context context;
    private static MaterialsManager materialsManager;

    public static void init(Context context) {
        DaoUtils.context = context.getApplicationContext();
    }
/**
 * 单例模式获取dbManager对象
*@date 创建时间 2018/4/8
*@author dingxujun
*
*/
    public static MaterialsManager getMaterialsManager() {
        if (materialsManager == null) {
            materialsManager = new MaterialsManager(context);
        }
        return materialsManager;
    }


}
