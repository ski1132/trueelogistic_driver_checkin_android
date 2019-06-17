package com.example.checklibrary.model

data class RootModel (
    private var data : DataModel,
    private var status : String? = null,
    private var statusCodes : Int? = null
)