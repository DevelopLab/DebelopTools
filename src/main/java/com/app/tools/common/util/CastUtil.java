package com.app.tools.common.util;

public class CastUtil {

	/**
	 * 戻り値の型に合わせてキャストする。
	 *
	 * @param obj キャストするObject
	 * @return　キャストしたObject
	 */
	@SuppressWarnings("unchecked")
	public static <T> T autoCast(Object obj) {
	    T castObj = (T) obj;
	    return castObj;
	}
}
