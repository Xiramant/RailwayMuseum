package com.omsk.railwaymuseum.data

import com.omsk.railwaymuseum.R

class HomeSectionModel private constructor(
        val name: Int,
        val iconResourceId: Int,
        val colorResourceId: Int
) {

    companion object{
        @JvmField
        val list = listOf(
                HomeSectionModel(R.string.name_section_event,
                        R.drawable.icon_section_event,
                        R.color.section_event),
                HomeSectionModel(R.string.name_section_interesting,
                        R.drawable.icon_section_interesting,
                        R.color.section_interesting),
                HomeSectionModel(R.string.name_section_exhibit,
                        R.drawable.icon_section_exhibit,
                        R.color.section_exhibit),
                HomeSectionModel(R.string.name_section_game,
                        R.drawable.icon_section_game,
                        R.color.section_game),
                HomeSectionModel(R.string.name_section_info,
                        R.drawable.icon_section_info,
                        R.color.section_info),
                HomeSectionModel(R.string.name_section_review,
                        R.drawable.icon_section_review,
                        R.color.section_review),
        )
    }

}