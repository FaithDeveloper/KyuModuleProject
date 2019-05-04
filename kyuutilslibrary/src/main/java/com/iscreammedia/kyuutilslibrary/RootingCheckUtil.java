package com.iscreammedia.kyuutilslibrary;

import android.os.Environment;

import java.io.File;

public class RootingCheckUtil {
    private static final String ROOT_PATH = Environment.getExternalStorageDirectory() + "";
    private static final String ROOTING_PATH_1 = "/system/bin/su";
    private static final String ROOTING_PATH_2 = "/system/xbin/su";
    private static final String ROOTING_PATH_3 = "/system/app/SuperUser.apk";
    private static final String ROOTING_PATH_4 = "/data/data/com.noshufou.android.su";
    private static final String[] RootFilesPath = new String[]{
            ROOT_PATH + ROOTING_PATH_1 ,
            ROOT_PATH + ROOTING_PATH_2 ,
            ROOT_PATH + ROOTING_PATH_3 ,
            ROOT_PATH + ROOTING_PATH_4
    };
    
    public static boolean isDeviceRooted() {
        boolean isDeviceRooted = false;
        
        try {
            Runtime.getRuntime().exec("su");
            isDeviceRooted = true;
        } catch (Exception e) {
            e.printStackTrace();
            isDeviceRooted = false;
        }
        
        if (isDeviceRooted == false) {
            isDeviceRooted = checkRootingFiles(createFiles(RootFilesPath));
        }
        
        return isDeviceRooted;
    }
    
    /**
     * 루팅파일 의심 Path를 가진 파일들을 생성 한다.
     */
    private static File[] createFiles(String[] sfiles){
        File[] rootingFiles = new File[sfiles.length];
        for (int i=0 ; i < sfiles.length; i++) {
            rootingFiles[i] = new File(sfiles[i]);
        }
        
        return rootingFiles;
    }
    
    /**
     * 루팅파일 여부를 확인 한다.
     */
    private static boolean checkRootingFiles(File... file){
        boolean result = false;
        
        for(File f : file) {
            if (f != null && f.exists() && f.isFile()) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        
        return result;
    }
}