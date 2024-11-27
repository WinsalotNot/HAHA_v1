package com.example.HAHA

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.HAHA.Data.RankingData
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [jobPosting.newInstance] factory method to
 * create an instance of this fragment.
 */
class jobPosting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel

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
        return inflater.inflate(R.layout.fragment_job_posting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // set up spinner
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        sharedRecyclerViewModel = ViewModelProvider(requireActivity()).get(SharedRecyclerViewModel::class.java)

        val enteredAddr: EditText = view.findViewById(R.id.editAddr)
        val enteredService: EditText = view.findViewById(R.id.editService)
        val enteredShortDesc: EditText = view.findViewById(R.id.editShortDesc)
        val enteredDesc: EditText = view.findViewById(R.id.editDesc)
        val enteredFee: EditText = view.findViewById(R.id.editFee)
        val postJobbtn: Button = view.findViewById(R.id.postJobbtn)
        val useAccountAddressCheckbox: CheckBox = view.findViewById(R.id.useAccAddress)

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", null)
        var accAddress : String? = null.toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val creatorId = currentUser?.uid ?: "ifNullThenNotLoggedIn"


        useAccountAddressCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Disable and gray out the EditText
                enteredAddr.setEnabled(false)
                enteredAddr.setAlpha(0.5f) // Optional, makes it visually grayed out
                accAddress = sharedPreferences.getString("USER_ADDRESS", null)
            } else {
                // Enable the EditText
                enteredAddr.setEnabled(true)
                enteredAddr.setAlpha(1.0f)
            }
        }

        postJobbtn.setOnClickListener {
            // Get data from the fields
            val name : String = if (username != null) {
                username
            } else {
                "unknown"
            }
            val address : String = if (accAddress != null) {
                accAddress as String
            } else {
                enteredAddr.text.toString()
            }
            val service = enteredService.text.toString()
            val shortDesc = enteredShortDesc.text.toString()
            val desc = enteredDesc.text.toString()
            val fee = enteredFee.text.toString().toIntOrNull()
            val category = categorySpinner.selectedItem.toString()

            // Validate the fields (ensure no empty data)
            if (name.isEmpty() || address.isEmpty() || service.isEmpty() ||
                shortDesc.isEmpty() || desc.isEmpty() || fee == null || category == "Set Category:") {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newJobPosting = RankingData(
                name = name,
                title = service,
                description = desc,
                rank = "E",
                rating = 0.0f,
                review = 0,
                addr = address,
                fee = fee,
                img = "boo",
                cat = category,
                shortDesc = shortDesc,
                creatorId = creatorId
            )

            sharedRecyclerViewModel.addRankingData(newJobPosting)

            // Proceed with the job posting logic (e.g., save data to ViewModel or database)
            // For now, we can just display a message
            Toast.makeText(requireContext(), "Job Posted Successfully!", Toast.LENGTH_SHORT).show()
        }
    }


}