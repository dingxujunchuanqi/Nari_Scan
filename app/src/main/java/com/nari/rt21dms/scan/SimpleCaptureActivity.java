package com.nari.rt21dms.scan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nari.rt21dms.qrcodelib.CaptureActivity;
import com.nari.rt21dms.scan.daoutils.DaoUtils;
import com.nari.rt21dms.scan.entity.MaterialsEntity;

import java.util.List;


/**
 * Created by xdj on 16/9/17.
 */

public class SimpleCaptureActivity extends CaptureActivity {
    protected Activity mActivity = this;

    private AlertDialog mDialog;
    private View customView;
    private List<MaterialsEntity> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleResult(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(mActivity, R.string.scan_failed, Toast.LENGTH_SHORT).show();
            restartPreview();
        } else {
            showDialog(resultString);
        }
    }

    /**
     * 显示扫描信息的对话框
     *
     * @date 创建时间 2018/4/7
     * @author dingxujun
     */
    private void showDialog(String resultString) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(mActivity)
                    // .setMessage(resultString)
                    .setTitle(getResources().getString(R.string.scan_info))
                    .setPositiveButton(getResources().getString(R.string.ok), null)
                    .create();
            mDialog.setOnDismissListener(dialog -> {
                mDialog = null;//解决数据不刷新
                restartPreview();
            });
        }
        if (!mDialog.isShowing()) {
            customView = View.inflate(SimpleCaptureActivity.this, R.layout.materials_dialog_view, null);
            mDialog.setView(customView);
            //  mDialog.setMessage(resultString);
            showData(resultString, customView);
            mDialog.show();
        }
    }

    /**
     * 显示数据
     *
     * @date 创建时间 2018/4/8
     * @author dingxujun
     */
    private void showData(String scanCode, View view) {
        list = DaoUtils.getMaterialsManager().queryAllList(scanCode);
        if (list != null && list.size() > 0) {
            TextView materialname_tv = view.findViewById(R.id.materialname_tv);//物资名称
            TextView materialcode_tv = view.findViewById(R.id.materialcode_tv);//物资编号
            TextView materialtype_tv = view.findViewById(R.id.materialtype_tv);//物资型号
            materialname_tv.setText(list.get(0).getMATERIALNAME());
            materialcode_tv.setText(list.get(0).getMATERIALCODE());
            materialtype_tv.setText(list.get(0).getMATERIALTYPE());
        } else {
            customView = View.inflate(SimpleCaptureActivity.this, R.layout.view_nodata, null);
            mDialog.setView(customView);
        }

    }
}
