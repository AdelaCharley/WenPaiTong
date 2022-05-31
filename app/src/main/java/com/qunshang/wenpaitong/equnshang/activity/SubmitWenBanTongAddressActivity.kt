package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import com.kongzue.dialog.v3.TipDialog
import com.kongzue.dialog.v3.WaitDialog
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_submit_wen_ban_tong_address.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.BaseHttpBean
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils
import java.util.regex.Pattern

class SubmitWenBanTongAddressActivity : BaseActivity() {

    var orderSn : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_wen_ban_tong_address)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.setText("提货地址")
        xuanzediqu.setOnClickListener {
            selectSite()
        }
        orderSn = intent.getStringExtra("orderSn")
        if (StringUtils.isEmpty(orderSn)){
            DialogUtil.toast(this,"出错了")
        }
        initView()
    }

    fun initView(){
        shouhuorenname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkEditTextStatus()
            }

        })
        shoujihaoma.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkEditTextStatus()
            }

        })
        xuanzediqu.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkEditTextStatus()
            }

        })
        xiangqingrizhi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkEditTextStatus()
            }

        })
        val filter1 =
            InputFilter { source, start, end, dest, dstart, dend -> // 判断是否输入空格
                val str = "1234567890"
                if (str.contains(source)) {
                    source
                } else ""
            }
        val filters = arrayOf(filter1)
        shoujihaoma.filters = filters
    }

    fun checkEditTextStatus(){
        if (!StringUtils.isEmpty(shouhuorenname.text.toString()) && !StringUtils.isEmpty(shoujihaoma.text.toString()) && !StringUtils.isEmpty(xuanzediqu.text.toString())
            && !StringUtils.isEmpty(xiangqingrizhi.text.toString())){
            submit.background = resources.getDrawable(R.drawable.bg_wenbantong_submit_selected)
            submit.isClickable = true
            submit.setOnClickListener {
                if (!isRightPhone()){
                    DialogUtil.showErrorDialog(this,"手机号格式不正确")
                    return@setOnClickListener
                }
                doSubmit()
            }
        } else {
            submit.isClickable = false
            submit.background = resources.getDrawable(R.drawable.bg_wenbantong_submit_unselectted)
        }
        
    }

    fun doSubmit(){
        WaitDialog.show(this,"正在提交")
        ApiManager.getInstance().getApiWenBanTong_ZhangJun().submitWenBanTongOrder(orderSn!!,shouhuorenname.text.toString(),shoujihaoma.text.toString(),address + "," + xiangqingrizhi.text.toString())
            .subscribeOn(Schedulers.io())
            .doOnError(object : Consumer<Throwable>{
                override fun accept(t: Throwable) {
                    WaitDialog.dismiss()
                    DialogUtil.toast(this@SubmitWenBanTongAddressActivity,"出错了")
                }

            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<BaseHttpBean<String>>{
                override fun accept(t: BaseHttpBean<String>) {
                    WaitDialog.dismiss()
                    if (t.code == 200){
                        EventBus.getDefault().post("wenbantongrefresh")   //执行刷新
                        TipDialog.show(this@SubmitWenBanTongAddressActivity,"提交成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                            finish()
                        }
                    }
                }
            },object : Consumer<Throwable>{
                override fun accept(t: Throwable) {
                    WaitDialog.dismiss()
                    DialogUtil.toast(this@SubmitWenBanTongAddressActivity,"出错了")
                }

            })
        /*ApiManager.getInstance().getApiWenBanTong_ZhangJun().submitWenBanTongOrder(orderSn!!,shouhuorenname.text.toString(),shoujihaoma.text.toString(),address + "," + xiangqingrizhi.text.toString())
            .enqueue(object : Callback<BaseHttpBean<String>>{
                override fun onResponse(
                    call: Call<BaseHttpBean<String>>,
                    response: Response<BaseHttpBean<String>>
                ) {
                    if (response.body() == null){
                        StringUtils.log(orderSn!!+"  " + shouhuorenname.text.toString() + "  " + shoujihaoma.text.toString() + "  " + address + "," + xiangqingrizhi.text.toString())
                        DialogUtil.showErrorDialog(this@SubmitWenBanTongAddressActivity,"出错了")
                        return
                    }
                    if (response.body()?.code == 200){
                        TipDialog.show(this@SubmitWenBanTongAddressActivity,"提交成功",TipDialog.TYPE.SUCCESS).setOnDismissListener {
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<BaseHttpBean<String>>, t: Throwable) {
                    StringUtils.log(t.message)
                }

            })*/
    }

    fun isRightPhone(): Boolean {
        if (shoujihaoma.text.toString().isEmpty()) {
            //util.setShortToast("请输入手机号");
            return false
        }
        val regexPhone = "^1[3-9]\\d{9}$"
        val p = Pattern.compile(regexPhone)
        val m = p.matcher(shoujihaoma.text.toString())
        if (!m.matches()) {
            //showWarnDialog("手机号不正确")
            //util.setShortToast("手机号不正确");
            return false
        } else {
            return true
        }
    }

    var address : String = ""

    fun selectSite() {
        val cityPicker = JDCityPicker()
        val jdCityConfig = JDCityConfig.Builder().build()

        jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
        cityPicker.init(this)
        cityPicker.setConfig(jdCityConfig)
        cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(
                province: ProvinceBean,
                city: CityBean,
                district: DistrictBean
            ) {
                xuanzediqu.setTextColor(resources.getColor(R.color.black))
                address = "${province.name},${city.name},${district.name}"
                xuanzediqu.setText(address)
            }

            override fun onCancel() {}
        })
        cityPicker.showCityPicker()
    }

}