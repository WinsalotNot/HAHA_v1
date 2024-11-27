package com.example.HAHA

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.User
import com.example.HAHA.RetrofitInstance.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupTextClick: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)

        // Initialize views
        emailEditText = view.findViewById(R.id.emailInput)
        passwordEditText = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        signupTextClick = view.findViewById(R.id.signupclick)

        // Set up login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call the login function
            loginUser(email, password)
        }

        signupTextClick.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_signupPage)
        }

        return view
    }

    // Function to save the user ID
    private fun saveUserId(userid: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", userid.toString()) // Save userId
        editor.apply()
    }

    private fun loginUser(email: String, password: String) {
        // Create a User object with email and password
        val user = User(email, password)

        lifecycleScope.launch {
            try {
                // Call the loginUser API method
                val response: Response<ApiResponse> = apiService.loginUser(user)

                // Check if the response is successful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        // Login successful
                        val userId = responseBody.userId // Assuming the user ID is part of the response

                        // Save the user ID in SharedPreferences
                        saveUserId(userId)

                        withContext(Dispatchers.Main) {
                            // Display success message
                            Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()

                            // Navigate to HomeFragment on success
                            findNavController().navigate(R.id.action_loginPage_to_homeFragment2)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            // Handle login failure
                            Toast.makeText(requireContext(), "Login Failed: ${responseBody?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle unsuccessful response (non-2xx status code)
                        Toast.makeText(requireContext(), "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("LoginError", "An error occurred: ${e.message}", e)
            }
        }
    }

}