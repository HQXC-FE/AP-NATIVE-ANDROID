package com.xtree.live.uitl;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.RomUtils;

public class PipModeUtils {

    /**
     * 华为 Android12 鸿蒙4.0
     * 自由浮窗模式
     * 多窗口模式
     * @param context
     * @return
     */
    public static boolean isSupportPipMode(Activity context){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) &&
                !WindowModeUtils.isFreeFormMode(context.getResources().getConfiguration())&&
                !context.isInMultiWindowMode()&&
                !(Build.VERSION.SDK_INT == Build.VERSION_CODES.S && RomUtils.isHuawei());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isPipPermissionGranted(Context context){
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return AppOpsManager.MODE_ALLOWED == appOpsManager.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                    Process.myUid(),context.getPackageName());
        }else {
            return AppOpsManager.MODE_ALLOWED == appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE,  Process.myUid(), context.getPackageName());
        }
    }

    public static void gotoPipSetting(Context context){
        Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
