package com.example.HAHA

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.HAHA.Data.PostingData
import com.example.HAHA.Data.RankResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedRecyclerViewModel : ViewModel() {

    // LiveData to observe the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _rankingList = MutableLiveData<List<RankResponse>>()
    val rankingList: LiveData<List<RankResponse>> get() = _rankingList

    private val _exploreList = MutableLiveData<List<PostingData>>()
    val exploreList: LiveData<List<PostingData>> get() = _exploreList

    private val _historyList = MutableLiveData<List<PostingData>>()
    val historyList: LiveData<List<PostingData>> get() = _historyList

    // Load initial data (this could be a network call or database fetch)
    fun loadData() {
        _isLoading.postValue(true)
        // Start a coroutine to load data asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the network call to fetch the posts
                val response = RetrofitInstance.apiService.getAllAvailablePosts()
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!

                    withContext(Dispatchers.Main) {
                        // Update the LiveData with the fetched posts
                        _exploreList.value = posts // Update the LiveData
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

    // Load initial data (this could be a network call or database fetch)
    fun loadDataHistory(userid: Int) {
        _isLoading.postValue(true)
        // Start a coroutine to load data asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the network call to fetch the posts
                val response = RetrofitInstance.apiService.getAllBoughtPosts(userid)
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!

                    withContext(Dispatchers.Main) {
                        // Update the LiveData with the fetched posts
                        _historyList.value = posts // Update the LiveData
                    }
                    Log.d("SharedRecyclerViewModel", "Transaction fetched successfully")
                } else {
                    Log.e("SharedRecyclerViewModel", "Failed to load transactions: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SharedRecyclerViewModel", "Error fetching transactions: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Load initial data (this could be a network call or database fetch)
    fun loadRanking() {
        _isLoading.postValue(true)
        // Start a coroutine to load data asynchronously
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Make the network call to fetch the posts
                val response = RetrofitInstance.apiService.getAllUserByRank()
                // Check if the response is successful
                if (response.isSuccessful && response.body() != null) {
                    val posts = response.body()!!

                    withContext(Dispatchers.Main) {
                        // Update the LiveData with the fetched posts
                        _rankingList.value = posts // Update the LiveData
                    }
                    Log.d("SharedRecyclerViewModel", "Ranks fetched successfully")
                } else {
                    Log.e("SharedRecyclerViewModel", "Failed to load rank: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("SharedRecyclerViewModel", "Error fetching rank: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
