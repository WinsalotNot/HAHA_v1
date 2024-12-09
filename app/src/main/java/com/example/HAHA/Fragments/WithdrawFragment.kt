package com.example.HAHA.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.PayoutData
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.RetrofitInstance
import com.example.HAHA.ViewModel.UIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class WithdrawFragment : Fragment() {

    private lateinit var inputAccField: EditText
    private lateinit var inputAmtField: EditText
    private lateinit var buttonBCA: ImageButton
    private lateinit var buttonBNI: ImageButton
    private lateinit var buttonBRI: ImageButton
    private lateinit var buttonGOPAY: ImageButton
    private lateinit var buttonMandiri: ImageButton
    private lateinit var buttonOVO: ImageButton
    private lateinit var confirmButton : Button
    private lateinit var inputAccNameField : EditText
    private lateinit var withdrawTries : TextView
    private lateinit var withdrawQuota : TextView
    private var selectedBank: String = ""
    private var amount: Double = 0.0
    private var accname: String = ""
    private var accountNumber: String = ""
    private val uiViewModel: UIViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        uiViewModel.fetchWithdrawQuotaAndTries()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_withdraw, container, false)
        (requireActivity() as MainActivity).hideLoading()

        inputAccField = view.findViewById(R.id.editAccountInput)
        inputAmtField = view.findViewById(R.id.editAmountWithdraw)

        // Inflate and reference the buttons after the dialog is inflated
        buttonBCA = view.findViewById(R.id.pickBCAWithdraw)
        buttonBNI = view.findViewById(R.id.pickBNIWithdraw)
        buttonBRI = view.findViewById(R.id.pickBRIWithdraw)
        buttonGOPAY = view.findViewById(R.id.pickGOPAYWithdraw)
        buttonMandiri = view.findViewById(R.id.pickMandiriWithdraw)
        buttonOVO = view.findViewById(R.id.pickOVOWithdraw)
        confirmButton = view.findViewById(R.id.confirmButtonWithdraw)
        selectedBank = ""
        inputAccNameField = view.findViewById(R.id.editAccountInputName)
        withdrawTries = view.findViewById(R.id.withdrawtries)
        withdrawQuota = view.findViewById(R.id.withdrawquota)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiViewModel.withdrawTries.observe(viewLifecycleOwner) {
            Log.d("WithdrawFragment", "Observing withdrawTries: $it")
            withdrawTries.text = it.toString()
        }

        uiViewModel.withdrawQuota.observe(viewLifecycleOwner) {
            Log.d("WithdrawFragment", "Observing withdrawQuota: $it")
            val formattedNumber = BigDecimal(it.toString()).setScale(2).toPlainString()
            withdrawQuota.text = formattedNumber
        }

        uiViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }

        val bankButtons = listOf(
            Pair(buttonBCA, "bca"),
            Pair(buttonBNI, "bni"),
            Pair(buttonBRI, "bri"),
            Pair(buttonGOPAY, "gopay"),
            Pair(buttonOVO, "ovo"),
            Pair(buttonMandiri, "mandiri")
        )

        // Set up button listeners after inflating the view
        bankButtons.forEach { (button, bank) ->
            button.setOnClickListener {
                selectedBank = bank
            }
        }

        confirmButton.setOnClickListener {
            amount = inputAmtField.text.toString().toDoubleOrNull() ?: 0.0
            accname = inputAccNameField.text.toString()
            accountNumber = inputAccField.text.toString()
            if (amount > 0 && selectedBank.isNotEmpty()) {
                // Show loading and make the transfer API call
                (requireContext() as MainActivity).showLoading()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        val userid = sharedPreferences.getInt("USER_ID_INT", 0)
                        val payoutData = PayoutData(userid, accname, accountNumber, amount, selectedBank)
                        val response = RetrofitInstance.apiService.payoutNow(payoutData)

                        withContext(Dispatchers.Main) {
                            (requireContext() as MainActivity).hideLoading()
                        }

                        if (response.isSuccessful && response.body()?.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    response.body()?.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_withdrawFragment_to_homeFragment2)
                            }
                        } else {
                            val errorMessage = response.body()?.message ?: "Payout Failed!"
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    errorMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            (requireContext() as MainActivity).hideLoading()
                        }
                    }
                }
            } else {
                Log.d("WithdrawFragment", "Invalid input or bank selection amount: ${amount} and bank: ${selectedBank}")
                Toast.makeText(
                    requireContext(),
                    "Invalid Input and/or Bank Selection",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


    }
}
