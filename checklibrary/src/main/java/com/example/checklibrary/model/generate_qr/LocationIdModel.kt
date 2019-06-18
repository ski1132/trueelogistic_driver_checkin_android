package com.example.checklibrary.model.generate_qr

data class LocationIdModel (
    var LocationPointModel: LocationPointModel,
    var deleted : Boolean? = false,
    var _id : String? = null,
    var locationName : String? = null,
    var updatedAt : String? = null,
    var createdAt : String? = null,
    var __v : String? = null
)