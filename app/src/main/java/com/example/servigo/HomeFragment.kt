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
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var navController: NavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout1)

        // Ensure fragmentContainerView contains a NavHostFragment
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView7) as? NavHostFragment
        navController = navHostFragment?.navController ?: return  // Safely get NavController

        // Add tabs to TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Service"))
        tabLayout.addTab(tabLayout.newTab().setText("Job"))

        // Retrieve the saved tab position (if any)
        val savedTabIndex = savedInstanceState?.getInt("selectedTabIndex", 0) ?: 0
        tabLayout.getTabAt(savedTabIndex)?.select()

        // Set up TabLayout with NavController
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        if (navController.currentDestination?.id != R.id.serviceListFragment) {
                            navController.navigate(R.id.serviceListFragment)
                        }
                    }
                    1 -> {
                        if (navController.currentDestination?.id != R.id.jobListFragment) {
                            navController.navigate(R.id.jobListFragment)
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        // Listen for navigation changes to update TabLayout selection
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.serviceListFragment -> tabLayout.selectTab(tabLayout.getTabAt(0))
                R.id.jobListFragment -> tabLayout.selectTab(tabLayout.getTabAt(1))
                // Add any other fragments here if necessary
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected tab index
        outState.putInt("selectedTabIndex", tabLayout.selectedTabPosition)
    }

}
