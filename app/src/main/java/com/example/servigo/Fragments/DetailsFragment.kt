package com.example.servigo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation

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

        // Retrieve arguments passed via the navigation graph
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")
        val date = arguments?.getString("date")
        val minStar = arguments?.getString("min_star")
        val requestedRank = arguments?.getString("requested_rank")
        val payment = arguments?.getString("payment")
        val howMuch = arguments?.getString("how_much")
        val address = arguments?.getString("address")

        // Set data to views in the fragment layout
        view.findViewById<TextView>(R.id.textView6).text = title
        view.findViewById<TextView>(R.id.aboutdesc).text = description
        view.findViewById<TextView>(R.id.textView9).text = "$minStar ($requestedRank)"
        view.findViewById<TextView>(R.id.textView5).text = payment
        view.findViewById<TextView>(R.id.textView10).text = howMuch
        view.findViewById<TextView>(R.id.textView7).text = address
    }
}