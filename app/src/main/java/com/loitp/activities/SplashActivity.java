package com.loitp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loitp.R;

import java.util.List;

import androidx.annotation.Nullable;
import vn.loitp.core.base.BaseFontActivity;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.utils.util.AppUtils;

public class SplashActivity extends BaseFontActivity {
    private InterstitialAd interstitialAd;
    private boolean isAnimDone;
    private boolean isCheckReadyDone;
    private boolean isShowDialogCheck;

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
        LScreenUtil.hideNavigationBar(activity);
        isShowAdWhenExit = false;
        interstitialAd = LUIUtil.createAdFull(activity);
        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("Version " + AppUtils.getAppVersionName());
        LUIUtil.setDelay(2000, new LUIUtil.DelayCallback() {
            @Override
            public void doAfter(int mls) {
                isAnimDone = true;
                goToHome();
            }
        });
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isShowDialogCheck) {
            checkPermission();
        }
    }

    private void checkPermission() {
        isShowDialogCheck = true;
        boolean isCanWriteSystem = LScreenUtil.checkSystemWritePermission(activity);
        isCanWriteSystem = true;//hardcode true
        if (isCanWriteSystem) {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.BROADCAST_STICKY)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                LLog.d(TAG, "onPermissionsChecked do you work now");
                                isCheckReadyDone = true;
                                goToHome();
                            } else {
                                LLog.d(TAG, "!areAllPermissionsGranted");
                                List<PermissionDeniedResponse> permissionDeniedResponseList = report.getDeniedPermissionResponses();
                                for (PermissionDeniedResponse permissionDeniedResponse : permissionDeniedResponseList) {
                                    LLog.d(TAG, "permissionDeniedResponse " + permissionDeniedResponse.getPermissionName());
                                }
                                showShouldAcceptPermission();
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                LLog.d(TAG, "onPermissionsChecked permission is denied permenantly, navigate user to app settings");
                                showSettingsDialog();
                            }
                            isShowDialogCheck = true;
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            LLog.d(TAG, "onPermissionRationaleShouldBeShown");
                            token.continuePermissionRequest();
                        }
                    })
                    .onSameThread()
                    .check();
        } else {
            AlertDialog alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to allow modifying system settings", "Okay", "Exit", new LDialogUtil.Callback2() {
                @Override
                public void onClick1() {
                    isShowDialogCheck = false;
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    LActivityUtil.tranIn(activity);
                }

                @Override
                public void onClick2() {
                    onBackPressed();
                }
            });
            alertDialog.setCancelable(false);
        }
    }

    private void goToHome() {
        LLog.d(TAG, "goToHome isAnimDone " + isAnimDone + ", isCheckReadyDone " + isCheckReadyDone);
        if (isAnimDone && isCheckReadyDone) {
            Intent intent = new Intent(activity, DMPlayerBaseActivity.class);
            startActivity(intent);
            LUIUtil.displayInterstitial(interstitialAd);
            LActivityUtil.tranIn(activity);
            finish();
        }
    }

    private void showSettingsDialog() {
        AlertDialog alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to use this feature. You can grant them in app settings.", "GOTO SETTINGS", "Cancel", new LDialogUtil.Callback2() {
            @Override
            public void onClick1() {
                isShowDialogCheck = false;
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }

            @Override
            public void onClick2() {
                onBackPressed();
            }
        });
        alertDialog.setCancelable(false);
    }

    private void showShouldAcceptPermission() {
        AlertDialog alertDialog = LDialogUtil.showDialog2(activity, "Need Permissions", "This app needs permission to use this feature.", "Okay", "Cancel", new LDialogUtil.Callback2() {
            @Override
            public void onClick1() {
                checkPermission();
            }

            @Override
            public void onClick2() {
                onBackPressed();
            }
        });
        alertDialog.setCancelable(false);
    }
}
