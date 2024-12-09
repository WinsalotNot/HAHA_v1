package com.example.HAHA.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.ViewModel.UIViewModel


class HomeFragment : Fragment() {
    private var drawerToggleListener: OnDrawerToggleListener? = null
    private val uiViewModel: UIViewModel by activityViewModels()
    private lateinit var accNameField: TextView

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
        // Fetch wallet amount when fragment resumes
        uiViewModel.fetchWalletAmount()
        // Fetch availability when fragment resumes
        uiViewModel.fetchAvailability()
        uiViewModel.fetchXpAndRank()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (requireActivity() as MainActivity).hideLoading()

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        accNameField = view.findViewById(R.id.accountNameField)

        // Retrieve username from SharedPreferences
        val username = sharedPreferences.getString("USER_NAME", null)

        // Log the username value to check if it's being fetched properly
        Log.e("HomeFragment", "Username fetched from SharedPreferences: $username")

        // Set the username to the TextView
        if (username != null) {
            accNameField.text = username
            Log.e("HomeFragment", "Account Name Field updated with username: $username")
        } else {
            Log.e("HomeFragment", "Username is null, not updating Account Name Field")
        }

        var currentRank: String
        uiViewModel.userXp.observe(viewLifecycleOwner) { xp ->
            // Observe userRank inside the userXp observer
            uiViewModel.userRank.observe(viewLifecycleOwner) { rank ->
                // Update the UI with the rank
                view.findViewById<TextView>(R.id.textView4).text = rank
                currentRank = rank
                Log.e("HomeFragment", "Observed rank: $rank")

                // Proceed with your progress bar logic after ensuring userRank is updated
                val progressBar = view.findViewById<ProgressBar>(R.id.xpBar)
                val nextRankXP = getNextRankXP(currentRank)
                val currentRankXP = getCurrentRankXP(currentRank)

                // Log to check XP and Rank Values
                Log.d("ProgressBar", "XP: $xp, Current Rank XP: $currentRankXP, Next Rank XP: $nextRankXP")

                // Ensure the progress bar is 0% if xp is 0
                if (xp == 0) {
                    progressBar.progress = 0
                } else {
                    // Ensure denominator is not zero
                    val denominator = nextRankXP - currentRankXP
                    val progressPercentage = if (denominator != 0) {
                        ((xp - currentRankXP) * 100) / denominator
                    } else {
                        100 // Default to max progress if no next rank
                    }

                    // Set progress bar
                    progressBar.progress = progressPercentage

                    // If progress reaches 100%, handle rank-up
                    if (progressPercentage >= 100) {
                        // Log rank-up and update rank
                        Log.d("ProgressBar", "Rank Up! New Rank: $currentRank")
                    }

                    Log.d("ProgressBar", "XP: $xp, Current Rank: $currentRank, Progress: $progressPercentage%")
                }
            }
        }

        // Observe walletAmount in the ViewModel and update the UI when it changes
        uiViewModel.walletAmount.observe(viewLifecycleOwner) { amount ->
            Log.e("HomeFragment", "Observed amount: $amount")
            // Ensure that we have a non-null value before accessing the UI
            view.findViewById<TextView>(R.id.walletAmountDisplay).text =
                uiViewModel.formatToIDR(amount)
        }

        // Observe availability in the ViewModel and update the UI when it changes
        uiViewModel.availability.observe(viewLifecycleOwner) { avail ->
            view.findViewById<TextView>(R.id.availabilityText).text = avail
            Log.e("HomeFragment", "Observed availability: $avail")
            val availabilityIconView = view.findViewById<ImageView>(R.id.availabilityIcon)
            when (avail.lowercase()) {
                "not available" -> availabilityIconView.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
                "available" -> availabilityIconView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#00FF00"))
                "working" -> availabilityIconView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF0000"))
                else -> availabilityIconView.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            }
        }

        uiViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

        // Set up the ImageView to toggle the drawer
        val drawerTrigger: ImageView = view.findViewById(R.id.accountImageView)
        drawerTrigger.setOnClickListener {
            drawerToggleListener?.onDrawerToggle()
        }

        // Handle the top-up button click
        val topupButton = view.findViewById<Button>(R.id.buttontopup)
        topupButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_topupPage)
        }

        return view
    }

    // Get the XP threshold for the current rank
    fun getCurrentRankXP(rank: String): Int {
        return when (rank) {
            "E" -> 0
            "D" -> 100
            "C" -> 500
            "B" -> 1000
            "A" -> 5000
            "S" -> 10000
            else -> 0
        }
    }

    // Get the XP threshold for the next rank
    private fun getNextRankXP(rank: String): Int {
        return when (rank) {
            "E" -> 100
            "D" -> 500
            "C" -> 1000
            "B" -> 5000
            "A" -> 10000
            "S" -> Int.MAX_VALUE // No next rank after S
            else -> 0
        }
    }
}
