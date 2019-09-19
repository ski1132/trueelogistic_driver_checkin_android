package com.trueelogistics.leader.model

data class LoadImageDataModel(
    var data : LoadImageInDataModel ,
    var status : String ?= null ,
    var statusCode : Int ?= 0
)