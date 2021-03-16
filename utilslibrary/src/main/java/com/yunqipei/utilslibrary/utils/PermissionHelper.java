package com.yunqipei.utilslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * edie create on 2018/12/24
 */
public class PermissionHelper {
    private static PermissionHelper sInstance;

    private PermissionHelper() {
    }

    public static PermissionHelper getInstance() {
        if (sInstance == null) {
            synchronized (PermissionHelper.class) {
                if (sInstance == null) {
                    sInstance = new PermissionHelper();
                }
            }
        }
        return sInstance;
    }

    public void requestPermission(Activity activity, String[] permissions, int code) {
        String[] strings = checkPermission(activity, permissions);
        if (strings != null && strings.length > 0) {
            ActivityCompat.requestPermissions(activity, strings, code);
        }
    }

    public String[] checkPermission(Activity activity, String[] permissions) {
        ArrayList<String> denyPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (hasPermissionInManifest(permission,activity)) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(permission);
                }
            }
        }
        return denyPermissions.toArray(new String[denyPermissions.size()]);
    }


    private boolean hasPermissionInManifest(String permission, Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
