package com.omsk.railwaymuseum.data

import com.omsk.railwaymuseum.R

class HomeSectionModel private constructor(
        val name: String,
        val iconResourceId: Int,
        val colorResourceId: Int
) {

    companion object{
        @JvmField
        val list = listOf(
                HomeSectionModel("События",
                        R.drawable.icon_section_event,
                        R.color.color_section_event),
                HomeSectionModel("Интересные факты",
                        R.drawable.icon_section_interesting,
                        R.color.color_section_interesting),
                HomeSectionModel("Экспонаты",
                        R.drawable.icon_section_exhibit,
                        R.color.color_section_exhibit),
                HomeSectionModel("Игры",
                        R.drawable.icon_section_game,
                        R.color.color_section_game),
                HomeSectionModel("О музее",
                        R.drawable.icon_section_info,
                        R.color.color_section_info),
                HomeSectionModel("Отзывы",
                        R.drawable.icon_section_review,
                        R.color.color_section_review),
        )
    }

}