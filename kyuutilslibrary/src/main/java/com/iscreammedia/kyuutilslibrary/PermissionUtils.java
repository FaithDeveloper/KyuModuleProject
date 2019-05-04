package com.iscreammedia.kyuutilslibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    public static final int PERMISSION_RESULT_RECORD_AUDIO = 1;

    private PermissionUtils() {
    }

    private static boolean checkVersionNonPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return false;
        }
        return true;
    }
    public static boolean hasPermission(Context context, String permission) {
        if (!checkVersionNonPermission()) {
            return true;
        }
        if (context == null || TextUtils.isEmpty(permission)) {
            return false;
        }
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, int resultCode) {
        if (!checkVersionNonPermission()) {
            return;
        }
        if (activity == null || TextUtils.isEmpty(permission)) {
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{permission}, resultCode);
    }

    public static boolean checkPermissions(Activity activity, String[] permissions) {
        if (!checkVersionNonPermission()) {
            return true;
        }

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (requestCode == 100) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // do something
//            }
//            return;
//        }
//    }
}