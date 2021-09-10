package com.omsk.railwaymuseum.util

import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.net.event.EventListModel
import com.omsk.railwaymuseum.ui.event.EventListAdapter

private const val START_COUNT = 70
private const val COUNT_INCREMENT = 10
private const val EVENT_LIST_TEXT_LINE_COUNT = 4

@BindingAdapter("eventListData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<EventListModel>?) {
    val adapter = recyclerView.adapter as EventListAdapter
    data?.let {
        adapter.submitList(data.sortedByDescending { it.order })
    }
}

@BindingAdapter("eventListImage")
fun setEventImage(imgView: ImageView, item: EventListModel) {
    val imgUrl = "${BASE_URL}${item.imageUri}"

    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.broken_image)
        )
        .into(imgView)
}

@BindingAdapter("eventListName")
fun TextView.setEventName(item: EventListModel) {
    text = item.shortName
}

@BindingAdapter("eventListText")
fun TextView.setEventText(item: EventListModel) {
    val clearText = getClearText(item.text)
    var count = START_COUNT
    text = clearText.smartTruncate(count)

    //При использовании OnGlobalLayoutListener иногда получаются некорректрые результаты: в альбомной
    //  ориентации при пролистывании recycler view вверх/вниз в некоторых карточках получается
    //  отображение текста с начальным значением счетчика длины (50 на текущий момент), поэтому
    //  использован OnPreDrawListener.
    viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                if (width == 0) {
                    return false
                }
                viewTreeObserver.removeOnPreDrawListener(this)

                while (lineCount <= EVENT_LIST_TEXT_LINE_COUNT) {
                    count += COUNT_INCREMENT
                    text = clearText.smartTruncate(count)
                    invalidate()
                }
                text = clearText.smartTruncate(count - COUNT_INCREMENT)

                return true
            }
        }
    )
}
