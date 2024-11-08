package com.example.servigo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RankingAdapter(private val rankingList : List<RankingData>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.R_textName)
        val textTitle: TextView = itemView.findViewById(R.id.R_textTitle)
        val textDesc: TextView = itemView.findViewById(R.id.R_textDesc)
        val textCat: TextView = itemView.findViewById(R.id.R_textCat)
        val textRating: TextView = itemView.findViewById(R.id.R_textRating)
        val textReview: TextView = itemView.findViewById(R.id.R_textReview)
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
        val rankingItem = rankingList[position]
        holder.textName.text = rankingItem.name
        holder.textTitle.text = rankingItem.title
        holder.textDesc.text = rankingItem.description
        holder.textCat.text = rankingItem.rank
        holder.textRating.text = rankingItem.rating
        holder.textReview.text = rankingItem.review
    }


}