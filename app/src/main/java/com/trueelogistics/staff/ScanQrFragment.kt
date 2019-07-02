package com.trueelogistics.staff


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.activity.GenQrActivity
import com.trueelogistics.checkin.scanqr.ScanQrActivity
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
            val intent = Intent(activity, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        checkBetBtn.setOnClickListener {
            val intent = Intent(activity, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        checkOutBtn.setOnClickListener {
            val intent = Intent(activity, ScanQrActivity::class.java)
            this.startActivity(intent)
        }
        genQr.setOnClickListener {
            val intent = Intent(activity, GenQrActivity::class.java)
            this.startActivity(intent)
        }
    }
}
