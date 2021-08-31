package com.omsk.railwaymuseum.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.NonNull

const val TAG = "MyApp"
const val BASE_URL = "http://www.museum55.ru/"
const val HOME_IMAGE_RATIO = 1080f / 720f

fun getDisplayHeight(@NonNull context: Context): Int {
    return getMetrics(context).heightPixels
}

fun getDisplayHeight(@NonNull activity: Activity): Int {
    return getMetrics(activity).heightPixels
}

fun getDisplayWidth(@NonNull context: Context): Int {
    return getMetrics(context).widthPixels
}

fun getDisplayWidth(@NonNull activity: Activity): Int {
    return getMetrics(activity).widthPixels
}

fun getDisplayDensity(@NonNull context: Context): Float {
    return getMetrics(context).density
}

fun getDisplayDensity(@NonNull activity: Activity): Float {
    return getMetrics(activity).density
}

fun getDisplayDensityDpi(@NonNull context: Context): Int {
    return getMetrics(context).densityDpi
}

fun getDisplayDensityDpi(@NonNull activity: Activity): Int {
    return getMetrics(activity).densityDpi
}

fun getMetrics(@NonNull context: Context): DisplayMetrics {
    val metrics = DisplayMetrics()
    context.display?.getRealMetrics(metrics)
    return metrics
}

fun getMetrics(@NonNull activity: Activity): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}