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
import com.omsk.railwaymuseum.net.game.GameListModel
import com.omsk.railwaymuseum.net.game.GameRulesModel
import com.omsk.railwaymuseum.ui.event.EventListAdapter
import com.omsk.railwaymuseum.ui.game.GameListAdapter

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



@BindingAdapter("gameListData")
fun bindGameRecyclerView(recyclerView: RecyclerView, data: List<GameListModel>?) {
    val adapter = recyclerView.adapter as GameListAdapter
    data?.let {
        adapter.submitList(data.sortedBy { it.order })
    }
}

@BindingAdapter("gameListImage")
fun setGameListImage(imgView: ImageView, item: GameListModel) {
    val image = when(item.type) {
        imgView.context.getString(R.string.game_type_quiz) -> R.drawable.icon_game_quiz
        imgView.context.getString(R.string.game_type_quest) -> R.drawable.icon_game_quest
        imgView.context.getString(R.string.game_type_fragment) -> R.drawable.icon_game_fragment
        else -> R.drawable.icon_game_question
    }
    Glide.with(imgView.context)
            .load(image)
            .into(imgView)
}

@BindingAdapter("gameListName")
fun setGameListName(textView: TextView, item: GameListModel) {
    textView.text = item.name
}

@BindingAdapter("gameListType")
fun setGameListType(textView: TextView, item: GameListModel) {
    textView.text = textView.context.getString(R.string.game_list_type, item.typeDescription)
}

@BindingAdapter("gameListDifficultyImage")
fun setGameListDifficultyImage(imgView: ImageView, item: GameListModel) {
    val image = when(item.difficulty) {
        1 -> R.drawable.icon_game_star_1
        2 -> R.drawable.icon_game_star_2
        3 -> R.drawable.icon_game_star_3
        else -> R.drawable.icon_game_star_3
    }
    Glide.with(imgView.context)
            .load(image)
            .into(imgView)
}



@BindingAdapter("gameRulesCharacter")
fun setGameRulesCharacter(imgView: ImageView, gameType: String?) {
    gameType?.let {
        val image = when(it) {
            imgView.context.getString(R.string.game_type_quiz) -> R.drawable.game_rules_character_quiz
            imgView.context.getString(R.string.game_type_quest) -> R.drawable.game_rules_character_quest
            imgView.context.getString(R.string.game_type_fragment) -> R.drawable.game_rules_character_fragment
            else -> R.drawable.game_rules_character_quest
        }
        Glide.with(imgView.context)
                .load(image)
                .into(imgView)
    }
}