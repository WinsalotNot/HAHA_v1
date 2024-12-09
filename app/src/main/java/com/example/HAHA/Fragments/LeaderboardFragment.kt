package com.example.HAHA.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.LeaderboardAdapter
import com.example.HAHA.Adapter.RecyclerAdapter
import com.example.HAHA.Data.PostingData
import com.example.HAHA.Data.RankResponse
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.SharedRecyclerViewModel
import com.example.HAHA.ViewModel.UIViewModel

class LeaderboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private val uiViewModel: UIViewModel by activityViewModels()
    private lateinit var recyclerAdapter: LeaderboardAdapter
    private lateinit var orderSpinner: Spinner
    private lateinit var rankSpinner: Spinner

    private var selectedSortOrder: String = "Set Order:"
    private var selectedRank: String = "Set Rank:"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        // Initialize ViewModel
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.leaderboard_recyclerView)
        // Initialize RankingAdapter without passing data in constructor
        recyclerAdapter = LeaderboardAdapter()

        // Set up categorySpinner
        orderSpinner = view.findViewById(R.id.orderSpinner)
        // Set up rankSpinner
        rankSpinner = view.findViewById(R.id.rankSpinner)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter

        // Observe data from ViewModel
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            applyFilters(rankingList)
        }

        sharedRecyclerViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.order,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            orderSpinner.adapter = adapter
        }
        orderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSortOrder = orderSpinner.selectedItem.toString()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ranks,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rankSpinner.adapter = adapter
        }
        rankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRank = rankSpinner.selectedItem.toString()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun applyFilters(rankingList: List<RankResponse>) {
        // Define the rank order mapping (ascending)
        val rankOrder = mapOf(
            "E" to 1,
            "D" to 2,
            "C" to 3,
            "B" to 4,
            "A" to 5,
            "S" to 6
        )

        // Filter the list based on selected category and rank
        val filteredList = rankingList.filter { ranking ->
            (selectedRank == "Set Rank:" || ranking.rank.trim('"') == selectedRank)
        }

        // Sort the list based on the selected rank order (ascending or descending)
        val sortedList = when (selectedSortOrder) {
            "Ascending" -> filteredList.sortedBy { rankOrder[it.rank.trim()] ?: Int.MAX_VALUE } // Ascending by rank order
            "Descending" -> filteredList.sortedByDescending { rankOrder[it.rank.trim()] ?: Int.MAX_VALUE } // Descending by rank order
            else -> filteredList // If no sort order selected, return the filtered list as is
        }

        // Update the adapter with the sorted list
        recyclerAdapter.submitList(sortedList)
    }

    override fun onResume() {
        super.onResume()
        Log.d("LeaderboardFragment", "onResume called: Loading Data...")
        sharedRecyclerViewModel.loadRanking()
    }
}
