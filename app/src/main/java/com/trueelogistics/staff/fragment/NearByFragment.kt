package com.trueelogistics.staff.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.checkin.interfaces.CheckInTELCallBack
import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_near_by.*

class NearByFragment : Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_near_by, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        toolbar.setOnClickListener {
            mainActivity.actionToolbar()
        }
        nearby_phone_fine.setOnClickListener {
            activity?.let {
                CheckInTEL.checkInTEL?.openNearBy(it, object : CheckInTELCallBack {
                    override fun onCancel() {

                    }

                    override fun onCheckInFailure(message: String) {

                    }

                    override fun onCheckInSuccess(result: String) {

                    }
                })
            }
        }

    }

}
