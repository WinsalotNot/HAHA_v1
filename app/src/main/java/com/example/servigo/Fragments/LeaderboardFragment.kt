package com.example.servigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Find the RecyclerView in the inflated view
        val recyclerView: RecyclerView = view.findViewById(R.id.leaderboard_recyclerView)

        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for RecyclerView (you can replace this with your dynamic data)
        val rankingList = listOf(
            RankingData("Hasanudin Alibama", "Professional House Keeper", "A hard working house keeper that will ensure the cleanliness of the house. You can come home at ease!", "S", "4.5/5", "(1500 reviews)"),
            RankingData("Jane Doe", "Professional Electrician", "If you're looking for fast work, don't ask for me, but if you're looking for the best, safest, and cleanest fixes out there. Call me!", "A", "2.5/5", "(450 reviews)")
            // Add more items as needed
        )

        // Set up the adapter
        recyclerView.adapter = RankingAdapter(rankingList)
    }
}