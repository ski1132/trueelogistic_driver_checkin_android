package com.example.checklibrary.model

data class RootModel (
    var data : DataModel,
    var status : String? = null,
    var statusCodes : Int? = null
)