package com.trueelogistics.staff

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trueelogistics.checkin.extensions.formatISO
import com.trueelogistics.checkin.model.HistoryRootModel
import com.trueelogistics.checkin.service.HistoryService
import com.trueelogistics.checkin.service.RetrofitGenerater
import com.trueelogistics.staff.model.ExpandableDataModel
import kotlinx.android.synthetic.main.fragment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity = activity as MainActivity
        toolBar.setOnClickListener{
            mainActivity.actionToolbar()
        }
        val retrofit = RetrofitGenerater().build().create(HistoryService::class.java)
        val call = retrofit?.getData()
        call?.enqueue(object : Callback<HistoryRootModel> {
            override fun onFailure(call: Call<HistoryRootModel>, t: Throwable) {
            }
            override fun onResponse(call: Call<HistoryRootModel>, response: Response<HistoryRootModel>) {
                if (response.code() == 200) {
                    val logModel: HistoryRootModel? = response.body()
                    val parentList = arrayListOf<ExpandableDataModel>()
                    if (logModel != null) {
                        var lastDate = ""
                        logModel.data.data.forEach {
                            val dateFormat = it.updatedAt?.formatISO("yyyy-MMMM-dd")
                            if( lastDate != dateFormat){
                                parentList.add(ExpandableDataModel(date = dateFormat.toString()) )
                                lastDate = dateFormat.toString()
                            }
                        }

                        parentList.forEach {parent->
                            logModel.data.data.forEach {log->
                                if(parent.date == log.updatedAt?.formatISO("yyyy-MMMM-dd")){
                                    parent.history.add(log)
                                }
                            }
                        }

                        activity?.let {
                            val listAdapter = HistoryExpandable(it, parentList)
                            expandListView.setAdapter(listAdapter)
                        }
                    }

                } else {
                    response.errorBody()
                }
            }
        })
    }

}