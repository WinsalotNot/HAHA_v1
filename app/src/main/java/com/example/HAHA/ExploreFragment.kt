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
import com.example.HAHA.Adapter.RankingAdapter
import com.example.HAHA.Data.RankingData

class ExploreFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var rankingAdapter: RankingAdapter

    private var selectedRank: String = "Set Rank:"
    private var selectedLocation: String = "Set Location:"
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        sharedRecyclerViewModel = ViewModelProvider(requireActivity()).get(SharedRecyclerViewModel::class.java)
        val searchEditText: EditText = view.findViewById(R.id.Esearch)
        val locationSpinner: Spinner = view.findViewById(R.id.locationSpinner)
        val rankSpinner: Spinner = view.findViewById(R.id.ratingSpinner)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.explore_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        // Set up the adapter
        rankingAdapter = RankingAdapter(emptyList())
        recyclerView.adapter = rankingAdapter

        // **Observe the data**
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            Log.d("ExploreFragment", "Observer triggered with list:: $rankingList")
            applyFilters(rankingList)  // When the ranking list changes, apply filters and update the UI
        }

        // Set up the spinners
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

        // **Add search functionality**
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString().lowercase()  // Updates search query on text change
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }
        })

        // **Ensure new data added from JobPosting page is merged**
        // (You need to call this method from the JobPosting page when adding new data)
    }

    // **Method to handle new data addition in ViewModel**

    // Apply filters and update the adapter with the filtered list
    private fun applyFilters(rankingList: List<RankingData>) {
        val filteredList = rankingList.filter { ranking ->
            (selectedLocation == "Set Location:" || ranking.addr.contains(selectedLocation, ignoreCase = true)) &&
                    (selectedRank == "Set Rank:" || ranking.rank == selectedRank) &&
                    (searchQuery.isEmpty() || ranking.name.contains(searchQuery, ignoreCase = true) ||
                            ranking.title.contains(searchQuery, ignoreCase = true) ||
                            ranking.shortDesc.contains(searchQuery, ignoreCase = true))
        }.sortedByDescending { it.rating } // Sort by rating in descending order

        rankingAdapter.updateData(filteredList)

        // **Added item click listener to navigate to the details fragment**
        rankingAdapter.onItemClick = { rankingItem ->
            val bundle = Bundle().apply {
                putParcelable("rankingData", rankingItem)  // Passing the selected item to the details fragment
            }
            findNavController().navigate(R.id.action_homeFragment2_to_detailsFragment2, bundle)
        }
    }

    // **Helper function to reset UI**
    override fun onResume() {
        super.onResume()

        // Reset RecyclerView scroll position
        (view?.findViewById<RecyclerView>(R.id.explore_recyclerView))?.scrollToPosition(0)

        // Reset Spinner selections to default
        (view?.findViewById<Spinner>(R.id.locationSpinner))?.setSelection(0)
    }
}
