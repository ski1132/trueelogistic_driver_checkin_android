package com.trueelogistics.staff.model

import com.trueelogistics.staff.enum.EnumHistory


data class DateModel(
    var date: String? = ""
) : BaseHistoryModel(EnumHistory.DATE.value)