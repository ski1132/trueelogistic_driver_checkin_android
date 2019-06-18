package com.example.checklibrary.model.generate_qr

data class RootModel (
    var data : DataModel,
    var status : String? = null,
    var statusCodes : Int? = null
)