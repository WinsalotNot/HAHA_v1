package com.example.HAHA.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.HAHA.R
import com.example.HAHA.Data.RankingData
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookbtn = view.findViewById<Button>(R.id.Dbookbtn)
        val chatbtn = view.findViewById<Button>(R.id.Dchatbtn)
        val rankingData = arguments?.getParcelable<RankingData>("rankingData")

        if (rankingData != null){

            val name: TextView = view.findViewById(R.id.Dname)
            val title: TextView = view.findViewById(R.id.Dtitle)
            val description: TextView = view.findViewById(R.id.Ddesc)
            val rank: TextView = view.findViewById(R.id.Drank)
            val rating: TextView = view.findViewById(R.id.Drating)
            val review: TextView = view.findViewById(R.id.Dreviews)
            val addr: TextView = view.findViewById(R.id.Daddr)
            val fee: TextView = view.findViewById(R.id.Dfee)


            // Set the values from rankingData
            name.text = rankingData.name
            title.text = rankingData.title
            description.text = rankingData.description
            rank.text = rankingData.rank
            rating.text = rankingData.rating.toString()  // Convert Float to String
            review.text = rankingData.review.toString()  // Convert Int to String
            addr.text = rankingData.addr
            fee.text = NumberFormat.getNumberInstance(Locale("in", "ID")).format(rankingData.fee)

            chatbtn.setOnClickListener {
                // Pass the text content of the TextViews to the bundle
                val bundle = Bundle().apply {
                    putString("creatorId", rankingData.creatorId)
                    putString("name", view.findViewById<TextView>(R.id.Dname).text.toString())
                }

                // Navigate to the PaymentFragment
                findNavController().navigate(R.id.action_detailsFragment_to_chatFragment, bundle)
            }

        }

        bookbtn.setOnClickListener {
            // Pass the text content of the TextViews to the bundle
            val bundle = Bundle().apply {
                putString("title", view.findViewById<TextView>(R.id.Dtitle).text.toString())
                putString("name", view.findViewById<TextView>(R.id.Dname).text.toString())
                putString("fee", view.findViewById<TextView>(R.id.Dfee).text.toString())
            }

            // Navigate to the PaymentFragment
            findNavController().navigate(R.id.action_detailsFragment_to_paymentPage, bundle)
        }

    }
}