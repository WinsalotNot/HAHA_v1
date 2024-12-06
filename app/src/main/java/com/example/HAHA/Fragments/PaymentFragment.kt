package com.example.HAHA.Fragments

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.HAHA.Adapter.PaymentAdapter
import com.example.HAHA.Data.PaymentData
import com.example.HAHA.Data.PurchaseData
import com.example.HAHA.Data.TransferData
import com.example.HAHA.MainActivity
import com.example.HAHA.R
import com.example.HAHA.RetrofitInstance
import com.example.HAHA.ViewModel.UIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

class PaymentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentList : ArrayList<PaymentData>
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var title: String
    private lateinit var feeString: String
    private lateinit var summaryTextView: TextView
    private lateinit var totalAmountTextView: TextView
    private lateinit var calendarButton: Button
    private lateinit var walletamount: TextView
    private lateinit var topupbutton2 : Button
    private lateinit var paymentConfirmedButton : Button
    private var dateSelected: Boolean = false // Flag to track if a date is selected

    private val uiViewModel: UIViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        uiViewModel.fetchWalletAmount()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment_page, container, false)

        title = arguments?.getString("title") ?: "Something went wrong"
        feeString = arguments?.getString("fee") ?: "0"

        summaryTextView = view.findViewById(R.id.Psummary)
        totalAmountTextView = view.findViewById(R.id.PtotalAmount)
        calendarButton = view.findViewById(R.id.Pcalendarbtn)
        walletamount = view.findViewById(R.id.walletAmount_Payment)
        topupbutton2 = view.findViewById(R.id.topup_button_2)
        paymentConfirmedButton = view.findViewById(R.id.payment_confirmed)

        // Observe walletAmount
        uiViewModel.walletAmount.observe(viewLifecycleOwner) { amount ->
            // Update UI when walletAmount changes
            walletamount.text = uiViewModel.formatToIDR(amount)
        }

        uiViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        }
        return view
    }

    // Set up the RecyclerView after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentConfirmedButton.isEnabled = false
        paymentConfirmedButton.alpha = 0.5f

        val feeInt = feeString.replace(".", "").toIntOrNull() ?: 0
        var numDays: Int
        var paymentFee : Double = 0.0
        var adminFee : Double = 0.0
        // Initially, set the TextView to ask for the date
        summaryTextView.text = "Please select booking date first"
        totalAmountTextView.text = "IDR 0"

        recyclerView = view.findViewById(R.id.payment_recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Set the listener to open the date picker
        calendarButton.setOnClickListener {
            showDatePickerDialog(feeInt, title, calendarButton) { startDate, endDate ->
                numDays = calculateNumDays(startDate, endDate)
                Log.d("DateRange", "Selected range: $startDate - $endDate, Number of days: $numDays")
                calendarButton.text = "$startDate - $endDate"

                // Update the Summary
                summaryTextView.text = "Summary"
                totalAmountTextView.text = "IDR ${NumberFormat.getInstance(Locale("id", "ID")).format((feeInt * numDays * 0.01).toInt() + (numDays * feeInt))}"

                paymentFee = (numDays * feeInt).toDouble()
                adminFee = (feeInt * numDays * 0.01)

                // Update the payment list
                val paymentList = arrayListOf(
                    PaymentData(
                        item = title,
                        desc = "Quantity 1 · $feeInt IDR /Day for $numDays day(s)",
                        amount = paymentFee
                    ),
                    PaymentData(
                        item = "Admin Fee",
                        desc = "Quantity 2 · 1% admin fee",
                        amount = adminFee
                    )
                )

                // Set the RecyclerView adapter to display the payment list
                val paymentAdapter = PaymentAdapter(paymentList)
                recyclerView.adapter = paymentAdapter

                // Enable the payment confirmation button
                dateSelected = true
                paymentConfirmedButton.isEnabled = true
                paymentConfirmedButton.alpha = 1.0f
            }
        }

        topupbutton2.setOnClickListener{
            findNavController().navigate(R.id.action_paymentPage_to_topupPage)
        }

        paymentConfirmedButton.setOnClickListener {
            if (!dateSelected) {
                Toast.makeText(requireContext(), "Please select a booking date first.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sharedPreferences =
                requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("USER_ID_INT", 0)
            val creatorId = arguments?.getInt("creatorId") ?: 0

            // Transfer data object
            val transferData = TransferData(
                transferAmount = paymentFee,
                adminFee = adminFee,
                senderId = userId,
                receiverId = creatorId,
            )

            val purchaseData = PurchaseData (
                creatorid = creatorId,
                purchaserid = userId
            )

            // Show loading and make the transfer API call
            (requireActivity() as MainActivity).showLoading()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val responseHire = RetrofitInstance.apiService.purchaseService(purchaseData)
                    if (responseHire.isSuccessful && responseHire.body()?.success == true) {
                        Log.d("Purchase Service", responseHire.message())
                        val response = RetrofitInstance.apiService.transfer(transferData)
                        withContext(Dispatchers.Main) {
                            (requireActivity() as MainActivity).hideLoading()
                        }

                        if (response.isSuccessful && response.body()?.success == true) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), "Payment successful!", Toast.LENGTH_LONG).show()
                            }
                            findNavController().navigate(R.id.action_paymentPage_to_homeFragment2)
                        } else {
                            val errorMessage = response.body()?.message ?: "Payment failed!"
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Purchase Failed!", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        (requireActivity() as MainActivity).hideLoading()
                    }
                }
            }
        }


    }

    // Function to show the start and end date pickers
    private fun showDatePickerDialog(feeInt: Int, title: String, calendarButton: Button, onDateSelected: (String, String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        Toast.makeText(requireContext(), "Select Start Date", Toast.LENGTH_SHORT).show()

        val startDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val startDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                showEndDatePickerDialog(selectedYear, selectedMonth, selectedDay, startDate, onDateSelected)
            },
            year, month, dayOfMonth
        )

        startDatePickerDialog.datePicker.minDate = calendar.timeInMillis
        startDatePickerDialog.show()
    }

    // Function to show the end date picker and pass both dates back
    private fun showEndDatePickerDialog(year: Int, month: Int, dayOfMonth: Int, startDate: String, onDateSelected: (String, String) -> Unit) {
        val calendar = Calendar.getInstance()
        val startCalendar = Calendar.getInstance()
        startCalendar.set(year, month, dayOfMonth)

        Toast.makeText(requireContext(), "Select End Date", Toast.LENGTH_SHORT).show()

        val endDatePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val endDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                onDateSelected(startDate, endDate)  // Return both start and end date
            },
            year, month, dayOfMonth
        )

        endDatePickerDialog.datePicker.minDate = startCalendar.timeInMillis
        endDatePickerDialog.setTitle("Select End Date")
        endDatePickerDialog.show()
    }

    // Function to calculate the number of days between start and end dates
    private fun calculateNumDays(startDate: String, endDate: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val start = dateFormat.parse(startDate)
        val end = dateFormat.parse(endDate)

        val diff = end.time - start.time
        val diffDays = diff / (24 * 60 * 60 * 1000)  // Convert milliseconds to days
        return (diffDays + 1).toInt()  // Return 1 day if same date
    }
}