package com.example.HAHA

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.RecyclerAdapter
import com.example.HAHA.Data.PostingData

class ExploreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var searchEditText: EditText
    private lateinit var locationSpinner: Spinner
    private lateinit var rankSpinner: Spinner

    private var selectedRank: String = "Set Rank:"
    private var selectedLocation: String = "Set Location:"
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore_page, container, false)

        // Initialize ViewModel
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]

        searchEditText = view.findViewById(R.id.Esearch)
        locationSpinner = view.findViewById(R.id.locationSpinner)
        rankSpinner = view.findViewById(R.id.ratingSpinner)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.explore_recyclerView)
        // Initialize the adapter without passing any data
        recyclerAdapter = RecyclerAdapter("Explore")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recyclerAdapter

        // Observe the data from the ViewModel
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            Log.d("ExploreFragment", "Data fetched: $rankingList")
            Log.d("Explore Fragment", "Data size for RecyclerView: ${rankingList.size}")
            applyFilters(rankingList)  // When data is fetched, apply filters
        }

        sharedRecyclerViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

        // Set up spinners
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.location,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            locationSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ranks,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rankSpinner.adapter = adapter
        }

        // Listeners for filtering
        locationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLocation = locationSpinner.selectedItem.toString()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        rankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRank = rankSpinner.selectedItem.toString()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Add search functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString().lowercase()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }
        })
    }

    private fun applyFilters(rankingList: List<PostingData>) {
        // Check if any filter is applied
        val isFilteringActive = selectedLocation != "Set Location:" ||
                selectedRank != "Set Rank:" ||
                searchQuery.isNotEmpty()

        // Filter the list only if filters are active
        val filteredList = if (isFilteringActive) {
            rankingList.filter { ranking ->
                (selectedLocation == "Set Location:" || ranking.addr?.contains(selectedLocation, ignoreCase = true) == true) &&
                        (selectedRank == "Set Rank:" || ranking.rank == selectedRank) &&
                        (searchQuery.isEmpty() || ranking.username?.contains(searchQuery, ignoreCase = true) == true ||
                                ranking.title?.contains(searchQuery, ignoreCase = true) == true ||
                                ranking.shortDesc?.contains(searchQuery, ignoreCase = true) == true)
            }.sortedByDescending { it.rating } // Sort by rating descending
        } else {
            // No filtering, show the full list
            Log.d("Filter", "No Filter")
            rankingList
        }

        // Log filtered list to verify it's not empty
        Log.d("ExploreFragment", "Filtered list size: ${filteredList.size}")
        Log.d("Filter", "Filtered to: $filteredList")

        // Update the adapter with the filtered list (or full list if no filters are active)
        recyclerAdapter.submitList(filteredList)

        // Add item click listener
        recyclerAdapter.onItemClick = { rankingItem ->
            val bundle = Bundle().apply {
                putParcelable("rankingData", rankingItem)
            }
            findNavController().navigate(R.id.action_homeFragment2_to_detailsFragment2, bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        // Optionally, you can call loadData() again if you want to refresh the data every time the fragment is resumed
        Log.d("ExploreFragment", "onResume called: Loading Data...")
        sharedRecyclerViewModel.loadData()
    }
}



