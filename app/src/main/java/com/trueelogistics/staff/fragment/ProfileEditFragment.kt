package com.trueelogistics.staff.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kotlinpermissions.KotlinPermissions
import com.theartofdev.edmodo.cropper.CropImage
import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.ProfileActivity
import com.trueelogistics.staff.model.ProfileRootModel
import com.trueelogistics.staff.service.ProfileEditService
import com.trueelogistics.staff.service.RetrofitGenerater
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileEditFragment : Fragment() {

    private var userId: String? = ""
    private var pathImg: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileData()
        user_pic.setOnClickListener {
            imagePicking()
        }
        back_to_main_menu.setOnClickListener {
            activity?.onBackPressed()
        }
        pass_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                if (text?.length == 8) {
                    confirm_edit.isEnabled = true
                    activity?.let {
                        confirm_edit.setBackgroundColor( ContextCompat
                            .getColor(it, R.color.purple))
                    }
                }
                else{
                    confirm_edit.isEnabled = false
                    activity?.let {
                        confirm_edit.setBackgroundColor( ContextCompat
                            .getColor(it, R.color.gray))
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
        confirm_edit.setOnClickListener {
            if (name_surname.length() > 0 && phone.length() == 10)
                sentEditData()
            else if (phone.length() in 1..9)
                show_warning.text = getString(R.string.phone_input)
            else
                show_warning.text = getString(R.string.data_input)

        }
    }

    private fun getProfileData() {
        ProfileActivity().getProfileData(object : ProfileActivity.ProfileDataCallback {
            override fun onResponceProfile(model: ProfileRootModel?) {
                userId = model?.data?._id
                model?.data?.imgProfile?.let { img ->
                    activity?.let {
                        pathImg = img
                        Glide.with(it)
                            .load(img)
                            .into(user_pic)
                    }
                }
                val firstName = model?.data?.firstname ?: ""
                val lastName = model?.data?.lastname ?: ""
                name_surname.text = Editable.Factory.getInstance()
                    .newEditable("$firstName $lastName")
                phone.text = Editable.Factory.getInstance()
                    .newEditable(model?.data?.phone)
            }

            override fun onFailureProfile(message: String) {
                activity?.let {
                    Toast.makeText(it, "get Data onFailure : $message", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun sentEditData() {
        val retrofit = RetrofitGenerater().build(false).create(ProfileEditService::class.java)
        val call = retrofit.getData(
            id = userId ?: "",
            imgProfile = pathImg ?:"",
            firstname = name_surname.text.toString(),
            phone = phone.text.toString(),
            password = pass_text.text.toString()
        )
        call.enqueue(object : Callback<ProfileRootModel> {
            override fun onFailure(call: Call<ProfileRootModel>, t: Throwable) {
                activity?.let {
                    Toast.makeText(it, "Edit Profile.onFailure : ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onResponse(
                call: Call<ProfileRootModel>,
                response: Response<ProfileRootModel>
            ) {
                when (response.code()) {
                    200 -> {
                        activity?.let {
                            Toast.makeText(it, " Edit Profile Success !!", Toast.LENGTH_SHORT)
                                .show()
                            it.onBackPressed()
                        }
                    }
                    404 -> {
                        activity?.let {
                            Toast.makeText(it, "Can't fine your ID !!", Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        })
    }

    private fun imagePicking() {
        activity?.let { activity ->
            KotlinPermissions.with(activity) // where this is an FragmentActivity instance
                .permissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).onAccepted {
                    CropImage.activity()
                        .start(activity,this)
                }.onDenied {
                    Toast.makeText(
                        activity, "Permission Denied",
                        Toast.LENGTH_LONG
                    ).show()
                    onDestroyView()
                }
                .ask()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                activity?.let {
                    pathImg = result.uri.toString()
                    Glide.with(it)
                        .load(result.uri)
                        .into(user_pic)
                }

            } else {
                Toast.makeText(
                    context,
                    "Fail.ResultCode : $resultCode",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
