package com.prome.simplenote;

import android.app.Application;

import org.litepal.LitePal;

import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by kingme on 2017/11/26.
 */

public class PromeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .setSkinStatusBarColorEnable(false)
                .setSkinWindowBackgroundEnable(false)
                .loadSkin();
    }
}
