package com.example.servigo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PaymentAdapter(private val paymentList : List<PaymentData>) : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payitem: TextView = itemView.findViewById(R.id.payItem)
        val payDesc: TextView = itemView.findViewById(R.id.payDesc)
        val payAmount: TextView = itemView.findViewById(R.id.payAmount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_list_design, parent, false)
        return PaymentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val paymentItem = paymentList[position]
        holder.payitem.text = paymentItem.item
        holder.payDesc.text = paymentItem.desc
        holder.payAmount.text = paymentItem.amount
    }


}