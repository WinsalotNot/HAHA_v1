package com.example.HAHA.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    private val sharedRecyclerViewModel: SharedRecyclerViewModel by activityViewModels()
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: EditText
    private var searchQuery: String = ""
    private var userid: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction_history, container, false)

        // Initialize search bar and RecyclerView
        searchHistory = view.findViewById(R.id.searchHistory)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerAdapter("transactionhistory")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userid = sharedPreferences.getInt("USER_ID_INT", 0)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter

        // Observe history list from ViewModel
        sharedRecyclerViewModel.historyList.observe(viewLifecycleOwner, Observer { historyList ->
            applyFilters(historyList)
        })

        // Observe loading state
        sharedRecyclerViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            val activity = requireActivity() as MainActivity
            if (isLoading) activity.showLoading() else activity.hideLoading()
        }

        // Add search functionality
        searchHistory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString().lowercase()
                sharedRecyclerViewModel.historyList.value?.let { applyFilters(it) }
            }
        })
    }

    // Apply filters to the history list and update the RecyclerAdapter
    private fun applyFilters(historyList: List<PostingData>) {
        Log.d("applyFilters", "Applying filters with query: '$searchQuery'")
        Log.d("applyFilters", "Initial list size: ${historyList.size}")

        val boughtNotCompleted = historyList.filter { it.isBought && !it.isCompleted }
        val completedItems = historyList.filter { it.isCompleted }

        val sortedList = boughtNotCompleted + completedItems

        val isFilteringActive = searchQuery.isNotEmpty()

        val filteredList = if (isFilteringActive) {
            sortedList.filter { history ->
                history.username?.contains(searchQuery, ignoreCase = true) == true ||
                        history.title?.contains(searchQuery, ignoreCase = true) == true ||
                        history.shortDesc?.contains(searchQuery, ignoreCase = true) == true
            }.sortedByDescending { it.rating }
        } else {
            sortedList
        }

        Log.d("applyFilters", "Filtered list size: ${filteredList.size}")
        recyclerAdapter.submitList(filteredList)
        recyclerAdapter.notifyDataSetChanged()

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
        sharedRecyclerViewModel.loadDataHistory(userid = userid)
    }
}
