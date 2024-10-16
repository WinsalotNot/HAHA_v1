package com.example.servigo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionHistoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_history, container, false)
    }

    // Set up the RecyclerView after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView in the inflated view
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for RecyclerView (you can replace this with your dynamic data)
        val historyList = listOf(
            HistoryItem("Hasanudin Alibama", "Professional House Keeper", "Job Description: Clean House", "17/12/24", "Rp.502.500,00", "5/5", "5/5", "S"),
            HistoryItem("Jane Doe", "Professional Electrician", "Job Description: Safe Wires", "17/11/24", "Rp.600.500,00", "4.5/5", "3/5", "A")
            // Add more items as needed
        )

        // Set up the adapter
        recyclerView.adapter = HistoryAdapter(historyList)
    }
}
