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
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.RecyclerAdapter
import com.example.HAHA.Data.PostingData
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.SharedRecyclerViewModel
import com.example.HAHA.ViewModel.UIViewModel

class LeaderboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var uiViewModel: UIViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var categorySpinner: Spinner
    private lateinit var rankSpinner: Spinner

    private var selectedCategory: String = "Set Category:"
    private var selectedRank: String = "Set Rank:"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        // Initialize ViewModel
        uiViewModel = ViewModelProvider(requireActivity()).get(UIViewModel::class.java)
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.leaderboard_recyclerView)
        // Initialize RankingAdapter without passing data in constructor
        recyclerAdapter = RecyclerAdapter("leaderboard")

        // Set up categorySpinner
        categorySpinner = view.findViewById(R.id.categorySpinner)
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
            R.array.categories,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categorySpinner.selectedItem.toString()
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

    private fun applyFilters(rankingList: List<PostingData>) {
        val filteredList = rankingList.filter { ranking ->
            (selectedCategory == "Set Category:" || ranking.cat.contains(selectedCategory, ignoreCase = true)) &&
                    (selectedRank == "Set Rank:" || ranking.rank == selectedRank)
        }.sortedByDescending { it.rating } // Sort by rating in descending order

        // Use submitList to update the data in the adapter
        recyclerAdapter.submitList(filteredList)

        // Set onItemClick listener
        recyclerAdapter.onItemClick = { rankingItem ->
            val bundle = Bundle().apply {
                putParcelable("rankingData", rankingItem)
            }
            findNavController().navigate(R.id.action_ranking_page_to_detailsFragment2, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("LeaderboardFragment", "onResume called: Loading Data...")
        sharedRecyclerViewModel.loadData()
    }
}
