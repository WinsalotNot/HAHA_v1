package com.example.HAHA.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Data.PostingData
import com.example.HAHA.R
import java.text.NumberFormat
import java.util.Locale

class RecyclerAdapter(private val design: String) : ListAdapter<PostingData, RecyclerView.ViewHolder>(RankingDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_EXPLORE = 1
        private const val VIEW_TYPE_LEADERBOARD = 2
    }

    var onItemClick: ((PostingData) -> Unit)? = null

    // ViewHolder for Explore Design
    class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jtextName: TextView = itemView.findViewById(R.id.jName)
        private val jtextTitle: TextView = itemView.findViewById(R.id.jTitle)
        private val jtextDesc: TextView = itemView.findViewById(R.id.jShortDesc)
        private val jtextRank: TextView = itemView.findViewById(R.id.jRank)
        private val jtextRating: TextView = itemView.findViewById(R.id.jRating)
        private val jtextReview: TextView = itemView.findViewById(R.id.jReview)
        private val jtextFee: TextView = itemView.findViewById(R.id.jFee)
        private val jtextAddress: TextView = itemView.findViewById(R.id.jAddress)
        private val jtextCat: TextView = itemView.findViewById(R.id.jCat)

        fun bind(rankingData: PostingData, onItemClick: ((PostingData) -> Unit)?) {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")) // Locale for Indonesian Rupiah
            numberFormat.maximumFractionDigits = 2 // Limit to 2 decimal places

            jtextName.text = rankingData.username.trim('"')
            jtextTitle.text = rankingData.title.trim('"')
            jtextDesc.text = (if (rankingData.shortDesc.length > 95) rankingData.shortDesc.take(95) + "..." else rankingData.shortDesc).trim('"')
            jtextRank.text = rankingData.rank.trim('"')
            jtextRating.text = rankingData.rating.toString()
            jtextReview.text = rankingData.review.toString()
            jtextFee.text = numberFormat.format(rankingData.fee) // Format the fee as Rupiah
            jtextAddress.text = rankingData.addr.trim('"')
            jtextCat.text = rankingData.cat.trim('"')

            itemView.setOnClickListener { onItemClick?.invoke(rankingData) }
        }

    }

    // ViewHolder for Leaderboard Design
    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.R_textName)
        private val textTitle: TextView = itemView.findViewById(R.id.R_textTitle)
        private val textDesc: TextView = itemView.findViewById(R.id.R_textDesc)
        private val textRank: TextView = itemView.findViewById(R.id.R_textRank)
        private val textRating: TextView = itemView.findViewById(R.id.R_textRating)
        private val textReview: TextView = itemView.findViewById(R.id.R_textReview)
        private val textCat: TextView = itemView.findViewById(R.id.R_textCat)

        fun bind(rankingData: PostingData, onItemClick: ((PostingData) -> Unit)?) {
            textName.text = rankingData.username.trim('"')
            textTitle.text = rankingData.title.trim('"')
            textDesc.text = (if (rankingData.shortDesc.length > 95) rankingData.shortDesc.take(95) + "..." else rankingData.shortDesc).trim('"')
            textRank.text = rankingData.rank.trim('"')
            textRating.text = rankingData.rating.toString()
            textReview.text = rankingData.review.toString()
            textCat.text = rankingData.cat.trim('"')

            itemView.setOnClickListener { onItemClick?.invoke(rankingData) }
        }
    }

    // Determine view type
    override fun getItemViewType(position: Int): Int {
        return if (design.lowercase() == "explore") {
            VIEW_TYPE_EXPLORE
        } else if (design.lowercase() == "leaderboard") {
            VIEW_TYPE_LEADERBOARD
        } else {
            throw IllegalArgumentException("Invalid design type: $design")
        }
    }

    // Create ViewHolder based on view type
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EXPLORE -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.job_list_design, parent, false)
                ExploreViewHolder(itemView)
            }
            VIEW_TYPE_LEADERBOARD -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ranking_list_design, parent, false)
                LeaderboardViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    // Bind data to the appropriate ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rankingData = getItem(position)
        when (holder) {
            is ExploreViewHolder -> holder.bind(rankingData, onItemClick)
            is LeaderboardViewHolder -> holder.bind(rankingData, onItemClick)
            else -> throw IllegalArgumentException("Invalid ViewHolder type")
        }
    }

    // DiffUtil for efficient list updates
    class RankingDiffCallback : DiffUtil.ItemCallback<PostingData>() {
        override fun areItemsTheSame(oldItem: PostingData, newItem: PostingData): Boolean {
            return oldItem.creatorid == newItem.creatorid
        }

        override fun areContentsTheSame(oldItem: PostingData, newItem: PostingData): Boolean {
            return oldItem == newItem
        }
    }
}
