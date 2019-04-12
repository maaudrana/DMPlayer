package com.loitp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.loitp.R;

import androidx.annotation.Nullable;
import vn.loitp.core.base.BaseFontActivity;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.utils.util.AppUtils;

public class SplashActivity extends BaseFontActivity {
    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("Version " + AppUtils.getAppVersionName());
        LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                Intent intent = new Intent(activity, DMPlayerBaseActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
                finish();
            }
        });
    }
}
