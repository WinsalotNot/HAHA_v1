package com.example.HAHA

import android.os.Bundle
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LeaderboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LeaderboardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var rankingAdapter: RankingAdapter

    private var selectedCategory: String = "Set Category:"
    private var selectedRank: String = "Set Rank:"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.leaderboard_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the adapter
        rankingAdapter = RankingAdapter(emptyList())
        recyclerView.adapter = rankingAdapter

        // Observe data from ViewModel
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            applyFilters(rankingList)
        }

        // Set up categorySpinner
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
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

        // Set up rankSpinner
        val rankSpinner: Spinner = view.findViewById(R.id.rankSpinner)
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

    private fun applyFilters(rankingList: List<RankingData>) {
        val filteredList = rankingList.filter { ranking ->
            (selectedCategory == "Set Category:" || ranking.cat.contains(selectedCategory, ignoreCase = true)) &&
                    (selectedRank == "Set Rank:" || ranking.rank == selectedRank)
        }.sortedByDescending { it.rating } // Sort by rating in descending order

        rankingAdapter.updateData(filteredList)
        // Set onItemClick listener
        rankingAdapter.onItemClick = { rankingItem ->
            val bundle = Bundle().apply {
                putParcelable("rankingData", rankingItem)
            }
            findNavController().navigate(R.id.action_ranking_page_to_detailsFragment2, bundle)
        }

    }
}
