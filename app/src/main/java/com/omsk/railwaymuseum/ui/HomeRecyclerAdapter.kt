package com.omsk.railwaymuseum.ui

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.omsk.railwaymuseum.R
import com.omsk.railwaymuseum.data.HomeSectionModel

class HomeRecyclerAdapter(private val data : List<HomeSectionModel>, private val context: Context):
        RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }



    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val sectionIcon : ImageView = itemView.findViewById(R.id.home_section_image)
        private val sectionDescription : TextView = itemView.findViewById(R.id.home_section_text)

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.card_home_section, parent, false)

                return ViewHolder(view)
            }
        }

        fun bind(item: HomeSectionModel, context: Context) {
            sectionIcon.setImageResource(item.iconResourceId)
            sectionDescription.text = item.name
            //для использования getColor(id, theme) требуется api не менее 23
            sectionDescription.setTextColor(
                if (Build.VERSION.SDK_INT >= 23) {
                    context.resources.getColor(item.colorResourceId, context.theme)
                } else {
                    context.resources.getColor(item.colorResourceId)
                }
            )
        }
    }
}