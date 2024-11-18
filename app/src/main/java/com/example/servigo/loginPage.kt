package com.example.servigo

import android.os.Bundle
import android.telecom.Call
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
import com.example.servigo.Data.ApiResponse
import com.example.servigo.Data.User
import com.example.servigo.RetrofitInstance.apiService
import kotlinx.coroutines.launch
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [loginPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class loginPage : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signup_text_click: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)

        // Initialize views
        emailEditText = view.findViewById(R.id.emailInput)
        passwordEditText = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        signup_text_click = view.findViewById(R.id.signupclick)

        // Set up login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call the login function
            loginUser(email, password)
        }

        signup_text_click.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_signupPage)
        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        // Create a User object with email and password
        val user = User(email, password)

        // Launch coroutine to call the login API
        lifecycleScope.launch {
            try {
                // Call the loginUser API method
                val response: Response<ApiResponse> = apiService.loginUser(user)

                // Check if the response is successful
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.success) {
                        // Login successful
                        Toast.makeText(requireContext(), "Login Successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to HomeFragment on success
                        findNavController().navigate(R.id.action_loginPage_to_homeFragment2)
                    } else {
                        // Handle login failure
                        Toast.makeText(requireContext(), "Login Failed: ${responseBody?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle unsuccessful response (non-2xx status code)
                    Toast.makeText(requireContext(), "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle exception
                Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("LoginError", "An error occurred: ${e.message}", e)
            }
        }
    }
}