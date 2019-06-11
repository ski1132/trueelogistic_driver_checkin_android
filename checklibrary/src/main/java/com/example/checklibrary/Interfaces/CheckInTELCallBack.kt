package com.example.checklibrary.Interfaces

import java.io.Serializable

interface CheckInTELCallBack : Serializable{
    fun onCheckInSuccess(result: String)
    fun onCheckInFailure(message: String)
    fun onCancel()
}