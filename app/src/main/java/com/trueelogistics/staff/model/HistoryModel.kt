package com.trueelogistics.staff.model

import com.trueelogistics.staff.enum.EnumHistory

data class HistoryModel(
    var eventType: String? = "",
    var hubName: String? = "",
    var timeCheckIn: String? = "",
    var checkInType: String? = "",
    var createdAt: String? = ""
): BaseHistoryModel(EnumHistory.HISTORY.value)