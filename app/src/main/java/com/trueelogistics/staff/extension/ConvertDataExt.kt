package com.trueelogistics.staff.extension

import com.trueelogistics.checkin.extensions.formatISO
import com.trueelogistics.checkin.model.HistoryInDataModel
import com.trueelogistics.checkin.model.HistoryRootModel
import com.trueelogistics.staff.model.BaseHistoryModel
import com.trueelogistics.staff.model.DateModel
import com.trueelogistics.staff.model.HistoryModel

fun HistoryRootModel?.convertBaseHistory(list: ArrayList<BaseHistoryModel>?): ArrayList<BaseHistoryModel> {
    val data = arrayListOf<BaseHistoryModel>()
    val date = this?.data?.data?.first()?.createdAt?.formatISO("yyyy-MM-dd").toString()
    if (list.isNullOrEmpty()) {
        data.add(DateModel(date))
    }

    this?.data?.data?.forEachIndexed { index, historyInDataModel ->
        if (index == 0) {
            if (list?.isNotEmpty() == true) {
                val itemLast = list.last()
                if (itemLast is HistoryModel && itemLast.createdAt
                    != historyInDataModel.createdAt?.formatISO("yyyy-MM-dd").toString()
                )
                    data.add(
                        DateModel(
                            historyInDataModel.createdAt
                                ?.formatISO("yyyy-MM-dd").toString()
                        )
                    )
            }
            data.add(setHistoryDataModel(historyInDataModel))
        } else if (index != 0 && (historyInDataModel.createdAt?.formatISO("yyyy-MM-dd").toString()
                    == this.data.data[index - 1].createdAt?.formatISO("yyyy-MM-dd").toString())
        ) {
            data.add(setHistoryDataModel(historyInDataModel))
        } else {
            data.add(DateModel(historyInDataModel.createdAt?.formatISO("yyyy-MM-dd").toString()))
            data.add(setHistoryDataModel(historyInDataModel))
        }
    }
    return data
}

private fun setHistoryDataModel(historyInDataModel: HistoryInDataModel): BaseHistoryModel {
    return HistoryModel(
        eventType = historyInDataModel.eventType,
        hubName = historyInDataModel.locationName,
        timeCheckIn = historyInDataModel.createdAt?.formatISO("HH:mm"),
        checkInType = historyInDataModel.checkinType,
        createdAt = historyInDataModel.createdAt?.formatISO("yyyy-MM-dd")

    )
}