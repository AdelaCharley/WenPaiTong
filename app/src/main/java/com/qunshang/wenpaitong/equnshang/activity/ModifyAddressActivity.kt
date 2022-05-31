package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.widget.CompoundButton
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_modify_address.*
import kotlinx.android.synthetic.main.activity_modify_address.detail
import kotlinx.android.synthetic.main.activity_modify_address.videoname
import kotlinx.android.synthetic.main.activity_modify_address.phone
import kotlinx.android.synthetic.main.activity_modify_address.save
import kotlinx.android.synthetic.main.activity_modify_address.switchbutton
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.lljjcoder.bean.DistrictBean

import com.lljjcoder.bean.ProvinceBean

import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean

import com.lljjcoder.style.cityjd.JDCityConfig

import com.lljjcoder.style.cityjd.JDCityPicker
import kotlinx.android.synthetic.main.activity_modify_address.site
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil

class ModifyAddressActivity : BaseActivity (){

    lateinit var bean : com.qunshang.wenpaitong.equnshang.data.AddressBean.DataBean

    var address : String = ""

    var default : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_address)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("修改收货地址")
        switchbutton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    default = "1"
                } else {
                    default = "0"
                }
            }

        })
        bean = intent.getSerializableExtra("data") as com.qunshang.wenpaitong.equnshang.data.AddressBean.DataBean
        videoname.setText(bean.name)
        phone.setText(bean.phone)

        site.setText(com.qunshang.wenpaitong.equnshang.utils.StringUtils.getBriefAddress(bean.site))
        address = com.qunshang.wenpaitong.equnshang.utils.StringUtils.getBriefAddress(bean.site)

        detail.setText(com.qunshang.wenpaitong.equnshang.utils.StringUtils.getDetail(bean.site))
        default = bean.getIsDefault()
        site.setOnClickListener {
            selectSite()
        }
        save.setOnClickListener {
            mofidy()
        }
        delete.setOnClickListener {
            delete()
        }
    }

    fun delete(){
        ApiManager.getInstance().getApiAddress().deleteAddress(bean.id).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>{
            override fun onResponse(
                call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>,
                response: Response<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>
            ) {
                val data = response.body()
                if (data?.statusCode == 0){
                    DialogUtil.toast(this@ModifyAddressActivity,"删除成功")
                    finish()
                }
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>, t: Throwable) {

            }

        })
    }

    fun selectSite(){
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
                site.setTextColor(resources.getColor(R.color.black))
                address = "${province.name},${city.getName()},${district.name}"
                site.setText(address)
            }

            override fun onCancel() {}
        })
        cityPicker.showCityPicker()
    }


    fun mofidy(){
        if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(videoname.text.toString())){
            DialogUtil.toast(this,"请输入收货人姓名")
            return
        } else if (phone.text.toString().length != 11){
            DialogUtil.toast(this,"请填写正确的手机号")
            return
        } else if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(address)){
            DialogUtil.toast(this,"请补充收货地址")
            return
        } else if (com.qunshang.wenpaitong.equnshang.utils.StringUtils.isEmpty(detail.text.toString())){
            DialogUtil.toast(this,"请补充详细信息")
            return
        }

        ApiManager.getInstance().getApiAddress().modifyAddress(
            UserInfoViewModel.getUserId(),
            videoname.text.toString(),
            phone.text.toString(),
            address + "," + detail.text.toString(),default,bean.id).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean> {
            override fun onResponse(
                call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>,
                response: Response<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>
            ) {
                if (response.body()!= null){
                    val body = response.body()
                    if (body?.statusCode == 0){
                        DialogUtil.toast(this@ModifyAddressActivity,"修改成功")
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>, t: Throwable) {

            }

        })

    }
}