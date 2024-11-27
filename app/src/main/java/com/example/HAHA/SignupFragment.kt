package com.example.HAHA

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
import com.example.HAHA.Data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SignupFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var firebaseAuth: FirebaseAuth

    // The 5-minute timeout value (in milliseconds)
    private val timeoutDuration = TimeUnit.MINUTES.toMillis(5)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_page, container, false)

        // Initialize FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnSignup = view.findViewById<Button>(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val rePassword = view.findViewById<EditText>(R.id.etRePassword).text.toString().trim()

            // Validate input
            if (email.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Invalid email format!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != rePassword) {
                Toast.makeText(requireContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isPasswordValid(password)) {
                Toast.makeText(
                    requireContext(),
                    "Password must be 8-16 characters, include at least one uppercase letter and one number.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Create Firebase user and send email verification
            verifyEmailAndRegister(email, password)
        }

        return view
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,16}$"
        return password.matches(passwordRegex.toRegex())
    }

    private fun verifyEmailAndRegister(email: String, password: String) {
        // Initialize FirebaseAuth
        val tempAuth = FirebaseAuth.getInstance()

        // Create a temporary user to send the email verification link
        tempAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { createTask ->
                if (createTask.isSuccessful) {
                    // Send email verification
                    val tempUser = tempAuth.currentUser
                    tempUser?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Verification link sent to $email. Please verify before proceeding.",
                                Toast.LENGTH_LONG
                            ).show()

                            // Wait for user to verify the email with a timeout logic
                            checkEmailVerification(tempAuth, email, password)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Failed to send verification email: ${emailTask.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Clean up temporary account
                            tempUser.delete()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to create a temporary account: ${createTask.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun checkEmailVerification(tempAuth: FirebaseAuth, email: String, password: String) {
        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()

            while (true) {
                // Reload user state to check verification status
                val tempUser = tempAuth.currentUser
                tempUser?.reload()

                if (tempUser?.isEmailVerified == true) {
                    Toast.makeText(
                        requireContext(),
                        "Email verified! Proceeding with registration.",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Call your Retrofit-based registerUser method
                    registerUser(email, password)

                    // Clean up temporary Firebase account
                    tempUser.delete()
                    break
                }

                // Check if the timeout has elapsed
                if (System.currentTimeMillis() - startTime > timeoutDuration) {
                    Toast.makeText(
                        requireContext(),
                        "Verification timed out. Please request a new link.",
                        Toast.LENGTH_LONG
                    ).show()

                    // Clean up temporary Firebase account
                    tempUser?.delete()
                    break
                }

                // Pause briefly before checking again
                delay(2000)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        val user = User(email, password)

        // Launch a coroutine to make the API call
        lifecycleScope.launch(Dispatchers.IO) {
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
                Log.e("RegistrationError", "An error occurred: ${e.message}", e)
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
