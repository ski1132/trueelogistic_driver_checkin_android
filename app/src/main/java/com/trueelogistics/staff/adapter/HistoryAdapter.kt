package com.trueelogistics.staff.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.enums.CheckInTELType
import com.trueelogistics.staff.R
import com.trueelogistics.staff.enum.EnumHistory
import com.trueelogistics.staff.model.BaseHistoryModel
import com.trueelogistics.staff.model.DateModel
import com.trueelogistics.staff.model.HistoryModel
import kotlinx.android.synthetic.main.item_history_child.view.*
import kotlinx.android.synthetic.main.item_history_parent.view.*

class HistoryAdapter(val list: ArrayList<BaseHistoryModel> = arrayListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (list[position].type == EnumHistory.DATE.value) {
            if (holder is DateHolder && item is DateModel) {
                holder.binding(item)
            }
        } else {
            if (holder is HistoryHolder && item is HistoryModel) {
                holder.binding(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EnumHistory.DATE.value) {
            DateHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_history_parent,
                    parent,
                    false
                )
            )
        } else {
            HistoryHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_history_child,
                    parent,
                    false
                )
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(dateModel: DateModel) {
            val dateView = itemView.date
            dateView.text = dateModel.date
        }
    }

    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binding(historyModel: HistoryModel) {
            val typeCheck = itemView.typeCheckIn
            val hubName = itemView.hubCheckIn
            val timeCheck = itemView.timeCheckIn
            val typeScan = itemView.type_scan
            val iconType = itemView.iconTypeCheckIn

            typeCheck.text = historyModel.eventType
            hubName.text = historyModel.hubName
            timeCheck.text = historyModel.timeCheckIn
            typeScan.text = historyModel.checkInType
            when(historyModel.eventType){
                CheckInTELType.CheckIn.value -> {
                    typeCheck.text = itemView.context.getString(R.string.full_checkin_text)
                    iconType.setImageResource(R.drawable.ic_check_in)
                }
                CheckInTELType.CheckBetween.value -> {
                    typeCheck.text = itemView.context.getString(R.string.full_check_between_text)
                    iconType.setImageResource(R.drawable.ic_check_between)
                }
                CheckInTELType.CheckOut.value -> {
                    typeCheck.text = itemView.context.getString(R.string.full_checkout_text)
                    iconType.setImageResource(R.drawable.ic_check_out)
                }
                else -> {
                    historyModel.eventType
                    iconType.setImageResource(R.drawable.ic_checkin_gray)
                }
            }
        }
    }
}