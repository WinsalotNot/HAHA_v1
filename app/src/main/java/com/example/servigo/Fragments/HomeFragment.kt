package com.example.servigo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var drawerToggleListener: OnDrawerToggleListener? = null

    // Define an interface for communication with the activity
    interface OnDrawerToggleListener {
        fun onDrawerToggle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ensure that the activity implements the interface
        try {
            drawerToggleListener = context as OnDrawerToggleListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDrawerToggleListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        drawerToggleListener = null // Clean up reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the ImageView and set its click listener to toggle the drawer
        val drawerTrigger: ImageView = view.findViewById(R.id.accountImageView)
        drawerTrigger.setOnClickListener {
            drawerToggleListener?.onDrawerToggle() // Call the interface method to toggle the drawer
        }

        return view
    }

}