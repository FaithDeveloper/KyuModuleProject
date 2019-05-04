package com.iscreammedia.kyuutilslibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonUtils {
    private CommonUtils(){

    }

    private static final String TAG = CommonUtils.class.getSimpleName();

    public static final String getSessionIdFromPref(Context context) {
        if (context == null) {
            Log.e(TAG, "getSessionIdFromPref() : nullpointerException");
            return null;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("login_session", null);
    }

    public static boolean hasSessionId(Context context) {
        String sessionId = getSessionIdFromPref(context);
        return sessionId != null && !sessionId.isEmpty();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
