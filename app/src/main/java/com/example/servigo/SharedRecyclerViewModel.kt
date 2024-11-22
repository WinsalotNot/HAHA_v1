package com.example.servigo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedRecyclerViewModel : ViewModel() {
    private val _rankingList = MutableLiveData<List<RankingData>>()
    val rankingList: LiveData<List<RankingData>> get() = _rankingList

    init {
        // Initialize the ranking data once
        _rankingList.value = listOf(
            RankingData(
                name = "Bob Marley",
                title = "Professional House Keeper",
                description = "Expert in keeping homes spotless and organized with 10+ years of experience.",
                rank = "A",
                rating = 4.8f,
                review = 120,
                addr = "123 Clean St, Neat City",
                fee = 500000,
                img = "baby",
                cat = "Housekeeping",
                shortDesc = "Experienced housekeeper ensuring cleanliness and order."
            ),
            RankingData(
                name = "Alice Johnson",
                title = "Certified Babysitter",
                description = "Caring babysitter with certifications in child care and safety.",
                rank = "S",
                rating = 4.9f,
                review = 250,
                addr = "45 Safe Rd, Kidstown",
                fee = 300000,
                img = "booby",
                cat = "Babysitting",
                shortDesc = "Trusted babysitter with a focus on child safety and fun."
            ),
            RankingData(
                name = "John Doe",
                title = "Handyman Extraordinaire",
                description = "Skilled handyman specializing in repairs, installations, and maintenance tasks.",
                rank = "S",
                rating = 4.5f,
                review = 85,
                addr = "78 Fixit Lane, Repairville",
                fee = 400000,
                img = "damn",
                cat = "Repairs",
                shortDesc = "Reliable handyman for all your home repair needs."
            )
        )
    }
}
