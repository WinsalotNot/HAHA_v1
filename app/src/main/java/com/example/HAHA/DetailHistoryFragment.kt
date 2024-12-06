package com.example.HAHA

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.PostingData
import java.text.NumberFormat
import java.util.Locale
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailHistoryFragment : Fragment() {

    private lateinit var backButton: ImageButton
    private lateinit var finishButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_history, container, false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postingData = arguments?.getParcelable<PostingData>("rankingData")
        backButton = view.findViewById(R.id.backButtDetailHistory)
        finishButton = view.findViewById(R.id.finishButton)

        val userId = postingData?.creatorid ?: 0

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        finishButton.setOnClickListener {
            // Show loading and make the transfer API call
            (requireActivity() as MainActivity).showLoading()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitInstance.apiService.transferConfirm(userId = userId)
                    if (response.isSuccessful && response.body()?.success == true) {
                        Log.d("Transfer Confirm", response.message())
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                            (requireActivity() as MainActivity).hideLoading()
                        }
                        findNavController().navigate(R.id.action_detailHistoryFragment_to_homeFragment2)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Transfer Failed!", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        (requireActivity() as MainActivity).hideLoading()
                    }
                }
            }
        }

        if (postingData != null) {
            Log.d("DetailsFragment", "rankingData: $postingData")

            val name: TextView = view.findViewById(R.id.DHname)
            val title: TextView = view.findViewById(R.id.DHtitle)
            val description: TextView = view.findViewById(R.id.DHdesc)
            val rank: TextView = view.findViewById(R.id.DHrank)
            val rating: TextView = view.findViewById(R.id.DHrating)
            val review: TextView = view.findViewById(R.id.DHreviews)
            val addr: TextView = view.findViewById(R.id.DHaddr)
            val fee: TextView = view.findViewById(R.id.DHfee)
            val totalcost : TextView = view.findViewById(R.id.DHtotalcost)
            val imageView: ImageView = view.findViewById(R.id.DHProfilePic)


            name.text = postingData.username
            title.text = postingData.title
            description.text = postingData.description
            rank.text = postingData.rank
            rating.text = postingData.rating.toString()
            review.text = postingData.review.toString()
            addr.text = postingData.addr
            fee.text = NumberFormat.getNumberInstance(Locale("in", "ID")).format(postingData.fee)
            totalcost.text = NumberFormat.getNumberInstance(Locale("in", "ID")).format(postingData.purchasedfor)

            if (!postingData.img.isNullOrEmpty()) {
                try {
                    Glide.with(this)
                        .asBitmap()
                        .load(Base64.decode(postingData.img, Base64.DEFAULT))
                        .into(imageView)
                } catch (e: IllegalArgumentException) {
                    Log.e("DetailHistoryFragment", "Error decoding Base64 image", e)
                }
            } else {
                Log.d("DetailHistoryFragment", "No image data available")
            }
        } else {
            Log.e("DetailsFragment", "No rankingData provided in arguments")
        }
    }
}
