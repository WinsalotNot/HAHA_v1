package com.example.HAHA

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HAHA.Data.PostingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedRecyclerViewModel : ViewModel() {

    // LiveData to observe the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _rankingList = MutableLiveData<List<PostingData>>()
    val rankingList: LiveData<List<PostingData>> get() = _rankingList
    val dataUpdated = MutableLiveData<Boolean>(false)  // Notify observers when data is updated

    // A list to hold unfiltered data for re-filtering when needed
    private var unfilteredRankingList: List<PostingData> = emptyList()

    // Load initial data (this could be a network call or database fetch)
    fun loadData() {
        _isLoading.postValue(true)
        // Start a coroutine to load data asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the network call to fetch the posts
                val response = RetrofitInstance.apiService.getAllPosts()
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!

                    withContext(Dispatchers.Main) {
                        // Update the LiveData with the fetched posts
                        unfilteredRankingList = posts // Store the decoded posts in the unfiltered list
                        _rankingList.value = posts // Update the LiveData
                    }
                    Log.d("SharedRecyclerViewModel", "Posts fetched successfully")
                } else {
                    Log.e("SharedRecyclerViewModel", "Failed to load posts: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SharedRecyclerViewModel", "Error fetching posts: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
