package com.trueelogistics.staff.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trueelogistics.checkin.adapter.HistoryStaffAdapter
import com.trueelogistics.checkin.enums.CheckInTELType
import com.trueelogistics.checkin.extensions.format
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.checkin.interfaces.ArrayListGenericCallback
import com.trueelogistics.checkin.interfaces.CheckInTELCallBack
import com.trueelogistics.checkin.interfaces.TypeCallback
import com.trueelogistics.checkin.model.HistoryInDataModel
import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_scan_qr.*
import java.util.*

class ScanQrFragment : Fragment() {
    private var adapter = HistoryStaffAdapter()
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
        openMenu.setOnClickListener {
            mainActivity.actionToolbar()
        }
        checkButton()
        getHistoryToday()
        val day = Date().format("EE")
        val nDay = Date().format("dd")
        val mouth = Date().format("MMM")
        date.text = String.format(this.getString(R.string.date_checkin), day, nDay, mouth)
        activity?.let { activity ->
            checkInBtn.setOnClickListener {
                openScanQr(activity, CheckInTELType.CheckIn.value, false)
            }
            checkBetBtn.setOnClickListener {
                openScanQr(activity, CheckInTELType.CheckBetween.value, false)
            }
            checkOutBtn.setOnClickListener {
                openScanQr(activity, CheckInTELType.CheckOut.value, false)
            }
        }
    }

    private fun getHistoryToday() {
        historyRecycle.adapter = adapter
        activity?.let {
            historyRecycle?.layoutManager = LinearLayoutManager(it)
            CheckInTEL.checkInTEL?.getHistory(object :
                ArrayListGenericCallback<HistoryInDataModel> {
                override fun onFailure(message: String?) {

                }

                override fun onResponse(dataModel: ArrayList<HistoryInDataModel>?) {
                    adapter.items.removeAll(dataModel ?: arrayListOf())
                    adapter.items.addAll(dataModel ?: arrayListOf())
                    adapter.notifyDataSetChanged()
                    historyRecycle.scrollToPosition((dataModel?.size)?.minus(1) ?: 0)
                }
            })
        }
    }

    private fun openScanQr(context: Context, type: String, disableBack: Boolean) {
        activity?.let {
            CheckInTEL.checkInTEL?.openScanQRCode(
                it,
                type,
                disableBack,
                object : CheckInTELCallBack {
                    override fun onCancel() {
                    }

                    override fun onCheckInFailure(message: String) {
                        Toast.makeText(
                            context,
                            " ScanQr.onCheckFail = $message ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onCheckInSuccess(result: String) {
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        getHistoryToday()
        checkButton()
    }

    private var checkFirstInDay = true
    private fun checkButton() {
        CheckInTEL.checkInTEL?.getLastCheckInHistory(object : TypeCallback {
            override fun onResponse(type: String?, today: Boolean) {
                buttonEnable(type ?: "", today)
            }

            override fun onFailure(message: String?) {
                Toast.makeText(context, " ScanQr.onCheckFail = $message ", Toast.LENGTH_SHORT)
                    .show()
                checkInBtn.visibility = View.GONE
                checkBetBtn.visibility = View.GONE
                checkOutBtn.visibility = View.GONE
                pic_checkin.visibility = View.VISIBLE
                layoutRecycle.visibility = View.GONE
            }
        })
    }

    private fun buttonEnable(type: String, today: Boolean) {
        if (type == CheckInTELType.CheckIn.value || type == CheckInTELType.CheckBetween.value) {
            checkFirstInDay = false
            checkInBtn.visibility = View.GONE
            checkBetBtn.visibility = View.VISIBLE
            checkOutBtn.visibility = View.VISIBLE
            pic_checkin.visibility = View.GONE
            layoutRecycle.visibility = View.VISIBLE
        } else if (type == CheckInTELType.CheckOut.value || type == CheckInTELType.CheckOutOverTime.value) {
            if (checkFirstInDay && !today && type != CheckInTELType.CheckOutOverTime.value) {
                activity?.let {
                    openScanQr(it, CheckInTELType.CheckIn.value, true)
                }
                checkFirstInDay = false
            }
            checkInBtn.visibility = View.VISIBLE
            checkBetBtn.visibility = View.GONE
            checkOutBtn.visibility = View.GONE
            if (today) {
                pic_checkin.visibility = View.GONE
                layoutRecycle.visibility = View.VISIBLE
            } else {
                pic_checkin.visibility = View.VISIBLE
                layoutRecycle.visibility = View.GONE
            }


        } else {
            checkFirstInDay = false
            checkInBtn.isEnabled = false
            checkBetBtn.isEnabled = false
            checkOutBtn.isEnabled = false
            activity?.let {
                checkInBtn.setBackgroundColor(ContextCompat.getColor(it, R.color.gray))
                checkBetBtn.setBackgroundColor(ContextCompat.getColor(it, R.color.gray))
                checkOutBtn.setBackgroundColor(ContextCompat.getColor(it, R.color.gray))
            }
        }
    }
}