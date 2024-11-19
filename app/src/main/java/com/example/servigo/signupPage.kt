package com.example.servigo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.servigo.Data.ApiResponse
import com.example.servigo.Data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class signupPage : Fragment() {

    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_page, container, false)

        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnSignup = view.findViewById<Button>(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val password = etPassword.text.toString()
            val checkPassword = view.findViewById<EditText>(R.id.etRePassword).text.toString()
            val email = etEmail.text.toString()

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Valid email address
                // Validate input
                if (isPasswordValid(password)) {
                    if (password.isNotBlank() && password == checkPassword) {
                        registerUser(email, password)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Please Make Sure Passwords Are Filled AND Matched!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Password Requirements NOT FULFILLED", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Invalid email address
                Toast.makeText(requireContext(), "Please Input Email ONLY!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,16}$"
        return password.matches(passwordRegex.toRegex())
    }

    private fun registerUser(email: String, password: String) {
        val user = User(email, password)


        // Launch a coroutine to make the API call
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.apiService.registerUser(user)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "User registered successfully!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupPage_to_loginPage)
                    } else {
                        Toast.makeText(context, "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginError", "An error occurred: ${e.message}", e)
                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}