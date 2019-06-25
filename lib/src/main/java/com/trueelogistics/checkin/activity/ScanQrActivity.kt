package com.trueelogistics.checkin.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.kotlinpermissions.KotlinPermissions
import com.trueelogistics.checkin.Interfaces.CheckInTELCallBack
import com.trueelogistics.checkin.R
import com.trueelogistics.checkin.model.generate_qr.RootModel
import com.trueelogistics.checkin.service.GetRetrofit
import com.trueelogistics.checkin.service.ScanQrService
import kotlinx.android.synthetic.main.activity_scan_qr.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("Registered")
class ScanQrActivity : AppCompatActivity() {

    private var checkInTELCallBack: CheckInTELCallBack? = null
    private var barcodeView: CompoundBarcodeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        KotlinPermissions.with(this) // where this is an FragmentActivity instance
            .permissions(
                Manifest.permission.CAMERA
            ).onAccepted {
                barcodeView = scanner_fragment
                barcodeView?.decodeContinuous(callback)
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

    private fun sentQr(result: String) {
        val retrofit = GetRetrofit.getRetrofit?.build()?.create(ScanQrService::class.java)
        val call = retrofit?.getData("CHECK_IN", result)

        call?.enqueue(object : Callback<RootModel> {
            override fun onFailure(call: Call<RootModel>, t: Throwable) {
                Log.e(" onFailure !!", " Something wrong")
            }

            override fun onResponse(call: Call<RootModel>, response: Response<RootModel>) {
                if (response.code() == 200) {
                    val root = response.body()
                    val show = root?.data?.qrcodeUniqueKey
                    Log.e(" QR code == ", "$show ...")

                } else {
                    response.errorBody()
                }
            }
        })
    }

    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            result.text.also {
                sentQr(it)
                onBackPressed()
            }

            Toast.makeText(
                this@ScanQrActivity, "QR code = ?????",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onResume() {
        barcodeView?.resume()
        super.onResume()
    }

    override fun onPause() {
        barcodeView?.pause()
        super.onPause()
    }
}