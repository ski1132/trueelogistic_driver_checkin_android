package com.trueelogistics.staff.model

data class ProfileRootModel(
    var data : ProfileDataModel ,
    var status : String ?= null ,
    var statusCodes : Int ?= 0
)