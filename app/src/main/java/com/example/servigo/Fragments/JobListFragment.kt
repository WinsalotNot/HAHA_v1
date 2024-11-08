package com.example.servigo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JobListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobListFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_job_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView in the inflated view
        val recyclerView: RecyclerView = view.findViewById(R.id.job_list)

        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for RecyclerView (you can replace this with your dynamic data)
        val jobList = listOf(
            JobListData("Someone who would do chores", "You will work for 5 days a week", "01/01/2025-30/01/2025", "B", "Paid Per Week", "Rp.500.000,00", "3/5", "Jakarta Barat"),
            JobListData("Someone who would tutor", "You will work for 3 days a week", "01/01/2025-30/06/2025", "A", "Paid Per Month", "Rp.5.000.000,00", "4/5", "Jakarta Selatan"),
            JobListData("Someone who would tutor", "You will work for 3 days a week", "01/01/2025-30/06/2025", "A", "Paid Per Month", "Rp.5.000.000,00", "4/5", "Jakarta Selatan")
            // Add more items as needed
        )

        // Set up the adapter
        recyclerView.adapter = JobListAdapter(jobList)
    }
}