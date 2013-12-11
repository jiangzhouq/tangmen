package com.jiangzhouq.tangmen.data;

import android.os.Environment;

public class Constants {
	public static final boolean LOG_SWITCH = true;
	public static final String LOG_TAG = "tangmen";
	public static final String NATIVE_PATH = getSdPath() + "/tangmen/jueshitangmen";
	private static String getSdPath(){
		return Environment.getExternalStorageDirectory().getPath();
	}
}
