package com.trueelogistics.checkin.scanqr


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.R
import com.trueelogistics.checkin.testretofit.DataService
import com.trueelogistics.checkin.testretofit.MainAdapter
import com.trueelogistics.checkin.testretofit.ResponseModel
import kotlinx.android.synthetic.main.fragment_stock_dialog.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StockDialogFragment : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRrtrofit()


    }

    private fun getRrtrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("http://rakgun.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(DataService::class.java)

        val call = service.getData("69i57j0l5910j0j7f4d5s4fs64g")

        call.enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                Log.e("call === ", call.toString())
            }

            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>) {


                if (response.code() == 200) {
                    val logModel: ResponseModel? = response.body()
                    activity?.also {
                        recycleView?.layoutManager = LinearLayoutManager(it)
                        if (logModel != null) {
                            Log.e("person [1] == ", logModel.person[1].firstName.toString())
                            recycleView.adapter = MainAdapter(logModel.person, it)
                        }
                    }
                } else {
                    response.errorBody()
                }
            }

        })
    }

}
