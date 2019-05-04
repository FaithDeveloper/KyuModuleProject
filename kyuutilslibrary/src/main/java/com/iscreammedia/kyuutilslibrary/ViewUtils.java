package com.iscreammedia.kyuutilslibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewUtils {
    private static final String TAG = ViewUtils.class.getSimpleName();
    private static String ERROR_TAG = "exception";

    /**
     * dp를 px로 변경
     * @param context
     * @param value
     * @return
     */
    public static float dpToPx(Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    /**
     * View의 가로 길이를 dp 값으로 변경
     * @param view
     * @param width
     * @return 가로 사이즈
     */
    public static int changeViewWidthInDP(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
        return params.width;
    }

    public static int changeViewWidthByPercentage(View view, float percentage) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (percentage * params.width);
        view.setLayoutParams(params);
        return params.width;
    }

    public static void changeViewHeightByPercentage(View view, float percentage) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (percentage * params.height);
        view.setLayoutParams(params);
    }

    public static void changeViewHeightByPercentage(View view, float percentage, float maxHeight) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) (percentage * maxHeight);
        view.setLayoutParams(params);
    }

    public static void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((ViewGroup.MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }

            tabLayout.requestLayout();
        }
    }

    private static int getDrawableId(Activity activity, String drawableName) {
        int resID = 0;
        try {
            Resources res = activity.getResources();
            resID = res.getIdentifier(drawableName, "drawable", activity.getPackageName());

        } catch (Exception e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return resID;
    }

    public static LinearGradient getLinearGradient(float left, float top, float right, float bottom, int[] colors) {
        return new LinearGradient(left, top, right, bottom,
                colors, // Colors to draw the gradient
                null, // No position defined
                Shader.TileMode.MIRROR // Shader tiling mode
        );
    }

    public static File getMediaFile(Context context, String imageName) {
        return new File(context.getFilesDir().getAbsolutePath(), imageName);
    }

    public static void storeImage(Bitmap image, File pictureFile) {
        if (pictureFile == null) {
            Log.d(ERROR_TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

        } catch (FileNotFoundException e) {
            Log.d(ERROR_TAG, "File not found: " + e.getMessage());

        } catch (IOException e) {
            Log.d(ERROR_TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Fragment Change Adapter
     * @param fragmentManager
     * @param fragment
     * @param frameId
     * @param <T>
     * @return
     */
    public static <T extends Fragment> T replaceFragmentToActivity (@NonNull FragmentManager fragmentManager, @NonNull T fragment, int frameId){

        //필수 사항
        if(fragmentManager == null || fragment == null){
            return null;
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(frameId, fragment).setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

        return fragment;
    }

    /**
     * 중복클릭 방지
     * @param v View
     * @return boolean 중복클릭이 실행되었는지 값
     */
    public static boolean avoidDoubleClick(final View v) {

        LogUtils.d(TAG, "avoidDoubleClick() ++ false" + ", enable : " + v.isEnabled());
        if (v.isEnabled() == false) {
            return false;
        }

        v.setEnabled(false);

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d(TAG, "avoidDoubleClick() -- true");
                v.setEnabled(true);
            }
        }, 500);

        return true;
    }

    /**
     * 중복클릭 방지,
     * 중복클릭을 방지 할 시간 설정 가능
     * @param v View
     * @param millisecond   시간
     * @return boolean 중복클릭시 실행 되었는지 값
     */
    public static boolean avoidDoubleClick(final View v, int millisecond) {

        LogUtils.d(TAG, "avoidDoubleClick() ++ false" + ", enable : " + v.isEnabled());
        if (v.isEnabled() == false) {
            return false;
        }

        v.setEnabled(false);

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d(TAG, "avoidDoubleClick() -- true");
                v.setEnabled(true);
            }
        }, millisecond);

        return true;
    }

    /**
     * 리스트의 높이를 계산해 주는 함수
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        LogUtils.d(TAG, "height = " + params.height + "");
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * 그리드뷰의 높이를 계산해 주는 함수
     * @param gridView      그리드뷰
     * @param devideCount   디바이트 카운트
     */
    @SuppressLint("NewApi")
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int devideCount) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.AT_MOST);
        int count = ((listAdapter.getCount() % devideCount) == 0 ? listAdapter.getCount() / devideCount : (listAdapter.getCount() / devideCount) + 1);
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getVerticalSpacing() * (count - 1));
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    /**
     * 디바이스의 높이 가져오기
     * @param context
     * @return  int 높이
     */
    public static int getDeviceHeightSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 디바이스의 넓이 가져오기
     * @param context
     * @return int 넓이
     */
    public static int getDeviceWidthSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }
}
