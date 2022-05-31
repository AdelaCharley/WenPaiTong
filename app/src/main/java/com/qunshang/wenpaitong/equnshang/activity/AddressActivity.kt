package com.qunshang.wenpaitong.equnshang.activity
import android.content.Intent

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.qunshang.wenpaitong.R
import com.qunshang.wenpaitong.equnshang.data.UserInfoViewModel
import com.qunshang.wenpaitong.equnshang.model.ApiManager
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.qunshang.wenpaitong.equnshang.adapter.AddressAdapter
import com.qunshang.wenpaitong.equnshang.data.AddressBean

class AddressActivity : BaseActivity() {

    var isSelect = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        setContentView(R.layout.activity_address)
        toolbar_back.setOnClickListener { finish() }
        toolbar_title.text = "地址管理"
        isSelect = intent.getBooleanExtra("isSelect" ,true)
        initView()
        loadData()
    }

    fun initView(){
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = manager
        addaddress.setOnClickListener {
            startActivity(Intent(this,AddAddressActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun def(i : AddressBean.DataBean){
        val intent = Intent()
        /*intent.putExtra("addressId",i.getId())
        intent.putExtra("",i.get)
        intent.putExtra()
        intent.putExtra()*/
        intent.putExtra("address",i)
        setResult(AddressAdapter.RESULT_CODE,intent)
        finish()
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    fun def(i : AddressAdapter.AddressData){
        val intent = Intent()
        intent.putExtra("addressId",i.getId())
        intent.putExtra("",i.get)
        intent.putExtra()
        intent.putExtra()
        setResult(AddressAdapter.AddressData.RESULT_CODE,intent)
        finish()
    }*/

    fun loadData(){
        ApiManager.getInstance().getApiAddress().loadAddress(UserInfoViewModel.getUserId()).enqueue(object : Callback<AddressBean>{
            override fun onResponse(call: Call<AddressBean>, response: Response<AddressBean>) {
                list.adapter = AddressAdapter(
                    this@AddressActivity,
                    response.body()!!.data
                ,isSelect)
            }

            override fun onFailure(call: Call<AddressBean>, t: Throwable) {

            }

        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}