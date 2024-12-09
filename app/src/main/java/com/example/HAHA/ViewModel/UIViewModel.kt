package com.example.HAHA.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.HAHA.Data.AvailabilityResponse
import com.example.HAHA.Data.WithdrawResponse
import com.example.HAHA.Data.XpResponse
import com.example.HAHA.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class UIViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to observe the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val walletAmount = MutableLiveData<Double>()
    val availability = MutableLiveData<String>()
    val withdrawQuota = MutableLiveData<Double>()
    val withdrawTries = MutableLiveData<Int>()
    val userRank = MutableLiveData<String>()
    val userXp = MutableLiveData<Int>()

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

    // Fetch wallet amount from the server
    fun fetchWalletAmount() {
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call API to get wallet amount
                    val response: Response<Double> = RetrofitInstance.apiService.getWalletAmount(userId.toInt())

                    if (response.isSuccessful) {
                        walletAmount.postValue(response.body())
                        Log.e("UIViewModel", "Wallet successfully fetched: ${response.body()}")
                    } else {
                        errorMessage.postValue("Failed to fetch wallet amount")
                    }
                } else {
                    errorMessage.postValue("User ID does not exist!")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Fetch availability from the server
    fun fetchAvailability() {
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call API to get availability
                    val response: Response<AvailabilityResponse> = RetrofitInstance.apiService.getAvailability(userId.toInt())

                    if (response.isSuccessful) {
                        val availabilityString = response.body()?.availability ?: "Unknown"
                        availability.postValue(availabilityString)
                        Log.e("UIViewModel", "Availability successfully fetched: $availabilityString")
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
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchWithdrawQuotaAndTries() {
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call API to get availability
                    val response: Response<WithdrawResponse> = RetrofitInstance.apiService.getWithdrawData(userId.toInt())

                    if (response.isSuccessful) {
                        val withQuota = response.body()?.withdrawQuota ?: 0.0
                        val withTries = response.body()?.withdrawTries ?: 0
                        withdrawQuota.postValue(withQuota)
                        withdrawTries.postValue(withTries)
                        Log.e("UIViewModel", "Withdraw Data successfully fetched: $withQuota and $withTries")
                    } else {
                        errorMessage.postValue("Failed to fetch withdraw data")
                        Log.e("UIViewModel", "Response Not Successful for getWithdrawData()")
                    }
                } else {
                    errorMessage.postValue("User ID does not exist!")
                    Log.e("UIViewModel", "UserID NULL for getWithdrawData()")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.localizedMessage}")
                Log.e("UIViewModel", "Error for getWithdrawData(): ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun fetchXpAndRank() {
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Call API to get availability
                    val response: Response<XpResponse> = RetrofitInstance.apiService.getXp(userId.toInt())

                    if (response.isSuccessful) {
                        val xp = response.body()?.xp ?: 0
                        val rank = response.body()?.rank ?: "U"
                        userXp.postValue(xp)
                        userRank.postValue(rank)
                        Log.e("UIViewModel", "Xp and Rank successfully fetched: $xp and $rank")
                    } else {
                        errorMessage.postValue("Failed to fetch Xp and Rank")
                        Log.e("UIViewModel", "Response Not Successful for getXp()")
                    }
                } else {
                    errorMessage.postValue("User ID does not exist!")
                    Log.e("UIViewModel", "UserID NULL for getXp()")
                }
            } catch (e: Exception) {
                errorMessage.postValue("Error: ${e.localizedMessage}")
                Log.e("UIViewModel", "Error for getXp(): ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Get wallet amount (if already fetched)
    fun getWalletAmount(): Double {
        return walletAmount.value ?: 0.0
    }

    // Get availability (if already fetched)
    fun getAvailability(): String {
        return availability.value ?: "Unknown"
    }
}
