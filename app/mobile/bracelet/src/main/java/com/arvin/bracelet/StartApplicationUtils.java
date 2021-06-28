package com.arvin.bracelet;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;

import androidx.multidex.MultiDex;

public class StartApplicationUtils {

    private static final String INSTALL_FLAG_FILE = "dexInstalled";

    public static void checkDexInstall(Context context) {
        if (!quickStart(context) && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MultiDex.install(context);
        }
    }

    private static boolean quickStart(Context context) {
        if (safelyContains(getCurProcessName(context), ":mini")) {
            return true;
        }
        return false;
    }

    private static boolean safelyContains(String src, String pattern) {
        if (isNullOrEmpty(src)) {
            return false;
        }
        return src.contains(pattern);
    }

    static public boolean isNullOrEmpty(final String string) {
        return (string == null || string.trim().length() <= 0);
    }

    public static boolean isMimiProcess(Context context) {
        return quickStart(context);
    }

    private static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    private static boolean needWait(Context context) {
        return !fileExist(context.getFilesDir() + INSTALL_FLAG_FILE);
    }

    private static boolean fileExist(final String path) {
        if (isNullOrEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    public static void installFinish(Context context) {
        try {
            new File(context.getFilesDir() + INSTALL_FLAG_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
