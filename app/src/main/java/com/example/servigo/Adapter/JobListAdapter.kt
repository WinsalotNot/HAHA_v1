package com.example.servigo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class JobListAdapter(private val jobList: List<JobListData>) : RecyclerView.Adapter<JobListAdapter.JobListViewHolder>() {

    class JobListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jtitleTextView: TextView = itemView.findViewById(R.id.jtitleTextView)
        val jdescriptionTextView: TextView = itemView.findViewById(R.id.jdescriptionTextView)
        val jdateTextView: TextView = itemView.findViewById(R.id.jdateTextView)
        val jminstarTextView: TextView = itemView.findViewById(R.id.jminstarTextView)
        val jrequestedrankTextView: TextView = itemView.findViewById(R.id.jrequestedrankTextView)
        val jpaymentTextView: TextView = itemView.findViewById(R.id.jpaymentTextView)
        val jhowmuchTextView: TextView = itemView.findViewById(R.id.jhowmuchTextView)
        val jaddressTextView: TextView = itemView.findViewById(R.id.jaddressTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_list_design, parent, false)
        return JobListViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobListViewHolder, position: Int) {
        val jobItem = jobList[position]
        holder.jtitleTextView.text = jobItem.title
        holder.jdescriptionTextView.text = jobItem.description
        holder.jdateTextView.text = jobItem.date
        holder.jminstarTextView.text = jobItem.min_star
        holder.jrequestedrankTextView.text = jobItem.requested_rank
        holder.jpaymentTextView.text = jobItem.payment
        holder.jhowmuchTextView.text = jobItem.how_much
        holder.jaddressTextView.text = jobItem.address

        // Set click listener to navigate to details fragment
        holder.itemView.setOnClickListener { view ->
            // Create a bundle with job details
            val bundle = Bundle().apply {
                putString("title", jobItem.title)
                putString("description", jobItem.description)
                putString("date", jobItem.date)
                putString("min_star", jobItem.min_star)
                putString("requested_rank", jobItem.requested_rank)
                putString("payment", jobItem.payment)
                putString("how_much", jobItem.how_much)
                putString("address", jobItem.address)
            }

            // Use the action generated from the navigation graph
            // Make sure the navController is correctly set from the view context
            try {
                view.findNavController().navigate(R.id.action_jobListFragment_to_detailsFragment2, bundle)
            } catch (e: Exception) {
                // Log any navigation errors
                Log.e("JobListAdapter", "Navigation error: ${e.message}")
            }
        }
    }

    override fun getItemCount() = jobList.size
}
