package com.nari.rt21dms.scan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.nari.rt21dms.qrcodelib.common.SizeUtils;
import com.nari.rt21dms.qrcodelib.zxing.camera.CameraManager;
import com.nari.rt21dms.qrcodelib.zxing.view.ViewfinderView;
import com.nari.rt21dms.scan.base.BaseApplication;
import com.nari.rt21dms.scan.daoutils.DaoUtils;
import com.nari.rt21dms.scan.entity.MaterialsEntity;
import com.nari.rt21dms.scan.utils.AsyncTaskUtils;
import com.nari.rt21dms.scan.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.yhq.utils.ToastUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_QR_CODE = 1;
    List<MaterialsEntity> materList = new ArrayList<>();
    private CircularProgressButton progressButton;
    private boolean completeFlag;//是否导入数据成功的标记

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOnClickListener();
    }


    private void initOnClickListener() {
        findViewById(R.id.tiaoma_btn).setOnClickListener(this);
        findViewById(R.id.erweima_btn).setOnClickListener(this);
        progressButton.setOnClickListener(this);
    }

    private void initView() {
        DaoUtils.getMaterialsManager().clerAllData();
        progressButton = (CircularProgressButton) findViewById(R.id.import_dataProBtn);
        progressButton.setIndeterminateProgressMode(true);
    }

    /**
     * 打开扫描界面
     *
     * @date 创建时间 2018/4/5
     * @author dingxujun
     */
    private void openScanUI() {
        Intent i = new Intent(MainActivity.this, SimpleCaptureActivity.class);
        MainActivity.this.startActivityForResult(i, REQUEST_QR_CODE);
    }


    /**
     * 改变扫描框的大小，并进行dp转换
     *
     * @date 创建时间 2018/4/5
     * @author dingxujun
     */
    private void changeFrameSize(int minFrameWidth, int minFrameHeight, int maxFrameWidth,
                                 int maxFrameHeight, int offset, String showText) {
        /***使点击按钮时，改变框大小生效***/
        CameraManager.init(this);
        CameraManager.get().framingRect = null;
        /***使点击按钮时，改变框大小生效***/
        CameraManager.MIN_FRAME_WIDTH = minFrameWidth;//改进扫描框的最小宽度
        CameraManager.MIN_FRAME_HEIGHT = minFrameHeight;//改进扫描框的最小高度
        CameraManager.MAX_FRAME_WIDTH = maxFrameWidth;//改进扫描框的最大宽度
        CameraManager.MAX_FRAME_HEIGHT = maxFrameHeight;//改进扫描框的最大高度

        ViewfinderView.translationOffset = offset;//改进控制扫描线的平移距离
        ViewfinderView.showText = showText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            System.out.println("-----result------" + result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tiaoma_btn:
                changeFrameSize(SizeUtils.dip2px(this, 320)
                        , SizeUtils.dip2px(this, 140),
                        SizeUtils.dip2px(this, 380),
                        SizeUtils.dip2px(this, 140),
                        SizeUtils.dip2px(this, 140), getResources().getString(R.string.Please_aim_the_barcode));
                openScanUI();
                break;
            case R.id.erweima_btn:
                changeFrameSize(SizeUtils.dip2px(this, 180)
                        , SizeUtils.dip2px(this, 180),
                        SizeUtils.dip2px(this, 240),
                        SizeUtils.dip2px(this, 240),
                        SizeUtils.dip2px(this, 240), getResources().getString(R.string.Please_aim_the_qrcode));
                openScanUI();
                break;
            case R.id.import_dataProBtn:
                if (progressButton.getProgress() == -1) {
                    progressButton.setProgress(0);
                } else if (progressButton.getProgress() == 100) {
                    progressButton.setProgress(0);
                } else {
                    importData();
                }
                break;
            default:
                break;

        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showToast(this, getResources().getString(R.string.exit_application));
                exitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);//保存主Activity的状态，当点击返回键键 避免销毁
                return super.onKeyDown(keyCode, event);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 导入数据
     *
     * @date 创建时间 2018/5/7
     * @author dingxujun
     */
    private void importData() {
        AsyncTaskUtils.doAsync(new AsyncTaskUtils.IDataCallBack<Object>() {
            @Override
            public void onTaskBefore() {
                progressButton.setProgress(20);
            }

            @Override
            public Object onTasking(Void... params) {
                readExcelToDB(BaseApplication.getInstance());
                return null;
            }

            @Override
            public void onTaskAfter(Object result) {
                if (completeFlag) {
                    progressButton.setProgress(100);
                } else {
                    progressButton.setProgress(-1);
                    ToastUtils.showToast(BaseApplication.getInstance(), "导入数据发生异常,请检查");
                }

            }
        });
    }

    private void readExcelToDB(Context context) {
        try {
            String filePath = FileUtils.getAbsolutePath(context);
            if (filePath != null) {
                File file = new File(filePath + "/materialsdata.xls");
                if (file.exists()) {
                    InputStream is = new FileInputStream(file);
                    Workbook book = Workbook.getWorkbook(is);
                    book.getNumberOfSheets();
                    // 获得第一个工作表对象
                    Sheet sheet = book.getSheet(0);
                    int Rows = sheet.getRows();
                    for (int i = 1; i < Rows; ++i) {
                        String materialname = (sheet.getCell(0, i)).getContents().trim();//物资名称
                        String materialcode = (sheet.getCell(1, i)).getContents().trim();//物资编码
                        String materialtype = (sheet.getCell(2, i)).getContents().trim();//物资型号
                        String scancode = (sheet.getCell(3, i)).getContents().trim();//扫描的编码
                        MaterialsEntity entity = new MaterialsEntity(Long.valueOf(i), materialname, materialcode, materialtype, scancode);
                        materList.add(entity);
                    }
                    book.close();
                    DaoUtils.getMaterialsManager().insetAllData(materList);//插入数据
                    materList.clear();
                    completeFlag = true;
                    Log.d("zsj_excel", "excel读取完成");
                } else {
                    runOnUiThread(() -> {
                        ToastUtils.showToast(BaseApplication.getInstance(), "数据文件不存在,请检查");
                        completeFlag = false;
                    });
                    return;
                }
            }
        } catch (IOException e) {
            completeFlag = false;
            e.printStackTrace();
        } catch (BiffException e) {
            completeFlag = false;
            e.printStackTrace();
        }
    }

}