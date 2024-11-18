package com.example.servigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TopupPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class TopupPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_topup_page, container, false)

        val topup_confirm_button = view.findViewById<Button>(R.id.topup_confirm)
        val amount_input = view.findViewById<EditText>(R.id.amount)

        val inputted = amount_input.toString()
        topup_confirm_button.setOnClickListener{
            if (inputted.isEmpty()) {
                amount_input.error = "Please Enter an Amount"
            } else if (inputted.toDoubleOrNull() == null) {
                amount_input.error = "Please Enter A Number"
            }else {
                // The input is a valid number
                val amount_to_topup = inputted.toDouble()
//                startPaymentFlow()
            }
        }
        return view
    }

//    private fun startPaymentFlow() {
//        SdkUIFlowBuilder.init()
//            .setClientKey("SB-Mid-client-QrtMd37smh-W08Ry") // Your client key from Midtrans dashboard
//            .setContext(requireContext()) // Context of the fragment or activity
//            .setMerchantBaseUrl("https://YOUR_MERCHANT_BASE_URL/") // Your server base URL
//            .setTransactionFinishedCallback { result ->
//                // Handle transaction result here, such as success or failure
//                if (result.isTransactionSuccessful) {
//                    // Handle successful transaction (e.g., show success message, update UI)
//                    Toast.makeText(requireContext(), "Transaction Successful", Toast.LENGTH_SHORT).show()
//                } else {
//                    // Handle failed transaction (e.g., show error message, update UI)
//                    Toast.makeText(requireContext(), "Transaction Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .enableLog(true) // Optional: Enable logs for debugging
//            .buildSDK() // This will open the Midtrans UI flow
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TopupPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TopupPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}