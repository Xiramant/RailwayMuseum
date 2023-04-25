package com.omsk.railwaymuseum.data

import android.os.Build
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import com.omsk.railwaymuseum.R

// В системе доступно 4 варианта темы:
// - светлая;
// - тёмная;
// - выбирается в зависимости от режима энергосбережения (Android 9 и ниже);
// - переключается в зависимости от настроек системы (Android 10 и выше).

enum class NightModeType(
    @NightMode val value: Int,
    @StringRes val title: Int
) {

    MODE_NIGHT_NO(
        AppCompatDelegate.MODE_NIGHT_NO,
        R.string.theme_light
    ),

    MODE_NIGHT_YES(
        AppCompatDelegate.MODE_NIGHT_YES,
        R.string.theme_dark
    ),

    MODE_NIGHT_FOLLOW_SYSTEM(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        R.string.theme_system
    ),

    MODE_NIGHT_AUTO_BATTERY(
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
        R.string.theme_battery
    );

    companion object {

        fun fromValue(@NightMode value: Int) = values().firstOrNull { it.value == value } ?: getDefaultMode()

        fun getDefaultMode(): NightModeType {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                MODE_NIGHT_AUTO_BATTERY
            }
        }
    }
}