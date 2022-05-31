package com.qunshang.wenpaitong.equnshang.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_workpoint_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.activity.GroupTicketRuleActivity
import com.qunshang.wenpaitong.equnshang.activity.LoginActivity
import com.qunshang.wenpaitong.equnshang.data.DayWorkPointBean
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.UserHelper

class WorkpointDetailFragment : MyBaseFragment() {

    var index = 0

    lateinit var textarray : Array<TextView>

    var fragments = arrayOf(
        DayWorkPointFragment(DayWorkPointFragment.TYPE_CURRENTDAY),
        DayWorkPointFragment(DayWorkPointFragment.TYPE_YESTERDAY),
        DayWorkPointFragment(DayWorkPointFragment.TYPE_CURRENTWEEK),
        DayWorkPointFragment(DayWorkPointFragment.TYPE_CURRENTMONTH),
        DayWorkPointFragment(DayWorkPointFragment.TYPE_LASTMONTH))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workpoint_detail, container, false)
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
                    initView(response.body()!!)
                }
            }

            override fun onFailure(call: Call<DayWorkPointBean>, t: Throwable) {

            }

        }
        groupticketrule.setOnClickListener { startActivity(Intent(requireActivity(),GroupTicketRuleActivity::class.java)) }
        if (UserHelper.isLogin(requireContext())){
            if (UserInfoViewModel.getUserInfo()!!.getIs_partner() >= 2){
                label_groupticket_count.setText("10000工分兑换3群票")
            }
            ApiManager.getInstance().getApiPersonalTest().loadCurrentDayWorkPoint(UserInfoViewModel.getUserId()).enqueue(callback)
        } else {//如果是未登录状态，也要添加事件
            textarray = arrayOf(currentday,yesterday,week,month,lashmonth)
            for (i in textarray.indices){
                textarray[i].setOnClickListener(MyClickListener(i))
            }
        }
    }

    fun initView(bean : DayWorkPointBean){

        initChart(bean)
        totalworkpoint.setText(bean.data.currentCredit)
        textarray = arrayOf(currentday,yesterday,week,month,lashmonth)
        for (i in textarray.indices){
            textarray[i].setOnClickListener(MyClickListener(i))
        }
        loadMyGroupTicket()
        showFragment(0)
    }

    fun loadMyGroupTicket(){
        ApiManager.getInstance().getApiPersonalTest().loadGroupTicketWorkPoint(UserInfoViewModel.getUserId()).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>{
            override fun onResponse(call: Call<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>, response: Response<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>) {
                if (response.body() != null){
                    mygroupticket.setText(response.body()!!.data.numberInfo.userQunCoin.toString())
                }
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.QunCoinBean>, t: Throwable) {

            }

        })
    }

    fun showFragment(index : Int){
        if (!UserHelper.isLogin(requireContext())){
            startActivity(Intent(context, LoginActivity::class.java))
            return
        }
        hideAllFragments()
        val tra = childFragmentManager.beginTransaction()
        if (!fragments[index].isAdded){
            tra.add(R.id.container,fragments[index])
        }
        tra.show(fragments[index]).commit()

    }

    //var isFirst = false

    /*override fun onResume() {
        super.onResume()
        if (!isFirst){
            isFirst = true
            return
        }
        init()
    }*/

    /*override fun onHiddenChanged(hidden: Boolean) {
        if (!isHidden()){
            if (!isFirst){
                isFirst = true
                return
            }
            ToastUtil.toast(requireContext(),"更新了啊")
            init()
        }
    }*/

    fun hideAllFragments(){
        for (i in fragments.indices){
            if (fragments[i].isAdded){
                childFragmentManager.beginTransaction().hide(fragments[i]).commit()
            }
        }
    }

    inner class MyClickListener(val index : Int) : View.OnClickListener{

        override fun onClick(v: View?) {
            if (index != this@WorkpointDetailFragment.index){
                textarray[this@WorkpointDetailFragment.index].setTextColor(resources.getColor(R.color.black))
                textarray[index].setTextColor(resources.getColor(R.color.blue))
                this@WorkpointDetailFragment.index = index
                showFragment(index)
            }
        }

    }

    fun initChart(bean: DayWorkPointBean){
        layout_chart.visibility = View.VISIBLE
        chart.description.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.setDragEnabled(false)
//是否能够缩放
        chart.setPinchZoom(false)
//允许X轴缩放
        chart.setScaleXEnabled(false)
//允许Y轴缩放
        chart.setScaleYEnabled(false)
//设置显示的动画,BarChart的动画有很多种 animateX，animateY，animateXY等
        chart.animateXY(0,0)//这个地方来设置动画的展示
        val xAxis: XAxis = chart.getXAxis()
//是否显示X轴的线(与X轴垂直的线),默认为true
//是否显示X轴的线(与X轴垂直的线),默认为true
        xAxis.setDrawGridLines(false)
//设置XAxis坐标的字在哪里显示 XAxisPosition{ TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE}
//设置XAxis坐标的字在哪里显示 XAxisPosition{ TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE}
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
//设置X轴的值的间隔，具体不同看下面的图
//设置X轴的值的间隔，具体不同看下面的图
        xAxis.setGranularity(1f)
//设置X轴的值的个数，默认为6个，最小为2个，最大为25个
//设置X轴的值的个数，默认为6个，最小为2个，最大为25个
        xAxis.valueFormatter = object : ValueFormatter(){
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                return bean.data.lastSevenCredit.get(value.toInt() - 1).day
            }

            override fun getFormattedValue(value: Float): String {
                return bean.data.lastSevenCredit.get(value.toInt() - 1).day
            }
        }
        xAxis.setDrawLabels(true)
        val leftYaxis: YAxis = chart.getAxisLeft()
//设置网格线
        //设置网格线
        leftYaxis.setDrawGridLines(false)
        //设置网格线的宽度
        //设置网格线的宽度
        //leftYaxis.gridLineWidth = Utils.convertDpToPixel(1)
        //设置网格线的宽度
        //设置网格线的宽度
        leftYaxis.gridColor = resources.getColor(android.R.color.background_dark)
        //设置Y轴的值的个数，默认为6个，最小为2个，最大为25个。第二个参数 如果启用，集标签数量将被强制，即标签的准确计数指定将绘制且分布均匀沿着轴 - 这可能导致标签有不均匀值
        //设置Y轴的值的个数，默认为6个，最小为2个，最大为25个。第二个参数 如果启用，集标签数量将被强制，即标签的准确计数指定将绘制且分布均匀沿着轴 - 这可能导致标签有不均匀值
        leftYaxis.setLabelCount(4, false)
//设置Y轴的起点值
//设置Y轴的起点值
        leftYaxis.setAxisMinValue(0f)
//设置将在全范围的百分比顶轴的空间。默认10F.设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）最大值和最高值的百分比 设置为100，则最高值为最大值的1倍
//设置将在全范围的百分比顶轴的空间。默认10F.设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）最大值和最高值的百分比 设置为100，则最高值为最大值的1倍
        leftYaxis.setSpaceTop(15f)
        leftYaxis.setDrawLabels(true)
        val barEntries: MutableList<BarEntry> = ArrayList()
        for (i in bean.data.lastSevenCredit.indices) {
            val barEntry = BarEntry((i + 1).toFloat(), bean.data.lastSevenCredit.get(i).number.toFloat())
            barEntries.add(barEntry)
        }
        val set1 = BarDataSet(barEntries,null)
//设置每个树状图的颜色，一共有两个方法setColors和setColor
        //设置每个树状图的颜色，一共有两个方法setColors和setColor
        set1.color = resources.getColor(R.color.blue)
        set1.label = "工分"

        val barDataSets = ArrayList<IBarDataSet>();
        barDataSets.add(set1);

        val barData = BarData(barDataSets);
//设置树状图的宽度
        barData.setBarWidth(0.5f)
        //设置树状图上的字的字号
        barData.setValueTextSize(10.0F)
        chart.legend.isEnabled = false

        chart.setData(barData)
    }

    fun refresh(){
        init()
        fragments[index].refresh()
    }

}