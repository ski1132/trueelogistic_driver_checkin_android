package com.example.testwithfirebase


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanQrFragment : Fragment() {
    private var zXingScannerView: ZXingScannerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanQR()
    }
    private fun scanQR() {
        zXingScannerView = ZXingScannerView(context)
        activity?.setContentView(zXingScannerView)
        zXingScannerView?.run {
            startCamera()
            setResultHandler {
                stopCamera()
                activity?.let {fragActivity ->
                    fragActivity.setContentView(R.layout.activity_main)
                    val resultString = it.text.toString()
                    Toast.makeText(
                        activity, "QR code = $resultString",
                        Toast.LENGTH_LONG
                    ).show()
                    fragActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.contentMainFrag, MainFragment()).commit()
                }
            }
        }
    }
}
