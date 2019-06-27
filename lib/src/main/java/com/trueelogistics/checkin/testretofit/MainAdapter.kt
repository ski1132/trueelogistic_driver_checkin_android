package com.trueelogistics.checkin.testretofit

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.R
import kotlinx.android.synthetic.main.item_retrofit.view.*

class MainAdapter(val items: ArrayList<PersonModel>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(view: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = view as ViewHolder
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_retrofit, viewGroup, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val nameText = view.stockBt
            nameText.text = items[position].firstName
        }

    }
}