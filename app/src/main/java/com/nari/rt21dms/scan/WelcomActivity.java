package com.nari.rt21dms.scan;

import android.Manifest;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nari.rt21dms.scan.utils.DataBaseUtils;
import com.nari.rt21dms.scan.utils.PermissionUtils;

public class WelcomActivity extends AppCompatActivity {
    private ConstraintLayout welcom;
    Animation anim1 = null;
    Animation anim2 = null;
    private String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission
            .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest
            .permission.CALL_PHONE, Manifest.permission.RECORD_AUDIO};
    private int REQUEST_CODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏，全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcom);
    }

    private void initData() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        welcom = findViewById(R.id.welcom);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.anim_in);
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_out);
        welcom.startAnimation(anim1);
        anim1.setFillEnabled(true);  //启动保持
        anim1.setFillAfter(true);//最后一帧保持，否则会跳动到原始
        anim1.setAnimationListener(new AnimationImpl() {
            @Override
            public void onAnimationEnd(Animation animation) {
                welcom.startAnimation(anim2);
                anim2.setFillAfter(true);
            }
        });

        anim2.setAnimationListener(new AnimationImpl() {
            @Override
            public void onAnimationEnd(Animation animation) {
                skip();
                new  Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DataBaseUtils.copyDBtoDataBase(WelcomActivity.this, "rt21dmsscan.db");
                    }
                }.start();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.checkAllPermissions(this, permissions)) {
            initData();
        } else {
            PermissionUtils.checkPermissions(this, REQUEST_CODE_PERMISSIONS, permissions);
        }
    }

    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    abstract class AnimationImpl implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        abstract public void onAnimationEnd(Animation animation);

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
