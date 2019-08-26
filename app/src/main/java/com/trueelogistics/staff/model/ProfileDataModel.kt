package com.trueelogistics.staff.model

data class ProfileDataModel(
    var address : ArrayList<String>,
    var isActive : Boolean ?= false,
    var deleted : Boolean ?= false,
    var _id : String ?= null,
    var email : String ?= null,
    var phone : String ?= null,
    var username : String ?= null,
    var citizenId : String ?= null,
    var firstName : String ?= null,
    var lastName : String ?= null,
    val imgProfile : String ?= null,
    var birthday : String ?= null,
    var religion : String ?= null,
    var role : String ?= null,
    var fileId : String ?= null,
    var hash : String ?= null,
    var updatedAt : String ?= null,
    var createdAt : String ?= null,
    var __v : Int ?= 0
)