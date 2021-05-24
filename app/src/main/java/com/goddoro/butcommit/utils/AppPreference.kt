package com.goddoro.butcommit.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class AppPreference(context: Application) {

    private val TAG = AppPreference::class.java.simpleName
    private val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    enum class KEY(val key: String) {

        KEY_GITHUB_ID("KEY_GITHUB_ID"),
        KEY_START_DATE("KEY_START_DATE"),
        KEY_FCM_TOKEN("KEY_FCM_TOKEN"),
        KEY_DEVICE_UUID("KEY_DEVICE_UUID")

    }


    @SuppressLint("ApplySharedPref")
    private operator fun set(key: KEY, value: Any?) {
        value?.let { value ->
            when (value) {
                is String -> preference.edit().putString(key.key, value).commit()
                is Int -> preference.edit().putInt(key.key, value).commit()
                is Boolean -> preference.edit().putBoolean(key.key, value).commit()
                is Float -> preference.edit().putFloat(key.key, value).commit()
                is Long -> preference.edit().putLong(key.key, value).commit()
                else -> throw UnsupportedOperationException("Not yet implemented")
            }
        } ?: kotlin.run {
            preference.edit().remove(key.key).commit()
        }
    }

    private inline operator fun <reified T : Any> get(key: KEY, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> preference.getString(key.key, defaultValue as? String) as T?
            Int::class -> preference.getInt(key.key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> preference.getBoolean(key.key, defaultValue as? Boolean ?: false) as T?
            Float::class -> preference.getFloat(key.key, defaultValue as? Float ?: -1f) as T?
            Long::class -> preference.getLong(key.key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }


    var githubId: String
        get() = get(KEY.KEY_GITHUB_ID) ?: ""
        set(value) = set(KEY.KEY_GITHUB_ID, value)

    var startDate: String
        get() = get(KEY.KEY_START_DATE) ?: ""
        set(value) = set(KEY.KEY_START_DATE, value)

    var curFcmToken: String
        get() = get(KEY.KEY_FCM_TOKEN) ?: ""
        set(value) = set(KEY.KEY_FCM_TOKEN, value)

    var curDeviceUUid : String
        get() = get(KEY.KEY_DEVICE_UUID) ?: ""
        set(value) = set(KEY.KEY_DEVICE_UUID,value)




}