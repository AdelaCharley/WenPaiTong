package com.qunshang.wenpaitong.equnshang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.fragment_day_work_point.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.DayWorkPointBean
import com.qunshang.wenpaitong.equnshang.data.YCWorkPointBean
import com.qunshang.wenpaitong.equnshang.utils.MyNumUtils

class DayWorkPointFragment(val type : Int) : MyBaseFragment() {

    companion object{
        val TYPE_CURRENTDAY = 1

        val TYPE_YESTERDAY = 2

        val TYPE_CURRENTWEEK = 3

        val TYPE_CURRENTMONTH = 4

        val TYPE_LASTMONTH = 5
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_work_point, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init(){
        val callback = object : Callback<DayWorkPointBean>{
            override fun onResponse(
                call: Call<DayWorkPointBean>,
                response: Response<DayWorkPointBean>
            ) {
                if (response.body() != null){
                    initView_Day(response.body()!!)
                }
            }

            override fun onFailure(call: Call<DayWorkPointBean>, t: Throwable) {

            }

        }
        val callback1 = object : Callback<YCWorkPointBean>{
            override fun onResponse(
                call: Call<YCWorkPointBean>,
                response: Response<YCWorkPointBean>
            ) {
                if (response.body() != null){
                    initView_YC(response.body()!!)
                }
            }

            override fun onFailure(call: Call<YCWorkPointBean>, t: Throwable) {

            }

        }
        if (type == TYPE_CURRENTDAY){
            label_totalworkpoint.setText("今日总分：")
            ApiManager.getInstance().getApiPersonalTest().loadCurrentDayWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback)
        } else if (type == TYPE_YESTERDAY){
            label_totalworkpoint.setText("昨日总分：")
            ApiManager.getInstance().getApiPersonalTest().loadYesterDayWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback1)
        } else if (type == TYPE_CURRENTWEEK){
            label_totalworkpoint.setText("本周总分：")
            ApiManager.getInstance().getApiPersonalTest().loadCurrentWeekWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback1)
        } else if (type == TYPE_CURRENTMONTH){
            label_totalworkpoint.setText("本月总分：")
            ApiManager.getInstance().getApiPersonalTest().loadCurrentMonthWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback1)
        } else if (type == TYPE_LASTMONTH){
            label_totalworkpoint.setText("上月总分：")
            ApiManager.getInstance().getApiPersonalTest().loadLastMonthWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback1)
        }
    }

    fun initView_Day(bean : DayWorkPointBean){
        count_up.setText(bean.data.todayCreditByKind.get(0).number)
        progress_up.progress = (bean.data.todayCreditByKind.get(0).number.toDouble() * 10).toInt()

        count_comment.setText(bean.data.todayCreditByKind.get(1).number)
        progress_comment.progress = (bean.data.todayCreditByKind.get(1).number.toDouble() * 10).toInt()

        count_browse.setText(bean.data.todayCreditByKind.get(2).number)
        progress_browse.progress = (bean.data.todayCreditByKind.get(2).number.toDouble() * 10).toInt()

        count_new.setText(bean.data.todayCreditByKind.get(3).number)
        progress_new.progress = (bean.data.todayCreditByKind.get(3).number.toDouble() * 10).toInt()

        count_consume.setText(bean.data.todayCreditByKind.get(4).number)
        progress_consume.progress = (bean.data.todayCreditByKind.get(4).number.toDouble() * 10).toInt()

        if (((UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2) or (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2)) and (bean.data.todayCreditByKind.size > 5)){
            count_content.setText(bean.data.todayCreditByKind.get(5).number)
            progress_content.progress = (bean.data.todayCreditByKind.get(5).number.toDouble() * 10).toInt()
            totalworkpoint.setText(MyNumUtils.remain((bean.data.todayCreditByKind.get(0).number.toDouble() +
                    bean.data.todayCreditByKind.get(1).number.toDouble() +
                    bean.data.todayCreditByKind.get(2).number.toDouble() +
                    bean.data.todayCreditByKind.get(3).number.toDouble() +
                    bean.data.todayCreditByKind.get(4).number.toDouble() +
                    bean.data.todayCreditByKind.get(5).number.toDouble())).toString())
            layout_content.visibility = View.VISIBLE
        } else {
            totalworkpoint.setText(MyNumUtils.remain((bean.data.todayCreditByKind.get(0).number.toDouble() +
                    bean.data.todayCreditByKind.get(1).number.toDouble() +
                    bean.data.todayCreditByKind.get(2).number.toDouble() +
                    bean.data.todayCreditByKind.get(3).number.toDouble() +
                    bean.data.todayCreditByKind.get(4).number.toDouble())).toString())
            layout_content.visibility = View.GONE
        }
    }

    fun initView_YC(bean : YCWorkPointBean){
        count_up.setText(bean.data.get(0).number.toString())
        progress_up.progress = (bean.data.get(0).number * 10).toInt()

        count_comment.setText(bean.data.get(1).number.toString())
        progress_comment.progress = (bean.data.get(1).number.toDouble() * 10).toInt()

        count_browse.setText(bean.data.get(2).number.toString())
        progress_browse.progress = (bean.data.get(2).number.toDouble() * 10).toInt()

        count_new.setText(bean.data.get(3).number.toString())
        progress_new.progress = (bean.data.get(3).number.toDouble() * 10).toInt()

        count_consume.setText(bean.data.get(4).number.toString())
        progress_consume.progress = (bean.data.get(4).number.toDouble() * 10).toInt()

        if (
            ((UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2) or (UserInfoViewModel.getUserInfo()!!.getIs_vip() >= 2)) and (bean.data.size > 5)
            //(UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2) or (bean.data.size > 5)
        ){
            count_content.setText(bean.data.get(5).number.toString())
            progress_content.progress = (bean.data.get(5).number * 10).toInt()
            totalworkpoint.setText(MyNumUtils.remain(
                (bean.data.get(0).number +
                        bean.data.get(1).number +
                        bean.data.get(2).number +
                        bean.data.get(3).number +
                        bean.data.get(4).number +
                        bean.data.get(5).number)
            ).toString())
            layout_content.visibility = View.VISIBLE
        } else {
            layout_content.visibility = View.GONE
            totalworkpoint.setText(MyNumUtils.remain(
                (bean.data.get(0).number +
                        bean.data.get(1).number +
                        bean.data.get(2).number +
                        bean.data.get(3).number +
                        bean.data.get(4).number)
            ).toString())
        }
    }

    var isFirst = false

    override fun onHiddenChanged(hidden: Boolean) {
        if (!isFirst){
            isFirst = true
            return
        }
        refresh()
    }

    fun refresh(){
        if (view == null){
            return
        }
        init()
    }

}