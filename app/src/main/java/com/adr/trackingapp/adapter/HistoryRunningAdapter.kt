package com.adr.trackingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adr.trackingapp.R
import com.adr.trackingapp.database.HistoryEntity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_history_running.view.*

class HistoryRunningAdapter: RecyclerView.Adapter<HistoryRunningAdapter.ViewHolder>() {

    private var context: Context? = null
    private var dataList: List<HistoryEntity> = ArrayList()

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val historyDate: TextView = itemView.tv_date
        val historyMapPreview: ImageView = itemView.iv_map_preview
        val historyDescription: TextView = itemView.tv_description
        val historyDistance: TextView = itemView.tv_distance
        val historyAverageSpeed: TextView = itemView.tv_average_speed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_running, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.historyDate.text = dataList[position].date

        if (context != null){
            Glide.with(context!!).load(dataList[position].screenshot).into(holder.historyMapPreview)
        }

        holder.historyDescription.text = dataList[position].description
        holder.historyDistance.text = dataList[position].distance.toString()
        holder.historyAverageSpeed.text = dataList[position].averageSpeed.toString()
    }

    fun setDataList(dataList: List<HistoryEntity>){
        this.dataList = dataList
    }
}