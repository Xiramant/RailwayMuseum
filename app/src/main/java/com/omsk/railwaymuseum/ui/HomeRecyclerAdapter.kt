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

    interface Listener {
        fun onClick(itemData: HomeSectionModel)
    }

    private var listener: Listener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemData = data[position]
        holder.bind(itemData, context)

        holder.itemView.setOnClickListener{
            listener?.onClick(itemData)
        }
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

        fun bind(itemData: HomeSectionModel, context: Context) {
            sectionIcon.setImageResource(itemData.iconResourceId)
            sectionDescription.text = context.getString(itemData.name)
            //для использования getColor(id, theme) требуется api не менее 23
            sectionDescription.setTextColor(
                if (Build.VERSION.SDK_INT >= 23) {
                    context.resources.getColor(itemData.colorResourceId, context.theme)
                } else {
                    context.resources.getColor(itemData.colorResourceId)
                }
            )
        }
    }
}