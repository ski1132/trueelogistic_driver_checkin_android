package com.trueelogistics.staff.model

data class LeaderRootModel(
    var data : LeaderDataModel ,
    var status : String ?= null ,
    var statusCode : Int ?= 0
)