package com.example.servigo

import android.service.notification.NotificationListenerService.Ranking
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private var rankingList : List<RankingData>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    var onItemClick : ((RankingData) -> Unit)? = null
    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.R_textName)
        val textTitle: TextView = itemView.findViewById(R.id.R_textTitle)
        val textDesc: TextView = itemView.findViewById(R.id.R_textDesc)
        val textRank: TextView = itemView.findViewById(R.id.R_textRank)
        val textRating: TextView = itemView.findViewById(R.id.R_textRating)
        val textReview: TextView = itemView.findViewById(R.id.R_textReview)
    }

    fun updateData(newList: List<RankingData>) {
        rankingList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list_design,
            parent, false)
        return RankingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val rankingData = rankingList[position]
        holder.textName.text = rankingData.name
        holder.textTitle.text = rankingData.title
        holder.textDesc.text = rankingData.shortDesc
        holder.textRank.text = rankingData.rank
        holder.textRating.text = rankingData.rating.toString() // Display Float as String
        holder.textReview.text = rankingData.review.toString() // Display Int as String

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(rankingData)
        }

    }


}