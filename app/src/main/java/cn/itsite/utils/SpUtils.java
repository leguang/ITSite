package cn.itsite.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtils {
	private static final String SETTINGS = "settings";
	private static SharedPreferences mSharedPreferences;

	private static SharedPreferences getSp(Context context) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
		}
		return mSharedPreferences;
	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		mSharedPreferences = getSp(context);
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static void setBoolean(Context context, String key, boolean value) {
		mSharedPreferences = getSp(context);
		Editor edit = mSharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static String getString(Context context, String key, String defaultValue) {
		mSharedPreferences = getSp(context);
		return mSharedPreferences.getString(key, defaultValue);
	}

	public static void setString(Context context, String key, String value) {
		mSharedPreferences = getSp(context);
		mSharedPreferences.edit().putString(key, value).commit();

	}

	public static Long getLong(Context context, String key, Long defaultValue) {
		mSharedPreferences = getSp(context);
		return mSharedPreferences.getLong(key, defaultValue);
	}

	public static void setLong(Context context, String key, Long value) {
		mSharedPreferences = getSp(context);
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	public static Float getFloat(Context context, String key, Float defaultValue) {
		mSharedPreferences = getSp(context);
		return mSharedPreferences.getFloat(key, defaultValue);
	}

	public static void setFloat(Context context, String key, Float value) {
		mSharedPreferences = getSp(context);
		mSharedPreferences.edit().putFloat(key, value).commit();
	}

	public static void remove(Context context, String key) {
		mSharedPreferences = getSp(context);
		mSharedPreferences.edit().remove(key).commit();
	}
}
