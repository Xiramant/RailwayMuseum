package com.omsk.railwaymuseum.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.net.event.EventListModel
import com.omsk.railwaymuseum.ui.event.EventListAdapter

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
    text = item.text.substring(0, 50)
}