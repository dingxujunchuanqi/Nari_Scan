package com.nari.rt21dms.scan.dbmanager;

import android.content.Context;

import com.nari.rt21dms.scan.daoutils.BaseDao;
import com.nari.rt21dms.scan.entity.MaterialsEntity;
import com.nari.rt21dms.scan.gen.MaterialsEntityDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by dingxujun on 2018/4/8.
 *
 * @project Nari_Scan
 */

public class MaterialsManager extends BaseDao<MaterialsEntity> {
    public MaterialsManager(Context context) {
        super(context);
    }

    /**
     * 根据扫描码查询数据
     *
     * @param scanCode 扫描编码
     * @return
     */
    public List<MaterialsEntity> queryAllList(String scanCode) {
        QueryBuilder<MaterialsEntity> queryBuilder = daoSession.getMaterialsEntityDao().queryBuilder();
        queryBuilder.where(MaterialsEntityDao.Properties.SCANCODE.eq(scanCode)).build();
        return queryBuilder.list();
    }

    /**
     * 插入数据
     */
    public void insetAllData(List<MaterialsEntity> list) {
        daoSession.getMaterialsEntityDao().insertOrReplaceInTx(list);
    }
/**
 * 删除所有数据
*@date 创建时间 2018/5/8
*@author dingxujun
*
*/
    public void clerAllData() {
        deleteAll(MaterialsEntity.class);
    }
}
