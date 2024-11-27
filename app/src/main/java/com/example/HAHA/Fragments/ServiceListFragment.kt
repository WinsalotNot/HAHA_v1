package com.example.HAHA.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.HistoryAdapter
import com.example.HAHA.Data.HistoryData
import com.example.HAHA.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ServiceListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView in the inflated view
        val recyclerView: RecyclerView = view.findViewById(R.id.service_list)

        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for RecyclerView (you can replace this with your dynamic data)
        val historyList = listOf(
            HistoryData("Hasanudin Alibama", "Professional House Keeper", "Job Description: Clean House", "17/12/24", "Rp.502.500,00", "5/5", "5/5", "S"),
            HistoryData("Jane Doe", "Professional Electrician", "Job Description: Safe Wires", "17/11/24", "Rp.600.500,00", "4.5/5", "3/5", "A")
            // Add more items as needed
        )

        // Set up the adapter
        recyclerView.adapter = HistoryAdapter(historyList)
    }
}