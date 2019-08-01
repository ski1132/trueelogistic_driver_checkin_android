package com.trueelogistics.staff.model

data class ProfileDataModel(
    var token : String ?= null,
    var reToken : String ?= null,
    var user : ProfileUserModel,
    var role : String ?= null
)