package com.example.HAHA.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Data.RankResponse
import com.example.HAHA.R

class LeaderboardAdapter : ListAdapter<RankResponse, LeaderboardAdapter.LeaderboardViewHolder>(LeaderboardDiffCallback()) {

    override fun submitList(list: List<RankResponse>?) {
        Log.d("Submit List", "Filtered List: $list")
        super.submitList(null) // Ensures RecyclerView clears previous state
        super.submitList(list)
    }

    // ViewHolder class
    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.R_textName)
        private val textRank: TextView = itemView.findViewById(R.id.R_textRank)

        fun bind(rankResponse: RankResponse) {
            textName.text = rankResponse.name.trim('"')
            textRank.text = rankResponse.rank.trim('"')
        }
    }

    // onCreateViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list_design, parent, false)
        return LeaderboardViewHolder(itemView)
    }

    // onBindViewHolder
    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val rankResponse = getItem(position)
        holder.bind(rankResponse)
    }

    // DiffUtil for efficient updates
    class LeaderboardDiffCallback : DiffUtil.ItemCallback<RankResponse>() {
        override fun areItemsTheSame(oldItem: RankResponse, newItem: RankResponse): Boolean {
            return oldItem.name == newItem.name // Use a unique identifier if available
        }

        override fun areContentsTheSame(oldItem: RankResponse, newItem: RankResponse): Boolean {
            return oldItem == newItem
        }
    }
}
