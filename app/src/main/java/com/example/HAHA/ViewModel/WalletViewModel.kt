package com.example.HAHA.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.HAHA.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    val walletAmount = MutableLiveData<Double>()
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String>()

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
                val sharedPreferences = getApplication<Application>().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call the API to get the wallet amount
                    val response: Response<Double> = RetrofitInstance.apiService.getWalletAmount(userId.toInt())

                    if (response.isSuccessful) {
                        walletAmount.postValue(response.body())
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

    // Function to get wallet amount (if already fetched)
    fun getWalletAmount(): Double {
        return walletAmount.value ?: 0.0
    }
}
