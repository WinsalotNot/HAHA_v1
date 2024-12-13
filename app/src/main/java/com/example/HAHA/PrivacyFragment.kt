package com.example.HAHA

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.PostingData
import com.example.HAHA.Data.RegisterData
import com.example.HAHA.Data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PrivacyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrivacyFragment : Fragment() {
    private lateinit var agree : Button
    private lateinit var agreeCheck : CheckBox

    private val email : String
        get() = arguments?.getString("email") ?: ""
    private val password : String
        get() = arguments?.getString("password") ?: ""
    private val name : String
        get() = arguments?.getString("name") ?: ""
    private val address : String
        get() = arguments?.getString("address") ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_privacy, container, false)
        agree = view.findViewById(R.id.agreeButton)
        agreeCheck = view.findViewById(R.id.checkBoxAgree)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hideCheckboxAndButton = arguments?.getBoolean("hideCheckboxAndButton", false) ?: false

        if (hideCheckboxAndButton) {
            agreeCheck.visibility = View.GONE
            agree.visibility = View.GONE
        }

        val register = arguments?.getParcelable<RegisterData>("registerData")

        val email = register?.email ?: ""
        val password = register?.password ?: ""
        val name = register?.name ?: ""
        val address = register?.address ?: ""

        agree.setOnClickListener {
            if (agreeCheck.isChecked) {
                // Call your Retrofit-based registerUser method
                registerUser(email, password, name, address)
            } else {
                Toast.makeText(context, "Please agree to the terms and conditions!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String, name: String, address: String) {
        val user = User(email, password, name, address)

        // Launch a coroutine to make the API call
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.apiService.registerUser(user)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_privacyFragment_to_loginPage)
                    } else {
                        Toast.makeText(context, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("RegistrationError", "An error occurred: ${e.message}", e)
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}