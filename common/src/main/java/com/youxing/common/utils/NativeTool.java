package com.youxing.common.utils;

import android.util.Log;

@SuppressWarnings("ALL")
public class NativeTool {
	
	/**
	 * 表示Native代码是否已经被正常载入
	 * <p>
	 * 使用前必须检查A是否为true
	 */
	public final static boolean A;
	static {
		boolean a = false;
		try {
			System.loadLibrary("nt");
			a = a();
		} catch (Throwable t) {
			Log.w("sign", "failed to load native tool");
		}
		A = a;
	}
	
	private native static boolean a();
	
	/**
	 * 加密（native encrypt）
	 * 
	 * @param source
	 * @return
	 */
	public native static String ne(byte[] source, int customer);

}
