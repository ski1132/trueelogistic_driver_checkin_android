package com.trueelogistics.staff.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.trueelogistics.checkin.handler.CheckInTEL
import com.trueelogistics.checkin.model.HistoryRootModel
import com.trueelogistics.checkin.model.SearchCitizenModel
import com.trueelogistics.checkin.service.HistoryService
import com.trueelogistics.checkin.service.RetrofitGenerater
import com.trueelogistics.staff.R
import com.trueelogistics.staff.activity.MainActivity
import com.trueelogistics.staff.adapter.HistoryAdapter
import com.trueelogistics.staff.extension.convertBaseHistory
import kotlinx.android.synthetic.main.fragment_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var adapter: HistoryAdapter? = null
    private var page = 0
    private var limit = 10
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var lastItemPosition = 0
    private var availableGetHistory = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingData()
    }

    private fun bindingData() {
        adapter = HistoryAdapter(arrayListOf())
        recycleView.adapter = adapter
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = recyclerView.layoutManager?.childCount ?: 0
                    totalItemCount = recyclerView.layoutManager?.itemCount ?: 0
                    lastItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    if (visibleItemCount + lastItemPosition >= (totalItemCount)) {
                        loadMore()
                    }
                }
            }
        })
        swipeRefreshLayout.setOnRefreshListener {
            onRefresh()
        }
        onRefresh()
        val mainActivity = activity as MainActivity
        toolBar.setOnClickListener {
            mainActivity.actionToolbar()
        }
    }

    private fun getStaffHistory() {
        showProcess()
        if (availableGetHistory) {
            availableGetHistory = false
            page++
            val retrofit = RetrofitGenerater().build().create(HistoryService::class.java)
            val call = retrofit.getData(
                "{\"\$or\":[" + Gson().toJson
                    (SearchCitizenModel(CheckInTEL.userId.toString())) + "]}",
                page, limit
            )
            call.enqueue(object : Callback<HistoryRootModel> {
                override fun onFailure(call: Call<HistoryRootModel>, t: Throwable) {
                    hideProcess()
                    onError(t.message)
                    availableGetHistory = true
                }

                override fun onResponse(
                    call: Call<HistoryRootModel>,
                    response: Response<HistoryRootModel>
                ) {

                    show_error.visibility = View.GONE
                    historyListView(response)
                    hideProcess()
                    availableGetHistory = true
                }
            })
        }
    }

    private fun historyListView(response: Response<HistoryRootModel>) {
        val limit = response.body()?.data?.limit?.toDouble() ?: 0.0
        val total = response.body()?.data?.total?.toDouble() ?: 0.0
        val maxPage = kotlin.math.ceil(total / limit)
        val nextPage = response.body()?.data?.page ?: 0 <= (maxPage)
        val historyRootModel: HistoryRootModel? = response.body()
        if (response.code() == 200 && nextPage) {
            if (historyRootModel?.data?.data != null) {
                adapter?.list?.addAll(historyRootModel.convertBaseHistory(adapter?.list))
            }
            adapter?.list?.size?.minus(1)?.let { adapter?.notifyItemChanged(it) }
        } else if (page == 1 && historyRootModel?.data?.data?.isEmpty() == true && !nextPage) {
            show_error.text = getString(R.string.data_not_found)
            show_error.visibility = View.VISIBLE
        }
    }

    private fun loadMore() {
        getStaffHistory()
    }

    private fun showProcess() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideProcess() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        setDefault()
        getStaffHistory()
    }

    private fun setDefault() {
        page = 0
        adapter?.list?.clear()
        adapter?.notifyDataSetChanged()
    }

    private fun onError(message: String?) {
        show_error.text = message
        show_error.visibility = View.VISIBLE
    }
}
