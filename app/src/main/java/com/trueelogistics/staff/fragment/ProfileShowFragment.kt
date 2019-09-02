package com.trueelogistics.staff.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide

import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.ProfileActivity
import com.trueelogistics.staff.model.ProfileRootModel
import com.trueelogistics.staff.service.ProfileService
import com.trueelogistics.staff.service.RetrofitGenerater
import kotlinx.android.synthetic.main.fragment_profile_show.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileShowFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        back_to_main_menu.setOnClickListener {
            activity?.onBackPressed()
        }
        edit_profile.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_profile,ProfileEditFragment())
                ?.addToBackStack(null)?.commit()
        }
        getProfileData()
    }

    private fun getProfileData(){
        ProfileActivity().getProfileData( object : ProfileActivity.ProfileDataCallback{
            override fun onResponceProfile(model: ProfileRootModel?) {
                model?.data?.imgProfile?.let {img ->
                    activity?.let {
                        Glide.with(it)
                            .load(img)
                            .into(user_pic)
                    }
                }

                val firstName = model?.data?.firstname?:""
                val lastName = model?.data?.lastname?:""
                name_surname.text =  Editable.Factory.getInstance()
                    .newEditable("$firstName $lastName")
                phone.text = Editable.Factory.getInstance()
                    .newEditable(model?.data?.phone)
                pass_text.text = Editable.Factory.getInstance()
                    .newEditable("********")
            }

            override fun onFailureProfile(message: String) {
                activity?.let{
                    Toast.makeText(it , "get Data onFailure : $message", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

}
