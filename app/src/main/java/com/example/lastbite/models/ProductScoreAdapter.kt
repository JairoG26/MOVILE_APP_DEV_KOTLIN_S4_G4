package com.example.lastbite.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.lastbite.R

class ProductScoreAdapter(context: Context, private val products: List<Product>) :
    ArrayAdapter<Product>(context, 0, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_product_score, parent, false)

        val nameView = view.findViewById<TextView>(R.id.product_name)
        val scoreView = view.findViewById<TextView>(R.id.product_score)

        val product = products[position]
        nameView.text = product.name
        scoreView.text = product.score.toString()

        return view
    }
}