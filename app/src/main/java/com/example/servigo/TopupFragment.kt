package com.example.servigo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.midtrans.sdk.uikit.external.UiKitApi
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.servigo.RetrofitInstance.apiService
import com.example.servigo.Data.ApiResponse
import com.example.servigo.Data.PaymentRequest
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import retrofit2.Response

class TopupFragment : Fragment() {

    private val CLIENT_KEY = BuildConfig.CLIENT_KEY  // Replace with your Midtrans client key
    private val BASE_URL = BuildConfig.BASE_URL// Replace with your backend base URL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topup_page, container, false)

        val topupConfirmButton = view.findViewById<Button>(R.id.topup_confirm)
        val amountInput = view.findViewById<EditText>(R.id.amount)

        topupConfirmButton.setOnClickListener {
            val inputted = amountInput.text.toString()
            if (inputted.isEmpty()) {
                amountInput.error = "Please Enter an Amount"
            } else if (inputted.toDoubleOrNull() == null) {
                amountInput.error = "Please Enter A Number"
            } else {
                val amountToTopup = inputted.toDouble()

                // Retrieve userId from SharedPreferences
                val sharedPreferences =
                    requireContext().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                    // Initiate payment request
                    initiatePayment(userId.toInt(), amountToTopup)
                } else {
                    Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun initiatePayment(userId: Int, amount: Double) {
        lifecycleScope.launch {
            try {
                // Prepare request payload
                val paymentRequest = PaymentRequest(userId, amount)
                // Make backend API call to create a transaction
                val response: Response<ApiResponse> = apiService.createTransaction(paymentRequest)

                if (response.isSuccessful) {
                    val paymentToken = response.body()?.token
                    if (paymentToken != null) {
                        startPaymentFlow(paymentToken, amount)
                    } else {
                        Toast.makeText(requireContext(), "Failed to get payment token", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Payment request failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startPaymentFlow(paymentToken: String, amount: Double) {
        // Initialize Midtrans UiKitApi
        val uiKitApi = UiKitApi.Builder()
            .withMerchantClientKey(CLIENT_KEY)
            .withContext(requireContext())
            .withMerchantUrl(BASE_URL)
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
    }
}
