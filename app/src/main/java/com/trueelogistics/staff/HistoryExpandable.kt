package com.trueelogistics.staff

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.trueelogistics.checkin.extensions.formatISO
import com.trueelogistics.staff.model.ExpandableDataModel
import kotlinx.android.synthetic.main.item_history_child.view.*
import kotlinx.android.synthetic.main.item_history_parent.view.*

class HistoryExpandable (private val context: Context ,private val list : ArrayList<ExpandableDataModel>) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): String {
        return ""
    }

    override fun isChildSelectable(p0: Int, p1: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded : Boolean, view : View?, parent: ViewGroup?): View? {
        var convertView = view
        if (convertView == null) {
            val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.item_history_parent, null)
        }
        convertView?.date?.text = list[groupPosition].date
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return list[groupPosition].history.size
    }

    override fun getChild(groupPosition: Int, childPosititon: Int): String? {
        return ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(groupPosition : Int, childPosition : Int, isLastChild : Boolean
                              , view: View?, parent: ViewGroup?): View? {
        var convertView = view

        if (convertView == null) {
            val infalInflater = this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.item_history_child, null)
        }
        val item = list[groupPosition].history[childPosition]
        convertView?.typeCheckIn?.text = item.eventType
        convertView?.hubCheckIn?.text = item.qrcodeId?.locationId ?:" Manual "
        convertView?.timeCheckIn?.text = item.updatedAt?.formatISO("HH:mm")

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return list.size
    }
}