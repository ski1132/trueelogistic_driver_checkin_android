package com.trueelogistics.staff.model
import com.trueelogistics.checkin.model.HistoryInDataModel

data class ExpandableDataModel(
    val date : String ="",
    val history : ArrayList<HistoryInDataModel> = ArrayList()

)