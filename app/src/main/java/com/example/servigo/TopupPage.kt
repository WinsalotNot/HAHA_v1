package com.example.servigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.models.TransactionResponse
import com.midtrans.sdk.corekit.models.snap.TransactionResult

class TopupPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_topup_page, container, false)

        val topup_confirm_button = view.findViewById<Button>(R.id.topup_confirm)
        val amount_input = view.findViewById<EditText>(R.id.amount)

        topup_confirm_button.setOnClickListener {
            val inputted = amount_input.text.toString()
            if (inputted.isEmpty()) {
                amount_input.error = "Please Enter an Amount"
            } else if (inputted.toDoubleOrNull() == null) {
                amount_input.error = "Please Enter A Number"
            } else {
                // The input is a valid number
                val amount_to_topup = inputted.toDouble()
                startPaymentFlow(amount_to_topup)
            }
        }
        return view
    }

    private fun startPaymentFlow(amount: Double) {
        // You should call your server to create a payment transaction first
        // For now, assume the backend has returned a valid transaction token

        val transactionToken = "your_token_from_backend" // Replace with actual token

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-QrtMd37smh-W08Ry")
            .setContext(requireContext())
            .setMerchantBaseUrl("https://views-crude.gl.at.ply.gg:59304/")
            .setTransactionFinishedCallback(object : TransactionFinishedCallback {
                override fun onTransactionFinished(result: TransactionResult?) {
                    // Handle the result
                    if (result != null) {
                        when (result.status) {
                            TransactionResult.STATUS_SUCCESS -> {
                                Toast.makeText(requireContext(), "Transaction Successful", Toast.LENGTH_SHORT).show()
                            }
                            TransactionResult.STATUS_PENDING -> {
                                Toast.makeText(requireContext(), "Transaction Pending", Toast.LENGTH_SHORT).show()
                            }
                            TransactionResult.STATUS_FAILED -> {
                                Toast.makeText(requireContext(), "Transaction Failed", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                Toast.makeText(requireContext(), "Unknown Transaction Status", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "No Transaction Result", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            .enableLog(true) // Optional: Enable logs for debugging
            .buildSDK() // This will open the Midtrans UI flow
    }
}
