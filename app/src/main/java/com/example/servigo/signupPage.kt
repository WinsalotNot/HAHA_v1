package com.example.servigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.servigo.Data.User
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [signupPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class signupPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var apiService: ApiService

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
        val view = inflater.inflate(R.layout.fragment_signup_page, container, false)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-server-url.com/") // Replace with your server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnSignup = view.findViewById<Button>(R.id.btnSignup)

        btnSignup.setOnClickListener {
            val password = etPassword.text.toString()
            val checkPassword = view.findViewById<EditText>(R.id.etRePassword).text.toString()
            val email = etEmail.text.toString()

            // Validate input
            if (password.isNotBlank() && email.isNotBlank() && password == checkPassword) {
                registerUser(password, email)
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun registerUser(password: String, email: String) {
        val user = User(password, email)

        // Use a coroutine to make the network request
        lifecycleScope.launch {
            try {
                val response = apiService.registerUser(user)
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(requireContext(), "Signup successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response.body()?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment signupPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            signupPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}