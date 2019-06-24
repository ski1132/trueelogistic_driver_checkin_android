package com.trueelogistics.checkin.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.trueelogistics.checkin.Interfaces.CheckInTELCallBack
import com.trueelogistics.checkin.model.generate_qr.RootModel
import com.trueelogistics.checkin.service.GetRetrofit
import com.trueelogistics.checkin.service.ScanQrService
import com.kotlinpermissions.KotlinPermissions
import me.dm7.barcodescanner.zxing.ZXingScannerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("Registered")
class ScanQrActivity : AppCompatActivity(){

    private var checkInTELCallBack: CheckInTELCallBack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBindingView()
        setBindingData()
    }

    private fun setBindingView(){

    }

    private fun setBindingData(){
        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(
                Manifest.permission.CAMERA
            ).onAccepted {
                val zXingScannerView = ZXingScannerView(this)
                setContentView(zXingScannerView)
                zXingScannerView.run {
                    startCamera()
                    setResultHandler {
                        stopCamera()

                        val resultString = it.text.toString()
                        sentQr(resultString)
                        Toast.makeText(
                            this@ScanQrActivity, "QR code = $resultString",
                            Toast.LENGTH_LONG
                        ).show()
                        setResult(Activity.RESULT_OK,Intent().apply { // result code and data intent
                            this.putExtra("result",resultString)
                        })
                        finish()
                    }
                }
            }.onDenied {

                Toast.makeText(
                    this, "Permission Denied",
                    Toast.LENGTH_LONG
                ).show()
                checkInTELCallBack?.onCheckInFailure("Permission Denied") // set
                finish()
            }
            .ask()
    }
    private fun sentQr( result : String)
    {
        val retrofit = GetRetrofit.build().create(ScanQrService::class.java)
        val call = retrofit.getData("CHECK_IN",result)

        call.enqueue( object : Callback<RootModel> {
            override fun onFailure(call: Call<RootModel>, t: Throwable) {
                Log.e(" onFailure !!"," Something wrong")
            }

            override fun onResponse(call: Call<RootModel>, response: Response<RootModel>) {
                if ( response.code() == 200){
                    val root  = response.body()
                    if(root?.status == "OK"){
                        val show = root.data.qrcodeUniqueKey
                        Log.e(" QR code == ", "$show ...")

                    }
                    else{

                    }
                }
                else{
                    response.errorBody()
                }
            }
        })
    }
}
