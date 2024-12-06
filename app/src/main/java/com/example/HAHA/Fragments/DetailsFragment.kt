package com.example.HAHA.Fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.HAHA.Data.PostingData
import com.example.HAHA.R
import java.text.NumberFormat
import java.util.Locale
import com.bumptech.glide.Glide

class DetailsFragment : Fragment() {

    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookbtn = view.findViewById<Button>(R.id.Dbookbtn)
        val chatbtn = view.findViewById<Button>(R.id.Dchatbtn)
        val postingData = arguments?.getParcelable<PostingData>("rankingData")
        backButton = view.findViewById(R.id.backButtDetail)

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        if (postingData != null) {

            Log.d("DetailsFragment", "rankingData: $postingData")

            val name: TextView = view.findViewById(R.id.Dname)
            val title: TextView = view.findViewById(R.id.Dtitle)
            val description: TextView = view.findViewById(R.id.Ddesc)
            val rank: TextView = view.findViewById(R.id.Drank)
            val rating: TextView = view.findViewById(R.id.Drating)
            val review: TextView = view.findViewById(R.id.Dreviews)
            val addr: TextView = view.findViewById(R.id.Daddr)
            val fee: TextView = view.findViewById(R.id.Dfee)
            val imageView: ImageView = view.findViewById(R.id.DProfilePic) // Assuming this is your ImageView

            // Set the values from postingData
            name.text = postingData.username
            title.text = postingData.title
            description.text = postingData.description
            rank.text = postingData.rank
            rating.text = postingData.rating.toString()  // Convert Float to String
            review.text = postingData.review.toString()  // Convert Int to String
            addr.text = postingData.addr
            fee.text = NumberFormat.getNumberInstance(Locale("in", "ID")).format(postingData.fee)

            // Decode the base64-encoded image string to a Bitmap
            val decodedBytes = Base64.decode(postingData.img, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Load the Bitmap into the ImageView using Glide
            Glide.with(this)
                .load(bitmap)
                .into(imageView)

            chatbtn.setOnClickListener {
                // Pass the text content of the TextViews to the bundle
                val bundle = Bundle().apply {
                    putInt("creatorId", postingData.creatorid)
                    putString("name", (view.findViewById<TextView>(R.id.Dname).text.toString()).trim('"'))
                }

                // Navigate to the PaymentFragment
                findNavController().navigate(R.id.action_detailsFragment_to_chatFragment, bundle)
            }

            bookbtn.setOnClickListener {
                // Pass the text content of the TextViews to the bundle
                val bundle = Bundle().apply {
                    putInt("creatorId", postingData.creatorid)
                    putString("title", view.findViewById<TextView>(R.id.Dtitle).text.toString())
                    putString("name", view.findViewById<TextView>(R.id.Dname).text.toString())
                    putString("fee", view.findViewById<TextView>(R.id.Dfee).text.toString())
                }

                // Navigate to the PaymentFragment
                findNavController().navigate(R.id.action_detailsFragment_to_paymentPage, bundle)
            }
        }
    }
}
