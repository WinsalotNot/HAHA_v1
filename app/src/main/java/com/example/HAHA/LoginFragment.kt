package com.example.HAHA

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.ApiResponse
import com.example.HAHA.Data.ChangePassReq
import com.example.HAHA.Data.User
import com.example.HAHA.RetrofitInstance.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signupTextClick: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private var isSharedInit : Boolean = false
    private lateinit var forgotPass : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_page, container, false)
        (requireActivity() as MainActivity).hideLoading()

        findNavController().popBackStack(R.id.loginPage, false)
        // Initialize views
        emailEditText = view.findViewById(R.id.emailInput)
        passwordEditText = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        signupTextClick = view.findViewById(R.id.signupclick)
        forgotPass = view.findViewById(R.id.forgorPass)

        // Set up login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call the login function
            loginUser(email, password)
        }

        forgotPass.setOnClickListener{
            showResetPasswordDialog()
        }
        signupTextClick.setOnClickListener {
            findNavController().navigate(R.id.action_loginPage_to_signupPage)
        }

        return view
    }

    private fun showResetPasswordDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Reset Password")

        // Set up the email input
        val emailInput = EditText(requireContext())
        emailInput.hint = "Enter your email"
        emailInput.setPadding(16, 16, 16, 16)  // Add padding to the EditText

        // Set up the new password input
        val passwordInput = EditText(requireContext())
        passwordInput.hint = "Enter your new password"
        passwordInput.setPadding(16, 16, 16, 16)  // Add padding to the EditText

        // Set up the requirements TextView
        val requirementsText = TextView(requireContext())
        requirementsText.text = getString(R.string.passReq)
        requirementsText.visibility = View.VISIBLE
        requirementsText.setPadding(16, 16, 16, 16)  // Add padding to the TextView

        // Create a layout to hold both inputs
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)  // Add padding to the layout for spacing
        layout.addView(emailInput)
        layout.addView(passwordInput)
        layout.addView(requirementsText)

        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton("Reset") { dialog, _ ->
            val email = emailInput.text.toString().trim()
            val newPassword = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && newPassword.isNotEmpty()) {
                if (isPasswordValid(newPassword)) {
                    resetPassword(email, newPassword)
                } else {
                    Toast.makeText(requireContext(), "Please Enter Proper Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        // Show the dialog
        builder.show()
    }


    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,16}$"
        return password.matches(passwordRegex.toRegex())
    }

    // Function to save the user ID
    private fun saveUser(userid: Int, username: String, address: String) {
        isSharedInit = true
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", userid.toString()) // Save userId
        editor.putInt("USER_ID_INT", userid)
        editor.putString("USER_NAME", username) // Save u
        editor.putString("USER_ADDRESS", address) // Save userId
        editor.apply()
    }

    private fun resetPassword(email: String, newPass : String) {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                (requireActivity() as MainActivity).showLoading()
            }
            Log.e("Paassword Reset", "Resetting Password For Email: $email")
            try {
                val changePassReq = ChangePassReq(email, newPass)
                val response: Response<ApiResponse> = apiService.resetPassword(changePassReq)

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null && responseBody.success) {
                        withContext(Dispatchers.Main) {
                            // Display success message
                            Toast.makeText(
                                requireContext(),
                                responseBody.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            (requireActivity() as MainActivity).hideLoading()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            // Handle login failure
                            Toast.makeText(
                                requireContext(),
                                responseBody?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            (requireActivity() as MainActivity).hideLoading()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle login failure
                        Toast.makeText(
                            requireContext(),
                            "Error, Failed to Get Response",
                            Toast.LENGTH_SHORT
                        ).show()
                        (requireActivity() as MainActivity).hideLoading()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    (requireActivity() as MainActivity).hideLoading()
                    Toast.makeText(
                        requireContext(),
                        "Failed Trying to Reset Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        // Create a User object with email and password
        val user = User(email, password, null.toString(), null.toString())

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    (requireActivity() as MainActivity).showLoading()
                }
                // Call the loginUser API method
                val response: Response<ApiResponse> = apiService.loginUser(user)

                // Check if the response is successful
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        Log.d("APIResponse", "User ID: ${responseBody.userId}, Name: ${responseBody.name}, Address: ${responseBody.address}, Rank: ${responseBody.token}")
                    }
                    if (responseBody != null && responseBody.success) {
                        // Login successful
                        val userId = responseBody.userId // Assuming the user ID is part of the response
                        val username = responseBody.name
                        val address = responseBody.address

                        // Save the user ID in SharedPreferences
                        saveUser(userId, username, address)

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
                            (requireActivity() as MainActivity).hideLoading()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Handle unsuccessful response (non-2xx status code)
                        Toast.makeText(requireContext(), "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        (requireActivity() as MainActivity).hideLoading()
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                    (requireActivity() as MainActivity).hideLoading()
                }
                Log.e("LoginError", "An error occurred: ${e.message}", e)
            }
        }
    }

}