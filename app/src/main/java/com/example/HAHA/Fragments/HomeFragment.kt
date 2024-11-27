package com.example.HAHA

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.HAHA.ViewModel.WalletViewModel

class HomeFragment : Fragment() {
    private var drawerToggleListener: OnDrawerToggleListener? = null
    private lateinit var walletViewModel: WalletViewModel

    // Define an interface for communication with the activity
    interface OnDrawerToggleListener {
        fun onDrawerToggle()
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

    override fun onResume() {
        super.onResume()
        // Refresh data or call the ViewModel to fetch new data
        walletViewModel.fetchWalletAmount()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize ViewModel
        walletViewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)

        // Observe walletAmount
        walletViewModel.walletAmount.observe(viewLifecycleOwner, Observer { amount ->
            // Update UI when walletAmount changes
            view.findViewById<TextView>(R.id.walletAmountDisplay).text = walletViewModel.formatToIDR(amount)
        })

        // Observe loading state and show a progress bar or loading indicator
        walletViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

        // Find the ImageView and set its click listener to toggle the drawer
        val drawerTrigger: ImageView = view.findViewById(R.id.accountImageView)
        drawerTrigger.setOnClickListener {
            drawerToggleListener?.onDrawerToggle() // Call the interface method to toggle the drawer
        }

        val topup_button = view.findViewById<Button>(R.id.buttontopup)
        topup_button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment2_to_topupPage)
        }

        return view
    }

}