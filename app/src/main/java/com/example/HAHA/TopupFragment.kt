package com.example.HAHA

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.HAHA.RetrofitInstance.apiService
import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.PaymentRequest
import com.example.HAHA.ViewModel.WalletViewModel
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class TopupFragment : Fragment() {

    private val CLIENT_KEY = BuildConfig.CLIENT_KEY
    private val BASE_URL = BuildConfig.BASE_URL
    private lateinit var walletViewModel: WalletViewModel

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                handleTransactionResult(transactionResult)
            }
        } else {
            Toast.makeText(requireContext(), "Payment canceled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topup_page, container, false)

        val topupConfirmButton = view.findViewById<Button>(R.id.topup_confirm)
        val amountInput = view.findViewById<EditText>(R.id.amount)

        // Initialize ViewModel
        walletViewModel = ViewModelProvider(requireActivity()).get(WalletViewModel::class.java)

        // Observe walletAmount
        walletViewModel.walletAmount.observe(viewLifecycleOwner, Observer { amount ->
            // Update UI when walletAmount changes
            view.findViewById<TextView>(R.id.walletAmountDisplay_topup).text = walletViewModel.formatToIDR(amount)
        })

        topupConfirmButton.setOnClickListener {
            val inputted = amountInput.text.toString()
            if (inputted.isEmpty()) {
                amountInput.error = "Please Enter an Amount"
            } else if (inputted.toDoubleOrNull() == null) {
                amountInput.error = "Please Enter A Number"
            } else {
                val amountToTopup = inputted.toDouble()

                val sharedPreferences =
                    requireContext().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
                val userId = sharedPreferences.getString("USER_ID", null)

                if (userId != null) {
                  initiatePayment(userId.toInt(), amountToTopup)
                } else {
                  Toast.makeText(requireContext(), "User not logged in!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Refresh data or call the ViewModel to fetch new data
        walletViewModel.fetchWalletAmount()
    }

    private fun initiatePayment(userId: Int, amount: Double) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val paymentRequest = PaymentRequest(userId, amount)
                val response: Response<ApiResponse> = apiService.createTransaction(paymentRequest)

                if (response.isSuccessful) {
                    val paymentToken = response.body()?.token
                    if (paymentToken != null) {
                        startPaymentFlow(paymentToken)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Failed to get payment token", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Payment request failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startPaymentFlow(paymentToken: String) {
        val uiKitApi = UiKitApi.Builder()
            .withMerchantClientKey(CLIENT_KEY)
            .withContext(requireContext())
            .withMerchantUrl(BASE_URL)
            .enableLog(true)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()

        uiKitApi.startPaymentUiFlow(
            this.requireActivity(),
            launcher,
            paymentToken
        )
    }

    private fun handleTransactionResult(transactionResult: com.midtrans.sdk.uikit.api.model.TransactionResult?) {
        transactionResult?.let {
            when (it.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    Toast.makeText(requireContext(), "Transaction successful! ID: ${it.transactionId}", Toast.LENGTH_LONG).show()
                }
                TransactionResult.STATUS_PENDING -> {
                    Toast.makeText(requireContext(), "Transaction pending. ID: ${it.transactionId}", Toast.LENGTH_LONG).show()
                }
                TransactionResult.STATUS_FAILED -> {
                    Toast.makeText(requireContext(), "Transaction failed. ID: ${it.transactionId}", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Transaction canceled or invalid.", Toast.LENGTH_LONG).show()
                }
            }
        } ?: run {
            Toast.makeText(requireContext(), "Transaction result is invalid", Toast.LENGTH_SHORT).show()
        }
    }
}
