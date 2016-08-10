package com.herokuapp.myapplication.tests;

import android.text.TextUtils;

public class Log {
	
	final static String TAG = "mylogs";
	public static void v(String msg) {
        android.util.Log.d(TAG, getLocation() + msg);
    }
	public static void e(String msg) {
		android.util.Log.e(TAG, getLocation() + msg);
	}
	public static void d(String msg) {
		android.util.Log.d(TAG, getLocation() + msg);
	}
	public static void d(float msg) {
		android.util.Log.d(TAG, getLocation() + msg);
	}
	public static void d(String tag,String msg) {
		android.util.Log.d(tag, getLocation() + msg);
	}
	public static void e(String tag,String msg) {
		android.util.Log.e(tag, getLocation() + msg);
	}
	public static void e(String tag,Exception e) {
		android.util.Log.e(tag, getLocation() + e.toString());
	}
    
	public static void e(Exception e) {
		android.util.Log.e(TAG, getLocation() + e.toString());
	}
	
    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;

        for (int i = 0; i < traces.length; i++) {
            StackTraceElement trace = traces[i];

            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber() + "]: ";
                    }
                }
                else if (trace.getClassName().startsWith(className)) {
                    found = true;
                    continue;
                }
            }
            catch (ClassNotFoundException e) {
            }
        }

        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }
}
