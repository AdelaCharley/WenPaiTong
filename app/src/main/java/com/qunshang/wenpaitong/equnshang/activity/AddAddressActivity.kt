package com.qunshang.wenpaitong.equnshang.activity

import android.os.Bundle
import android.widget.CompoundButton
import com.qunshang.wenpaitong.R
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.activity_add_address.detail
import kotlinx.android.synthetic.main.activity_add_address.videoname
import kotlinx.android.synthetic.main.activity_add_address.phone
import kotlinx.android.synthetic.main.activity_add_address.save
import kotlinx.android.synthetic.main.activity_add_address.switchbutton
import kotlinx.android.synthetic.main.layout_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import com.qunshang.wenpaitong.equnshang.utils.DialogUtil
import com.qunshang.wenpaitong.equnshang.utils.StringUtils

class AddAddressActivity : BaseActivity() {

    var address : String = ""

    var default : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)
        toolbar_back.setOnClickListener {
            finish()
        }
        toolbar_title.setText("新增收货地址")
        switchbutton.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    default = "1"
                    DialogUtil.toast(this@AddAddressActivity,"点击了")
                } else {
                    default = "0"
                    DialogUtil.toast(this@AddAddressActivity,"ina")
                }
            }

        })
        cancel.setOnClickListener { finish() }
        save.setOnClickListener {
            save()
        }
        site.setOnClickListener {
            selectSite()
        }
    }

    fun selectSite() {
        val cityPicker = JDCityPicker()
        val jdCityConfig = JDCityConfig.Builder().build()

        jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
        cityPicker.init(this@AddAddressActivity)
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
                /*address = """
                城市选择结果：
                ${province.name}(${province.id})
                ${city.getName()}(${city.getId()})
                ${district.name}(${district.id})
                """.trimIndent()*/
            }

            override fun onCancel() {}
        })
        cityPicker.showCityPicker()
    }

    fun save(){
        if (StringUtils.isEmpty(videoname.text.toString())){
            DialogUtil.toast(this,"请输入收货人姓名")
            return
        } else if (phone.text.toString().length != 11){
            DialogUtil.toast(this,"请填写正确的手机号")
            return
        } else if (StringUtils.isEmpty(address)){
            DialogUtil.toast(this,"请补充收货地址")
            return
        } else if (StringUtils.isEmpty(detail.text.toString())){
            DialogUtil.toast(this,"请补充详细信息")
            return
        }

        ApiManager.getInstance().getApiAddress().saveAddress(
            UserInfoViewModel.getUserId(),
            videoname.text.toString(),
            phone.text.toString(),
            address + "," + detail.text.toString(),default).enqueue(object : Callback<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>{
            override fun onResponse(
                call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>,
                response: Response<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>
            ) {
                if (response.body()!= null){
                    val body = response.body()
                    if (body?.statusCode == 0){
                        DialogUtil.toast(this@AddAddressActivity,"上传成功")
                        finish()
                    } else {
                        DialogUtil.toast(this@AddAddressActivity,body?.message + "")
                    }
                }
            }

            override fun onFailure(call: Call<com.qunshang.wenpaitong.equnshang.data.OperateAddressBean>, t: Throwable) {

            }

        })

    }

}