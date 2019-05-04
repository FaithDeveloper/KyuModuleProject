package com.iscreammedia.kyuutilslibrary;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;



public class FormatUtils {
    private FormatUtils(){

    }

    /**
     * 천 단위로 변경한다.
     * @param amout
     * @return
     */
    public static String getFormatedAmount(int amout){
        return NumberFormat.getNumberInstance(Locale.KOREA).format(amout);
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");

    public static String formatNumber(int cnt) {
        return DECIMAL_FORMAT.format(cnt);
    }

    public static String formatNumber(long cnt) {
        return DECIMAL_FORMAT.format(cnt);
    }
}
