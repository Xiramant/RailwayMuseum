package com.omsk.railwaymuseum.util

import android.app.Activity
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.GameType
import com.omsk.railwaymuseum.data.GameType.*
import com.omsk.railwaymuseum.net.event.EventListModel
import com.omsk.railwaymuseum.net.game.GameListModel
import com.omsk.railwaymuseum.net.game.GameQuestionsModel
import com.omsk.railwaymuseum.net.review.ReviewModel
import com.omsk.railwaymuseum.ui.event.EventListAdapter
import com.omsk.railwaymuseum.ui.game.GameListAdapter
import com.omsk.railwaymuseum.ui.review.ReviewAdapter
import com.omsk.railwaymuseum.ui.review.ReviewAddingAdapter
import com.omsk.railwaymuseum.ui.review.ReviewImageListAdapter
import java.io.File
import java.sql.Timestamp

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
    text = if (item.shortName == "") {
        item.name
    } else {
        item.shortName
    }
}

@BindingAdapter("eventListText")
fun TextView.setEventText(item: EventListModel) {
    if (item.text.isEmpty()) {
        text = ""
        return
    }
    val clearText = getClearText(item.text)
    if (clearText.length <= START_COUNT) {
        text = clearText
        return
    }
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
    val image = if(item.imagePreview.isNotEmpty()) {
        "${BASE_URL}${item.imagePreview}"
    } else {
        when (item.type) {
            QUIZ -> R.drawable.icon_game_quiz
            QUEST -> R.drawable.icon_game_quest
            FRAGMENT -> R.drawable.icon_game_fragment
            else -> R.drawable.icon_game_question
        }
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


//Система требует для @BindingAdapter использование 1 или 2 параметров, поэтому пришлось
//  использовать обычную функцию.
fun setGameBackground(imgView: ImageView){
    Glide.with(imgView.context)
            .load(R.drawable.app_background)
            .into(imgView)
    imgView.alpha = 0.7F
}



@BindingAdapter("gameRulesCharacter")
fun setGameRulesCharacter(imgView: ImageView, gameType: GameType?) {
    gameType?.let {
        val image = when (it) {
            QUIZ -> R.drawable.game_rules_character_quiz
            QUEST -> R.drawable.game_rules_character_quest
            FRAGMENT -> R.drawable.game_rules_character_fragment
            else -> R.drawable.game_rules_character_quest
        }
        Glide.with(imgView.context)
            .load(image)
            .into(imgView)
    }
}

//Значения для ширины и высоты:
//public static final int MATCH_PARENT = -1;
//public static final int WRAP_CONTENT = -2;
//
//Layout у gameQuiz и gameQuest в течении игры не перегружаются, в них только обновляются данные.
//  Это привдодит к багу: установленные в imageView ширина и высота = wrap_content приводят к
//  постепенному уменьшению картинки - размеры предыдущего изображения становятся границами
//  в которые вписывается текущее изображение. Для устранения этого бага перед загрузкой изображения
//  в imageView я сбрасываю его высоту и ширину на фиксированные значения, а после загрузки
//  устанавливаю их обратно во wrap_content. (И после этого делаю изображение видимым.)
@BindingAdapter(value = ["gameQuizImage", "maxHeightDp"], requireAll = true)
fun setGameQuizImage(imgView: ImageView, image: String?, maxHeightDp: Int) {
    imgView.visibility = GONE
    image?.let {
        if (image == "") {
            return
        }

        val displayDensity = if (Build.VERSION.SDK_INT >= 30) {
            getDisplayDensity(imgView.context)
        } else {
            getDisplayDensity(imgView.context as Activity)
        }

        val displayWidth = if (Build.VERSION.SDK_INT >= 30) {
            getDisplayWidth(imgView.context)
        } else {
            getDisplayWidth(imgView.context as Activity)
        }

        imgView.layoutParams.height = (maxHeightDp * displayDensity).toInt()
        imgView.layoutParams.width = displayWidth

        val imgUrl = "${BASE_URL}${it}"
        Glide.with(imgView.context)
            .load(imgUrl)
            .into(imgView)

        imgView.layoutParams.height = -2
        imgView.layoutParams.width = -2
        imgView.visibility = VISIBLE
    }
}

@BindingAdapter(value = ["gameQuizAnswers", "radioButtonId"], requireAll = true)
fun setGameQuizAnswers(radioButton: RadioButton, item: GameQuestionsModel?, radioButtonId: Int) {
    item?.let {
        if (radioButtonId < item.quizAnswer.size) {
            radioButton.text = item.quizAnswer[radioButtonId]
            radioButton.visibility = VISIBLE
        } else {
            radioButton.visibility = GONE
        }
    }
}

@BindingAdapter("gameQuestQuestion")
fun setGameQuestQuestion(textView: TextView, question: String?) {
    if (question == null || question == "") {
        textView.text = textView.context.getString(R.string.game_quest_null_question)
    } else {
        textView.text = question
    }
}



@BindingAdapter("reviewListData")
fun bindReviewRecyclerView(recyclerView: RecyclerView, data: List<ReviewModel>?) {
    val adapter = recyclerView.adapter as ReviewAdapter
    data?.let {
        adapter.submitList(data.sortedByDescending { it.data })
    }
}

@BindingAdapter("reviewData")
fun setReviewData(textView: TextView, data: Timestamp) {
    textView.text = data.toString().split(" ")[0]
}



@BindingAdapter("reviewImageList")
fun bindReviewImageList(recyclerView: RecyclerView, data: List<String>?) {
    if(data.isNullOrEmpty()) {
        recyclerView.visibility = GONE
    } else {
        val adapter = recyclerView.adapter as ReviewImageListAdapter
        adapter.submitList(data)
        recyclerView.visibility = VISIBLE
    }
}

@BindingAdapter("reviewAddingUriImage")
fun setReviewAddingUriImage(imgView: ImageView, imageUri: String?) {
    imageUri?.let {
        Glide.with(imgView.context)
            .load("$BASE_URL$it")
            .into(imgView)
    }
}

@BindingAdapter("reviewAddingData")
fun bindReviewAddingData(recyclerView: RecyclerView, data: List<File>?) {
    if(data.isNullOrEmpty()) {
        recyclerView.visibility = GONE
    } else {
        val adapter = recyclerView.adapter as ReviewAddingAdapter
        adapter.submitList(data)
        recyclerView.visibility = VISIBLE
    }
}

@BindingAdapter("reviewAddingImage")
fun setReviewAddingImage(imgView: ImageView, imageFile: File?) {
    imageFile?.let {
        Glide.with(imgView.context)
            .load(it)
            .into(imgView)
    }
}