package com.example.servigo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentList : ArrayList<PaymentData>
    private lateinit var paymentAdapter: PaymentAdapter

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
        return inflater.inflate(R.layout.fragment_payment_page, container, false)
    }

    // Set up the RecyclerView after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title") ?: "Something went wrong"
        val feeString = arguments?.getString("fee") ?: "0"
        val feeStringCleaned = feeString.replace(".", "")
        val feeInt = feeStringCleaned.toIntOrNull() ?: 0

        recyclerView = view.findViewById(R.id.payment_recyclerView)
        recyclerView.setHasFixedSize(true)

        // Set up LayoutManager (LinearLayoutManager for vertical scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context)

        paymentList = ArrayList()
        paymentList.add(
            PaymentData(
                item = title,
                desc = "Quantity 1 · 100.000 IDR /hour",
                amount = feeInt
            )
        )
        paymentList.add(
            PaymentData(
                item = "Transport",
                desc = "Quantity 2 · 4km",
                amount = 25000
            )
        )
        paymentList.add(
            PaymentData(
                item = "Admin Fee",
                desc = "Quantity 3 · 1%",
                amount = (feeInt * 0.01).toInt()
            )
        )

        // Set up the adapter
        paymentAdapter = PaymentAdapter(paymentList)
        recyclerView.adapter = paymentAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment paymentPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}