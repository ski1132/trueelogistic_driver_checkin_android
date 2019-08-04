package com.trueelogistics.staff.model

data class LoginDataModel(
    var token : String ?= null,
    var reToken : String ?= null,
    var user : LoginUserModel,
    var role : String ?= null
)