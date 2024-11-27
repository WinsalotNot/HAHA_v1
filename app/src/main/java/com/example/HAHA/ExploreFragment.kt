package com.example.HAHA

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]
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

        // Observe the data
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            applyFilters(rankingList)
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


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString().lowercase()
                sharedRecyclerViewModel.rankingList.value?.let { applyFilters(it) }
            }
        })

    }

    private fun applyFilters(rankingList: List<RankingData>) {
        val filteredList = rankingList.filter { ranking ->
            (selectedLocation == "Set Location:" || ranking.addr.contains(selectedLocation, ignoreCase = true)) &&
                    (selectedRank == "Set Rank:" || ranking.rank == selectedRank) &&
                    (searchQuery.isEmpty() || ranking.name.contains(searchQuery, ignoreCase = true) ||
                            ranking.title.contains(searchQuery, ignoreCase = true) ||
                            ranking.shortDesc.contains(searchQuery, ignoreCase = true))
        }.sortedByDescending { it.rating } // Sort by rating in descending order

        rankingAdapter.updateData(filteredList)

        rankingAdapter.updateData(filteredList)

        // Set onItemClick listener
        rankingAdapter.onItemClick = { rankingItem ->
            val bundle = Bundle().apply {
                putParcelable("rankingData", rankingItem)
            }
            findNavController().navigate(R.id.action_homeFragment2_to_detailsFragment2, bundle)
        }
    }


    private fun Spinner.setOnItemSelectedListener(listener: () -> Unit) {
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                listener()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }
    override fun onResume() {
        super.onResume()
        // Reset any UI elements or data that need to be refreshed when returning to this fragment
        (view?.findViewById<RecyclerView>(R.id.explore_recyclerView))?.scrollToPosition(0)
        (view?.findViewById<Spinner>(R.id.locationSpinner))?.setSelection(0)
        // Add more resets as necessary
    }
}
