package com.example.servigo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var rankingAdapter: RankingAdapter

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        sharedRecyclerViewModel = ViewModelProvider(requireActivity())[SharedRecyclerViewModel::class.java]

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.leaderboard_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set up the adapter
        rankingAdapter = RankingAdapter(emptyList())
        recyclerView.adapter = rankingAdapter

        // Observe the data
        sharedRecyclerViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            rankingAdapter.updateData(rankingList)
        }

//        // Find the RecyclerView in the inflated view
//        recyclerView = view.findViewById(R.id.leaderboard_recyclerView)
//
//        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        // Sample data for RecyclerView (you can replace this with your dynamic data)
//        rankingList = ArrayList()
//
//        rankingList.add(
//            RankingData(
//                name = "Bob Marley",
//                title = "Professional House Keeper",
//                description = "Expert in keeping homes spotless and organized with 10+ years of experience.",
//                rank = "A",
//                rating = 4.8f, // Example Float value
//                review = 120, // Example Int value
//                addr = "123 Clean St, Neat City",
//                fee = 500000,
//                img = "baby", // Placeholder for now
//                cat = "Housekeeping",
//                shortDesc = "Experienced housekeeper ensuring cleanliness and order."
//            )
//        )
//
//        rankingList.add(
//            RankingData(
//                name = "Alice Johnson",
//                title = "Certified Babysitter",
//                description = "Caring babysitter with certifications in child care and safety.",
//                rank = "S",
//                rating = 4.9f,
//                review = 250,
//                addr = "45 Safe Rd, Kidstown",
//                fee = 300000,
//                img = "booby",
//                cat = "Babysitting",
//                shortDesc = "Trusted babysitter with a focus on child safety and fun."
//            )
//        )
//
//        rankingList.add(
//            RankingData(
//                name = "John Doe",
//                title = "Handyman Extraordinaire",
//                description = "Skilled handyman specializing in repairs, installations, and maintenance tasks.",
//                rank = "S",
//                rating = 4.5f,
//                review = 85,
//                addr = "78 Fixit Lane, Repairville",
//                fee = 400000,
//                img = "damn",
//                cat = "Repairs",
//                shortDesc = "Reliable handyman for all your home repair needs."
//            )
//        )
//        // Set up the adapter
//        recyclerView.adapter = RankingAdapter(rankingList)
//

        // Set up the categorySpinner
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        categorySpinner.visibility = View.VISIBLE

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // Set up the rankSpinner
        val rankSpinner: Spinner = view.findViewById(R.id.rankSpinner)
        rankSpinner.visibility = View.VISIBLE

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ranks,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rankSpinner.adapter = adapter
        }
    }
}
