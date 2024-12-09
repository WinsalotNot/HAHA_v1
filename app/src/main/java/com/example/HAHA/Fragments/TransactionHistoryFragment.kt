package com.example.HAHA.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.RecyclerAdapter
import com.example.HAHA.Data.PostingData
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.SharedRecyclerViewModel

class TransactionHistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var sharedPreferences: SharedPreferences // Make sharedPreferences a global variable
    private var userid: Int = 0 // Global userId

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_history, container, false)

        // Initialize ViewModel
        sharedRecyclerViewModel = activityViewModels<SharedRecyclerViewModel>().value

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        // Initialize RecyclerAdapter for "transactionhistory" design
        recyclerAdapter = RecyclerAdapter("transactionhistory")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userid = sharedPreferences.getInt("USER_ID_INT", 0)

        // Set up RecyclerView layout manager
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter

        // Observe data from ViewModel
        sharedRecyclerViewModel.historyList.observe(viewLifecycleOwner, Observer { historyList ->
            // Call applyFilters with the list (no filtering logic for now)
            applyFilters(historyList)
        })

        sharedRecyclerViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

    }

    // Handle data and update the adapter (without filtering)
    private fun applyFilters(historyList: List<PostingData>) {

        // Separate items based on the conditions
        val boughtNotCompleted = historyList.filter { it.isBought && !it.isCompleted }
        val completedItems = historyList.filter { it.isCompleted }

        // Combine the lists with the desired order
        val sortedList = boughtNotCompleted + completedItems
        recyclerAdapter.submitList(sortedList)

        // Add item click listener
        recyclerAdapter.onItemClick = { rankingItem ->
            if (rankingItem.isBought || rankingItem.isCompleted) {
                val bundle = Bundle().apply {
                    putParcelable("rankingData", rankingItem)
                }
                findNavController().navigate(R.id.action_transactionHistory_to_detailHistoryFragment, bundle)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TransactionHistoryFragment", "onResume called: Loading Data...")
        sharedRecyclerViewModel.loadDataHistory(userid = userid) // Replace with actual user ID
    }
}
