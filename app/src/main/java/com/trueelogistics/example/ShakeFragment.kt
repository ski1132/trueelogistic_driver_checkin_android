package com.trueelogistics.example


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.activity.ShakeActivity
import kotlinx.android.synthetic.main.fragment_near_by.toolbar
import kotlinx.android.synthetic.main.fragment_shake.*

class ShakeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shake, container, false)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity
        toolbar.setOnClickListener {
            mainActivity.actionToolbar()
        }
        shake_phone_fine.setOnClickListener {
            val intent = Intent(activity, ShakeActivity::class.java)
            this.startActivity(intent)
        }
    }
}
