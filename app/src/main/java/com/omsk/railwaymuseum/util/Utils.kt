package com.omsk.railwaymuseum.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.omsk.railwaymuseum.ui.ImageFullscreenActivity

const val TAG = "MyApp"
const val IMAGE_FULLSCREEN_ACTIVITY_TAG = "imageUri"
const val BASE_URL = "http://www.museum55.ru/"
const val HOME_IMAGE_RATIO = 1080f / 720f

private const val LI_REPLACEMENT = "- "
private const val TEXT_REVIEW_END = "..."
private val PUNCTUATION = listOf(", ", "; ", ": ", "\r\n ", " ")

enum class WebTags(val tag: String) {
    IMAGE_BEGIN("<div class=\"image-container\">"),
    DIV_BEGIN("<div>"),
    DIV_END("</div>"),
    UL_BEGIN("<ul>"),
    UL_END("</ul>"),
    LI_BEGIN("<li>"),
    LI_END("</li>");
}

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


/**
 * Получение текста без картинок и тегов из html-текста.
 */
fun getClearText(rawText: String): String {
    // !!! Удаление картинок с тэгами и описанием должно быть перед удалением остальных тэгов.
    val textWithoutImage = removeImageFromText(rawText)
    return removeTagsFromText(textWithoutImage)
}

/**
 * Удаление картинок из html-текста.
 * !!! Совершенно не универсален и действует только для разработанной структуры
 * описания картинок на museum55.ru.
 */
private fun removeImageFromText(textArg: String): String {
    val out = StringBuilder(textArg)
    while (true) {
        val begin = out.indexOf(WebTags.IMAGE_BEGIN.tag)
        if (begin == -1) {
            break
        }
        val end = out.indexOf(WebTags.DIV_END.tag, begin)
        out.delete(begin, end)
    }
    return out.toString()
}

/**
 * Удаление тэгов из html-текста (тег  "li"  заменяет на LI_REPLACEMENT).
 * !!! Не универсален и ограничен перечислением тегов в классе WebTags.
 */
private fun removeTagsFromText(textArg: String): String {
    // !!! Замена <li> на "- " должно быть перед удалением остальных тэгов
    val textWithoutLi = textArg.replace(WebTags.LI_BEGIN.tag.toRegex(), LI_REPLACEMENT)
    var out = StringBuilder(textWithoutLi)
    for (webTag in WebTags.values()) {
        val textTemp = out.toString().replace(webTag.tag.toRegex(), "")
        out = StringBuilder(textTemp)
    }
    return out.toString().trim { it <= ' ' }
}

/**
 * Обрезка текста по длине с окончанием на целое слово с удалением пунктуации у последнего слова.
 * Знаки пунктуации для удаления заданы в константе PUNCTUATION.
 */
fun String.smartTruncate(length: Int): String {
    //Для разделения слов, расположенных на стыке абзацев. Иначе текст "путешественников.\r\nМузей"
    //  будет считаться одним словом.
    val paragraph = replace("\r\n", "\r\n ")

    val words = paragraph.split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append(TEXT_REVIEW_END)
    }
    return builder.toString()
}

fun showFullscreenImage(fragment: Fragment, uri: String) {
    val intent = Intent(fragment.activity, ImageFullscreenActivity::class.java)
    intent.putExtra(IMAGE_FULLSCREEN_ACTIVITY_TAG, uri)
    fragment.startActivity(intent)
}