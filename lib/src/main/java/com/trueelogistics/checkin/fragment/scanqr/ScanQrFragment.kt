package com.trueelogistics.checkin.fragment.scanqr

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.trueelogistics.checkin.R
import com.trueelogistics.checkin.dialog.SuccessDialogFragment
import com.trueelogistics.checkin.model.generate_qr.RootModel
import com.trueelogistics.checkin.service.GetRetrofit
import com.trueelogistics.checkin.service.ScanQrService
import kotlinx.android.synthetic.main.fragment_scan_qrcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanQrFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_scan_qrcode, container, false)
    }
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            result.text.also {
                sentQr(it)
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner_fragment?.setStatusText("")
        scanner_fragment?.decodeContinuous(callback)

        self_checkin.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fragment,ManualCheckinFragment())?.addToBackStack(null)?.commit()
        }
    }

    private fun sentQr(result: String) {
        val retrofit = GetRetrofit.getRetrofit?.build()?.create(ScanQrService::class.java)

        val call = retrofit?.getData("CHECK_IN", result)

        SuccessDialogFragment().show(activity?.supportFragmentManager,"show")

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



    override fun onResume() {
        scanner_fragment?.resume()
        super.onResume()
    }

    override fun onPause() {
        scanner_fragment?.pause()
        super.onPause()
    }



}
