package com.example.HAHA

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream

class jobPosting : Fragment() {
    private var selectedImageUri: Uri? = null
    private lateinit var sharedRecyclerViewModel: SharedRecyclerViewModel
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ActivityResultLauncher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                Toast.makeText(requireContext(), "Image Selected!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_posting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        sharedRecyclerViewModel =
            ViewModelProvider(requireActivity()).get(SharedRecyclerViewModel::class.java)

        val enteredAddr: EditText = view.findViewById(R.id.editAddr)
        val enteredService: EditText = view.findViewById(R.id.editService)
        val enteredShortDesc: EditText = view.findViewById(R.id.editShortDesc)
        val enteredDesc: EditText = view.findViewById(R.id.editDesc)
        val enteredFee: EditText = view.findViewById(R.id.editFee)
        val postJobbtn: Button = view.findViewById(R.id.postJobbtn)
        val useAccountAddressCheckbox: CheckBox = view.findViewById(R.id.useAccAddress)
        val imageUpload: ImageView = view.findViewById(R.id.uploadPic)

        val sharedPreferences =
            requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USER_NAME", null)
        val userid: Int = sharedPreferences.getString("USER_ID", null)?.toInt() ?: 0
        var accAddress: String? = null.toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val creatorId = username


        useAccountAddressCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enteredAddr.isEnabled = false
                enteredAddr.alpha = 0.5f
                accAddress = sharedPreferences.getString("USER_ADDRESS", null)
            } else {
                enteredAddr.isEnabled = true
                enteredAddr.alpha = 1.0f
            }
        }

        imageUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            imagePickerLauncher.launch(intent)
        }

        postJobbtn.setOnClickListener {
            val name = username ?: "unknown"
            val address = accAddress ?: enteredAddr.text.toString()
            val service = enteredService.text.toString()
            val shortDesc = enteredShortDesc.text.toString()
            val desc = enteredDesc.text.toString()
            val fee = enteredFee.text.toString().toFloatOrNull()
            val category = categorySpinner.selectedItem.toString()

            if (name.isEmpty() || address.isEmpty() || service.isEmpty() ||
                shortDesc.isEmpty() || desc.isEmpty() || fee == null || category == "Set Category:"
            ) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val imageBytes = getImageBytes(selectedImageUri!!)

            if (imageBytes != null) {
                val requestFile =
                    imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0)
                val imagePart =
                    MultipartBody.Part.createFormData("file", "upload_image.jpg", requestFile)

                // Now we pass all form fields and the image individually
                CoroutineScope(Dispatchers.IO).launch {
                    val response = RetrofitInstance.apiService.createPost(
                        file = imagePart,
                        name = name,
                        title = service,
                        description = desc,
                        rank = "A",
                        rating = 0.0f,
                        review = 0,
                        addr = address,
                        fee = fee,
                        cat = category,
                        shortDesc = shortDesc,
                        creatorid = userid
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            if (response.body()?.success == true) {
                                Toast.makeText(context, "Job Posted Successfully!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "User Posted Already", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(context, "Error posting job", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Failed to get image bytes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Convert image URI to byte array
    private fun getImageBytes(uri: Uri): ByteArray? {
        var inputStream: InputStream? = null
        return try {
            inputStream = requireContext().contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            inputStream?.close()
        }
    }
}


