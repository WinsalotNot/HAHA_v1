package com.example.servigo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedRecyclerViewModel : ViewModel() {
    private val _rankingList = MutableLiveData<List<RankingData>>()
    val rankingList: LiveData<List<RankingData>> get() = _rankingList

    init {
        _rankingList.value = listOf(
            // Existing Entries
            RankingData(
                name = "Dewi Lestari",
                title = "Electrical Expert",
                description = "Specialist in electrical installation and troubleshooting with over 8 years of experience.",
                rank = "S",
                rating = 4.9f,
                review = 140,
                addr = "Jl. Sudirman, Jakarta Selatan",
                fee = 600000,
                img = "boo",
                cat = "Electrical",
                shortDesc = "Experienced and certified electrical technician."
            ),
            RankingData(
                name = "Ahmad Santoso",
                title = "Plumbing Specialist",
                description = "Experienced plumber handling all types of water system repairs and installations.",
                rank = "A",
                rating = 4.7f,
                review = 110,
                addr = "Jl. Mangga Dua, Jakarta Barat",
                fee = 450000,
                img = "boo",
                cat = "Plumbing",
                shortDesc = "Reliable plumber with expertise in water systems."
            )

        )
    }

    fun addRankingData(newData: RankingData) {
        val currentList = _rankingList.value?.toMutableList() ?: mutableListOf()
        currentList.add(newData)  // Add new data to the list
        _rankingList.postValue(currentList) // This will notify the observer
        Log.d("SharedRecyclerViewModel", "ranking list on SharedRecyclerViewModel: ${_rankingList.value}")
    }





}
