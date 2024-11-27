package com.example.HAHA.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.HAHA.Data.AvailabilityResponse
import com.example.HAHA.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class UIViewModel(application: Application) : AndroidViewModel(application) {

    val walletAmount = MutableLiveData<Double>()
    val availability = MutableLiveData<String>()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String>()
    val sharedPreferences = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Function to format wallet amount to IDR
    fun formatToIDR(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        return formatter.format(amount)
    }

    // Function to set the wallet amount
    fun setWalletAmount(amount: Double) {
        walletAmount.value = amount
    }

    // Function to get the wallet amount from the server
    fun fetchWalletAmount() {

        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call the API to get the wallet amount
                    val response: Response<Double> = RetrofitInstance.apiService.getWalletAmount(userId.toInt())

                    if (response.isSuccessful) {
                        walletAmount.postValue(response.body())
                        Log.e("UIViewModel", "Wallet successfully get: ${response.body()}")
                    } else {
                        errorMessage.postValue("Failed to fetch wallet amount")
                    }
                } else {
                    errorMessage.postValue("User ID does not exist!")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.localizedMessage}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    // Function to get the wallet amount from the server
    fun fetchAvailability() {

        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call the API to get the wallet amount
                    val response: Response<AvailabilityResponse> = RetrofitInstance.apiService.getAvailability(userId.toInt())

                    if (response.isSuccessful) {
                        val availabilityString = response.body()?.availability ?: "Unknown"
                        availability.postValue(availabilityString)
                        Log.e("UIViewModel", "Availability successfully retrieved: $availabilityString")
                    } else {
                        errorMessage.postValue("Failed to fetch availability")
                        Log.e("UIViewModel", "Response Not Successful for getAvailability()")
                    }
                } else {
                    errorMessage.postValue("User ID does not exist!")
                    Log.e("UIViewModel", "UserID NULL for getAvailability()")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.localizedMessage}")
                Log.e("UIViewModel", "Error for getAvailability(): ${e.localizedMessage}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    // Function to get wallet amount (if already fetched)
    fun getWalletAmount(): Double {
        return walletAmount.value ?: 0.0
    }

    // Function to get availability (if already fetched)
    fun getAvailability(): String {
        return availability.value ?: "Unknown"
    }
}
