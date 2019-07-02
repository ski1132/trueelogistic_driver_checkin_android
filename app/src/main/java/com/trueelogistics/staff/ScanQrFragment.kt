package com.trueelogistics.staff

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trueelogistics.checkin.Interfaces.CheckInTELCallBack
import com.trueelogistics.checkin.handler.CheckInTEL
import kotlinx.android.synthetic.main.fragment_scan_qr.*

class ScanQrFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        toolbar.setOnClickListener {
            mainActivity.actionToolbar()
        }
        checkInBtn.setOnClickListener {
            activity?.let {
                openScanQr(it)
            }
        }
        checkBetBtn.setOnClickListener {
            activity?.let {
                openScanQr(it)
            }
        }
        checkOutBtn.setOnClickListener {
            activity?.let {
                openScanQr(it)
            }
        }
        genQr.setOnClickListener {
            activity?.let {
                CheckInTEL.checkInTEL?.openGenarateQRCode(it, object : CheckInTELCallBack {
                    override fun onCancel() {
                        Toast.makeText(activity, " GenQr.onCancel === ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCheckInFailure(message: String) {
                        Toast.makeText(activity, " GenQr.onCheckFail = $message ", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCheckInSuccess(result: String) {
                        Toast.makeText(activity, " GenQr.onCheckSuccess = $result", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        CheckInTEL.checkInTEL?.onActivityResult(requestCode,resultCode,data)

    }
    private fun openScanQr( activity: FragmentActivity){
        CheckInTEL.checkInTEL?.openScanQRCode(activity, object : CheckInTELCallBack {
            override fun onCancel() {
                Toast.makeText(activity, " ScanQr.onCancel === ", Toast.LENGTH_SHORT).show()
            }

            override fun onCheckInFailure(message: String) {
                Toast.makeText(activity, " ScanQr.onCheckFail = $message ", Toast.LENGTH_SHORT).show()
            }

            override fun onCheckInSuccess(result: String) {
                Toast.makeText(activity, " ScanQr.onCheckSuccess = $result", Toast.LENGTH_SHORT).show()
            }

        })
    }
}
