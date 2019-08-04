package com.trueelogistics.staff.model

data class LoginRootModel(
    var data : LoginDataModel,
    var status : String ?= null,
    var statusCodes : Int ?= 0
)